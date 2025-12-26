package frc.robot.subsystems;
import frc.robot.Constants;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.*;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.*;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;

public class ArmSubsystem extends SubsystemBase {
  public enum ArmState {
    AT_TARGET,
    OFF_TARGET,
    IDLE
  }

  private final SparkMax m_motor;
  private final ArmFeedforward armFF;
  private final RelativeEncoder encoder;
  private final DutyCycleEncoder ab_encoder;
  private final SparkClosedLoopController closedLoop;

  private double targetAngle = Double.NaN;
  private boolean hasInitialized = false;

  private ArmState state = ArmState.IDLE;

  /** Creates a new ArmSubsystem. */
  public ArmSubsystem() {
    m_motor = new SparkMax(Constants.ArmConstants.kMotorID, MotorType.kBrushless);
    encoder = m_motor.getEncoder();
    ab_encoder = new DutyCycleEncoder(Constants.ArmConstants.kAbsoluteEncoderID, 360, Constants.ArmConstants.kEncoderOffset);
    
    SparkMaxConfig config = new SparkMaxConfig();
    
    // Feedforward expects radians, but we'll work in degrees and convert
    armFF = new ArmFeedforward(
      Constants.ArmConstants.kS, 
      Constants.ArmConstants.kG, 
      Constants.ArmConstants.kV, 
      Constants.ArmConstants.kA
    );

    config
      .idleMode(IdleMode.kBrake)
      .inverted(false)
      .voltageCompensation(12.0); // Enable voltage compensation
    
    // Configure current limiting (adjust values for your arm)
    config.smartCurrentLimit(40, 60); // Stall limit: 40A, Free limit: 60A
    
    // Encoder configuration - degrees per rotation
    config.encoder
      .positionConversionFactor(360.0 / Constants.ArmConstants.kGearRatio) // degrees
      .velocityConversionFactor(360.0 / Constants.ArmConstants.kGearRatio / 60.0); // degrees/sec

    // Absolute encoder configuration
    config.absoluteEncoder
      .positionConversionFactor(360.0) // degrees
      .inverted(false)
      .zeroOffset(Constants.ArmConstants.kEncoderOffset);

    // Closed loop configuration
    config.closedLoop
      .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
      .pidf(
        Constants.ArmConstants.kP, 
        Constants.ArmConstants.kI, 
        Constants.ArmConstants.kD, 
        Constants.ArmConstants.kFF
      )
      .maxMotion
        .positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal)
        .maxAcceleration(Constants.ArmConstants.kMaxAcceleration) // deg/s²
        .maxVelocity(Constants.ArmConstants.kMaxVelocity) // deg/s
        .allowedClosedLoopError(Constants.ArmConstants.kTolerance);

    // Set soft limits (adjust min/max for your mechanism)
    config.softLimit
      .forwardSoftLimit(Constants.ArmConstants.kMaxAngle)
      .forwardSoftLimitEnabled(true)
      .reverseSoftLimit(Constants.ArmConstants.kMinAngle)
      .reverseSoftLimitEnabled(true);

    m_motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    closedLoop = m_motor.getClosedLoopController();

    // Initial sync with absolute encoder
    syncEncoders();
  }

  /**
   * Syncs the relative encoder with the absolute encoder position
   */
  private void syncEncoders() {
    if (ab_encoder.isConnected()) {
      double absolutePosition = ab_encoder.get();
      encoder.setPosition(absolutePosition);
      hasInitialized = true;
    }
  }

  /**
   * Checks if encoders need re-syncing (e.g., after power cycle or large drift)
   */
  private void checkEncoderSync() {
    if (ab_encoder.isConnected()) {
      double absolutePosition = ab_encoder.get() * 360.0 - Constants.ArmConstants.kEncoderOffset;
      double relativePosition = encoder.getPosition();
      double error = Math.abs(absolutePosition - relativePosition);
      
      // If error is large, re-sync (could indicate power cycle or issue)
      if (error > 10.0) { // 10 degree threshold
        encoder.setPosition(absolutePosition);
        SmartDashboard.putBoolean("Arm/EncoderResynced", true);
      }
    }
  }

  // boilerplate command to use the subsystem
  public Command setPositionCommand(double angle) {
    return runOnce(() -> {
      setTargetAngle(angle);
    });
  }

  public ArmState getState() { 
    return this.state; 
  }

  public void setTargetAngle(double angle) { 
    // Clamp to soft limits
    this.targetAngle = Math.max(
      Constants.ArmConstants.kMinAngle,
      Math.min(angle, Constants.ArmConstants.kMaxAngle)
    );
  }
  
  public double getTargetAngle() { 
    return this.targetAngle; 
  }

  public double getAngle() { 
    return encoder.getPosition(); 
  }

  public double getAbsoluteAngle() {
    return ab_encoder.get();
  }

  public boolean isAtTargetPosition() {
    if (Double.isNaN(targetAngle)) {
      return false;
    }
    return Math.abs(targetAngle - getAngle()) < Constants.ArmConstants.kTolerance;
  }

  public void stop() { 
    targetAngle = Double.NaN;
    m_motor.stopMotor();
  }

  /**
   * Calculate feedforward voltage
   * Converts degrees to radians for ArmFeedforward
   */
  private double calcFF() {
    double positionRad = Units.degreesToRadians(getAngle());
    double velocityRadPerSec = state == ArmState.OFF_TARGET 
      ? Units.degreesToRadians(Constants.ArmConstants.kMovingVelocity) 
      : 0.0;
    
    return armFF.calculate(positionRad, velocityRadPerSec);
  }

  @Override
  public void periodic() {
    // Periodically check encoder sync (every ~100 cycles / 2 seconds)
    if (!hasInitialized || (System.currentTimeMillis() % 2000 < 20)) {
      checkEncoderSync();
    }

    // Update state based on target
    if (Double.isNaN(targetAngle)) {
      state = ArmState.IDLE;
      m_motor.stopMotor();
    } else {
      if (isAtTargetPosition()) {
        state = ArmState.AT_TARGET;
      } else {
        state = ArmState.OFF_TARGET;
      }
      
      // Set reference with feedforward
      closedLoop.setReference(
        targetAngle,
        ControlType.kMAXMotionPositionControl, 
        ClosedLoopSlot.kSlot0, 
        calcFF()
      );
    }

    // Telemetry
    SmartDashboard.putNumber("Arm/CurrentAngle", getAngle());
    SmartDashboard.putNumber("Arm/TargetAngle", targetAngle);
    SmartDashboard.putNumber("Arm/AbsoluteAngle", getAbsoluteAngle());
    SmartDashboard.putString("Arm/State", state.toString());
    SmartDashboard.putNumber("Arm/Current", m_motor.getOutputCurrent());
    SmartDashboard.putNumber("Arm/Velocity", encoder.getVelocity());
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}