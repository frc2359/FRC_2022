package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around. -- This is a standard created by the previous team (thanks!).
 */

public interface RobotMap { // Change this to an enum
    public static final int ID_DRIVE_FR = 0;
    public static final int ID_DRIVE_FL = 1;
    public static final int ID_DRIVE_BR = 2;
    public static final int ID_DRIVE_BL = 3;

    public static final double DRIVE_SPEED_MULT = 0.0675862069; //14M->50->14->56D

    //This is the mapping of the buttons to the various functions of the robot
    public static final int  DRIVE_PORT = 0;

    final int COUNTS_PER_REV = 2048;
    final double DRIVE_GEAR_RATIO = 0.0675862; //1:14.7959
    // final int COUNTS_PER_REV = 2084;
    // final double DRIVE_GEAR_RATIO = 0.07;
    final int DRIVE_DIAMETER = 4;

    //Controls mode    -    https://store.ctr-electronics.com/content/api/java/html/enumcom_1_1ctre_1_1phoenix_1_1motorcontrol_1_1_talon_f_x_control_mode.html
    public static final ControlMode DRIVE_CONTROL_MODE = ControlMode.PercentOutput; //this controls in what unit drive is measured in.
    public static final ControlMode AUTO_CONTROL_MODE = ControlMode.Velocity;

    // Motor Control Brake Modes
        //false = coast; true = brake
    public static final boolean BRAKE_MODE_DRIVE = true; //when the controller is moved back to a neutral position, the motors will STOP
}
