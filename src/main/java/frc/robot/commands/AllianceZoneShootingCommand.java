package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AllianceZoneShootingCommand extends Command {
    private final ShooterSubsystem shooterSubsystem;
    private final HoodSubsystem hoodSubsystem;
    private final FeederSubsystem feederSubsystem;

    public AllianceZoneShootingCommand(ShooterSubsystem shooterSubsystem, HoodSubsystem hoodSubsystem, FeederSubsystem feederSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.hoodSubsystem = hoodSubsystem;
        this.feederSubsystem = feederSubsystem;
        addRequirements(shooterSubsystem, hoodSubsystem, feederSubsystem);
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
