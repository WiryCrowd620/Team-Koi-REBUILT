package frc.robot.subsystems;

import java.util.Arrays;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import frc.robot.Constants;
import frc.robot.StateRobot;
import frc.robot.utils.LimelightHelpers;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
    private AHRS m_gyro;
    private SwerveDrivePoseEstimator m_poseEstimator;

    public Vision() {
        m_gyro = new AHRS(NavXComType.kMXP_SPI);
    }

    public void updatePoseEstimation() {
        
    }

    @Override
    public void periodic() {
        
    }

    public void simulationPeriodic()
    {

    }
}
