package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.Vision;

import frc.robot.subsystems.ShooterSubsystem.ShooterState;

public class ScoreCommand extends Command {
    public record ShooterPoint(
        double distanceMeters,
        double rpm
    ) {}

    private final ShooterSubsystem shooterSubsystem;
    private final Vision vision;

    public ScoreCommand(ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.vision = Vision.getInstance();
    }

    @Override
    public void execute() {
        double vHubDist = vision.getPosition().getTranslation().minus(Constants.FieldConstants.getHubPose().getTranslation()).getNorm();
        if (vHubDist > Constants.ShooterConstants.kMaxShootingDist) {
            System.out.println("Robot is too far from the hub");
            return;
        }
        if (!vision.isHubLocked()) {
            System.out.println("Robot is not angled correctly");
        }
        ShooterPoint sp = interpolate(vHubDist);
        shooterSubsystem.setTargetRPM(sp.rpm);
        if (shooterSubsystem.getState() != ShooterState.AT_TARGET) return;
        // we will be releasing the ball into the shooter here I guess, and prolly do some other stuff.
    }

    public static ShooterPoint interpolate(double distance) {
    final ShooterPoint[] shooterLUT = Constants.ShooterConstants.kShooterLUT;
    for (int i = 0; i < shooterLUT.length- 1; i++) {
        ShooterPoint p1 = shooterLUT[i];
        ShooterPoint p2 = shooterLUT[i + 1];

        if (distance >= p1.distanceMeters()
         && distance <= p2.distanceMeters()) {

            double t = (distance - p1.distanceMeters())
                     / (p2.distanceMeters() - p1.distanceMeters());

            return new ShooterPoint(
                distance,
                lerp(p1.rpm(),      p2.rpm(),      t)
            );
        }
    }

    // Clamp to ends
    if (distance < shooterLUT[0].distanceMeters())
        return shooterLUT[0];
    return shooterLUT[shooterLUT.length - 1];
}

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}