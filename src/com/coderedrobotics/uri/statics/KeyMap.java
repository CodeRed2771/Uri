package com.coderedrobotics.uri.statics;

import com.coderedrobotics.libs.HID.HID;
import com.coderedrobotics.libs.HID.HID.Axis;
import com.coderedrobotics.libs.HID.HID.Button;
import com.coderedrobotics.libs.HID.LogitechF310;

/**
 *
 * @author Michael
 */
public class KeyMap {
       
    // GAMEPADS
    private final HID gp1 = new HID(0);
    private final HID gp2 = new HID(1);
    private final int gamepad1 = 0;
    private final int gamepad2 = 1;    
    
    // MANAGEMENT BOOLEANS
    private boolean reverseDrive = false;
    private boolean singleControllerMode = false;
    
    // CONTROLLER 1
    private final Axis driveYAxis = LogitechF310.STICK_LEFT_Y;
    private final Axis driveXAxis = LogitechF310.STICK_LEFT_X;
    private final Axis driveRotAxis = LogitechF310.STICK_RIGHT_X;
    private final Axis liftAxis = LogitechF310.DPAD_Y;
    private final Button reverseDriveButton = LogitechF310.BUMPER_LEFT;
    private final Button singleControllerModeButton = LogitechF310.STICK_RIGHT;
    private final Button gearButton = LogitechF310.BUMPER_RIGHT;
    // CONTROLLER 2
    
    // BUTTON STATES
    private final HID.ButtonState reverseDriveButtonState = HID.newButtonState();
    private final HID.ButtonState singleControllerModeState = HID.newButtonState();
    private final HID.ButtonState gearStateA = HID.newButtonState();
    private final HID.ButtonState gearStateB = HID.newButtonState();
    
    public KeyMap() {
        
    }
    
    private HID getHID(int gamepad) {
        if (!singleControllerMode) {
            switch (gamepad) {
                case gamepad1:
                    return gp1;
                case gamepad2:
                    return gp2;
                default:
                    return null;
            }
        } else {
            return gp1;
        }
    }
    
    public boolean getReverseDriveButton() {
        return getHID(1).buttonPressed(reverseDriveButton, reverseDriveButtonState);
    }

    public void toggleReverseDrive() {
        reverseDrive = !reverseDrive;
    }
    
    public boolean getReverseDrive() {
        return reverseDrive;
    }

    public void setSingleControllerMode(boolean state) {
        singleControllerMode = state;
    }

    public boolean getSingleControllerMode() {
        return singleControllerMode;
    }

    public void toggleSingleControllerMode() {
        singleControllerMode = !singleControllerMode;
    }
    
    public boolean getSingleControllerToggleButton() {
        return getHID(1).buttonPressed(singleControllerModeButton, singleControllerModeState);
    }

    public double getYDriveAxis() {
        double val = getHID(gamepad1).axis(driveYAxis);
        val *= getHID(gamepad1).buttonToggled(gearButton, gearStateA, gearStateB) ? 0.4d : 1d;
        if (reverseDrive) {
            return -val;
        } else {
            return val;
        }
    }
    
    public double getXDriveAxis() {
        double val = getHID(gamepad1).axis(driveXAxis);
        val *= getHID(gamepad1).buttonToggled(gearButton, gearStateA, gearStateB) ? 0.4d : 1d;
        if (reverseDrive) {
            return -val;
        } else {
            return val;
        }
    }
    
    public double getRotDriveAxis() {
        return getHID(gamepad1).axis(driveRotAxis) *
                (getHID(gamepad1).buttonToggled(gearButton, gearStateA, gearStateB) ? 0.4d : 1d);
    }
    
    public double getLiftAxis() {
        return getHID(gamepad1).axis(liftAxis);
    }
}
