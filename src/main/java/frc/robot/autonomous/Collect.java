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

    //autoshoot variables
    double desiredDistance;
    double threshold;
    boolean autoShoot;
    
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
    
    /**Gets the state of the collect comman */
    public int getCollectorState() {
        return state;
    }

    /**Sets a new state for the correct distance command */
    public void setCorrectState(int setPoint) {
        rotateState = setPoint;
    }
    
    /**Gets the state for the correct distance command */
    public int getCorrectState() {
        return rotateState;
    }

    public void init() {
   //     frontCamera = CameraServer.startAutomaticCapture(0);
   //     rearCamera = CameraServer.startAutomaticCapture(1);
        // vidSink = CameraServer.getServer();
        // vidSink.setSource(frontCamera);

    
   //     cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");

        // Set the resolution
   //     frontCamera.setResolution(640, 480);
   //     rearCamera.setResolution(640, 480);

   //     frontCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
   //     rearCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    }


    /**Sets the distance away from the goal we want to shoot from */
    public void setDesiredDistance(double dist) {
        desiredDistance = dist;
    }

    /**Sets the threshold forward and backwards that is our safezone for shooting */
    public void setShootThreshold(double thresh) {
        threshold = thresh;
    }

    public void setAutoShoot(boolean auto) {
        autoShoot = auto;
    }

    /**Set both desired distance and threshold */
    public void setAutoShootConstraints(double dist, double thresh) {
        setDesiredDistance(dist);
        setShootThreshold(thresh);
    }

    
    

    


    /**Corrects distance and angle to goal before shooting */
    public void correctDistance() {
        double distanceToGoalInches = IO.calculateDistance(LIMELIGHT_MOUNT_ANGLE, LIMELIGHT_MOUNT_HEIGHT, HIGH_GOAL_DISTANCE);
        SmartDashboard.putNumber("Goal Dist", distanceToGoalInches);
        System.out.println("Goal Dist " + distanceToGoalInches);
        SmartDashboard.putNumber("Goal Angle", IO.getLimelightYAngle());
        //System.out.println("Distance to Goal: " + distanceToGoalInches);
        SmartDashboard.putNumber("Rotate State", rotateState);
        
        
        double targetOffsetAngleHorizontal = IO.getLimelightXAngle();
        SmartDashboard.putNumber("Target Offset", targetOffsetAngleHorizontal);
        //System.out.println(targetOffsetAngleHorizontal);
      
        double minDistance = desiredDistance - threshold;
        double maxDistance = desiredDistance + threshold;
        
        //This is the code that should correct for distance and angle
        switch(rotateState) {
          case STATE_DRIVEROP:
            //reset stuff 
            break;
          case STATE_PREPARE:
            gyro.reset();
            drivetrain.zeroEncoders();
            rotateState = STATE_CORRECT_DISTANCE;
            break;
          case STATE_CORRECT_DISTANCE: /*
            if(distanceToGoalInches > minDistance && distanceToGoalInches < maxDistance) {
              drivetrain.driveAuto(0);
              rotateState = STATE_CORRECT_ANGLE;
            } else if(distanceToGoalInches > maxDistance) {  //73 is the distance to goal in inches that shoots in low power mode to into the goal on the stools
              drivetrain.driveAuto(-0.7); 
            } else if (distanceToGoalInches < minDistance) {
              drivetrain.driveAuto(0.7);
            } 

            */
            SmartDashboard.putNumber("Desired", desiredDistance);
            SmartDashboard.putNumber("TO Goal", distanceToGoalInches);

            drivetrain.driveAuto(5, (desiredDistance - distanceToGoalInches), 0);
            if(distanceToGoalInches > minDistance && distanceToGoalInches < maxDistance) {
                rotateState = STATE_CORRECT_ANGLE;
            }
            break;
          case STATE_CORRECT_ANGLE:
            // driveCommand.turnToAngle(driveCommand.getRotationalPower(0.043, -targetOffsetAngleHorizontal)); //Turn to angle has a +/- 10 degree window where it will stop
            System.out.println("Target Offset: " + targetOffsetAngleHorizontal);
            double rotateRCW = driveCommand.getRotationalPower(0.043, targetOffsetAngleHorizontal);
            SmartDashboard.putNumber("RCW", rotateRCW);
            if(Math.abs(rotateRCW) <= 0.01) {
                rotateState = STATE_RESET;
                iter = 0;
              } else {
                driveCommand.turnToAngle(rotateRCW);
                iter ++;
              }
            
            break;
          case STATE_RESET:
            drivetrain.driveAuto(0);
            driveCommand.cancelTurn();
            rotateState = STATE_DRIVEROP;
            break;
          case STATE_CORRECT_DONE:
            break;
        }

        SmartDashboard.putNumber("Rotate State", rotateState);
    }

    /** The program to collect balls. Each state represents a different part of the process to shoot. */
    public void collect(boolean isAuto) {
        SmartDashboard.putNumber("Col. State", state);
        //System.out.println("Collect State " + state);
        if(IO.xButtonIsPressed(false) || IO.isHIDButtonPressed(HID_COLLECTOR_OFF, false)) {
            state = STATE_NOT_COLLECTING;
        }
        // int ballColor = arduino.getBallColor();
        switch(state) {
            case STATE_UNKNOWN:
                    collector.setIntakeSpeed(0);
                    // shooter.pickBallUp(state);
                    break;

            case STATE_NOT_COLLECTING:
                    collector.setBallLifterState(true);
                    collector.setIntakeSpeed(0);
                    //arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_BLACK);
                    
                    //SHOOTER-----
                    shooter.setPercentPower(0);
                    //SmartDashboard.putBoolean("Low Shooter Power Mode", lowPower);

                    //MOVE INTO THE NEXT STATE ---------
                    if(IO.isHIDButtonPressed(HID_COLLECTOR_ON, false)) {  // IO.aButtonIsPressed(false) || 
                       state = STATE_COLLECTING; 
                    } if (IO.isHIDButtonPressed(HID_COLLECTOR_REVERSE, false)) {
                        state = STATE_REVERSE_COLLECTOR;
                    }
                    break;

            case STATE_COLLECTING:
                    //SmartDashboard.putNumber("Ball Col", arduino.getBallColor());
                    collector.setBallLifterState(false);
                    collector.setIntakeSpeed(.4);
                    //arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_YELLOW);
                    // SHOOTER----------
                    shooter.setPercentPower(0,2);      // Shooter motor 2 off
                    shooter.setPercentPower(0,1);      // Shooter motor 1  off
                    shooter.setPercentPower(0.1, 0); 

                    //MOVE INTO THE NEXT STATE
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
                    //SmartDashboard.putNumber("Ball Col", arduino.getBallColor());
                    counterTimer = 0;
                    collector.setIntakeSpeed(0);
                    shooter.setPercentPower(0); // Shooter motors off
                    collector.setBallLifterState(true);
                    arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_GREEN);
                    /*
                    if (false) {
                        if (ballColor != COLOR_UNKNOWN) {
                            if (ballColor != IO.getAllianceColor()) {
                                shooter.setShotPower(0.15);
                                state = STATE_PREPARE_TO_SHOOT;
                            }
                        }
                    }
                    */
                    if (IO.isHIDButtonPressed(HID_COLLECTOR_REVERSE, false)) {
                        state = STATE_REVERSE_COLLECTOR;
                    }
                    if (IO.isHIDButtonPressed(HID_SHOOT_HIGH, false)) {
                        shooter.setShotPower(SHOOT_HIGH_POWER);
                        //SmartDashboard.putNumber("Shooter Power", 1);
                        state = STATE_PREPARE_TO_SHOOT;
                    }
                    if (IO.isHIDButtonPressed(HID_SHOOT_LOW, false)) {
                        shooter.setShotPower(SHOOT_LOW_POWER);
                        //SmartDashboard.putNumber("Shooter Power", 2);
                        if(autoShoot) {
                            state = STATE_POSITION_SHOT;
                        } else {
                            state = STATE_PREPARE_TO_SHOOT;
                        }
                    }
                    if (IO.isHIDButtonPressed(HID_SHOOT_LAUNCH_PAD, false)) {
                        shooter.setShotPower(SHOOT_LAUNCH_POWER);  // .9 or 1?
                        //SmartDashboard.putNumber("Shooter Power", 3);
                        state = STATE_PREPARE_TO_SHOOT;
                    }
                    if (IO.isHIDButtonPressed(HID_SHOOT_EJECT, false)) {
                        shooter.setShotPower(0.15);
                        //SmartDashboard.putNumber("Shooter Power", 4);
                        state = STATE_PREPARE_TO_SHOOT;
                    }
                    break;

            case STATE_PREPARE_TO_SHOOT:
                    if(counterTimer == 0) {
                        collector.setBallLifterState(false);
                        collector.setIntakeSpeed(0);
                        arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_BLUE);
                        shooter.setPercentPowerToShotPowerLevel();
                    }   
                    
                    // setCorrectState(1);
                    // if(getCorrectState() == STATE_RESET) {
                    //     state = STATE_SHOOT;
                    // }
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
                    if(counterTimer == 10) {
                        collector.setBallLifterState(true);
                        setCorrectState(STATE_DRIVEROP);
                    }
                    if(counterTimer == 50){
                        counterTimer = 0;
                        state = 1;
                    }
                    else {
                        counterTimer++;
                    }
                    break;  
            case STATE_POSITION_SHOT:
                break;
            case STATE_REVERSE_COLLECTOR:
                collector.setBallLifterState(true);
                collector.setIntakeSpeed(-.4);
                arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_RED);
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
