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
    public static final int  HID_LIFTER_LOWER = 11; // Check Button
    public static final int  HID_LIFTER_RAISE = 12; // Check Button

    // ALLIANCE & BALL COLORS
    public static final int  COLOR_UNKNOWN = 0;
    public static final int  COLOR_BLUE = 1;
    public static final int  COLOR_RED = 2;

    // Drive Behaviors
    public static final boolean BRAKE_MODE_DRIVE = true; //false = coast; true = brake  
    public static final double DRIVE_SPEED_MULT = 1;
    public static final double TURN_SPEED_MULT = 1;
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
    // Management of the available ports will be critical
    public static final int DIO_BALL_SENSOR_1 = 0;  // Moved to DIO 0 on robot
    public static final int DIO_LED = 1;
    
    //public static final int DIO_BALL_SENSOR_2 = 2;  // Not using second ball sensor
    public static final int DIO_LIFTER_LOW_HOOK_ROT_HOME = 1; 
    public static final int DIO_LIFTER_LIFTING_HOOK_VERT_HOME = 2; 
    public static final int DIO_LIFTER_LEFT_HOOK_CLOSED = 3; 
    public static final int DIO_LIFTER_RIGHT_HOOK_CLOSED = 4;  
    public static final int DIO_LIFTER_LEFT_HOOK_LIMIT = 5;
    public static final int DIO_LIFTER_VERT_PIPE_DETECT = 6;
    public static final int DIO_LIFTER_HORIZ_PIPE_DETECT_LEFT = 7;
    public static final int DIO_LIFTER_HORIZ_PIPE_DETECT_RIGHT = 8;
    
    //PNEUMATICS CONTROLLER ---------------------
    public static final int ID_SOL_BALL_LIFTER = 7;
    public static final int ID_SOL_LIFTER_HOOK = 6;

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
    public static final int STATE_POSITION_SHOT = 10;
    public static final int STATE_REVERSE_COLLECTOR = 11;


    public static final double SHOOT_HIGH_POWER = 0.7;
    public static final double SHOOT_LOW_POWER = 0.5;
    public static final double SHOOT_LAUNCH_POWER = 1;
    public static final double SHOOT_AUTO = 0.7;

    //STATE FOR AUTOMATED SHOOTING CORRECTION
    public static final double LOW_GOAL_DISTANCE = 33.5;
    public static final double HIGH_GOAL_DISTANCE = 104;
    public static final double LIMELIGHT_MOUNT_ANGLE = 28;
    public static final double LIMELIGHT_MOUNT_HEIGHT = 35.8;

    //STATE FOR AUTONOMOUS OPERATIONS
    public static final int AUTO_LEAVE_TARMAC = 1;
    public static final int AUTO_SHOOT_1 = 2;
    public static final int AUTO_TURN = 3;
    public static final int AUTO_DRIVE_BACK = 4;
    public static final int AUTO_TURN_BACK = 5;
    public static final int AUTO_LIMELIGHT_DRIVE = 6;

    public static final int AUTO_CANCEL_TURN = 10;


    //cases for autoshoot
    public static final int STATE_DRIVEROP = 0;
    public static final int STATE_PREPARE = 1;
    public static final int STATE_CORRECT_DISTANCE = 2;
    public static final int STATE_CORRECT_ANGLE = 3;
    public static final int STATE_RESET = 4;
    public static final int STATE_CORRECT_DONE = 5;
    
    //LIFTER / CLIMBER ------------------------
    public static final int CAN_ID_LIFTER_LEFT = 12;
    public static final int CAN_ID_LIFTER_RIGHT = 11;
    public static final int CAN_ID_LIFTER_ARM_ROTATE = 13;  // TBD for TalonSRX  -- motor that rotates the arms
    public static final int CAN_ID_LIFTER_ARM_ROLLER = 14;  // TBD for TalonSRX  -- motor that moves robot left or right on bar

    
    /*
    public static final int ST_LIFTER_UNKNOWN = 0;
    public static final int ST_LIFTER_NOT_CLIMBING = 1; // not climbing, so driving, etc.
    public static final int ST_LIFTER_CALIBRATE = 2;
    public static final int ST_LIFTER_RAISE = 3;  // raise lifter abover bar
    public static final int ST_LIFTER_RAISED = 4;  // lifter raised
    public static final int ST_LIFTER_LOWER = 5;  // lower lifter to home position
    public static final int ST_LIFTER_HOME = 6;  // lifter at home position
    // public static final int ST_
    public static final int ST_LINE_UP = 7; */
    public static final int ST_LIFTER_UNKNOWN = 0;
    public static final int ST_LIFTER_PREPARE = 1;
    public static final int ST_TO_BAR = 2;
    public static final int ST_ABOVE_BAR = 3;
    public static final int ST_LIFT_INITIAL = 4;
    public static final int ST_TRAVERSE_GRAB_NEXT = 6;
    public static final int ST_SECURE_NEXT = 7;

    public static final int ST_LIFTER_CALIBRATE = 12;
    public static final int ST_LIFTER_NOT_CLIMBING = 13;
    public static final int ST_LIFTER_ON_BAR = 14;
    public static final int ST_TRAVERSE = 15;
    



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
