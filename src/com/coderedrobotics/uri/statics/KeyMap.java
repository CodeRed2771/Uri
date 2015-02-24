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
    private final Axis extenderAxis = LogitechF310.STICK_RIGHT_Y;
    private final Button reverseDriveButton = LogitechF310.BUMPER_LEFT;
    private final Button singleControllerModeButton = LogitechF310.STICK_RIGHT;
    private final Button slowButton = LogitechF310.BUMPER_RIGHT;
    // CONTROLLER 2
    
    // BUTTON STATES
    private final HID.ButtonState reverseDriveButtonState = HID.newButtonState();
    private final HID.ButtonState singleControllerModeState = HID.newButtonState();
    private final HID.ButtonState gearStateA = HID.newButtonState();
    private final HID.ButtonState gearStateB = HID.newButtonState();

    private final HID.ButtonState p = HID.newButtonState();
    private final HID.ButtonState pd = HID.newButtonState();
    private final HID.ButtonState i = HID.newButtonState();
    private final HID.ButtonState id = HID.newButtonState();
    private final HID.ButtonState d = HID.newButtonState();
    private final HID.ButtonState dd = HID.newButtonState();
    
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
        return getHID(gamepad1).buttonPressed(reverseDriveButton, reverseDriveButtonState);
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
        return getHID(gamepad1).buttonPressed(singleControllerModeButton, singleControllerModeState);
    }

    public double getYDriveAxis() {
        if (reverseDrive) {
            return -getHID(gamepad1).axis(driveYAxis);
        } else {
            return getHID(gamepad1).axis(driveYAxis);
        }
    }
    
    public double getXDriveAxis() {
        if (reverseDrive) {
            return -getHID(gamepad1).axis(driveXAxis);
        } else {
            return getHID(gamepad1).axis(driveXAxis);
        }
    }
    
    public double getRotDriveAxis() {
        return getHID(gamepad1).axis(driveRotAxis);
    }
    
    public double getLiftAxis() {
        return getHID(gamepad2).axis(liftAxis);
    }
    
    public double getExtendAxis() {
        return getHID(gamepad2).axis(extenderAxis);
    }
    
    public boolean getSlowButton() {
        return getHID(gamepad1).button(slowButton);
    }

    public boolean getp(){return getHID(gamepad2).buttonPressed(LogitechF310.DPAD_UP, p);}
    public boolean getpd(){return getHID(gamepad2).buttonPressed(LogitechF310.DPAD_DOWN, pd);}
    public boolean geti(){return getHID(gamepad2).buttonPressed(LogitechF310.STICK_LEFT_UP, i);}
    public boolean getid(){return getHID(gamepad2).buttonPressed(LogitechF310.STICK_LEFT_DOWN, id);}
    public boolean getd(){return getHID(gamepad2).buttonPressed(LogitechF310.STICK_RIGHT_UP, d);}
    public boolean getdd(){return getHID(gamepad2).buttonPressed(LogitechF310.STICK_RIGHT_DOWN, dd);}
}
