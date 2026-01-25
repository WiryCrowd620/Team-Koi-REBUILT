package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.FieldConstants;
import frc.robot.Superstructure;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.Vision;

import frc.robot.subsystems.ShooterSubsystem.ShooterState;

public class ScoreCommand extends Command {
    public record ShooterPoint(
            double distanceMeters,
            double rpm,
            double hoodAngle) {
    }

    private final ShooterSubsystem shooterSubsystem;
    private final HoodSubsystem hoodSubsystem;
    private final FeederSubsystem feederSubsystem;
    private final Vision vision;
    private boolean firstTimeReady = true;

    public ScoreCommand(ShooterSubsystem shooterSubsystem, HoodSubsystem hoodSubsystem,
            FeederSubsystem feederSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.hoodSubsystem = hoodSubsystem;
        this.feederSubsystem = feederSubsystem;
        this.vision = Vision.getInstance();
        addRequirements(shooterSubsystem, hoodSubsystem, feederSubsystem);
    }

    @Override
    public void execute() {
        double vHubDist = vision.getPosition().getTranslation()
                .minus(new Translation2d(FieldConstants.Hub.innerCenterPoint.getX(),
                        FieldConstants.Hub.innerCenterPoint.getY()))
                .getNorm();
        if (vHubDist > Constants.ShooterConstants.kMaxShootingDist) {
            System.out.println("Robot is too far from the hub");
            return;
        }
        if (!vision.isHubLocked()) {
            System.out.println("Robot is not angled correctly");
        }
        ShooterPoint sp = interpolate(vHubDist);
        // for shooting while moving we will only be adjusting rpm
        shooterSubsystem.setTargetRPM(sp.rpm + Constants.ShooterConstants.kRadialRPMComp
                * Superstructure.getInstance().getSwerveHubRelativeRadialSpeed());
        hoodSubsystem.setAngle(sp.hoodAngle);
        if (shooterSubsystem.getState() != ShooterState.AT_TARGET)
            return;
        if (firstTimeReady) {
            Superstructure.getInstance().getRumbleSubsystem().rumble(Constants.ShooterConstants.kRumbleScoreReady);
            firstTimeReady = false;
        }
        feederSubsystem.setVoltage(Constants.FeederConstants.kGrabPower);
    }

    public static ShooterPoint interpolate(double distance) {
        return new ShooterPoint(
                distance,
                Constants.ShooterConstants.kRpmMap.get(distance),
                Constants.ShooterConstants.kHoodMap.get(distance));
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.setTargetRPM(0);
        firstTimeReady = true;
    }
}