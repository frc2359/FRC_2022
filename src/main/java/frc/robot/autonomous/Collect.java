package frc.robot.autonomous;

import static frc.robot.RobotMap.*;
import frc.robot.autonomous.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.IO;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
// import edu.wpi.first.cscore.VideoSink;


public class Collect {
    private Collector collector;
    private Shooter shooter;
    private ADXRS450_Gyro gyro;
    private Drivetrain drivetrain;
    private int state = 0;
    private int counterTimer = 0;
    private Drive driveCommand;

    
    public UsbCamera frontCamera;
    public UsbCamera rearCamera;
    public CameraServer camServe;
    public NetworkTableEntry cameraSelection;

    public Collect(Collector col, Shooter shoot, Drivetrain dr, ADXRS450_Gyro gy, Drive drCom) {
        collector = col;
        shooter = shoot;
        drivetrain = dr;
        gyro = gy;
        driveCommand = drCom;
    }

    public Collect(Collector col, Shooter shoot) {
        collector = col;
        shooter = shoot;
    }

    /** Sets a new state of the collect command. */
    public void setCollectorState(int setPoint) {
        state = setPoint;
    }
    
    /**Gets the state of the collect comman */
    public int getCollectorState() {
        return state;
    }

    /** The program to collect balls. Each state represents a different part of the process to shoot. 
     * @param isAuto
     * Defines whether the function is run autonomously or driver activated
    */
    public void collect(boolean isAuto) {
        if(IO.xButtonIsPressed(false) || IO.isHIDButtonPressed(HID_COLLECTOR_OFF, false)) {
            state = STATE_COLLECTING;
        }
        switch(state) {
            case STATE_UNKNOWN:
                collector.setIntakeSpeed(0);
                break;

            case STATE_NOT_COLLECTING:
                collector.setBallLifterState(true);
                collector.setIntakeSpeed(0);
                shooter.setPercentPower(0);

                //MOVE INTO THE NEXT STATE ---------
                if(IO.isHIDButtonPressed(HID_COLLECTOR_ON, false)) {
                    state = STATE_COLLECTING; 
                } if (IO.isHIDButtonPressed(HID_COLLECTOR_REVERSE, false)) {
                    state = STATE_REVERSE_COLLECTOR;
                }
                break;

            case STATE_COLLECTING:
                collector.setBallLifterState(false);
                collector.setIntakeSpeed(.4);
                // Turn off shooter motors 1 and 2 and sets 0 to a low speed to help push the ball into the collector
                shooter.setPercentPower(0,2);
                shooter.setPercentPower(0,1);
                shooter.setPercentPower(0.1, 0); 

                //MOVE INTO THE NEXT STATE
                if(collector.isBallLoaded()) {
                    state = STATE_SECURE_BALL; 
                }
                //move into any other states based of button presses
                if(IO.isHIDButtonPressed(HID_COLLECTOR_OFF, false)) {
                    state = STATE_NOT_COLLECTING;
                }if(IO.isHIDButtonPressed(HID_COLLECTOR_REVERSE, false)) {
                    state = STATE_REVERSE_COLLECTOR;
                }
                break;

            case STATE_SECURE_BALL:
                counterTimer = 0;
                collector.setIntakeSpeed(0);
                shooter.setPercentPower(0); // Shooter motors off
                collector.setBallLifterState(true);
                if (IO.isHIDButtonPressed(HID_COLLECTOR_REVERSE, false)) {
                    state = STATE_REVERSE_COLLECTOR;
                }
                if (IO.isHIDButtonPressed(HID_SHOOT_LOW, false)) {
                    shooter.setShotPower(0.5);
                    SmartDashboard.putNumber("Shooter Power", 2);
                    state = STATE_PREPARE_TO_SHOOT;
                }
                if (IO.isHIDButtonPressed(HID_SHOOT_EJECT, false)) {
                    shooter.setShotPower(0.15);
                    SmartDashboard.putNumber("Shooter Power", 4);
                    state = STATE_PREPARE_TO_SHOOT;
                }
                break;

            case STATE_PREPARE_TO_SHOOT:
                if(counterTimer == 0) {
                    collector.setBallLifterState(false);
                    collector.setIntakeSpeed(0);
                    shooter.setPercentPowerToShotPowerLevel();
                }   
                if(counterTimer == 50) {
                    counterTimer = 0;
                    state = STATE_SHOOT;
                }
                else {
                    counterTimer++;
                }
                break;

            case STATE_SHOOT: 
                if(counterTimer == 10) {
                    collector.setBallLifterState(true);
                }
                if(counterTimer == 50){
                    counterTimer = 0;
                    state = 1;
                }
                else {
                    counterTimer++;
                }   
                break;  
            case STATE_REVERSE_COLLECTOR:
                collector.setBallLifterState(true);
                collector.setIntakeSpeed(-.4);
                //SHOOTER----
                shooter.setPercentPower(-0.1, 2);       // Run motors backwards to send ball out the collector
                shooter.setPercentPower(-0.1, 1);
                shooter.setPercentPower(-0.1, 0);
                // shooter.pickBallUp(state);
                if(IO.isHIDButtonPressed(HID_COLLECTOR_OFF, false)) {
                    state = STATE_NOT_COLLECTING;
                }if(IO.isHIDButtonPressed(HID_COLLECTOR_ON, false)) {
                    state = STATE_COLLECTING;
                }
                break;
        }
    }
}
