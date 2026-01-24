package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AllianceZoneShooting extends Command {
    private final ShooterSubsystem shooterSubsystem;
    private final HoodSubsystem hoodSubsystem;

    public AllianceZoneShooting(ShooterSubsystem shooterSubsystem, HoodSubsystem hoodSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.hoodSubsystem = hoodSubsystem;
        addRequirements(shooterSubsystem, hoodSubsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
    }
}
