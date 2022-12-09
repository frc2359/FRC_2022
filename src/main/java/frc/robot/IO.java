package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
// import edu.wpi.first.wpilibj.GenericHID.*;
import edu.wpi.first.wpilibj.Joystick;
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
    private static Joystick driver = new Joystick(SHOOT_PORT);;
    private static GenericHID otherController = new GenericHID(HID_PORT);
    
    /**Returns the angle of the DRIVER joystick in degrees */
    public static double getDriveDirection() {
        return driver.getDirectionDegrees();
    }

    /**Returns the magnitude of the DRIVER joystick in degrees */
    public static double getDriveMagnitude() {
        return driver.getMagnitude();
    }

    /**gets whether the A button on the controller has been released
     * @param isDriver specifies if the selected input is from the driver
     * **/
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
