package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class feederSubsystem extends SubsystemBase {
    private final SparkMax m_SparkMax;

    /** Creates a new ExampleSubsystem. */
    public feederSubsystem() {
        m_SparkMax = new SparkMax(Constants.feederConstants.kMotorID, MotorType.kBrushless);
    }

    public void setVoltage(double power) {
        m_SparkMax.setVoltage(power);
    }

    public Command feederMethodCommand(double power) {
        return runOnce(() -> {
            setVoltage(power);
        });
    }
    
    @Override
    public void periodic() {
        // Called once per scheduler run
    }

    @Override
    public void simulationPeriodic() {
        // Called once per scheduler run during simulation
    }
}
