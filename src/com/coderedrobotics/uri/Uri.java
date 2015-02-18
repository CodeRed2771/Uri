package com.coderedrobotics.uri;

import com.coderedrobotics.libs.ControlsBoxLEDs;
import com.coderedrobotics.libs.Drive;
import com.coderedrobotics.libs.HID.HID;
import com.coderedrobotics.libs.HID.LogitechF310;
import com.coderedrobotics.libs.MechanumDrive;
import com.coderedrobotics.libs.PWMController;
import com.coderedrobotics.uri.statics.KeyMap;
import com.coderedrobotics.uri.statics.Wiring;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 *
 * @author Michael
 */
public class Uri extends IterativeRobot {

    Drive mechanum;
    KeyMap keyMap;
    ControlsBoxLEDs leds;
    PWMController lift;

    @Override
    public void robotInit() {
        mechanum = new MechanumDrive(new PWMController(Wiring.FRONT_LEFT_MOTOR, false),
                new PWMController(Wiring.FRONT_RIGHT_MOTOR, true),
                new PWMController(Wiring.REAR_LEFT_MOTOR, false),
                new PWMController(Wiring.REAR_RIGHT_MOTOR, true));
        keyMap = new KeyMap();
        leds = new ControlsBoxLEDs(Wiring.RED_AND_GREEN_LEDS, Wiring.BLUE_LEDS);
        lift = new PWMController(Wiring.LIFT_MOTOR, false);
    }

    @Override
    public void autonomousInit() {
        leds.activateAutonomous();
    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopInit() {
        leds.activateTeleop();
    }

    @Override
    public void teleopPeriodic() {
        double x = keyMap.getXDriveAxis();
        double y = keyMap.getYDriveAxis();
        double rot = keyMap.getRotDriveAxis();

        mechanum.setXYRot(x, y,
                rot);
        lift.set(keyMap.getLiftAxis());
    }

    @Override
    public void testInit() {
        leds.activateTest();
    }

    @Override
    public void testPeriodic() {

    }

    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {

    }
}
