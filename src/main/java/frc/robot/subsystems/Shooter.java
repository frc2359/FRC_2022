package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
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
    double speed;
    double shotPowerLevel;
    

    /**Initialize Shooter */
    public void init() {
        setVelocity(0);
        setPercentPower(0);  
    }


    /**Sets the shot power level */
    public void setShotPower(double pwr) {
        shotPowerLevel = pwr;
    } 

    /**Sets the speed of all three motors (for usage with percent power control mode) */
    public void setPower(double pwr) {
        for (int i = 0; i < shootMotors.length; i++) {
            shootPIDs[i].setReference(pwr, CANSparkMax.ControlType.kVoltage);
        }
    } 

    /**Set a specific motor at a percent power */
    public void setPower(double pwr, int ind) {
        System.out.println(ind > shootMotors.length ? "Out of Bounds Value" : "Good value");
        shootPIDs[ind].setReference(pwr, CANSparkMax.ControlType.kVoltage);
    } 

    /**Get speeds of all motors from the encoders and post them to SmartDashboard */
    public void getAllSpeeds() {
        for (int i = 0; i < motorEncoders.length; i++) {
            IO.putNumberToSmartDashboard(("Motor " +  i), motorEncoders[i].getVelocity());
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

    /**Sets the percent power of each of the shooter motors */
    public void setPercentPower(double setPoint) {
        for (int i = 0; i < motorEncoders.length; i++) {
            shootMotors[i].set(setPoint);
        }
    }

    /**Sets the percent power of one specific shooter motor at index */
    public void setPercentPower(double setPoint, int index) {
        if (index <= 2) {
            shootMotors[index].set(setPoint);
        }
    }

    /**Sets the percent power to the set shot power level */
    public void setPercentPowerToShotPowerLevel() {
        setPercentPower(shotPowerLevel);

    }

    /**Shooter mode for what to do upon being given to command to pick a ball up */
    public void pickBallUp(int state) {
        switch(state) {
            case STATE_UNKNOWN:
                    break;

            case STATE_NOT_COLLECTING:
                    setPercentPower(0);
                    //SmartDashboard.putBoolean("Low Shooter Power Mode", lowPower);
                    speed = SmartDashboard.getNumber("Speed", 0);
                    break;

            case STATE_COLLECTING:
                    setPercentPower(0,2);      // Shooter motor off
                    setPercentPower(0,1);      // Shooter motor off
                    setPercentPower(0.1, 0);   // Shooter motor on top set to low speed to help collect ball
                    break;

            case STATE_SECURE_BALL:
                    setPercentPower(0);         // Shooter motors off
                    break;

            case STATE_PREPARE_TO_SHOOT: // Ready to Shoot
                    setPercentPower(0);         // Shooter motors off; should be waiting for the velocity to be correct and then shooting
                    break;
            case STATE_SHOOT: // Shoot
                    setPercentPower(shotPowerLevel);
                    break;

            case STATE_REVERSE_COLLECTOR:
                    setPercentPower(-0.1, 2);       // Run motors backwards to send ball out the collector
                    setPercentPower(-0.1, 1);
                    setPercentPower(-0.1, 0);
                    break;
        }
    }

    /**Stops all motors */
    public void stopSys() {
        for(int i=0; i<shootPIDs.length; i++) {
            shootPIDs[i].setReference(0, CANSparkMax.ControlType.kVoltage);
        }
    }

    /**Sets the velocity of each of the shooter motors using PID controls */
    public void setVelocity(double setPoint) {
        for (int i = 0; i < motorEncoders.length; i++) {
        shootPIDs[i].setReference(setPoint, CANSparkMax.ControlType.kVelocity);
        SmartDashboard.putNumber("Vel SetPoint", setPoint);
        SmartDashboard.putNumber("Pro. Var.", motorEncoders[0].getVelocity());
        }
    }
}