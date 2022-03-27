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
    public static final int  HID_AUTO_EJECT_MODE = 9;

    // ALLIANCE & BALL COLORS
    public static final int  COLOR_UNKNOWN = 0;
    public static final int  COLOR_BLUE = 1;
    public static final int  COLOR_RED = 2;

    // Drive Behaviors
    public static final boolean BRAKE_MODE_DRIVE = true; //false = coast; true = brake  
    public static final double DRIVE_SPEED_MULT = 1;
    public static final double TURN_SPEED_MULT = 0.5;
    //Controls mode for drive    -    https://store.ctr-electronics.com/content/api/java/html/enumcom_1_1ctre_1_1phoenix_1_1motorcontrol_1_1_talon_f_x_control_mode.html
    public static final ControlMode DRIVE_CONTROL_MODE = ControlMode.PercentOutput; //this controls in what unit drive is measured in.
    public static final ControlMode AUTO_CONTROL_MODE = ControlMode.Velocity;

    final int COUNTS_PER_REV = 2048;
    final double DRIVE_GEAR_RATIO = 1 / 10.71; //1:14.7959 0.0675862069 10.71:1
    final double DRIVE_DIAMETER = 6;
    final double DRIVE_RADIUS = DRIVE_DIAMETER / 2;
    final double DRIVE_RADIUS_FEET = DRIVE_RADIUS / 12;

    //LED-----------------
    public static final int ID_LED = 1;

    //ROBORIO DIGITAL IO PORTS ---------------------------------
    public static final int DIO_BALL_SENSOR_1 = 1;
    public static final int DIO_BALL_SENSOR_2 = 2;
    public static final int DIO_BALL_COLOR_BLUE = 3;   // remove - using REV Color Sensor instead
    public static final int DIO_BALL_COLOR_RED = 4;    // remove - using REV Color Sensor instead
    public static final int DIO_LIFTER_PADDLE_LEFT = 5;
    public static final int DIO_LIFTER_PADDLE_RIGHT = 6;
    public static final int DIO_LIFTER_HOME_LEFT = 7;
    public static final int DIO_LIFTER_HOME_RIGHT = 8;
    public static final int DIO_LIFTER_HOOK_OPEN_LEFT = 9;
    public static final int DIO_LIFTER_HOOK_OPEN_RIGHT = 10;
    public static final int DIO_LIFTER_HOOK_CLOSED_LEFT = 11;
    public static final int DIO_LIFTER_HOOK_CLOSED_RIGHT = 12;

    //PNEUMATICS CONTROLLER ---------------------
    public static final int ID_SOL_BALL_LIFTER = 7;
    public static final int ID_SOL_LIFTER_HOOK = 8;

    /* SHOOTER---------------------------------------------------------------------------------------------------------------------------- */
    public static final int ID_SHOOTER_1 = 6;
    public static final int ID_SHOOTER_2 = 5;
    public static final int ID_SHOOTER_3 = 2;
    public static final double MAX_SHOOT_VELOCITY = 7500;
    public static final double MAX_ACCEL = 1500; 
    
    //COLLECTOR-----------------------------------------------------------------------------------------------------
    public static final int ID_PNEUMATIC_HUB = 1;
    public static final int ID_INTAKE_MOTOR = 3;

    //STATE FOR COLLECTOR AND SHOOTER
    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_NOT_COLLECTING = 1;
    public static final int STATE_COLLECTING = 2;
    public static final int STATE_SECURE_BALL = 3;
    public static final int STATE_PREPARE_TO_SHOOT = 4;
    public static final int STATE_SHOOT = 5;
    public static final int STATE_REVERSE_COLLECTOR = 11;

    //STATE FOR AUTOMATED SHOOTING CORRECTION
    public static final int STATE_DRIVEROP = 0;
    public static final int STATE_CORRECT_DISTANCE = 1;
    public static final int STATE_CORRECT_ANGLE = 2;
    public static final int STATE_RESET = 3;
    
    //LIFTER ------------------------
    public static final int CAN_ID_LIFTER_LEFT = 11;
    public static final int CAN_ID_LIFTER_RIGHT = 12;
    public static final int CAN_ID_LIFTER_ARM = 13;

    public static final int ST_LIFTER_NOT_LIFTING = 1;
    

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

    //ARDUINO LED COLOR MAP
    public static final int LED_COLOR_BLACK = 0; // 0,0,0
    public static final int LED_COLOR_WHITE = 1; // 255,255,255
    public static final int LED_COLOR_RED = 2; // 255,0,0
    public static final int LED_COLOR_LIME = 3; // 0,255,0
    public static final int LED_COLOR_BLUE = 4; // 0,0,255
    public static final int LED_COLOR_YELLOW = 5; // 255,255,0
    public static final int LED_COLOR_CYAN = 6; // 0,255,255
    public static final int LED_COLOR_MAGENTA = 7; // 255,0,255 
    public static final int LED_COLOR_SILVER = 8; // 192,192,192
    public static final int LED_COLOR_GRAY = 9; // 128,128,128
    public static final int LED_COLOR_MAROON = 10; // 128,0,0
    public static final int LED_COLOR_OLIVE = 11; // 128,128,0
    public static final int LED_COLOR_GREEN = 12; // 128,128,0
    public static final int LED_COLOR_PURPLE = 13; // 128,0,128 
    public static final int LED_COLOR_TEAL = 14; // 0,128,128
    public static final int LED_COLOR_NAVY = 15; // 0,0,128

    //ARDUINO LED STRINGS
    public static final int LED_STRING_COLLECTOR = 0;
    public static final int LED_STRING_LIFTER_LEFT = 1;
    public static final int LED_STRING_LIFTER_RIGHT = 2;
    public static final int LED_STRING_UNDERBODY = 3;
    public static final int LED_STRING_UPPERBODY = 4;

}
