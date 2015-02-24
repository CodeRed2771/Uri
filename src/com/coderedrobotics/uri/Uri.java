package com.coderedrobotics.uri;

import com.coderedrobotics.libs.ControlsBoxLEDs;
import com.coderedrobotics.libs.Drive;
import com.coderedrobotics.libs.DriveMux;
import com.coderedrobotics.libs.FieldOrientedDrive;
import com.coderedrobotics.libs.MechanumDrive;
import com.coderedrobotics.libs.MechanumPlaceTracker;
import com.coderedrobotics.libs.PIDDerivativeCalculator;
import com.coderedrobotics.libs.PIDDrive;
import com.coderedrobotics.libs.PWMController;
import com.coderedrobotics.libs.PlaceTracker;
import com.coderedrobotics.libs.dash.DashBoard;
import com.coderedrobotics.uri.statics.Calibration;
import com.coderedrobotics.uri.statics.KeyMap;
import com.coderedrobotics.uri.statics.Wiring;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 *
 * @author Michael
 */
public class Uri extends IterativeRobot {

    double p, i, d;

    Drive mechanum;
    PIDDrive pIDDrive;
    DriveMux driveMux;
    MechanumPlaceTracker placeTracker;
    Drive teleopDrive;
    KeyMap keyMap;
    ControlsBoxLEDs leds;
//    Lift lift;
//    Extender extender;
    PWMController extender;
    PWMController lift;
//    VirtualizableAnalogInput stringpot;

    DashBoard dash;

    @Override
    public void robotInit() {
        DashBoard.setConnectionAddress("10.27.71.59");
        dash = new DashBoard();
        
        keyMap = new KeyMap();
        leds = new ControlsBoxLEDs(Wiring.RED_AND_GREEN_LEDS, Wiring.BLUE_LEDS);
//        lift = new Lift(dash);

        placeTracker = new MechanumPlaceTracker(
                Wiring.REAR_RIGHT_ENCODER_A, Wiring.REAR_RIGHT_ENCODER_B,
                Wiring.FRONT_RIGHT_ENCODER_A, Wiring.FRONT_RIGHT_ENCODER_B,
                Wiring.FRONT_LEFT_ENCODER_A, Wiring.FRONT_LEFT_ENCODER_B,
                Wiring.REAR_LEFT_ENCODER_A, Wiring.REAR_LEFT_ENCODER_B, Wiring.GYRO,
                Calibration.X_SCALE, Calibration.Y_SCALE, Calibration.ROT_SCALE);

        mechanum = new MechanumDrive(new PWMController(Wiring.FRONT_LEFT_MOTOR, false, placeTracker.leftFrontEncoder),
                new PWMController(Wiring.FRONT_RIGHT_MOTOR, true, placeTracker.rightFrontEncoder),
                new PWMController(Wiring.REAR_LEFT_MOTOR, false, placeTracker.leftBackEncoder),
                new PWMController(Wiring.REAR_RIGHT_MOTOR, true, placeTracker.rightBackEncoder));

        pIDDrive = new PIDDrive(mechanum,
                new PIDDerivativeCalculator(placeTracker.getLateralPIDSource()),
                new PIDDerivativeCalculator(placeTracker.getLinearPIDSource()),
                new PIDDerivativeCalculator(placeTracker.getRotPIDSource()),
                Calibration.X_DRIVE_P, Calibration.X_DRIVE_I, Calibration.X_DRIVE_D,
                Calibration.Y_DRIVE_P, Calibration.Y_DRIVE_I, Calibration.Y_DRIVE_D,
                Calibration.ROT_DRIVE_P, Calibration.ROT_DRIVE_I, Calibration.ROT_DRIVE_D,
                Calibration.X_TOP_SPEED, Calibration.Y_TOP_SPEED, Calibration.ROT_TOP_SPEED, dash);

        teleopDrive = new FieldOrientedDrive(pIDDrive, placeTracker.getRotPIDSource());
        ((FieldOrientedDrive) teleopDrive).disableFieldOrientedControl();

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
        
        teleopDrive.setXYRot(keyMap.getXDriveAxis() * gear, keyMap.getYDriveAxis() * gear,
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

        if (keyMap.getp()) {
            p += 0.5;
            System.out.println("p: " + p);
        }
        if (keyMap.getpd()) {
            p -= 0.1;
            System.out.println("p: " + p);
        }
        if (keyMap.geti()) {
            i += 0.5;
            System.out.println("i: " + i);
        }
        if (keyMap.getid()) {
            i -= 0.1;
            System.out.println("i: " + i);
        }
        if (keyMap.getd()) {
            d += 0.5;
            System.out.println("d: " + d);
        }
        if (keyMap.getdd()) {
            d -= 0.1;
            System.out.println("d: " + d);
        }

        //pIDDrive.yPID.setPID(p, i, d);
        placeTracker.step();
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
