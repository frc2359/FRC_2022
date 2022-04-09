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
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

//import frc.robot.IO;


public class Lifter {

    private CANSparkMax lifterLeft = new CANSparkMax(CAN_ID_LIFTER_LEFT, MotorType.kBrushless);
    private CANSparkMax lifterRight = new CANSparkMax(CAN_ID_LIFTER_RIGHT, MotorType.kBrushless);
    private WPI_TalonSRX lifterArm = new WPI_TalonSRX(CAN_ID_LIFTER_ARM_ROTATE);
    private WPI_TalonSRX barRoller = new WPI_TalonSRX(CAN_ID_LIFTER_ARM_ROLLER);
 
    private RelativeEncoder lifterLeftEncoder = lifterLeft.getEncoder();
    private RelativeEncoder lifterRightEncoder = lifterRight.getEncoder(); 

    private DigitalInput sensorArmsHome = new DigitalInput(DIO_LIFTER_LOW_HOOK_ROT_HOME);
    private DigitalInput sensorLifterHome = new DigitalInput(DIO_LIFTER_LIFTING_HOOK_VERT_HOME);
   // private DigitalInput sensorHookClosedLeft = new DigitalInput(DIO_LIFTER_LEFT_HOOK_CLOSED);
    private DigitalInput sensorHookClosedRight = new DigitalInput(DIO_LIFTER_RIGHT_HOOK_CLOSED);
    private DigitalInput sensorHookLimit = new DigitalInput(DIO_LIFTER_LEFT_HOOK_CLOSED);
    private DigitalInput sensorVertPipe = new DigitalInput(DIO_LIFTER_VERT_PIPE_DETECT);
    private DigitalInput sensorHorizPipeLeft = new DigitalInput(DIO_LIFTER_HORIZ_PIPE_DETECT_LEFT);
    private DigitalInput sensorHorizPipeRight = new DigitalInput(DIO_LIFTER_HORIZ_PIPE_DETECT_RIGHT);
    
    private Solenoid solLifterHook = new Solenoid(PneumaticsModuleType.REVPH, ID_SOL_LIFTER_HOOK);

    private boolean manualControl = true;

    private double lifterMotorSpeed = 0; 
    private boolean lifterCalibrated = false;
    private double armsMotorSpeed = 0;
    private boolean armsCalibrated = true;

    private int isHomeState = 0;  // 0 = unknown; 1 = above home; 2 = on home; 3 = below home

    private boolean aboveBar = false;

    private static final double minLifterHeight = -4; // replace with Encoder Value at min height
    private static final double maxLifterHeight = 137; // replace with Encoder Value at max height
    private static final double barHeight = 134; // replace with Encoder Value at above bar height
    private static final double lifterSlowRange = 20; // buffer range at each end to go slow

    private static final double minArmRotation = 0;
    private static final double maxArmRotation = 1400;
    private static final double armSlowRange = 30;

    public void show() {
  
        SmartDashboard.putNumber("L. Height", getHeight());
        SmartDashboard.putBoolean("LiftHome", isHome());
        SmartDashboard.putBoolean("L. Pipe", isHorizPipe(true));
        SmartDashboard.putBoolean("R. Pipe", isHorizPipe(false));
        SmartDashboard.putBoolean("V. Pipe", isOnBar());
        SmartDashboard.putNumber("LL Enc", lifterLeftEncoder.getPosition());
        SmartDashboard.putNumber("LR Rnc", lifterRightEncoder.getPosition());
        SmartDashboard.putBoolean("lifter cal", lifterCalibrated);
        SmartDashboard.putBoolean("arms cal", armsCalibrated);
        SmartDashboard.putNumber("Hooks", lifterArm.getSelectedSensorPosition());
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
        lifterArm.setSelectedSensorPosition(0);
         /* Home Lifter */

         lifterCalibrated = false;
         armsCalibrated = false;
         setLifterHookState(true);
    }

    public void setLifterHookState(boolean open) {
        solLifterHook.set(open);
    }

