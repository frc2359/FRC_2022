package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Solenoid;
import static frc.robot.RobotMap.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
//import com.revrobotics.SparkMaxPIDController;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

//import frc.robot.IO;


public class Lifter {

    private CANSparkMax lifterLeft = new CANSparkMax(CAN_ID_LIFTER_LEFT, MotorType.kBrushless);
    private CANSparkMax lifterRight = new CANSparkMax(CAN_ID_LIFTER_RIGHT, MotorType.kBrushless);
    private TalonSRX lifterArm = new TalonSRX(CAN_ID_LIFTER_ARM_ROTATE);
    private TalonSRX armRoller = new TalonSRX(CAN_ID_LIFTER_ARM_ROLLER);
 
    private RelativeEncoder lifterLeftEncoder = lifterLeft.getEncoder();
    private RelativeEncoder lifterRightEncoder = lifterRight.getEncoder(); 

    private DigitalInput sensorPaddleLeft = new DigitalInput(DIO_LIFTER_PADDLE_LEFT);
    private DigitalInput sensorPaddleRight = new DigitalInput(DIO_LIFTER_PADDLE_RIGHT);
    private DigitalInput sensorHomeLeft = new DigitalInput(DIO_LIFTER_ARMS_HOME_LEFT);
    private DigitalInput sensorHomeRight = new DigitalInput(DIO_LIFTER_ARMS_HOME_RIGHT);
    private DigitalInput sensorAboveBarLeft = new DigitalInput(DIO_LIFTER_ARMS_ABOVE_BAR_LEFT);
    private DigitalInput sensorAboveBarRight = new DigitalInput(DIO_LIFTER_ARMS_ABOVE_BAR_RIGHT);
    private DigitalInput sensorHookOpenLeft = new DigitalInput(DIO_LIFTER_HOOK_OPEN_LEFT);
    private DigitalInput sensorHookOpenRight = new DigitalInput(DIO_LIFTER_HOOK_OPEN_RIGHT);
    private DigitalInput sensorHookClosedLeft = new DigitalInput(DIO_LIFTER_HOOK_CLOSED_LEFT);
    //private DigitalInput sensorHookClosedRight = new DigitalInput(DIO_LIFTER_HOOK_CLOSED_RIGHT);
 
    private Solenoid solLifterHook = new Solenoid(PneumaticsModuleType.REVPH, ID_SOL_LIFTER_HOOK);

    private double lifterMotorSpeed = 0; 

    private boolean aboveBar = false;

    private static final double maxLifterHeight = 100; // replace with Encoder Value at Max Height



    public void init() {
        lifterRight.setInverted(true);  // invert right motor
        lifterRight.follow(lifterLeft); // right lifter follows left lifter
    }

    public boolean isAboveBar () {
       return aboveBar;
    }

    public boolean isHome() {
        return (sensorHomeLeft.get() || sensorHomeRight.get());
    }

    public double getHeight() {
        return ((lifterLeftEncoder.getPosition() + lifterRightEncoder.getPosition()) / 2); // Average of both Encoders
    }

    public void stopLifter() {
         moveLifter(0);
    }

    public void moveLifter(double spd) {  // pos spd is up; neg spd is down
        lifterMotorSpeed = spd;
        if (lifterMotorSpeed > 0) {  // Move Up
            if (lifterLeftEncoder.getPosition() < maxLifterHeight && lifterRightEncoder.getPosition() < maxLifterHeight) {  // if less than max height
                lifterMotorSpeed = spd;
            } else {
                lifterMotorSpeed = 0;  // stop lifter -- reached upper limit
            } 
            // motors up
            if(!sensorAboveBarLeft.get() || !sensorAboveBarRight.get()) { // passing sensor going up, so above bar
                aboveBar = true;
            }
        } else { // move down
            if (!sensorHomeLeft.get() || !sensorHomeRight.get()) { // reached home position
                lifterMotorSpeed = 0;
                aboveBar = false;
                lifterLeftEncoder.setPosition(0);  // Reset Lifter Encoder value
                lifterRightEncoder.setPosition(0);
            }
            else {
                lifterMotorSpeed = spd;
            }
            if(!sensorAboveBarLeft.get() || !sensorAboveBarRight.get()) { // passing sensor going down, so below bar
                aboveBar = false;
            }
        }
        lifterLeft.set(lifterMotorSpeed);
    }


       
}
