package frc.robot.autonomous;

import static frc.robot.RobotMap.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.IO;

public class Climb {

    private Lifter lifter;
    private int state = 0;
    private int counterTimer = 0; //counter that we use to count time. TeleOP periodic runs 50 times per second, so checking for a count of 50 = 1s
 
    public Climb(Lifter lif) {
        lifter = lif;
    }

    /** Sets a new state of the lift command. */
    public void setState(int setPoint) {
        state = setPoint;
    }

    public void init() {
       
    }

    public int getState() {
        return state;
    }

    /** The program to climb */
    public void collect(boolean isAuto) {
        SmartDashboard.putNumber("Col. State", state);
        System.out.println("Collect State " + state);
        //SmartDashboard.putNumber("Ball Col", arduino.getBallColor());
        if(IO.xButtonIsPressed(false) || IO.isHIDButtonPressed(HID_COLLECTOR_OFF, false)) {
            state = STATE_COLLECTING;
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
                    arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_BLACK);
                    shooter.pickBallUp(state);
                    if(IO.isHIDButtonPressed(HID_COLLECTOR_ON, false)) {  // IO.aButtonIsPressed(false) || 
                       state = STATE_COLLECTING; 
                    }
                    if(IO.isHIDButtonPressed(HID_COLLECTOR_REVERSE, false)) {
                        state = STATE_REVERSE_COLLECTOR;
                    }
                    break;

            case STATE_COLLECTING: // Looking for ball
            // if(!isAuto) {vidSink.setSource(frontCamera);}

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

            case STATE_SECURE_BALL: // Secure ball
            // if(!isAuto) {vidSink.setSource(rearCamera);}

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

            case STATE_PREPARE_TO_SHOOT: // Prepare to Shoot
            // if(!isAuto) {vidSink.setSource(rearCamera);}
                    if(counterTimer == 0) {
                        collector.setBallLifterState(false);
                        collector.setIntakeSpeed(0);
                        arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_BLUE);
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
                        // vidSink.setSource(rearCamera);
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
