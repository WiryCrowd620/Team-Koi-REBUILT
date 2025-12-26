package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kOperatorControllerPort = 1;
  }

  public static class SwerveDriveConstants {
    public static final double kMaxSpeed = Units.feetToMeters(20);

    private static final Translation2d kFrontLeftPos = new Translation2d(0, 0);
    private static final Translation2d kFrontRightPos = new Translation2d(0, 0);
    private static final Translation2d kBackLeftPos   = new Translation2d(0, 0);
    private static final Translation2d kBackRightPos  = new Translation2d(0, 0);

    public static final SwerveDriveKinematics kSwerveDriveKinematics = new SwerveDriveKinematics(
      kFrontLeftPos, kFrontRightPos,
      kBackLeftPos, kBackRightPos
    );
  }

  public static class ShooterConstants {
    public static final int kMainMotorID = 0;
    public static final int kSecondaryMotorID = 0;
    public static final boolean kSecondaryInverted = true;
    
    public static final double kGearRatio = 1.0;
    public static final double kTolerance = 50.0;
    
    public static final double kP = 0.0001;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double kFF = 0.0;
    
    public static final double kS = 0.1;
    public static final double kV = 0.12;
    public static final double kA = 0.01;
}

  public static class ArmConstants {
    public static final int kMotorID = 0;
    public static final int kAbsoluteEncoderID = 0;
    public static final double kGearRatio = 100.0;
    public static final double kEncoderOffset = 0.0;
    public static final double kMinAngle = -5.0;
    public static final double kMaxAngle = 120.0;
    public static final double kTolerance = 3.0;
    public static final double kMovingVelocity = 20.0;
    public static final double kP = 0.02;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double kFF = 0.0;
    public static final double kS = 0.1;
    public static final double kG = 0.5;
    public static final double kV = 2.5;
    public static final double kA = 0.1;
    public static final double kMaxVelocity = 90.0;
    public static final double kMaxAcceleration = 180.0;
}
}
