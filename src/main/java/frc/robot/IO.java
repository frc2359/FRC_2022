package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj.GenericHID.*;
// import edu.wpi.first.wpilibj.Joystick;
import static frc.robot.RobotMap.*;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;


public class IO {
    private static XboxController driver = new XboxController(DRIVE_PORT); //this is a beta feature that was not used in the original code

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
        return driver.getLeftX();
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
}
