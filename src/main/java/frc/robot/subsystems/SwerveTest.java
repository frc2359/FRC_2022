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
    WPI_TalonFX speed = new WPI_TalonFX(0);
    WPI_TalonFX rotate = new WPI_TalonFX(1);
    CANCoder cancoder = new CANCoder(2);
    AHRS navX = new AHRS(SPI.Port.kMXP, (byte) 200);


    /** initialize the drivetrain **/
    public void init() {
        //Reset Motor Controllers to Factory Configuration
        speed.configFactoryDefault();
        rotate.configFactoryDefault();


        //Sets the motor's direction to the actual movement of the robot
        speed.setInverted(false);
        rotate.setInverted(true); 

        //Sets the sensor's detection of direction to the actual movement of the robot
        speed.setSensorPhase(false);
        rotate.setSensorPhase(true);

        //reset motor sensors
        speed.setSelectedSensorPosition(0);
        rotate.setSelectedSensorPosition(0);

        //Set Brake/Coast Options
        speed.setNeutralMode(BRAKE_MODE_DRIVE ? NeutralMode.Brake : NeutralMode.Coast);
        rotate.setNeutralMode(BRAKE_MODE_DRIVE ? NeutralMode.Brake : NeutralMode.Coast);
        

        //Set Math.clamp Switch Positions
        final int kTimeoutMs = 30;
        speed.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.Disabled, kTimeoutMs);
        rotate.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.Disabled, kTimeoutMs);
    }

    /**Display encoder values for each of the motor */
    public void display(){
        SmartDashboard.putNumber(("Cancoder Values: "), cancoder.getPosition());
        SmartDashboard.putNumber(("Falcon Speed: "), speed.getSelectedSensorPosition());
        SmartDashboard.putNumber(("Falcon Rotate: "), rotate.getSelectedSensorPosition());
        SmartDashboard.putNumber("Angle", navX.getAngle());
        SmartDashboard.putNumber("JoyAng", IO.getDriveDirection());
        SmartDashboard.putNumber("JoyMag", IO.getDriveMagnitude());
    }

    /**Zero each of the encoders. */
    public void zeroEncoders() {
        speed.setSelectedSensorPosition(0);
        rotate.setSelectedSensorPosition(0);
        cancoder.setPosition(0);
        navX.zeroYaw();
    }

    public double getAng() {
        // question ? true : false
        // 2048 counts = 360 deg
        return (rotate.getSelectedSensorPosition() / STEER_GEAR_RATIO) / FALCON_ENC_COUNT;
    }

    public void turnRobot(){
        SmartDashboard.putNumber("Angle", getAng());
        SmartDashboard.putNumber("JoyAng", IO.getDriveDirection());
        if(getAng() > IO.getDriveDirection() + 10 || getAng() < IO.getDriveDirection() - 10){
            double rot = -1;
            rotate.set(rot * .4);  
        } else {
            rotate.set(0);
        }

    }

    public void testRot() {

    }
}
