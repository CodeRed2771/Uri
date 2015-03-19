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
   // Lift lift;
    //Extender extender;
    //PWMController extenderPWM;
    PWMController lift;
//    VirtualizableAnalogInput stringpot;

    DashBoard dash;

    double distance;
    long time;
    double angle;
    boolean disable = false;

    @Override
    public void robotInit() {
        DashBoard.setConnectionAddress("10.27.71.55");
        dash = new DashBoard();

        keyMap = new KeyMap();
        leds = new ControlsBoxLEDs(Wiring.RED_AND_GREEN_LEDS, Wiring.BLUE_LEDS);
       // lift = new Lift(dash);
        lift = new PWMController(4, false);

        placeTracker = new MechanumPlaceTracker(
                Wiring.REAR_RIGHT_ENCODER_A, Wiring.REAR_RIGHT_ENCODER_B,
                Wiring.FRONT_RIGHT_ENCODER_A, Wiring.FRONT_RIGHT_ENCODER_B,
                Wiring.FRONT_LEFT_ENCODER_A, Wiring.FRONT_LEFT_ENCODER_B,
                Wiring.REAR_LEFT_ENCODER_A, Wiring.REAR_LEFT_ENCODER_B, Wiring.GYRO,
                Calibration.X_SCALE, Calibration.Y_SCALE, Calibration.ROT_SCALE, dash);

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
                Calibration.X_TOP_SPEED, Calibration.Y_TOP_SPEED, Calibration.ROT_TOP_SPEED,
                -1 / Calibration.X_TOP_SPEED,
                -1 / Calibration.Y_TOP_SPEED,
                1 / Calibration.ROT_TOP_SPEED, dash);

        teleopDrive = new FieldOrientedDrive(pIDDrive, placeTracker.getRotPIDSource());
        ((FieldOrientedDrive) teleopDrive).disableFieldOrientedControl();

        //extender = new Extender(dash);
//        stringpot = new VirtualizableAnalogInput(Wiring.EXTENDER_STRING_POT);
    }

    @Override
    public void autonomousInit() {
        leds.activateAutonomous();
        distance = placeTracker.getLinearPIDSource().pidGet();
        angle = placeTracker.getRot();
    }

    @Override
    public void autonomousPeriodic() {
        placeTracker.step();

//        if (!lift.calibrate()) {
//            teleopDrive.setXYRot(0, 0, 0);
//            time = System.currentTimeMillis();
//        } else if (time + 800 > System.currentTimeMillis()) {
//            teleopDrive.setXYRot(0, 0, 0);
//            lift.set(1);
//            angle = placeTracker.getRot();
//        } else if (angle - 75 < placeTracker.getRot() && !disable) {
//            dash.prtln("rot: "+placeTracker.getRot(), 13);
//            teleopDrive.setXYRot(0, 0, 0.37);
//            lift.set(0);
//        } else if (distance + 23 > placeTracker.getLinearPIDSource().pidGet()) {
//            teleopDrive.setXYRot(0, -0.6, 0);
//            lift.set(0);
//            disable = true;
//        } else {
//            teleopDrive.setXYRot(0, 0, 0);
//        }
    }

    @Override
    public void teleopInit() {
        leds.activateTeleop();
    }

    @Override
    public void teleopPeriodic() {
        // ----- DRIVE ----- //
        double gear = 1;
        if (keyMap.getSlowButton()) {
            gear = .3;
        }

        teleopDrive.setXYRot(-keyMap.getXDriveAxis() * gear, -keyMap.getYDriveAxis() * gear,
                keyMap.getRotDriveAxis() * gear);

        if (keyMap.getReverseDriveButton()) {
            keyMap.toggleReverseDrive();
        }

        ///////////////////////////////
        lift.set(-keyMap.getLiftAxis());

//        if (keyMap.getToggleLiftFallback()) {
//            lift.togglePIDEnabled();
//        }

        ///////////////////////////////
        if (keyMap.getExtendButton()) {
            //extender.extend();
        }
        if (keyMap.getRetractButton()) {
            //extender.retract();
        }
        //extender.change(keyMap.getExtendAxis());
        //extenderPWM.set(keyMap.getExtendAxis());

        ///////////////////////////////
        if (keyMap.getSingleControllerToggleButton()) {
            keyMap.toggleSingleControllerMode();
        }
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
