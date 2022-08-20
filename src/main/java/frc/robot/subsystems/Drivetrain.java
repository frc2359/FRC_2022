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

    public DifferentialDrive getDiffDrive() {
        return drive;
    }

    /** drive function that can be called without having to pass in private vairables **/
    public void arcadeDrive() {
        if ((IO.getDriveTrigger(true) - IO.getReverseTrigger(true)) > 1 || (IO.getDriveTrigger(true) - IO.getReverseTrigger(true)) < -1) {
            System.out.println("out of bounds drive value. go to Drivetrain.java line 34 and edit to an in-bounds expression");
        } else {
            if(IO.getThrottle() < 0){
                drive.arcadeDrive(IO.getThrottle(), IO.getLeftXAxis(true) * TURN_SPEED_MULT);
                IO.putNumberToSmartDashboard(("Vel. R"), frontRight.getSelectedSensorVelocity());
                IO.putNumberToSmartDashboard(("Vel. L"), frontLeft.getSelectedSensorVelocity());
            } else {
                drive.arcadeDrive(IO.getThrottle(), IO.getLeftXAxis(true) * TURN_SPEED_MULT, true);
            }
            IO.putNumberToSmartDashboard(("R Enc"),  frontLeft.getSelectedSensorPosition());
            IO.putNumberToSmartDashboard(("L Enc"),  frontRight.getSelectedSensorPosition());
        }
    }

    public boolean getAutoDrive() {
        return autoDrive;
    }

    public void setAutoDrive(boolean setpoint) {
        autoDrive = setpoint;
    }
    
    public void tankDrive(double speedL, double speedR) {
        drive.tankDrive(speedL, speedR);
    }

    public void driveAuto(double speed) {
        drive.arcadeDrive(speed, 0);
    }

    public double getAverageDriveDistance() {
        return ((frontLeft.getSelectedSensorPosition() + frontRight.getSelectedSensorPosition()) / 2);
    }

    public double getRotations() {
        return (frontLeft.getSelectedSensorPosition() + frontRight.getSelectedSensorPosition()) / 2;
    }

    public double getAverageDriveDistanceInches() {
        System.out.println("encoder distance: " + (frontLeft.getSelectedSensorPosition() + frontRight.getSelectedSensorPosition()) / 2);
        System.out.println("encoder distance: " + (((frontLeft.getSelectedSensorPosition() + frontRight.getSelectedSensorPosition()) / 2) / COUNTS_PER_REV) * 2 * Math.PI * DRIVE_RADIUS_FEET);
        return (((frontLeft.getSelectedSensorPosition() + frontRight.getSelectedSensorPosition()) / 2) / COUNTS_PER_REV) * 2 * Math.PI * DRIVE_RADIUS_FEET;
    }

    public void turn(double power) {
        drive.arcadeDrive(0, power);
    }


    public void zeroEncoders() {
        frontLeft.setSelectedSensorPosition(0);
        frontRight.setSelectedSensorPosition(0);
    }

    /**initialize the drivetrain**/
    public void init() {
        //Reset Motor Controllers to Factory Configuration
        frontLeft.configFactoryDefault();
        frontRight.configFactoryDefault();


        //Set Motor Direction and Encoder Sensor Phase
        frontLeft.setInverted(false);      // Positive is forward
        frontRight.setInverted(true);      // Invert so positive is forward

        frontLeft.setSensorPhase(false); // Check
        frontRight.setSensorPhase(true); // Check

        //init sensor position
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


    private int distanceToNativeUnits(double positionInches){
        double wheelRotations = positionInches/(2 * Math.PI * DRIVE_RADIUS);
        double motorRotations = wheelRotations * DRIVE_GEAR_RATIO;
        int sensorCounts = (int)(motorRotations * COUNTS_PER_REV);
        return sensorCounts;
    }
    
    private double nativeUnitsToDistanceMeters(double sensorCounts){
        double motorRotations = (double)sensorCounts / COUNTS_PER_REV;
        double wheelRotations = motorRotations / DRIVE_GEAR_RATIO;
        double positionMeters = wheelRotations * (2 * Math.PI * Units.inchesToMeters(DRIVE_RADIUS));
        return positionMeters;
    }

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

