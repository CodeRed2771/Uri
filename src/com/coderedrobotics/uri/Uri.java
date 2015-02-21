package com.coderedrobotics.uri;

import com.coderedrobotics.libs.ControlsBoxLEDs;
import com.coderedrobotics.libs.DebugConsole;
import com.coderedrobotics.libs.Drive;
import com.coderedrobotics.libs.MechanumDrive;
import com.coderedrobotics.libs.PWMController;
import com.coderedrobotics.libs.VirtualizableAnalogInput;
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
    Lift lift;
//    Extender extender;
    PWMController extender;
//    VirtualizableAnalogInput stringpot;
    
    @Override
    public void robotInit() {
        mechanum = new MechanumDrive(new PWMController(Wiring.FRONT_LEFT_MOTOR, false),
                new PWMController(Wiring.FRONT_RIGHT_MOTOR, true),
                new PWMController(Wiring.REAR_LEFT_MOTOR, false),
                new PWMController(Wiring.REAR_RIGHT_MOTOR, true));
        keyMap = new KeyMap();
        leds = new ControlsBoxLEDs(Wiring.RED_AND_GREEN_LEDS, Wiring.BLUE_LEDS);
        lift = new Lift();
//        extender = new Extender();
        extender = new PWMController(5, true);
//        stringpot = new VirtualizableAnalogInput(Wiring.EXTENDER_STRING_POT);
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
        double gear = 1;
        if (keyMap.getSlowButton()) {
            gear = .3;
        }

        mechanum.setXYRot(keyMap.getXDriveAxis() * gear, keyMap.getYDriveAxis() * gear,
                keyMap.getRotDriveAxis() * gear);
        lift.set(keyMap.getLiftAxis());

        if (keyMap.getReverseDriveButton()) {
            keyMap.toggleReverseDrive();
        }
        if (keyMap.getSingleControllerToggleButton()) {
            keyMap.toggleSingleControllerMode();
        }

        if (keyMap.getReverseDrive()) {
            extender.set(.7);
        } else {
            extender.set(0);
        }
        //DebugConsole.getInstance().println("hi", "hi");
//        DebugConsole.getInstance().println(stringpot.get(), "stringpot");
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