    public boolean isHookOpen() {
        return !sensorHookLimit.get();
    }

    public boolean isHookClosed(boolean left) {
        if (left) {
            return false; //!sensorHookClosedLeft.get();
        } else {
            return !sensorHookClosedRight.get();
        }
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

    public boolean isManual() {
        return manualControl;
    }

    public void setAutoMode(boolean auto) {
        manualControl = !auto;
    }

    public boolean isArmsHome() {
        return (!sensorArmsHome.get());
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

    public void stopSpin() {
        spin(0);
    }

    public boolean isLifterCalibrated() {
        return lifterCalibrated;
    }

    public boolean isArmsCalibrated () {
        return armsCalibrated;
    }

    public void moveLifter(double spd) {  // pos spd is up; neg spd is down
        lifterMotorSpeed = spd;
        if (lifterCalibrated) {
            if (lifterMotorSpeed > 0) {  // Move Up
                if (getHeight() < maxLifterHeight) {  // if less than max height
                    if (getHeight() > maxLifterHeight - lifterSlowRange) {
                        if (lifterMotorSpeed > .15) {
                            lifterMotorSpeed = .15;
                        }
                    } else {
                        lifterMotorSpeed = spd;
                    }
                } else {
                    lifterMotorSpeed = 0;  // stop lifter -- reached upper limit
                } 
                if (isHome()) {
                    if (isHomeState > 1) {
                        isHomeState = 2;
                    }
                } else if (isHomeState == 2) {
                    isHomeState = 1;
                }
            } else { // move down
                if (getHeight() > minLifterHeight && !isHome()) {  // if greater than min height
                    if (getHeight()> lifterSlowRange) {
                        lifterMotorSpeed = spd;
                    } else {
                        if (lifterMotorSpeed < -.15) {
                            lifterMotorSpeed = -.15;
                        }

                    }
                } else {
                    lifterMotorSpeed = 0;  // stop lifter -- reached lower limit
                }
                if (isHome()) { // reached home position
                    aboveBar = false;
                    if (isHomeState < 2) { // unknown or above bar
                        lifterLeftEncoder.setPosition(0);  // Reset Lifter Encoder value
                        lifterRightEncoder.setPosition(0);
                        lifterCalibrated = true;
                        isHomeState = 2;
                    }
                } else if (isHomeState == 2) {
                    isHomeState = 3;
                }
            } 
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

    
    public void manualCalibration(double setpoint) {
        if(Math.abs(setpoint) <= 1) {
            lifterArm.set(setpoint);
        }
        if(isArmsHome()) {
            lifterArm.setSelectedSensorPosition(0);
        }
    }
    
    
    public void spin(double setpoint) {
        armsMotorSpeed = setpoint;
        if (armsCalibrated) {
            if (armsMotorSpeed > 0) {  // Rotate Forward
                if (lifterArm.getSelectedSensorPosition() < maxArmRotation) {  // if less than max height
                    if (lifterArm.getSelectedSensorPosition() > maxArmRotation - armSlowRange) {
                        if (armsMotorSpeed > .15) {
                            armsMotorSpeed = .15;
                        }
                    } else {
                        armsMotorSpeed = setpoint;
                    }
                } else {
                    armsMotorSpeed = 0;  // stop lifter -- reached upper limit
                } 
            } else { // Rotate Back
                if (lifterArm.getSelectedSensorPosition() > minArmRotation && !isArmsHome()) {  // if greater than min height
                    if (lifterArm.getSelectedSensorPosition()> armSlowRange) {
                        armsMotorSpeed = setpoint;
                    } else {
                        if (armsMotorSpeed < -.15) {
                            armsMotorSpeed = -.15;
                        }
                    }
                } else {
                    armsMotorSpeed = 0;  // stop lifter -- reached upper limit
                }
                if (isArmsHome()) { // reached home position
                    armsCalibrated = true;
                    stopSpin();
                    lifterArm.setSelectedSensorPosition(0);
                }
            } 
        }
        
        lifterArm.set(armsMotorSpeed);
    }
    

}
