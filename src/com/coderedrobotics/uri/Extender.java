package com.coderedrobotics.uri;

import com.coderedrobotics.libs.dash.PIDControllerAIAO;
import com.coderedrobotics.libs.PWMController;
import com.coderedrobotics.libs.VirtualizableAnalogInput;
import com.coderedrobotics.libs.dash.DashBoard;
import com.coderedrobotics.uri.statics.Calibration;
import com.coderedrobotics.uri.statics.Wiring;

/**
 *
 * @author michael
 */
public class Extender {

    private VirtualizableAnalogInput stringPot;
    private final PIDControllerAIAO pid;
    private final PWMController controller;
    private final double maxCalibration;
    private final double minCalibration;
    private final DashBoard dash;

    public Extender(DashBoard dash) {
        this.dash = dash;
        controller = new PWMController(Wiring.EXTENDER_MOTOR, false);
        stringPot = new VirtualizableAnalogInput(Wiring.EXTENDER_STRING_POT);
        pid = new PIDControllerAIAO(Calibration.EXTENDER_P, Calibration.EXTENDER_I,
                Calibration.EXTENDER_D, stringPot, controller, dash, "extender");
        maxCalibration = Calibration.EXTENDER_POT_MAX;
        minCalibration = Calibration.EXTENDER_POT_MIN;
        pid.setOutputRange(0, Calibration.EXTENDER_MOVE_SPEED);
        pid.enable();
    }

    public void extend() {
        set(maxCalibration);
    }

    public void retract() {
        set(minCalibration);
    }

    public void change(double change) {
        set(pid.getSetpoint() + change);
        dash.prtln(""+stringPot.get(), 2);
    }

    public void set(double setpoint) {
        pid.setSetpoint(Math.max(minCalibration, Math.min(maxCalibration, setpoint)));
    }

//    public boolean isExtended() {
//        return pid.isEnable();
//    }

//    public boolean isReracted() {
//        return !isExtended();
//    }
}
