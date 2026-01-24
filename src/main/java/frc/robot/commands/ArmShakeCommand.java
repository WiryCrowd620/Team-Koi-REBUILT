package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeArmSubsystem;

/* You're allowed to refer to this command as the thug shake, please do */
public class ArmShakeCommand extends Command {
    private final IntakeArmSubsystem intakeArmSubsystem;

    public ArmShakeCommand(IntakeArmSubsystem intakeArmSubsystem) {
        this.intakeArmSubsystem = intakeArmSubsystem;
        addRequirements(intakeArmSubsystem);
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
