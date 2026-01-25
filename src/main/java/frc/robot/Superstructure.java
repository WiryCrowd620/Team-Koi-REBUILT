package frc.robot;

import java.io.File;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.FeederSubsystem.FeederState;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.IntakeArmSubsystem;
import frc.robot.subsystems.IntakeArmSubsystem.IntakeArmState;
import frc.robot.subsystems.IntakeRollerSubsytem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterSubsystem.ShooterState;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.ClimberSubsystem.ClimberState;
import frc.robot.subsystems.IntakeRollerSubsytem.IntakeRollerState;
import frc.robot.utils.GameDataSubsystem;
import frc.robot.utils.RumbleSubsystem;

public class Superstructure {
    private static Superstructure _instance;

    // Subsystems
    private final RumbleSubsystem rumbleSubsystem;
    private final GameDataSubsystem gameDataSubsystem;
    private final ShooterSubsystem shooterSubsystem;
    private final FeederSubsystem feederSubsystem;
    private final IntakeArmSubsystem intakeArmSubsystem;
    private final IntakeRollerSubsytem intakeRollerSubsytem;
    private final ClimberSubsystem climberSubsystem;
    private final HoodSubsystem hoodSubsystem;
    private final SwerveSubsystem drivebase;

    private Superstructure() {
        // Instantiate subsystems
        rumbleSubsystem = new RumbleSubsystem();
        gameDataSubsystem = new GameDataSubsystem(rumbleSubsystem);

        shooterSubsystem = new ShooterSubsystem();
        feederSubsystem = new FeederSubsystem();
        intakeArmSubsystem = new IntakeArmSubsystem();
        intakeRollerSubsytem = new IntakeRollerSubsytem();
        climberSubsystem = new ClimberSubsystem();
        hoodSubsystem = new HoodSubsystem();

        drivebase = new SwerveSubsystem(
                new File(Filesystem.getDeployDirectory(),
                        RobotBase.isSimulation() ? "swerve-sim" : "swerve"));
    }

    // Singleton accessor
    public static Superstructure getInstance() {
        if (_instance == null) {
            _instance = new Superstructure();
        }
        return _instance;
    }

    // Getters
    public RumbleSubsystem getRumbleSubsystem() {
        return rumbleSubsystem;
    }

    public GameDataSubsystem getGameDataSubsystem() {
        return gameDataSubsystem;
    }

    public ShooterSubsystem getShooterSubsystem() {
        return shooterSubsystem;
    }

    public FeederSubsystem getFeederSubsystem() {
        return feederSubsystem;
    }

    public IntakeArmSubsystem getIntakeArmSubsystem() {
        return intakeArmSubsystem;
    }

    public IntakeRollerSubsytem getIntakeRollerSubsytem() {
        return intakeRollerSubsytem;
    }

    public ClimberSubsystem getClimberSubsystem() {
        return climberSubsystem;
    }

    public HoodSubsystem getHoodSubsystem() {
        return hoodSubsystem;
    }

    public SwerveSubsystem getDrivebase() {
        return drivebase;
    }

    // state getters

    public ShooterState getShooterState() {
        return shooterSubsystem.getState();
    }

    public FeederState getFeederState() {
        return feederSubsystem.getState();
    }

    public IntakeArmState getIntakeState() {
        return intakeArmSubsystem.getState();
    }

    public IntakeRollerState getIntakeRollerState() {
        return intakeRollerSubsytem.getState();
    }

    public ClimberState getClimberState() {
        return climberSubsystem.getState();
    }

    public double getClimberHeight() {
        return climberSubsystem.getHeight();
    }

    public double getSwerveHubRelativeRadialSpeed() {
        return drivebase.getHubRelativeVelocity().radialSpeed();
    }

    
    public double getSwerveHubRelativeStrafeSpeed() {
        return drivebase.getHubRelativeVelocity().radialSpeed();
    }
}
