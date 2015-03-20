package com.coderedrobotics.uri;

import com.coderedrobotics.libs.PIDDerivativeCalculator;
import com.coderedrobotics.libs.PWMController;
import com.coderedrobotics.libs.VirtualizablePsuedoDigitalInput;
import com.coderedrobotics.libs.dash.DashBoard;
import com.coderedrobotics.libs.dash.PIDControllerAIAO;
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
    private final DashBoard dash;

    private boolean calibrated = false;
    private int calibration;
    private final double maxDistance;
    private boolean manualMode;
    private final double minDistance;

    public Lift(DashBoard dash) {
        this.dash = dash;
        this.controller = new PWMController(Wiring.LIFT_MOTOR, true);
        encoder = new Encoder(Wiring.LIFT_ENCODER_A, Wiring.LIFT_ENCODER_B);
        limitSwitch = new VirtualizablePsuedoDigitalInput(Wiring.LIFT_LIMIT_SWITCH, dash);
        pid = new PIDControllerAIAO(Calibration.LIFT_P, Calibration.LIFT_I,
                Calibration.LIFT_D, new PIDDerivativeCalculator(encoder), this, this.dash, "lift");
        maxDistance = Calibration.LIFT_MAX_DISTANCE;
        minDistance = Calibration.LIFT_MIN_DISTANCE;
    }

    public void set(double speed) {
        if (pid.isEnable()) {
            pid.setSetpoint(speed * 2.5);
        } else {
            move(speed);
        }
        if (dash != null) {
            dash.prtln("Lift Encoder: " + encoder.getRaw(), 3);
            dash.prtln("Lift Limit: " + limitSwitch.getState(), 4);
        }
    }

    public void disablePID() {
        pid.disable();
    }

    public void enablePID() {
        pid.enable();
    }

    public void setPIDEnabled(boolean enable) {
        if (enable) {
            enablePID();
        } else {
            disablePID();
        }
    }

    public void togglePIDEnabled() {
        setPIDEnabled(!pid.isEnable());
    }

    private void move(double speed) {
        if (calibrated) {
            if ((speed > 0 && encoder.getRaw() > calibration + maxDistance)
                    || (speed < 0 && encoder.getRaw() < calibration + minDistance)) {
                controller.set(0);
            } else {
                controller.set(speed);
            }
        } else {
            controller.set(Math.max(-0.4, Math.min(0, speed)));
            if (!limitSwitch.getState()) {
                calibrated = true;
                calibration = encoder.getRaw();
                pid.setSetpoint(calibration);
                pid.enable();
                move(speed);
            }
        }
    }

    public boolean isCalibrated() {
        return calibrated;
    }

    public boolean calibrate() {
        if (!isCalibrated()) move(-1);
        return calibrated;
    }

    @Override
    public void pidWrite(double output) {
        move(output);
    }
}
