package frc.robot.utils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class GameDataSubsystem extends SubsystemBase {
    private final RumbleSubsystem rumble;

    private String gameData = null;
    private int currentShift = 0;
    private int shiftFollower = 0;
    private boolean[] shiftTriggered = new boolean[5];
    private double nextShiftTime = 130;
    private boolean teleopStarted = false;

    public GameDataSubsystem(RumbleSubsystem rumble) {
        this.rumble = rumble;
    }

    @Override
    public void periodic() {
        if (DriverStation.isTeleop() && !teleopStarted) {
            teleopStarted = true;

            if (DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Red) {
                shiftFollower--;
            }
            System.out.println("Teleop started, shiftFollower=" + shiftFollower);
        }

        handleGameData();
        handleShifts();
    }

    private void handleGameData() {
        if (gameData != null)
            return;

        String data = DriverStation.getGameSpecificMessage();
        if (data == null || data.isEmpty())
            return;

        gameData = data.strip();
        if (gameData.charAt(0) == 'R')
            shiftFollower++;
    }

    private void handleShifts() {

        if (currentShift >= shiftTriggered.length)
            return;

        double matchTime = DriverStation.getMatchTime();
        if (nextShiftTime >= matchTime && !shiftTriggered[currentShift]) {
            shiftTriggered[currentShift] = true;

            nextShiftTime -= Constants.OperatorConstants.kTeleopInterval;
            if (nextShiftTime < Constants.OperatorConstants.kEndGameTime) {
                nextShiftTime = -1;
            }

            // trigger rumble only on even shifts for alliance logic
            if (shiftFollower % 2 == 0) {
                rumble.rumble(Constants.OperatorConstants.kGameShiftRumble);
            }

            currentShift++;
            shiftFollower++;
        }
    }
}
