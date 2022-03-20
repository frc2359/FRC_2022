package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
// import edu.wpi.first.wpilibj.GenericHID.*;
// import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;
import static frc.robot.RobotMap.*;
// import com.ctre.phoenix.motorcontrol.can.BaseMotorController.*;
// import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;


/**
 * The IO class is primarily dedicated to handling the inputs and outputs from the XBOX Controller, as well as various telemetric data on the robot from our vision system
 * The XBOX controller is containerized in IO in order to ensure that we don't create an excess of traffic to the controller, as well as to ensure that the physical 
 * link to the controller can stay private, and so that we can specify combinations of button presses that would otherwise pollute other classes.
 */


public class IO {
    //Driver Controller
    private static XboxController driver = new XboxController(DRIVE_PORT);
    private static XboxController shootController = new XboxController(SHOOT_PORT);
    private static GenericHID otherController = new GenericHID(HID_PORT);
    //NetworkTables values
    private static NetworkTableInstance rpi3;
    private static NetworkTable table;
    private static NetworkTableEntry ultrasonicReading;

    /**Returns whether or not the trigger mapped to the throttle is pressed.**/
    public static boolean throttleTriggerIsPressed(boolean isDriver) {
        return (isDriver ? driver : shootController).getRightTriggerAxis() > 0 ? true : false;
    }
    
    /**Returns the current value of the trigger mapped to the throttle.**/
    public static double getDriveTrigger(boolean isDriver) {
        return (isDriver ? driver : shootController).getRightTriggerAxis();
    }

    /**Returns whether or not the trigger mapped to reverse is pressed.**/
    public static boolean reverseTriggerIsPressed(boolean isDriver) {
        return (isDriver ? driver : shootController).getLeftTriggerAxis() > 0 ? true : false;
    }

    /**Returns the current value of the trigger mapped to the reverse.**/
    public static double getReverseTrigger(boolean isDriver) {
        return (isDriver ? driver : shootController).getLeftTriggerAxis();
    }

    /**gets the amount of tilt in the x-axis for directional steering**/
    public static double getLeftXAxis(boolean isDriver) {
        SmartDashboard.putNumber(("LeftX"), (isDriver ? driver : shootController).getLeftX());
        return (isDriver ? driver : shootController).getLeftX();
    }

    /**gets the amount of tilt in the x-axis for velocity control**/
    public static double getRightXAxis(boolean isDriver) {
        SmartDashboard.putNumber(("RightX"), (isDriver ? driver : shootController).getRightX());
        return (isDriver ? driver : shootController).getRightX();
    }

    /**gets throttle value (negative is backwards, positive is forwards)**/
    public static double getThrottle() {
        return (getDriveTrigger(true) - getReverseTrigger(true)) * DRIVE_SPEED_MULT;
    }

    /**gets whether the B button on the controller has been pressed**/
    public static boolean bButtonIsPressed(boolean isDriver) {
        return (isDriver ? driver : shootController).getBButtonPressed();
    }

    /**gets whether the B button on the controller has been released**/
    public static boolean bButtonIsReleased(boolean isDriver) {
        return (isDriver ? driver : shootController).getBButtonReleased();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean aButtonIsPressed(boolean isDriver) {
        return (isDriver ? driver : shootController).getAButtonPressed();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean aButtonIsReleased(boolean isDriver) {
        return (isDriver ? driver : shootController).getAButtonReleased();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean xButtonIsPressed(boolean isDriver) {
        return (isDriver ? driver : shootController).getXButtonPressed();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean xButtonIsReleased(boolean isDriver) {
        return (isDriver ? driver : shootController).getXButtonReleased();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean yButtonIsPressed(boolean isDriver) {
        return (isDriver ? driver : shootController).getYButtonPressed();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean yButtonIsReleased(boolean isDriver) {
        return (isDriver ? driver : shootController).getYButtonReleased();
    }

    /**Initialize the NetworkTables for reading camera and distance data. */
    public static void initNetworkTables() {
        rpi3 = NetworkTableInstance.getDefault();
        table = rpi3.getTable("datatable");
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean isHIDButtonPressed(int button, boolean isDriver) {
        return (isDriver ? driver : otherController).getRawButton(button);
    }

    /**gets the team color from FMS**/
    public static boolean isAllianceBlue() {
        if (DriverStation.getAlliance() == DriverStation.Alliance.Blue)
            return true;
        else
            return false;
    }

    public static boolean isAllianceRed() {
        if (DriverStation.getAlliance() == DriverStation.Alliance.Red)
            return true;
        else
            return false;     
    }

    public static int getAllianceColor() {
        if (DriverStation.getAlliance() == DriverStation.Alliance.Blue) {
            return COLOR_BLUE;
        }
        else if (DriverStation.getAlliance() == DriverStation.Alliance.Red) {
            return COLOR_RED;
        }
        else {
            return COLOR_BLUE; //COLOR_UNKNOWN;
        }   
    }

    /**Gets distance calculated by LiDAR sensors on the robot - we used this for our goal detection */
   // public static double getLidarDistance() {
   //     return table.getEntry("lidardist").getDouble(-1);
   // }

    /**Gets distance caluclated by Microsoft webcams with triangulation (no fancy LiDAR) - we used this for our ball detection */
   // public static double getVisionDistance() {
   //     return table.getEntry("visiondist").getDouble(-1);
   // }

    /**Gets the left stick value when a is pushed - mod for setting intake velocity */
    public static double getIntakeX(boolean isLeft) {
        if (aButtonIsPressed(true) && isLeft) {
            return driver.getLeftTriggerAxis();
        } else if (aButtonIsPressed(true) && !isLeft) {
            return driver.getRightTriggerAxis();
        } else {
            return 0;
        }
    }

    //We are using IO to interface with SmartDashboard to reduce the amount of imports we will need to make per file

    /**Puts number of the "value" at the id of "key" */
    public static void putNumberToSmartDashboard(String id, double value) {
        SmartDashboard.putNumber(id,  value); //NOTE: WPILib refers to this "id" as a "key". Since the 2022 devs are familiar with HTML/JS, the "id" tag was more intuitive for us
    }

    /**Gets value of the number at the id of "key" */
    public static double getNumberFromSmartDashboard(String id, double defaultValue) {
        //NOTE: WPILib refers to this "id" as a "key". Since the 2022 devs are familiar with HTML/JS, the "id" tag was more intuitive for us
        return SmartDashboard.getNumber(id,  defaultValue);     
    }
}
