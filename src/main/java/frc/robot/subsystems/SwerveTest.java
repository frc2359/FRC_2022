package frc.robot.subsystems;
import com.ctre.phoenix.sensors.CANCoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.IO;
import static frc.robot.RobotMap.*;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;

//We need to make this more cool. This is how: https://github.com/SwerveDriveSpecialties/swerve-template

//another really cool link to look at https://github.com/SwerveDriveSpecialties/swerve-lib

public class SwerveTest {
    WPI_TalonFX speed = new WPI_TalonFX(0);
    WPI_TalonFX rotate = new WPI_TalonFX(1);
    CANCoder cancoder = new CANCoder(0);


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

    }

    /**Zero each of the encoders. */
    public void zeroEncoders() {
        speed.setSelectedSensorPosition(0);
        rotate.setSelectedSensorPosition(0);
        cancoder.setPosition(0);
    }

    /** Drive the robot, using the x and y axis of the driver's motor*/
    public void drive() {
        double rot = IO.getLeftXAxis(true);
        double sp = IO.getLeftYAxis(true);
        speed.set(sp);
        rotate.set(rot);
    }
}
