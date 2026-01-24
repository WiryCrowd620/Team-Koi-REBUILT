// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.*;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ResetMode;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.OperatorConstants.ClimberConstants;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ClimberSubsystem extends SubsystemBase {
   public enum climberstate
   {
      MOVING,
      AT_TARGET
   }

    private double targetHeight = 0.0;
    private climberstate state = climberstate.MOVING; 
    private final SparkMax motor1;
    private final SparkMax motor2;
    private final RelativeEncoder encoder;
    private final SparkClosedLoopController closedLoop;
    private final ElevatorFeedforward feedforward;
  public ClimberSubsystem() {
    motor1 = new SparkMax(Constants.ClimberConstants.MOTOR1_CAN_ID, MotorType.kBrushless);
    motor2 = new SparkMax(Constants.ClimberConstants.MOTOR2_CAN_ID, MotorType.kBrushless);
    encoder = motor1.getEncoder();
    closedLoop = motor1.getClosedLoopController();
    SparkMaxConfig config = new SparkMaxConfig();
    SparkMaxConfig followerConfig = new SparkMaxConfig();

    config.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    .pid(
      Constants.ClimberConstants.kP,
      Constants.ClimberConstants.kI,
      Constants.ClimberConstants.kD)

      .feedForward
      .kS(Constants.ClimberConstants.kS)
      .kG(Constants.ClimberConstants.kG)
      .kV(Constants.ClimberConstants.kV)
      .kA(Constants.ClimberConstants.kA);

    config.idleMode(IdleMode.kBrake);
    config.encoder
        .positionConversionFactor(Constants.ClimberConstants.METERS_PER_ROTATION)
        .velocityConversionFactor(Constants.ClimberConstants.METERS_PER_ROTATION / 60.0);
  

    motor1.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    followerConfig.follow(motor1, true);
    motor2.configure(followerConfig, ResetMode.kResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);

    

  }
 
  public void stop() 
  {
    motor1.stopMotor();
  }
  public double getHeight() 
  {
    return encoder.getPosition();
  }

  public double getVelocity() 
  {
    return encoder.getVelocity();
  }
  public void setPosition(double targetHeight) 
  {
    this.targetHeight = targetHeight;
  }
  @Override
  public void periodic() {
    closedLoop.setSetpoint(targetHeight, ControlType.kPosition);
    if(Math.abs(targetHeight - getHeight()) < Constants.ClimberConstants.tolerance)
    {
        state = climberstate.AT_TARGET;
    }
    else state = climberstate.MOVING;
  }
}