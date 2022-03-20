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

    //This is the mapping of controllers to their ports
    public static final int  DRIVE_PORT = 0;
    public static final int  SHOOT_PORT = 1;
    public static final int  HID_PORT = 3;

    // HID Buttons
    public static final int  HID_COLLECTOR_OFF = 1;
    public static final int  HID_COLLECTOR_ON = 2;
    public static final int  HID_COLLECTOR_REVERSE = 3;
    public static final int  HID_SHOOT_AUTO_MODE = 4;
    public static final int  HID_SHOOT_HIGH = 5;
    public static final int  HID_SHOOT_LOW = 6;
    public static final int  HID_SHOOT_LAUNCH_PAD = 7;
    public static final int  HID_SHOOT_EJECT = 8;

    // Drive Behaviors
    public static final boolean BRAKE_MODE_DRIVE = true; //false = coast; true = brake  
    public static final double DRIVE_SPEED_MULT = 1;
    //Controls mode for drive    -    https://store.ctr-electronics.com/content/api/java/html/enumcom_1_1ctre_1_1phoenix_1_1motorcontrol_1_1_talon_f_x_control_mode.html
    public static final ControlMode DRIVE_CONTROL_MODE = ControlMode.PercentOutput; //this controls in what unit drive is measured in.
    public static final ControlMode AUTO_CONTROL_MODE = ControlMode.Velocity;

    final int COUNTS_PER_REV = 2048;
    final double DRIVE_GEAR_RATIO = 1 / 10.71; //1:14.7959 0.0675862069 10.71:1
    final int DRIVE_DIAMETER = 6;
    final int DRIVE_RADIUS = DRIVE_DIAMETER / 2;

    //LED-----------------
    public static final int ID_LED = 1;

    /* SHOOTER---------------------------------------------------------------------------------------------------------------------------- */
    public static final int ID_SHOOTER_1 = 6;
    public static final int ID_SHOOTER_2 = 5;
    public static final int ID_SHOOTER_3 = 2;
    public static final double MAX_SHOOT_VELOCITY = 7500;

    //COLLECTOR-----------------------------------------------------------------------------------------------------
    public static final int ID_PNEUMATIC_HUB = 1;
    public static final int ID_SOLENOID_CHANNEL = 7;
    public static final int ID_INTAKE_MOTOR = 3;

    //STATE
    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_NOT_COLLECTING = 1;
    public static final int STATE_COLLECTING = 2;
    public static final int STATE_SECURE_BALL = 3;
    public static final int STATE_PREPARE_TO_SHOOT = 4;
    public static final int STATE_SHOOT = 5;
    public static final int STATE_REVERSE_COLLECTOR = 11;
    
    //AUTO ------
    public static final int ST_AUTO_START = 0;
    public static final int ST_AUTO_DRIVE_BACK1 = 1;
    public static final int ST_AUTO_SHOOT1 = 2;
    public static final int ST_AUTO_DRIVE_BACK2 = 3;
    public static final int ST_AUTO_CHECK_2ND_BALL = 4;
    public static final int ST_AUTO_PAUSE = 5;
    public static final int ST_AUTO_DRIVE_FORWARD = 6;
    public static final int ST_AUTO_SHOOT2 = 7;
    public static final int ST_AUTO_DONE = 8;
}
