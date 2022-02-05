package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import static frc.robot.RobotMap.*;
import frc.robot.IO;

public class Shooter implements Subsystem {
    private CANSparkMax shootMtr = new CANSparkMax(ID_SHOOTER_1, MotorType.kBrushless);
    private CANSparkMax shootMtr1 = new CANSparkMax(ID_SHOOTER_2, MotorType.kBrushless);
    
    private CANSparkMax shootMtr2 = new CANSparkMax(ID_SHOOTER_3, MotorType.kBrushless);
    private CANSparkMax shootMotors[] = new CANSparkMax[]{shootMtr, shootMtr1, shootMtr2};
    private SparkMaxPIDController shootPIDs[] = new SparkMaxPIDController[3];
    // private SparkMaxPIDController shootPID = shootMtr.getPIDController();
    private RelativeEncoder motorEncoders[] = new RelativeEncoder[3];
    private final double INITIAL_VELOCITY = 1419;
    private final double MAX_VELOCITY = 10000;



    public void init() {
        shootMtr1.follow(shootMtr);
        shootMtr2.follow(shootMtr);
        for (int i = 0; i < shootMotors.length; i++ ) {
            // shootPIDs[i] = shootMotors[i].getPIDController();
            motorEncoders[i] = shootMotors[i].getEncoder();
        }
    }

    public void setSpeed(double pwr) {
            shootMtr.set(pwr * MAX_VELOCITY);
    } 

    public void getAllSpeeds() {
        for (int i = 0; i < motorEncoders.length; i++) {
            SmartDashboard.putNumber(("Motor " +  i), motorEncoders[i].getVelocity());
            System.out.println("Motor " +  i + ": " + motorEncoders[i].getVelocity());
        }
    }

    public double getSpeed(int mtrIndex) {
        if (mtrIndex <= 2) {
            return motorEncoders[mtrIndex].getVelocity();
        } else {
            return -1;
        }
    }

    public void shoot() {
        setSpeed(1);
        getAllSpeeds();
        if(IO.bButtonIsPressed()){
            shootMtr.set(IO.getLeftXAxis());
        }
            // // System.out.println(motorV);
            // // while (IO.aButtonIsPressed()) {
            //     for (int i = 0; i < shootMotors.length; i++ ) {
            //         shootPIDs[i].setReference(IO.getLeftXAxis() * MAX_VELOCITY, CANSparkMax.ControlType.kVelocity);
            //         System.out.println(motorV);

            //     }
            // // }
    }
    
}
