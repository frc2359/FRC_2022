package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Solenoid;
import static frc.robot.RobotMap.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
//import com.revrobotics.SparkMaxPIDController;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

//import frc.robot.IO;


public class Lifter {

    private CANSparkMax lifterLeft = new CANSparkMax(CAN_ID_LIFTER_LEFT, MotorType.kBrushless);
    private CANSparkMax lifterRight = new CANSparkMax(CAN_ID_LIFTER_RIGHT, MotorType.kBrushless);
    //private TalonSRX lifterArm = new TalonSRX(CAN_ID_LIFTER_ARM_ROTATE);
    private TalonSRX barRoller = new TalonSRX(CAN_ID_LIFTER_ARM_ROLLER);
 
    private RelativeEncoder lifterLeftEncoder = lifterLeft.getEncoder();
    private RelativeEncoder lifterRightEncoder = lifterRight.getEncoder(); 

    //private DigitalInput sensorArmsHome = new DigitalInput(DIO_LIFTER_LOW_HOOK_ROT_HOME);
    private DigitalInput sensorLifterHome = new DigitalInput(DIO_LIFTER_LIFTING_HOOK_VERT_HOME);
    //private DigitalInput sensorHookClosedLeft = new DigitalInput(DIO_LIFTER_LEFT_HOOK_CLOSED);
    //private DigitalInput sensorHookClosedRight = new DigitalInput(DIO_LIFTER_RIGHT_HOOK_CLOSED);
    //private DigitalInput sensorHookLimit = new DigitalInput(DIO_LIFTER_LEFT_HOOK_CLOSED);
    private DigitalInput sensorVertPipe = new DigitalInput(DIO_LIFTER_VERT_PIPE_DETECT);
    private DigitalInput sensorHorizPipeLeft = new DigitalInput(DIO_LIFTER_HORIZ_PIPE_DETECT_LEFT);
    private DigitalInput sensorHorizPipeRight = new DigitalInput(DIO_LIFTER_HORIZ_PIPE_DETECT_RIGHT);
    
    private Solenoid solLifterHook = new Solenoid(PneumaticsModuleType.REVPH, ID_SOL_LIFTER_HOOK);

    private double lifterMotorSpeed = 0; 
    private boolean lifterCalibrated = false;

    private boolean aboveBar = false;

    private static final double minLifterHeight = -1; // replace with Encoder Value at min height
    private static final double maxLifterHeight = 137; // replace with Encoder Value at max height
    private static final double barHeight = 120; // replace with Encoder Value at above bar height
    private static final double lifterSlowRange = 20; // buffer range at each end to go slow

    public void show() {
  
        SmartDashboard.putNumber("L. Height", getHeight());
        SmartDashboard.putBoolean("LiftHome", isHome());
        SmartDashboard.putBoolean("L. Pipe", isHorizPipe(true));
        SmartDashboard.putBoolean("R. Pipe", isHorizPipe(false));
        SmartDashboard.putBoolean("V. Pipe", isOnBar());
        SmartDashboard.putNumber("LL Enc", lifterLeftEncoder.getPosition());
        SmartDashboard.putNumber("LR Rnc", lifterRightEncoder.getPosition());
        SmartDashboard.putBoolean("cal", lifterCalibrated);
    }

    public void init() {
        /* Setup Lifter Motors */
        lifterLeft.restoreFactoryDefaults();
        lifterRight.restoreFactoryDefaults();
        //lifterRight.follow(lifterLeft); // right lifter follows left lifter
        lifterRight.setInverted(true);  // invert right motor

        lifterLeft.setIdleMode(IdleMode.kBrake);
        lifterLeftEncoder.setPositionConversionFactor(1);
        lifterRight.setIdleMode(IdleMode.kBrake);
        lifterRightEncoder.setPositionConversionFactor(1);
         /* Home Lifter */

         lifterCalibrated = false;
    }

    public void setLifterHookState(boolean open) {
        solLifterHook.set(open);
    }

    public boolean isAboveBar () {
       return aboveBar;
    }

    public boolean isOnBar () {
        return sensorVertPipe.get();
    }

    public boolean isHome() {
        return (!sensorLifterHome.get());
    }
   
    public boolean isHorizPipe(boolean left) {
        if (left) {
            return !sensorHorizPipeLeft.get();
        } else {
            return !sensorHorizPipeRight.get();
        }
    }

    public double getHeight() {
        return ((lifterLeftEncoder.getPosition() + lifterRightEncoder.getPosition()) / -2); // Average of both Encoders times negative 1 to invert
    }

    public void stopLifter() {
         moveLifter(0);
         rollLifter(0);
    }

    public void moveLifter(double spd) {  // pos spd is up; neg spd is down
        lifterMotorSpeed = spd;
        if (lifterCalibrated) {
            if (lifterMotorSpeed > 0) {  // Move Up
                if (getHeight() < maxLifterHeight) {  // if less than max height
                    if (getHeight() > maxLifterHeight - lifterSlowRange) {
                        lifterMotorSpeed = .15;
                    } else {
                        lifterMotorSpeed = spd;
                    }
                } else {
                    lifterMotorSpeed = 0;  // stop lifter -- reached upper limit
                } 
            } else { // move down
                if (getHeight() > minLifterHeight && !isHome()) {  // if greater than min height
                    if (getHeight()> lifterSlowRange) {
                        lifterMotorSpeed = spd;
                    } else {
                        lifterMotorSpeed = -.1;
                    }
                } else {
                    lifterMotorSpeed = 0;  // stop lifter -- reached upper limit
                }
            } 
        }
        if (isHome()) { // reached home position
            aboveBar = false;
            lifterCalibrated = true;
            lifterLeftEncoder.setPosition(0);  // Reset Lifter Encoder value
            lifterRightEncoder.setPosition(0);
        }
        if(getHeight() > barHeight) { // passing sensor going up, so above bar
            aboveBar = true;
        }
        lifterLeft.set(-lifterMotorSpeed);
        lifterRight.set(-lifterMotorSpeed);
    }

    public void rollLifter(double spd) { // Positive is left; Negative is right
        barRoller.set(TalonSRXControlMode.PercentOutput, spd);
    }

    public void runLifter() {
        
    }

}
