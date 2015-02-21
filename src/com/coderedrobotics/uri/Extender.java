package com.coderedrobotics.uri;

import com.coderedrobotics.libs.PIDControllerAIAO;
import com.coderedrobotics.libs.PWMController;
import com.coderedrobotics.libs.VirtualizableAnalogInput;
import com.coderedrobotics.uri.statics.Calibration;
import com.coderedrobotics.uri.statics.Wiring;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 *
 * @author michael
 */
public class Extender implements PIDOutput {
    
    private VirtualizableAnalogInput stringPot;
    private final PIDControllerAIAO pid;
    private final PWMController controller;
    private final double calibration;
    
    public Extender() {
        pid = new PIDControllerAIAO(Calibration.EXTENDER_P, Calibration.EXTENDER_I, 
                Calibration.EXTENDER_D, stringPot, this, "extender");
        stringPot = new VirtualizableAnalogInput(Wiring.EXTENDER_STRING_POT);
        controller = new PWMController(Wiring.EXTENDER_MOTOR, false);
        calibration = Calibration.EXTENDER_POT_DISTANCE;
        double moveSpeed = Calibration.EXTENDER_MOVE_SPEED;
        pid.setOutputRange(-moveSpeed, moveSpeed);
    }

    public void extend() {
        pid.enable();
        pid.setSetpoint(calibration);
    }
    
    public void retract() {
        pid.disable();
    }
    
    public boolean isExtended() {
        return pid.isEnable();
    }
    
    public boolean isReracted() {
        return !isExtended();
    }
    
    @Override
    public void pidWrite(double output) {
        controller.set(output);
    }
    
}
