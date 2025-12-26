package frc.robot.subsystems;
import java.util.ArrayList;

import frc.robot.Constants;
import swervelib.SwerveDrive;
import frc.robot.utils.LimelightHelpers;
import frc.robot.utils.LimelightHelpers.PoseEstimate;
import frc.robot.utils.LimelightHelpers.RawFiducial;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.Optional;

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

    // returns a prioritized tag for scoring that was captured by the vision system
    public Optional<RawFiducial> getScoringTag() {
        var fiducials = LimelightHelpers.getRawFiducials(_limelightName);
        ArrayList<RawFiducial> found = new ArrayList<>();
    
        // Filter only scoring tags first
        for (RawFiducial f : fiducials) {
            if (Constants.VisionConstants.scoringTags.containsKey(f.id)) {
                found.add(f);
            }
        }
    
        if (found.isEmpty()) return Optional.empty();
        if (found.size() == 1) return Optional.of(found.get(0));
    
        // Filter by ambiguity without mutating during iteration
        ArrayList<RawFiducial> filtered = new ArrayList<>();
        for (RawFiducial f : found) {
            if (f.ambiguity <= Constants.VisionConstants.kAmbiguityTolerance) {
                filtered.add(f);
            }
        }
    
        if (filtered.isEmpty()) return Optional.empty();
    
        // Pick the closest tag
        double minDist = Double.MAX_VALUE;
        RawFiducial best = null;
        for (RawFiducial f : filtered) {
            if (f.distToRobot < minDist) {
                minDist = f.distToRobot;
                best = f;
            }
        }
    
        return Optional.ofNullable(best);
    }
    

    @Override
    public void periodic() {
        
    }

    public void simulationPeriodic()
    {

    }
}
