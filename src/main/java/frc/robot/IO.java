package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
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
    //NetworkTables values
    private static NetworkTableInstance rpi3;
    private static NetworkTable table;
    private static NetworkTableEntry ultrasonicReading;

    /**Returns whether or not the trigger mapped to the throttle is pressed.**/
    public static boolean throttleTriggerIsPressed() {
        return driver.getRightTriggerAxis() > 0 ? true : false;
    }
    
    /**Returns the current value of the trigger mapped to the throttle.**/
    public static double getDriveTrigger() {
        return driver.getRightTriggerAxis();
    }

    /**Returns whether or not the trigger mapped to reverse is pressed.**/
    public static boolean reverseTriggerIsPressed() {
        return driver.getLeftTriggerAxis() > 0 ? true : false;
    }

    /**Returns the current value of the trigger mapped to the reverse.**/
    public static double getReverseTrigger() {
        return driver.getLeftTriggerAxis();
    }

    /**gets the amount of tilt in the x-axis for directional steering**/
    public static double getLeftXAxis() {
        SmartDashboard.putNumber(("LeftX"), driver.getLeftX());
        return driver.getLeftX();
    }

    /**gets the amount of tilt in the x-axis for velocity control**/
    public static double getRightXAxis() {
        SmartDashboard.putNumber(("RightX"), driver.getRightX());
        return driver.getRightX();
    }

    /**gets throttle value (negative is backwards, positive is forwards)**/
    public static double getThrottle() {
        return (getDriveTrigger() - getReverseTrigger()) * DRIVE_SPEED_MULT;
    }

    /**gets whether the B button on the controller has been pressed**/
    public static boolean bButtonIsPressed() {
        return driver.getBButtonPressed();
    }

    /**gets whether the B button on the controller has been released**/
    public static boolean bButtonIsReleased() {
        return driver.getBButtonReleased();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean aButtonIsPressed() {
        return driver.getAButtonPressed();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean aButtonIsReleased() {
        return driver.getAButtonReleased();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean xButtonIsPressed() {
        return driver.getXButtonPressed();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean xButtonIsReleased() {
        return driver.getXButtonReleased();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean yButtonIsPressed() {
        return driver.getYButtonPressed();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean yButtonIsReleased() {
        return driver.getYButtonReleased();
    }

    public static void initNetworkTables() {
        rpi3 = NetworkTableInstance.getDefault();
        table = rpi3.getTable("datatable");
    }

    /**Gets distance calculated by LiDAR sensors on the robot - we used this for our goal detection */
    public static double getLidarDistance() {
        return table.getEntry("lidardist").getDouble(-1);
    }

    /**Gets distance caluclated by Microsoft webcams with triangulation (no fancy LiDAR) - we used this for our ball detection */
    public static double getVisionDistance() {
        return table.getEntry("visiondist").getDouble(-1);
    }

    /**gets the current travel distance of the current encoder */
    public static double getDriveDistance(double left, double right, boolean isAvg) {
        double distance;
        double rawPosition = isAvg ? ((left + right) /2) : ((left > right) ? left : right);
        distance = (rawPosition / COUNTS_PER_REV) * DRIVE_GEAR_RATIO * DRIVE_DIAMETER;
        return distance;
    }

    /**Gets the left stick value when a is pushed - mod for setting intake velocity */
    public static double getIntakeX(boolean isLeft) {
        if (aButtonIsPressed() && isLeft) {
            return driver.getLeftTriggerAxis();
        } else if (aButtonIsPressed() && !isLeft) {
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
