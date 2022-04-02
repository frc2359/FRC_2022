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
    private Arduino arduino;
    private int state = 0;
    private int iter = 0;
    private int rotateState = 0;
    private int counterTimer = 0; //counter that we use to count time. TeleOP periodic runs 50 times per second, so checking for a count of 50 = 1s
    private Drive driveCommand;
    
    public UsbCamera frontCamera;
    public UsbCamera rearCamera;
    public CameraServer camServe;
    public NetworkTableEntry cameraSelection;
    // public VideoSink vidSink;

    public Collect(Collector col, Shooter shoot, Arduino ard, Drivetrain dr, ADXRS450_Gyro gy, Drive drCom) {
        collector = col;
        shooter = shoot;
        arduino = ard;
        drivetrain = dr;
        gyro = gy;
        driveCommand = drCom;
    }

    public Collect(Collector col, Shooter shoot, Arduino ard) {
        collector = col;
        shooter = shoot;
        arduino = ard;
    }

    /** Sets a new state of the collect command. */
    public void setCollectorState(int setPoint) {
        state = setPoint;
    }
    
    public int getCollectorState() {
        return state;
    }

    public void setCorrectState(int setPoint) {
        rotateState = setPoint;
    }
    
    public int getCorrectState() {
        return rotateState;
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

    public void correctDistance() {
        double distanceToGoalInches = IO.calculateDistance(LIMELIGHT_MOUNT_ANGLE, LIMELIGHT_MOUNT_HEIGHT, LOW_GOAL_DISTANCE);
        SmartDashboard.putNumber("Goal Dist", distanceToGoalInches);
        System.out.println("Goal Dist " + distanceToGoalInches);
        SmartDashboard.putNumber("Goal Angle", IO.getLimelightYAngle());
        //System.out.println("Distance to Goal: " + distanceToGoalInches);
        
        
        double targetOffsetAngleHorizontal = IO.getLimelightXAngle();
        SmartDashboard.putNumber("Target Offset", targetOffsetAngleHorizontal);
        //System.out.println(targetOffsetAngleHorizontal);
        SmartDashboard.putNumber("Drive Distance", drivetrain.getAverageDriveDistanceInches());
      
        double desiredDistance = 73;
        double threshold = 3;
        double minDistance = desiredDistance - threshold;
        double maxDistance = desiredDistance + threshold;
        
        //This is the code that should correct for distance and angle when the driver pushes "A"
        switch(rotateState) {
          case STATE_DRIVEROP:
            //reset stuff 
          case STATE_PREPARE:
            gyro.reset();
            drivetrain.zeroEncoders();
          case STATE_CORRECT_DISTANCE:
            if(distanceToGoalInches < minDistance && distanceToGoalInches > maxDistance) {
              drivetrain.driveAuto(0);
              rotateState = STATE_CORRECT_ANGLE;
            } else if(distanceToGoalInches > maxDistance) {  //73 is the distance to goal in inches that shoots in low power mode to into the goal on the stools
              drivetrain.driveAuto(-0.5); 
            } else if (distanceToGoalInches < minDistance) {
              drivetrain.driveAuto(0.5);
            } 
              break;
          case STATE_CORRECT_ANGLE:
            boolean isTurned = driveCommand.turnToAngle(-targetOffsetAngleHorizontal, 0.043); //Turn to angle has a +/- 10 degree window where it will stop
            System.out.println("Target Offset: " + targetOffsetAngleHorizontal);
            if(isTurned) {
              if(iter >= 50) {
                rotateState = STATE_RESET;
                iter = 0;
              } else {
                iter ++;
              }
            }
            break;
          case STATE_RESET:
            drivetrain.driveAuto(0);
            driveCommand.cancelTurn();
            rotateState = 0;
            break;
        }
        
        SmartDashboard.putNumber("Rotate State", rotateState);
    }

    /** The program to collect balls. Each state represents a different part of the process to shoot. */
    public void collect(boolean isAuto) {
        SmartDashboard.putNumber("Col. State", state);
        System.out.println("Collect State " + state);
        //SmartDashboard.putNumber("Ball Col", arduino.getBallColor());
        if(IO.xButtonIsPressed(false) || IO.isHIDButtonPressed(HID_COLLECTOR_OFF, false)) {
            state = STATE_COLLECTING;
        }
        switch(state) {
            case STATE_UNKNOWN:
                    collector.setIntakeSpeed(0);
                    shooter.pickBallUp(state);
                    break;

            case STATE_NOT_COLLECTING:
                    collector.setBallLifterState(true);
                    // if(!isAuto) {vidSink.setSource(frontCamera);}
                    collector.setIntakeSpeed(0);
                    arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_BLACK);
                    shooter.pickBallUp(state);
                    if(IO.isHIDButtonPressed(HID_COLLECTOR_ON, false)) {  // IO.aButtonIsPressed(false) || 
                       state = STATE_COLLECTING; 
                    }
                    if(IO.isHIDButtonPressed(HID_COLLECTOR_REVERSE, false)) {
                        state = STATE_REVERSE_COLLECTOR;
                    }
                    break;

            case STATE_COLLECTING:
                    collector.setBallLifterState(false);
                    collector.setIntakeSpeed(.4);
                    arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_YELLOW);
                    shooter.pickBallUp(state);
                    if(collector.isBallLoaded()) {
                        state = STATE_SECURE_BALL; 
                    }
                    if(IO.isHIDButtonPressed(HID_COLLECTOR_OFF, false)) {
                        state = STATE_NOT_COLLECTING;
                    }if(IO.isHIDButtonPressed(HID_COLLECTOR_REVERSE, false)) {
                        state = STATE_REVERSE_COLLECTOR;
                    }
                    break;

            case STATE_SECURE_BALL:
                    collector.setIntakeSpeed(0);
                    shooter.pickBallUp(state);
                    collector.setBallLifterState(true);
                    arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_GREEN);
                    //SmartDashboard.putNumber("Ball", arduino.getBallColor());
                    /*
                    if(IO.bButtonIsPressed(false)) { 
                        state = STATE_PREPARE_TO_SHOOT; 
                    } 
                    */
                    if (IO.isHIDButtonPressed(HID_AUTO_EJECT_MODE, false)) {
                        if (arduino.getBallColor() != COLOR_UNKNOWN) {
                            if (arduino.getBallColor() != IO.getAllianceColor()) {
                                shooter.setShotPower(0.15);
                                state = STATE_PREPARE_TO_SHOOT;
                            }
                        }
                    }
                    if(IO.isHIDButtonPressed(HID_COLLECTOR_REVERSE, false)) {
                        state = STATE_REVERSE_COLLECTOR;
                    }
                    if(IO.isHIDButtonPressed(HID_SHOOT_HIGH, false)) {
                        shooter.setShotPower(0.6);
                        state = STATE_PREPARE_TO_SHOOT;
                    }
                    if(IO.isHIDButtonPressed(HID_SHOOT_LOW, false)) {
                        shooter.setShotPower(0.5);
                        state = STATE_PREPARE_TO_SHOOT;
                    }
                    if(IO.isHIDButtonPressed(HID_SHOOT_LAUNCH_PAD, false)) {
                        shooter.setShotPower(0.7);  // .9 or 1?
                        state = STATE_PREPARE_TO_SHOOT;
                    }
                    if(IO.isHIDButtonPressed(HID_SHOOT_EJECT, false)) {
                        shooter.setShotPower(0.15);
                        state = STATE_PREPARE_TO_SHOOT;
                    }
                    break;

            case STATE_PREPARE_TO_SHOOT:
                    if(counterTimer == 0) {
                        collector.setBallLifterState(false);
                        collector.setIntakeSpeed(0);
                        arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_BLUE);
                        shooter.pickBallUp(state);
                    }   
                    
                    setCorrectState(1);
                    if(counterTimer == 50) {
                        counterTimer = 0;
                        state = STATE_SHOOT;
                    }
                    else {
                        counterTimer++;
                    }
                    break;

            case STATE_SHOOT: 
                    arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_PURPLE);
                    shooter.pickBallUp(state);
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
                arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_RED);
                shooter.pickBallUp(state);
                if(IO.isHIDButtonPressed(HID_COLLECTOR_OFF, false)) {
                    state = STATE_NOT_COLLECTING;
                }if(IO.isHIDButtonPressed(HID_COLLECTOR_ON, false)) {
                    state = STATE_COLLECTING;
                }
                break;
        }
    }
}
