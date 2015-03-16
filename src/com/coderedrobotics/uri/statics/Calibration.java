package com.coderedrobotics.uri.statics;

/**
 *
 * @author Michael
 */
public class Calibration {
    
    // LIFT
    public static final double LIFT_MAX_DISTANCE = 8100;
    public static final double LIFT_MIN_DISTANCE = 200;
    public static final double LIFT_P = 0.8;
    public static final double LIFT_I = 0;
    public static final double LIFT_D = 0;
    
    // DRIVE
    public static final double X_DRIVE_P = -10;
    public static final double X_DRIVE_I = 0;
    public static final double X_DRIVE_D = 0;
    public static final double Y_DRIVE_P = -10;
    public static final double Y_DRIVE_I = 0;
    public static final double Y_DRIVE_D = 0;

    public static final double ROT_DRIVE_P = 2;
    public static final double ROT_DRIVE_I = 1;
    public static final double ROT_DRIVE_D = 0;
    
    public static final double X_SCALE = 0.013310328 / 8.064516129;
    public static final double Y_SCALE =  0.010912563;
    public static final double ROT_SCALE = 0.027147274;
    public static final double X_TOP_SPEED = 0.12 * 8.064516129; // 0.35?
    public static final double Y_TOP_SPEED = 0.12;
    public static final double ROT_TOP_SPEED = 0.2;
    
    // EXTENDER
    public static final double EXTENDER_P = 0.12;
    public static final double EXTENDER_I = 0;
    public static final double EXTENDER_D = 0;
    public static final double EXTENDER_MOVE_SPEED = 1;

    public static final double EXTENDER_POT_MAX = 3.3;
    public static final double EXTENDER_POT_MIN = 0.3;
}
