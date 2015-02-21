package com.coderedrobotics.uri;

import com.coderedrobotics.libs.PIDControllerAIAO;
import com.coderedrobotics.libs.PWMController;
import com.coderedrobotics.libs.VirtualizablePsuedoDigitalInput;
import com.coderedrobotics.uri.statics.Calibration;
import com.coderedrobotics.uri.statics.Wiring;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 *
 * @author michael
 */
public class Lift implements PIDOutput {

    private final PIDControllerAIAO pid;
    private final PWMController controller;
    private final VirtualizablePsuedoDigitalInput limitSwitch;
    private final Encoder encoder;
    private boolean calibrated = false;
    private int calibration;
    private final double maxDistance;

    public Lift() {
        this.controller = new PWMController(Wiring.LIFT_MOTOR, false);
        encoder = new Encoder(Wiring.LIFT_ENCODER_A, Wiring.LIFT_ENCODER_B);
        limitSwitch = new VirtualizablePsuedoDigitalInput(Wiring.LIFT_LIMIT_SWITCH);
        pid = new PIDControllerAIAO(Calibration.LIFT_P, Calibration.LIFT_I, 
                Calibration.LIFT_D, encoder, this, "lift");
        maxDistance = Calibration.LIFT_MAX_DISTANCE;
    }

    public void set(double speed) {
        if (!calibrated && speed < 0 || calibrated) {
            move(speed);
        }
    }

    private void move(double speed) {
        if (calibrated) {
            if (!limitSwitch.getState() && encoder.get() < calibration + maxDistance) {
                controller.set(speed);
            } else {
                controller.set(0);
            }
        } else {
            if (!limitSwitch.getState() && speed < 0) {
                controller.set(speed);
            } else {
                controller.set(0);
            }
        }
    }

    public boolean isCalibrated() {
        return calibrated;
    }

    public boolean calibrate() {
        if (!limitSwitch.getState()) {
            move(-1);
            calibrated = false;
        } else {
            move(0);
            calibration = encoder.get();
            calibrated = true;
        }
        return calibrated;
    }

    @Override
    public void pidWrite(double output) {
        move(output);
    }

}
