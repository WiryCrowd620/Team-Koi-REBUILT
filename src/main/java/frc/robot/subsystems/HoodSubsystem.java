package frc.robot.subsystems;

import frc.robot.Constants;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class HoodSubsystem extends SubsystemBase {
    private Servo servoRight, servoLeft;

    public HoodSubsystem() {
        servoRight = new Servo(Constants.HoodConstants.kServoRightID);
        servoLeft = new Servo(Constants.HoodConstants.kServoLeftID);

        setAngle(Constants.HoodConstants.kStartingPos);
    }

    public void setAngle(double degrees) {
        // makes sure we don't surpass the physical limits of the servos range of motion
        degrees = MathUtil.clamp(degrees, Constants.HoodConstants.kMinDeg, Constants.HoodConstants.kMaxDeg);

        // turns the degrees into "percentage" (0.0-1.0)
        double normalized = (degrees - Constants.HoodConstants.kMinDeg)
                / (Constants.HoodConstants.kMaxDeg - Constants.HoodConstants.kMinDeg);

        // calculates the needed PWM value for the final set function

        // Normal servo
        double leftPwm = Constants.HoodConstants.kServoMin
                + normalized * (Constants.HoodConstants.kServoMax - Constants.HoodConstants.kServoMin);

        // Inverted servo
        double rightPwm = Constants.HoodConstants.kServoMin
                + (1.0 - normalized) * (Constants.HoodConstants.kServoMax - Constants.HoodConstants.kServoMin);

        servoRight.set(leftPwm);
        servoLeft.set(rightPwm);

        Timer.delay(Constants.HoodConstants.kServoDelay);
    }

    public Command HoodMethodCommand(double angle) {
        return runOnce(
                () -> {
                    setAngle(angle);
                });
    }
}