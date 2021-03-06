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
import static com.coderedrobotics.libs.ControlsBoxLEDs.*;

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
    Lift lift;
    Extender extender;
    //PWMController extenderPWM;
    // PWMController lift;
//    VirtualizableAnalogInput stringpot;

    DashBoard dash;

    double distance;
    double encTicksPerInch = 92;
    long time;
    double angle;
    boolean disable = false;

    int stage;
    private enum AutonType {
        NONE, LANDFILL, YELLOW_TOTE
    }
    private AutonType autonType = AutonType.YELLOW_TOTE;
    private long startAutonConfirmTime;

    @Override
    public void robotInit() {
        DashBoard.setConnectionAddress("10.27.71.55");
        //dash = new DashBoard();

        keyMap = new KeyMap();
        leds = new ControlsBoxLEDs(Wiring.RED_AND_GREEN_LEDS, Wiring.BLUE_LEDS);
        lift = new Lift(dash);
        //  lift = new PWMController(4, false);

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

        extender = new Extender(dash);
//        stringpot = new VirtualizableAnalogInput(Wiring.EXTENDER_STRING_POT);
    }

    private int colorcounter = 0;
    private long lastcolortime = 0;
    @Override
    public void autonomousInit() {
        lastcolortime = System.currentTimeMillis();
    }

    @Override
    public void autonomousPeriodic() {
        if (System.currentTimeMillis() - 1000 > lastcolortime) {
          lastcolortime = System.currentTimeMillis();
          colorcounter++;
          if (colorcounter == 7) {
            colorcounter = 0;
          }
        }
        switch (colorcounter) {
          case 0:
            leds.setColor(Color.RED);
            break;
          case 1:
            leds.setColor(Color.GREEN);
            break;
          case 2:
            leds.setColor(Color.WHITE);
            break;
          case 3:
            leds.setColor(Color.YELLOW);
            break;
          case 4:
            leds.setColor(Color.BLUE);
            break;
          case 5:
            leds.setColor(Color.MAGENTA);
            break;
          case 6:
            leds.setColor(Color.CYAN);
            break;
        }
    }

/* normal code:
    @Override
    public void autonomousInit() {
        leds.activateAutonomous;
        // CHANGE distance = placeTracker.getLinearPIDSource().pidGet();
        distance = placeTracker.leftFrontEncoder.getRaw();
        angle = placeTracker.getRot();
        System.out.println("Distance: " + distance);
    }

    @Override
    public void autonomousPeriodic() {
        placeTracker.step();

//        System.out.println("PLACE TRACKER: " + placeTracker.getLinearPIDSource().pidGet());
//        System.out.println("LB: " + placeTracker.leftBackEncoder.getRaw()
//                + " RB: " + placeTracker.rightBackEncoder.getRaw()
//                + " LF: " + placeTracker.leftFrontEncoder.getRaw()
//                + " RF: " + placeTracker.rightFrontEncoder.getRaw());
//
//        // CHANGE } else if (distance + 70 > placeTracker.getLinearPIDSource().pidGet()) {
//        if (!lift.calibrate()) {
//            teleopDrive.setXYRot(0, 0, 0);
//            time = System.currentTimeMillis();
//        } else if (time + 800 > System.currentTimeMillis()) {
//            teleopDrive.setXYRot(0, 0, 0);
//            lift.set(1);
//            angle = placeTracker.getRot();
//        } else if (angle - 75 < placeTracker.getRot() && !disable) {
//            teleopDrive.setXYRot(0, 0, 0.37);
//            lift.set(0);
//        } else if (distance + (70 * encTicksPerInch) > placeTracker.leftFrontEncoder.getRaw()) {
//            // if we change the 23, I'd say go to 1.75 * 23 = 40
//            teleopDrive.setXYRot(0, -0.5, 0);
//            lift.set(0);
//            disable = true;
//            System.out.println("LB: " + placeTracker.leftBackEncoder.getRaw()
//                    + " RB: " + placeTracker.rightBackEncoder.getRaw()
//                    + " LF: " + placeTracker.leftFrontEncoder.getRaw()
//                    + " RF: " + placeTracker.rightFrontEncoder.getRaw());
//        } else {
//            teleopDrive.setXYRot(0, 0, 0);
//        }

        switch (stage) {
            case 0:
                if (!lift.calibrate()) {
                    teleopDrive.setXYRot(0, 0, 0);
                    time = System.currentTimeMillis();
                } else {
                    stage = 1;
                }
                break;
            case 1:
                if (time + 800 > System.currentTimeMillis()) {
                    teleopDrive.setXYRot(0, 0, 0);
                    lift.set(1);
                    angle = placeTracker.getRot();
                } else {
                    stage = 2;
                }
                break;
            case 2:
                if (angle - 75 < placeTracker.getRot()) {
                    teleopDrive.setXYRot(0, 0, 0.37);
                    lift.set(0);
                } else {
                    stage = 3;
                    distance = placeTracker.leftFrontEncoder.getRaw();
                }
                break;
            case 3:
                if (distance + (375 * encTicksPerInch) > placeTracker.leftFrontEncoder.getRaw()) {
                    // if we change the 23, I'd say go to 1.75 * 23 = 40
                    teleopDrive.setXYRot(0, -0.5, 0);
                    lift.set(0);
                    System.out.println("LB: " + placeTracker.leftBackEncoder.getRaw()
                            + " RB: " + placeTracker.rightBackEncoder.getRaw()
                            + " LF: " + placeTracker.leftFrontEncoder.getRaw()
                            + " RF: " + placeTracker.rightFrontEncoder.getRaw());
                } else {
                    stage = 4;
                }
                break;
            case 4:
                teleopDrive.setXYRot(0, 0, 0);
                break;
        }
    }
*/
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
        lift.set(keyMap.getLiftAxis());
        if (keyMap.getLiftAxis() > 0) {
          leds.setColor(Color.YELLOW);
          leds.setHz(2);
        } else if (keyMap.getLiftAxis() < 0) {
          leds.setColor(Color.BLUE);
          leds.setHz(2);
        } else {
          leds.setColor(Color.RED);
        }
        leds.blinkTick();

        if (keyMap.getToggleLiftFallback()) {
            lift.togglePIDEnabled();
        }

        if (keyMap.getForceCalibrationButton()) {
            if (!lift.isCalibrated()) {
                lift.forceCalibrate();
            }
        }

        ///////////////////////////////
        if (keyMap.getExtendButton()) {
            extender.extend();
        }
        if (keyMap.getRetractButton()) {
            extender.retract();
        }
        extender.change(keyMap.getExtendAxis());

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
        if (keyMap.getNoAutonButton()) {
            autonType = AutonType.NONE;
            startAutonConfirmTime = System.currentTimeMillis();
        }

        if (keyMap.getYellowToteAutonButton()) {
            autonType = AutonType.YELLOW_TOTE;
            startAutonConfirmTime = System.currentTimeMillis();
        }

        if (keyMap.getLandfillAutonButton()) {
            autonType = AutonType.LANDFILL;
            startAutonConfirmTime = System.currentTimeMillis();
        }

        if (startAutonConfirmTime + 1000 > System.currentTimeMillis()) {
            keyMap.confirmAuton();
        } else {
            keyMap.stopConfirmAuton();
        }
    }
}
