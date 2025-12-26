package frc.robot.subsystems;

import frc.robot.Constants;
import swervelib.SwerveDrive;
import frc.robot.utils.LimelightHelpers;
import frc.robot.utils.LimelightHelpers.PoseEstimate;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
    private final String _limelightName;

    public Vision() {
        _limelightName = Constants.VisionConstants.kLimelightName;
    }

    public void updatePoseEstimation(SwerveDrive swerveDrive) {
        double robotYaw = swerveDrive.getYaw().getDegrees();
        LimelightHelpers.SetRobotOrientation(_limelightName, robotYaw, 0.0, 0.0, 0.0, 0.0, 0.0);
    
        PoseEstimate est = null;
        if (DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Blue) {
            est = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(_limelightName);
        } else {
            est = LimelightHelpers.getBotPoseEstimate_wpiRed_MegaTag2(_limelightName);
        }

        if (est == null) return;
        if (est.tagCount < 1) return;
        if (est.pose == null) return;

        swerveDrive.addVisionMeasurement(est.pose,est.timestampSeconds);
    }

    @Override
    public void periodic() {
        
    }

    public void simulationPeriodic()
    {

    }
}
