package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import static frc.robot.RobotMap.*;
import frc.robot.IO;
import edu.wpi.first.wpilibj.PneumaticHub;

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
    private CANSparkMax.ControlType velocityMode = CANSparkMax.ControlType.kVelocity;
    private PneumaticHub pneumatics = new PneumaticHub(ID_PNEUMATIC_HUB);

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
        // = 10e-5; 
        kP = 6e-5; 
        kI = 0;
        kD = 0; 
        kIz = 0; 
        kFF = 0.000015; 
        kMaxOutput = 1; 
        kMinOutput = -1;
        maxRPM = 5700;
        
        for (int i = 0; i < shootMotors.length; i++ ) {
            shootPIDs[i] = shootMotors[i].getPIDController();
            motorEncoders[i] = shootMotors[i].getEncoder();
        }
        //setPID(10e-30, 0, 0, 0, 0.000015, 1, -1); // set PID coefficients
        setPID(kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput); // set PID coefficients

        // display PID coefficients on SmartDashboard
        SmartDashboard.putNumber("P Gain", kP);
        SmartDashboard.putNumber("I Gain", kI);
        SmartDashboard.putNumber("D Gain", kD);
        SmartDashboard.putNumber("I Zone", kIz);
        SmartDashboard.putNumber("Feed Forward", kFF);
        SmartDashboard.putNumber("Max Output", kMaxOutput);
        SmartDashboard.putNumber("Min Output", kMinOutput);
        setVelocity(0);
        setPctPower(0);

        //Prime Pneumatics
        
    }

    /**Sets the speed of all three motors (for usage with percent power control mode) */
    public void setPercentPower(double pwr) {
        for (int i = 0; i < shootMotors.length; i++) {
            shootPIDs[i].setReference(pwr, CANSparkMax.ControlType.kVoltage);
        }
    } 

    /**Set a specific motor at a percent power */
    public void setPercentPower(double pwr, int ind) {
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

    /**Shooter mode for what to do upon being given to command to pick a ball up */
    public void pickBallUp() {
        // setPercentPower(0.05, 1);
        shootPIDs[0].setReference(0, CANSparkMax.ControlType.kVoltage);
        shootPIDs[1].setReference(0, CANSparkMax.ControlType.kVoltage);
        setPercentPower(2.5, 2);

    }

    /**Stops all motors */
    public void stopSys() {
        for(int i=0; i<shootPIDs.length; i++) {
            shootPIDs[i].setReference(0, CANSparkMax.ControlType.kVoltage);

        }
    }

    /**Sets the velocity of each of the shooter motors using PID controls */
    public void setPctPower(double setPoint) {
        for (int i = 0; i < motorEncoders.length; i++) {
            //shootPIDs[i].setReference(setPoint, CANSparkMax.ControlType.kVoltage);
            shootMotors[i].set(setPoint);
        }
    }


    /**Sets the velocity of each of the shooter motors using PID controls */
    public void setVelocity(double setPoint) {
        for (int i = 0; i < motorEncoders.length; i++) {
        shootPIDs[i].setReference(setPoint, CANSparkMax.ControlType.kVelocity);
        SmartDashboard.putNumber("Velocity SetPoint", setPoint);
        SmartDashboard.putNumber("Process Variable", motorEncoders[0].getVelocity());
        }
    }

    /**Sets the PID coefficients for each of the shooter motors */
    public void setPID(double kP, double kI, double kD, double kIz, double kFF, double kMinOutput, double kMaxOutput) {
        for(int i = 0; i < shootPIDs.length; i++){
            shootPIDs[i].setP(kP);
            shootPIDs[i].setI(kI);
            shootPIDs[i].setD(kD);
            shootPIDs[i].setIZone(kIz);
            shootPIDs[i].setFF(kFF);
            shootPIDs[i].setOutputRange(kMinOutput, kMaxOutput);
        }
    }

    /**Get PID Coefficients from the Smart Dashboard entered on-the-fly */
    public void setPIDFromSmartDashboard () {
        double newP, newI, newD, newIZone, newFF, newMaxOuput, newMinOutput;
        newP = IO.getNumberFromSmartDashboard("P Gain", kP);
        newI = IO.getNumberFromSmartDashboard("I Gain", kI);
        newD = IO.getNumberFromSmartDashboard("D Gain", kD);
        newIZone = IO.getNumberFromSmartDashboard("I Zone", kIz);
        newFF = IO.getNumberFromSmartDashboard("Feed Forward", kFF);
        newMaxOuput = IO.getNumberFromSmartDashboard("Max Output", kMaxOutput);
        newMinOutput = IO.getNumberFromSmartDashboard("Min Output", kMinOutput);
        if(newP != kP || newI!= kI || newD!= kD || newIZone!= kIz || newFF !=kFF || newMaxOuput!=kMaxOutput || newMinOutput!=kMinOutput){
            // setPID(newP, newI, newD, newIZone, newFF, newMaxOuput, newMinOutput);
        }
    }

    /**Manually setting veliocty */
    public void manualControl() {
        getAllSpeeds();
        // setPIDFromSmartDashboard();
        if(IO.aButtonIsPressed()) {
            setVelocity(.5*maxRPM);
            //setPctPower(.5);
            System.out.println("1");         
        }
        else if(IO.bButtonIsPressed()){
            setVelocity(0); 
            //setPctPower(0);
            System.out.println("2");         
        }
        else if(IO.xButtonIsPressed()) {
            //stopSys();
            setVelocity(.7*maxRPM);
            //setPctPower(.7);
            System.out.println("3");         
        }else if(IO.yButtonIsPressed()) {
            //stopSys();
            setVelocity(.9*maxRPM);
            //setPctPower(.9);
            System.out.println("3");         
        }
    }

    /**What the shooter does and checks for periodically */
    public void shooterPeriodic() {        
        //code
    }
}


/** */