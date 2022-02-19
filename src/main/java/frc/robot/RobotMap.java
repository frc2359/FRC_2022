package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around. -- This is a standard created by the previous team (thanks!).
 */

public interface RobotMap { // Change this to an enum
    
    /* DRIVE ------------------------------------------------------------------------------------------------------------------------ */
    public static final int ID_DRIVE_FR = 0;
    public static final int ID_DRIVE_FL = 1;

    //This is the mapping of the buttons to the various functions of the robot
    public static final int  DRIVE_PORT = 0;

    // Drive Behaviors
    public static final boolean BRAKE_MODE_DRIVE = true; //false = coast; true = brake  
    public static final double DRIVE_SPEED_MULT = 1;
    //Controls mode for drive    -    https://store.ctr-electronics.com/content/api/java/html/enumcom_1_1ctre_1_1phoenix_1_1motorcontrol_1_1_talon_f_x_control_mode.html
    public static final ControlMode DRIVE_CONTROL_MODE = ControlMode.PercentOutput; //this controls in what unit drive is measured in.
    public static final ControlMode AUTO_CONTROL_MODE = ControlMode.Velocity;

    final int COUNTS_PER_REV = 2048;
    final double DRIVE_GEAR_RATIO = 0.25; //1:14.7959 0.0675862069
    final int DRIVE_DIAMETER = 4;

    /* SHOOTER---------------------------------------------------------------------------------------------------------------------------- */
    public static final int ID_SHOOTER_1 = 6;
    public static final int ID_SHOOTER_2 = 5;
    public static final int ID_SHOOTER_3 = 2;
    public static final double MAX_SHOOT_VELOCITY = 7500;
    
}
