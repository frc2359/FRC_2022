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
    public static double getDriveXAxis() {
        return driver.getRightX();
    }

    /**gets throttle value (negative is backwards, positive is forwards)**/
    public static double getThrottle() {
        return (getDriveTrigger() - getReverseTrigger()) * DRIVE_SPEED_MULT;
    }

    /**gets whether the B button on the controller has been released**/
    public static boolean bButtonIsReleased() {
        return driver.getBButtonReleased();
    }


}
