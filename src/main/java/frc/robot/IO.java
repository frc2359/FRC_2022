package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
// import edu.wpi.first.wpilibj.GenericHID.*;
// import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;
import static frc.robot.RobotMap.*;
import edu.wpi.first.math.util.Units;
import java.lang.Math;

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

    /**gets the amount of tilt in the y-axis for directional steering**/
    public static double getLeftYAxis(boolean isDriver) {
        SmartDashboard.putNumber(("LeftY"), (isDriver ? driver : shootController).getLeftY());
        return (isDriver ? driver : shootController).getLeftY();
    }

    /**gets the amount of tilt in the y-axis for velocity control**/
    public static double getRightYAxis(boolean isDriver) {
        SmartDashboard.putNumber(("RightY"), (isDriver ? driver : shootController).getRightY());
        return (isDriver ? driver : shootController).getRightY();
    }

    /***gets the angle that the joystick is rotated at **/
    // public static double getLeftJoyAngle(){
    //     int add = Math.cos(getRightYAxis(true)/getRightXAxis(true)) < 0 ? 180 : 0;
    //     return Math.atan(getLeftYAxis(true)/getLeftXAxis(true)) * (180/Math.PI);
    // }

    /***gets the angle that the joystick is rotated at **/
    public static double getRJoyAngle(){
        int add = Math.cos(getRightYAxis(true)/getRightXAxis(true)) < 0 ? 180 : 0;
        int m = getDriveMagnitude() == 0 ? 0 : 1;
        int s = getRightYAxis(true) < 0 ? 1 : -1;
        double ret = s * m * Math.abs(Math.toDegrees(Math.atan(getRightYAxis(true)/getRightXAxis(true))));
        return IO.getDriveMagnitude() > 0.1 ? ret : 0;
    }

    public static double getDriveMagnitude() {
        double c = Math.sqrt(Math.pow(getRightYAxis(true), 2) + Math.pow(getRightXAxis(true), 2));
        return c > 1 ? 1 : c > 0.1 ? c : 0;
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

    /**gets whether the A button on the controller has been released**/
    public static boolean startButtonIsPressed(boolean isDriver) {
        return (isDriver ? driver : shootController).getStartButtonPressed();
    }

    /**gets whether the A button on the controller has been released**/
    public static boolean isHIDButtonPressed(int button, boolean isDriver) {
        return (isDriver ? driver : otherController).getRawButton(button);
    }

    /**gets the amount of tilt in the x-axis for directional steering**/
    public static double getHIDAxis(int axis) {
        return otherController.getRawAxis(axis);
    }
    
    public static int getPOV(boolean isDriver){
        return (isDriver ? driver : otherController).getPOV();
    }

    /**gets the team color from FMS**/
    public static boolean isAllianceBlue() {
        return (DriverStation.getAlliance() == DriverStation.Alliance.Blue ? true : false); 
    }

    public static boolean isAllianceRed() { 
        return (DriverStation.getAlliance() == DriverStation.Alliance.Red ? true : false);    
    }

    public static int getAllianceColor() {
        if (DriverStation.getAlliance() == DriverStation.Alliance.Blue) {
            return COLOR_BLUE;
        }
        else if (DriverStation.getAlliance() == DriverStation.Alliance.Red) {
            return COLOR_RED;
        }
        else {
            return COLOR_UNKNOWN; //COLOR_BLUE;
        }   
    }

    public static double getLimelightXAngle() {
        NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");
        NetworkTableEntry tx = limelight.getEntry("tx");
        return tx.getDouble(0.0);
    }

    public static double getLimelightYAngle() {
        NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");
        NetworkTableEntry ty = limelight.getEntry("ty");
        return ty.getDouble(0.0);
    }

    public static double calculateDistance(double limelightMountAngleDegrees, double limelightLensHeightInches, double goalHeightInches) {
        double angleToGoalDegrees = limelightMountAngleDegrees + getLimelightYAngle();
        double angleToGoalRadians = Units.degreesToRadians(angleToGoalDegrees);
        return (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);
    }

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
