package com.coderedrobotics.uri;

import com.coderedrobotics.libs.ControlsBoxLEDs;
import com.coderedrobotics.libs.Drive;
import com.coderedrobotics.libs.MechanumDrive;
import com.coderedrobotics.libs.PWMController;
import com.coderedrobotics.uri.statics.KeyMap;
import com.coderedrobotics.uri.statics.Wiring;
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
    
    @Override
    public void robotInit() {
        mechanum = new MechanumDrive(new PWMController(Wiring.FRONT_LEFT_MOTOR, false), 
                new PWMController(Wiring.FRONT_RIGHT_MOTOR, true), 
                new PWMController(Wiring.REAR_LEFT_MOTOR, false),
                new PWMController(Wiring.REAR_RIGHT_MOTOR, true));
        keyMap = new KeyMap();
        leds = new ControlsBoxLEDs(Wiring.RED_AND_GREEN_LEDS, Wiring.BLUE_LEDS);
        lift = new Lift(new PWMController(Wiring.LIFT_MOTOR, false));
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
        
        mechanum.setXYRot(keyMap.getXDriveAxis() * gear, -keyMap.getYDriveAxis() * gear, 
                keyMap.getRotDriveAxis() * gear);
        lift.set(-keyMap.getLiftAxis());
        
        if (keyMap.getReverseDriveButton()) {
            keyMap.toggleReverseDrive();
        }
        if (keyMap.getSingleControllerToggleButton()) {
            keyMap.toggleSingleControllerMode();
        }
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
