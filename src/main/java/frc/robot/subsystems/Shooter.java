package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import static frc.robot.RobotMap.*;
import frc.robot.IO;

public class Shooter implements Subsystem {
    private CANSparkMax shootMtr = new CANSparkMax(ID_SHOOTER_1, MotorType.kBrushless);
    private SparkMaxPIDController shootPID = shootMtr.getPIDController();
    private final double INITIAL_VELOCITY = 1419;
    private final double MAX_VELOCITY = 5676;


    public void init() {
        shootPID.setReference(INITIAL_VELOCITY, CANSparkMax.ControlType.kVelocity);
    }

    public void setSpeed(double pwr) {
        shootPID.setReference(pwr, CANSparkMax.ControlType.kVelocity);
    } 

    public void shoot() {
        double motorV;
        if(IO.bButtonIsPressed()){
            while (IO.bButtonIsPressed()) {
                motorV = IO.getLeftXAxis() * MAX_VELOCITY;
                shootPID.setReference(motorV, CANSparkMax.ControlType.kVelocity);
            }
            // System.out.println(motorV);
            while (IO.aButtonIsPressed()) {
                shootPID.setReference(IO.getLeftXAxis() * MAX_VELOCITY, CANSparkMax.ControlType.kVelocity);
            }
        } 
    }
    
}
