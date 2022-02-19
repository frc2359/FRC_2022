package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import static frc.robot.RobotMap.*;
import frc.robot.IO;

/**
 * The Shooter is an abstraction of the real-life system of three belts run by REV NEOs that run our shooting mechanism. It is an example of an FRC "Subsystem".
 */

public class Shooter implements Subsystem {
    private CANSparkMax shootMtr = new CANSparkMax(ID_SHOOTER_1, MotorType.kBrushless);
    private CANSparkMax shootMtr1 = new CANSparkMax(ID_SHOOTER_2, MotorType.kBrushless);
    private CANSparkMax shootMtr2 = new CANSparkMax(ID_SHOOTER_3, MotorType.kBrushless);
    private CANSparkMax shootMotors[] = new CANSparkMax[]{shootMtr, shootMtr1, shootMtr2};
    private SparkMaxPIDController shootPIDs[] = new SparkMaxPIDController[3];
    private RelativeEncoder motorEncoders[] = new RelativeEncoder[3];
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;


     /**
     * PIDController objects are commanded to a set point using the 
     * SetReference() method.
     * 
     * The first parameter is the value of the set point, whose units vary
     * depending on the control type set in the second parameter.
     * 
     * The second parameter is the control type can be set to one of four 
     * parameters:
     *  com.revrobotics.CANSparkMax.ControlType.kDutyCycle
     *  com.revrobotics.CANSparkMax.ControlType.kPosition
     *  com.revrobotics.CANSparkMax.ControlType.kVelocity
     *  com.revrobotics.CANSparkMax.ControlType.kVoltage
     */
    


    /**Initialize Shooter */
    public void init() {
        // PID coefficients
        kP = 10e-5; 
        kI = 0;
        kD = 0; 
        kIz = 0; 
        kFF = 0.000015; 
        kMaxOutput = 1; 
        kMinOutput = -1;
        maxRPM = MAX_SHOOT_VELOCITY;
        
        // shootMtr1.follow(shootMtr);
        // shootMtr2.follow(shootMtr);
        for (int i = 0; i < shootMotors.length; i++ ) {
            shootPIDs[i] = shootMotors[i].getPIDController();
            motorEncoders[i] = shootMotors[i].getEncoder();
            // set PID coefficients
            shootPIDs[i].setP(kP);
            shootPIDs[i].setI(kI);
            shootPIDs[i].setD(kD);
            shootPIDs[i].setIZone(kIz);
            shootPIDs[i].setFF(kFF);
            shootPIDs[i].setOutputRange(kMinOutput, kMaxOutput);
        }
        // display PID coefficients on SmartDashboard
        SmartDashboard.putNumber("P Gain", kP);
        SmartDashboard.putNumber("I Gain", kI);
        SmartDashboard.putNumber("D Gain", kD);
        SmartDashboard.putNumber("I Zone", kIz);
        SmartDashboard.putNumber("Feed Forward", kFF);
        SmartDashboard.putNumber("Max Output", kMaxOutput);
        SmartDashboard.putNumber("Min Output", kMinOutput);
    }

    /**Sets the speed of all three motors (for usage with percent power control mode) */
    public void setPercentPower(double pwr) {
        for (int i = 0; i < shootMotors.length; i++) {
            shootMotors[i].set(pwr);
        }
    } 

    /**Set a specific motor at a percent power */
    public void setPercentPower(double pwr, int ind) {
        System.out.println(ind > shootMotors.length ? "Out of Bounds Value" : "Good value");
        shootMotors[ind].set(pwr);
    } 

    /**Get speeds of all motors from the encoders and post them to SmartDashboard */
    public void getAllSpeeds() {
        for (int i = 0; i < motorEncoders.length; i++) {
            SmartDashboard.putNumber(("Motor " +  i), motorEncoders[i].getVelocity());
        }
    }

    /**Get speeds of motor of selected index from its encoder and post it to SmartDashboard */
    public double getSpeed(int motorIndex) {
        if (motorIndex <= 2) {
            return motorEncoders[motorIndex].getVelocity();
        } else {
            return -1;
        }
    }

    public void pickBallUp() {
        setPercentPower(0.05, 1);
    }

    public void setVelocity(double setPoint) {
        for (int i = 0; i < motorEncoders.length; i++) {
        shootPIDs[i].setReference(setPoint, CANSparkMax.ControlType.kVelocity);
        }
    }

    public void shoot() {        
        getAllSpeeds();
        if(IO.aButtonIsPressed()) {
            setPercentPower(0.1);
        }
        if(IO.bButtonIsPressed()){
            setVelocity(IO.getRightXAxis() * MAX_SHOOT_VELOCITY);            
        }
    }
    
}
