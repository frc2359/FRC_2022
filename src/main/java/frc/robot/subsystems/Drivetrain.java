package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.math.util.Units;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import static frc.robot.RobotMap.*;
import frc.robot.IO;

/**
 * The Drivetrain is an abstraction of the real-life system of two wheels powered by Falcon 500s that run our drive mechanism. It is an example of an FRC "Subsystem".
 */

public class Drivetrain implements Subsystem {
    //The Falcon 500s are a unit that include Talon FXs as their base motors, in which there is an encoder built in.
    WPI_TalonFX frontLeft = new WPI_TalonFX(ID_DRIVE_FL);
    WPI_TalonFX frontRight = new WPI_TalonFX(ID_DRIVE_FR);
    boolean autoDrive = false;

    private DifferentialDrive drive = new DifferentialDrive(frontLeft, frontRight); //front motors are masters & control inputs for both front and back
    
    /** drive function that can be called without having to pass in private vairables **/
    public void arcadeDrive() {
        if ((IO.getDriveTrigger(true) - IO.getReverseTrigger(true)) > 1 || (IO.getDriveTrigger(true) - IO.getReverseTrigger(true)) < -1) {
            System.out.println("out of bounds drive value.");
        } else {
            drive.arcadeDrive(IO.getThrottle(), IO.getLeftXAxis(true) * TURN_SPEED_MULT);
        }
    }

    /** Drives forward at set speed parameter
     * @param speed
     * The percent power to set the motor to
     */
    public void driveAuto(double speed) {
        drive.arcadeDrive(speed, 0);
    }

    /** Gets the average calculated distance from the drive motors */
    public double getAverageDriveDistance() {
        return ((frontLeft.getSelectedSensorPosition() + frontRight.getSelectedSensorPosition()) / 2);
    }

    /** Gets the average of the number of rotations between the drive motors */
    public double getRotations() {
        return (frontLeft.getSelectedSensorPosition() + frontRight.getSelectedSensorPosition()) / 2;
    }

    /** Gets the average calculated distance in inches from the drive motors */
    public double getAverageDriveDistanceInches() {
        return (((frontLeft.getSelectedSensorPosition() + frontRight.getSelectedSensorPosition()) / 2) / COUNTS_PER_REV) * 2 * Math.PI * DRIVE_RADIUS_FEET;
    }

    public void zeroEncoders() {
        frontLeft.setSelectedSensorPosition(0);
        frontRight.setSelectedSensorPosition(0);
    }

    /** initialize the drivetrain **/
    public void init() {
        //Reset Motor Controllers to Factory Configuration
        frontLeft.configFactoryDefault();
        frontRight.configFactoryDefault();


        //Sets the motor's direction to the actual movement of the robot
        frontLeft.setInverted(false);
        frontRight.setInverted(true); 

        //Sets the sensor's detection of direction to the actual movement of the robot
        frontLeft.setSensorPhase(false);
        frontRight.setSensorPhase(true);

        //reset motor sensors
        frontLeft.setSelectedSensorPosition(0);
        frontRight.setSelectedSensorPosition(0);

        //Set Brake/Coast Options
        frontLeft.setNeutralMode(BRAKE_MODE_DRIVE ? NeutralMode.Brake : NeutralMode.Coast);
        frontRight.setNeutralMode(BRAKE_MODE_DRIVE ? NeutralMode.Brake : NeutralMode.Coast);
        

        //Set Math.clamp Switch Positions
        final int kTimeoutMs = 30;
        frontLeft.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.Disabled, kTimeoutMs);
        frontRight.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.Disabled, kTimeoutMs);
    }

    /** Converts distance to native encoder units */
    private int distanceToNativeUnits(double positionInches){
        double wheelRotations = positionInches/(2 * Math.PI * DRIVE_RADIUS);
        double motorRotations = wheelRotations * DRIVE_GEAR_RATIO;
        int sensorCounts = (int)(motorRotations * COUNTS_PER_REV);
        return sensorCounts;
    }
    
    /** Converts native encoder units to distance in feet */
    private double nativeUnitsToDistanceFeet(double sensorCounts){
        double motorRotations = (double)sensorCounts / COUNTS_PER_REV;
        double wheelRotations = motorRotations / DRIVE_GEAR_RATIO;
        double positionMeters = wheelRotations * (2 * Math.PI * 12 * DRIVE_RADIUS);
        return positionMeters;
    }

    /**stops motors manually**/
    public void stopMotors() {
        frontLeft.stopMotor();
        frontRight.stopMotor();
    }
}