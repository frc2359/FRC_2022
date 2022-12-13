package frc.robot.subsystems;
import com.ctre.phoenix.sensors.CANCoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.IO;
import static frc.robot.RobotMap.*;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

//We need to make this more cool. This is how: https://github.com/SwerveDriveSpecialties/swerve-template

//another really cool link to look at https://github.com/SwerveDriveSpecialties/swerve-lib

//this is the real link that should be looked at https://jacobmisirian.gitbooks.io/frc-swerve-drive-programming/content/chapter1.html

public class SwerveTest {
    WheelDrive left = new WheelDrive(1, 0, 0);
    WheelDrive right = new WheelDrive(2, 3, 2);
    AHRS navX = new AHRS(SPI.Port.kMXP, (byte) 200);


    /** initialize the drivetrain **/
    public void init() {
        //Reset Motor Controllers to Factory Configuration
        left.getSpeedMotor().configFactoryDefault();
        right.getSpeedMotor().configFactoryDefault();
        left.getAngleMotor().configFactoryDefault();
        right.getAngleMotor().configFactoryDefault();


        //Sets the motor's direction to the actual movement of the robot
        left.getSpeedMotor().setInverted(false);
        right.getSpeedMotor().setInverted(false);
        left.getAngleMotor().setInverted(true); 
        right.getAngleMotor().setInverted(true); 

        //Sets the sensor's detection of direction to the actual movement of the robot
        left.getSpeedMotor().setSensorPhase(false);
        right.getSpeedMotor().setSensorPhase(false);
        left.getAngleMotor().setSensorPhase(true);
        right.getAngleMotor().setSensorPhase(true);

        //reset motor sensors
        left.getSpeedMotor().setSelectedSensorPosition(0);
        right.getSpeedMotor().setSelectedSensorPosition(0);
        left.getAngleMotor().setSelectedSensorPosition(0);
        right.getAngleMotor().setSelectedSensorPosition(0);

        //Set Brake/Coast Options
        left.getSpeedMotor().setNeutralMode(BRAKE_MODE_DRIVE ? NeutralMode.Brake : NeutralMode.Coast);
        right.getSpeedMotor().setNeutralMode(BRAKE_MODE_DRIVE ? NeutralMode.Brake : NeutralMode.Coast);
        left.getAngleMotor().setNeutralMode(BRAKE_MODE_DRIVE ? NeutralMode.Brake : NeutralMode.Coast);
        right.getAngleMotor().setNeutralMode(BRAKE_MODE_DRIVE ? NeutralMode.Brake : NeutralMode.Coast);
        

        //Set Math.clamp Switch Positions
        final int kTimeoutMs = 30;
        left.getSpeedMotor().configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.Disabled, kTimeoutMs);
        right.getSpeedMotor().configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.Disabled, kTimeoutMs);
        left.getAngleMotor().configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.Disabled, kTimeoutMs);
        right.getAngleMotor().configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.Disabled, kTimeoutMs);
    }

    /**Display encoder values for each of the motor */
    public void display(){
        SmartDashboard.putNumber(("1 Cancoder Values: "), left.getEncoder().getPosition());
        SmartDashboard.putNumber(("2 Cancoder Values: "), right.getEncoder().getPosition());
        SmartDashboard.putNumber(("Falcon Speed: "), left.getSpeedMotor().getSelectedSensorPosition());
        SmartDashboard.putNumber(("Falcon Rotate: "), left.getAngleMotor().getSelectedSensorPosition());
        SmartDashboard.putNumber("Angle", navX.getAngle());
        SmartDashboard.putNumber("JoyAng", IO.getDriveDirection());
        SmartDashboard.putNumber("JoyMag", IO.getDriveMagnitude());
    }

    /**Zero each of the encoders. */
    public void zeroEncoders() {
        left.getSpeedMotor().setSelectedSensorPosition(0);
        right.getSpeedMotor().setSelectedSensorPosition(0);
        left.getAngleMotor().setSelectedSensorPosition(0);
        right.getAngleMotor().setSelectedSensorPosition(0);
        left.getEncoder().setPosition(0);
        right.getEncoder().setPosition(0);
        navX.zeroYaw();
    }

    public double getAng() {
        // question ? true : false
        // 2048 counts = 360 deg
        double x = (left.getAngleMotor().getSelectedSensorPosition() + right.getAngleMotor().getSelectedSensorPosition()) / 2;
        return ( x / STEER_GEAR_RATIO) / FALCON_ENC_COUNT;
    }

    public void turnRobot(){
        SmartDashboard.putNumber("Angle", getAng());
        SmartDashboard.putNumber("JoyAng", IO.getDriveDirection());
        if(getAng() > IO.getDriveDirection() + 10 || getAng() < IO.getDriveDirection() - 10){
            double rot = -1;
            left.getAngleMotor().set(rot * .4 * IO.getDriveMagnitude());  
            right.getAngleMotor().set(rot * .4 * IO.getDriveMagnitude());  
        } else {
            left.getAngleMotor().set(0);
            right.getAngleMotor().set(0);
        }

    }

    public void testRot() {

    }
}
