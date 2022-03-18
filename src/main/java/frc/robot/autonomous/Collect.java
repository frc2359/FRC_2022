package frc.robot.autonomous;

import static frc.robot.RobotMap.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.IO;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.networktables.*;
// import edu.wpi.first.cscore.VideoSink;


public class Collect {
    private Collector collector;
    private Shooter shooter;
    private int state = 0;
    private int counterTimer = 0; //counter that we use to count time. TeleOP periodic runs 50 times per second, so checking for a count of 50 = 1s
    public UsbCamera frontCamera;
    public UsbCamera rearCamera;
    public CameraServer camServe;
    public NetworkTableEntry cameraSelection;
    // public VideoSink vidSink;

    public Collect(Collector col, Shooter shoot) {
        collector = col;
        shooter = shoot;
    }

    /** Sets a new state of the collect command. */
    public void setState(int setPoint) {
        state = setPoint;
    }

    public void init() {
        frontCamera = CameraServer.startAutomaticCapture(0);
        rearCamera = CameraServer.startAutomaticCapture(1);
        // vidSink = CameraServer.getServer();
        // vidSink.setSource(frontCamera);

    
        cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");

        // Set the resolution
        frontCamera.setResolution(640, 480);
        rearCamera.setResolution(640, 480);


        frontCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
        rearCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    }

    public int getState() {
        return state;
    }

    /** The program to collect balls. Each state represents a different part of the process to shoot. */
    public void collect(boolean isAuto) {
        SmartDashboard.putNumber("Collect State", state);
        System.out.println("Collect State " + state);
        if(IO.xButtonIsPressed(false)) {
            state = 1;
        }
        switch(state) {

            case STATE_UNKNOWN: // unknown
                    collector.setIntakeSpeed(0);
                    shooter.pickBallUp(state);
                    break;

            case STATE_NOT_COLLECTING: // not collectiong
                    collector.setBallLifterState(true);
                    // if(!isAuto) {vidSink.setSource(frontCamera);}
                    collector.setIntakeSpeed(0);
                    shooter.pickBallUp(state);
                    if(IO.aButtonIsPressed(false)) {
                       state = STATE_COLLECTING; 
                    }
                    break;

            case STATE_COLLECTING: // Looking for ball
            // if(!isAuto) {vidSink.setSource(frontCamera);}

                    collector.setBallLifterState(false);
                    collector.setIntakeSpeed(.4);
                    shooter.pickBallUp(state);
                    if(collector.isBallLoaded()) {
                        state = STATE_SECURE_BALL; 
                    }
                    break;

            case STATE_SECURE_BALL: // Secure ball
            // if(!isAuto) {vidSink.setSource(rearCamera);}

                    collector.setIntakeSpeed(0);
                    shooter.pickBallUp(state);
                    collector.setBallLifterState(true);
                    if(IO.bButtonIsPressed(false)) { 
                        state = STATE_PREPARE_TO_SHOOT; 
                    } 
                    break;

            case STATE_PREPARE_TO_SHOOT: // Prepare to Shoot
            // if(!isAuto) {vidSink.setSource(rearCamera);}
                    if(counterTimer == 0) {
                        collector.setBallLifterState(false);
                        collector.setIntakeSpeed(0);
                        shooter.pickBallUp(state);
                    }   
                    
                    //Add the code to pull and correct distance here
                    if(counterTimer == 50) {
                        counterTimer = 0;
                        state = STATE_SHOOT;
                    }
                    else {
                        counterTimer++;
                    }
                    break;

            case STATE_SHOOT: // Shooting
            // if(!isAuto) {vidSink.setSource(rearCamera);}
                    shooter.pickBallUp(state);
                    if(counterTimer == 10) {
                        collector.setBallLifterState(true);
                    }
                    if(counterTimer == 50){
                        counterTimer = 0;
                        state = 1;
                    }
                    else {
                        // vidSink.setSource(rearCamera);
                        counterTimer++;
                    }   
                    break;   
        }
    }
}
