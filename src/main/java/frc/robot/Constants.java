package frc.robot;
import java.util.Map;
import edu.wpi.first.math.util.Units;

public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kOperatorControllerPort = 1;
  }

  public static class VisionConstants {
    public static final String kLimelightName = "";

    public static final Map<Integer, Boolean> scoringTags = Map.of(
      // unintellegent pre-season data
     1,true,
     5, true
    );
    public static final double kAmbiguityTolerance = 0.7;
  }

  public static class SwerveDriveConstants {
    public static final double kMaxSpeed = Units.feetToMeters(20);
    public static final double kVisionPeriod = 0.1; // 10Hz
    
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
