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
    // Management of the available ports will be critical
    public static final int DIO_BALL_SENSOR_1 = 0;  // Moved to DIO 0 on robot
    public static final int DIO_BALL_SENSOR_2 = 2;  // Not using second ball sensor
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


    //cases
    public static final int STATE_DRIVEROP = 0;
    public static final int STATE_PREPARE = 1;
    public static final int STATE_CORRECT_DISTANCE = 2;
    public static final int STATE_CORRECT_ANGLE = 3;
    public static final int STATE_RESET = 4;

    // SwerveDrive FACTS
    public static final double Wheel_Width = 0; // width between two wheels (center to center)
    public static final double Wheel_Length = 0; // length between two wheel (center to center)

    
    public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 0; //  Set front left module drive motor ID
    public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 0; //  Set front left module steer motor ID
    public static final int FRONT_LEFT_MODULE_STEER_ENCODER = 0; //  Set front left steer encoder ID
    public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(0.0); //   Measure and set front left steer offset

    public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 0; //  Set front right drive motor ID
    public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 0; //  Set front right steer motor ID
    public static final int FRONT_RIGHT_MODULE_STEER_ENCODER = 0; //  Set front right steer encoder ID
    public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(0.0); //   Measure and set front right steer offset

    public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 0; //  Set back left drive motor ID
    public static final int BACK_LEFT_MODULE_STEER_MOTOR = 0; //  Set back left steer motor ID
    public static final int BACK_LEFT_MODULE_STEER_ENCODER = 0; //  Set back left steer encoder ID
    public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(0.0); //   Measure and set back left steer offset

    public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 0; //  Set back right drive motor ID
    public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 0; //  Set back right steer motor ID
    public static final int BACK_RIGHT_MODULE_STEER_ENCODER = 0; //  Set back right steer encoder ID
    public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(0.0); //   Measure and set back right steer offset


}
