package frc.robot.autonomous;

import static frc.robot.RobotMap.*;

import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.IO;

public class Climb {

    private Lifter lifter;
    private Drivetrain drivetrain;
    private int state = 0;
    private int iter = 0;
    
    private int counterTimer = 0; //counter that we use to count time. TeleOP periodic runs 50 times per second, so checking for a count of 50 = 1s
    private int counterArmsTimer = 0;
    private boolean calibrateDir = false;
    private boolean calibrateArmsDir = false;
 
    public Climb(Lifter lif, Drivetrain dr) {
        lifter = lif;
        drivetrain = dr;
    }

    /** Sets a new state of the lift command. */
    public void setState(int setPoint) {
        state = setPoint;
    }

    public void init() {
        lifter.moveLifter(-1); // Move Lifter to Home Position
    }

    public int getState() {
        return state;
    }

    public boolean isLinedUp() {
        return (lifter.isHorizPipe(true) && lifter.isHorizPipe(false));
    }

    public void lineUp() {
        if(isLinedUp()) {
            drivetrain.tankDrive(0, 0);
        } else if (lifter.isHorizPipe(true)) {
            drivetrain.tankDrive(0, 0.2);
        } else if (lifter.isHorizPipe(false)) {
            drivetrain.tankDrive(0.2, 0);
        } else {
            if(iter < 50) {
                System.out.println("Driving...");
                drivetrain.driveAuto(-0.2);
                iter++;
            } else {
                drivetrain.tankDrive(0, 0);
            }
        }
    }
    
    public void climb(boolean isAuto) {
        if(IO.xButtonIsPressed(true)) {
            state = ST_LIFTER_UNKNOWN;
        } if(IO.yButtonIsPressed(true)) {
            state = ST_LIFTER_PREPARE;
        }
        SmartDashboard.putNumber("Lift St", state);
        switch(state) {
            // Lifter Unknown State when first powered on
            case ST_LIFTER_UNKNOWN:
                lifter.stopLifter();
                counterArmsTimer = 0;
                counterTimer = 0;
                calibrateDir = false;
                calibrateArmsDir = false;
                // state = ST_LIFTER_CALIBRATE;
                state = ST_LIFTER_NOT_CLIMBING;
                break;
            // Calibrate Lifter and Arms
            case ST_LIFTER_CALIBRATE:
                lifter.setAutoMode(true);
                if(lifter.isHome() && lifter.isLifterCalibrated() && lifter.isArmsHome() && lifter.isArmsCalibrated()) {
                    lifter.stopLifter();
                    lifter.stopSpin();
                    state = ST_LIFTER_NOT_CLIMBING;
                    counterTimer = 0;
                } else {
                    /* Calibrate Lifter */
                    /*if(!lifter.isHome()) {
                        counterTimer++;
                        if (calibrateDir) { // Down
                            lifter.moveLifter(-.15);
                        } else { // Up
                            lifter.moveLifter(.15);
                            if (counterTimer > 50) { // Up for 1 Second
                                calibrateDir = true;
                            }    
                        }
                    } else {
                        counterTimer = 0;
                        lifter.stopLifter();
                    }*/
                    counterTimer++;
                    if (calibrateDir) { // Down
                        lifter.moveLifter(-.15);
                        if (lifter.isHome()) {
                            lifter.stopLifter();
                            counterTimer = 0;
                        }
                    } else { // Up
                        lifter.moveLifter(.15);
                        if (counterTimer > 50) { // Up for 1 Second
                            calibrateDir = true;
                        }    
                    }
                    /* Calibrate Arms */
                    /*if(lifter.isArmsHome()) {
                        lifter.stopSpin();
                        counterArmsTimer = 0;
                    } else {
                        counterArmsTimer++;
                        if (calibrateArmsDir) { // Reverse until home
                            lifter.spin(-.15);
                        } else { // Forward
                            lifter.spin(.15);
                            if (counterArmsTimer > 50) { // forward for 1 Second
                                calibrateArmsDir = true;
                            }    
                        }
                    }*/
                    counterArmsTimer++;
                    if (calibrateArmsDir) { // Reverse until home
                        lifter.spin(-.15);
                        if(lifter.isArmsHome()) {
                            lifter.stopSpin();
                            counterArmsTimer = 0;
                        }
                    } else { // Forward
                        lifter.spin(.15);
                        if (counterArmsTimer > 50) { // forward for 1 Second
                            calibrateArmsDir = true;
                        }    
                    }
                }
                break;
            // Lifter is not climbing (until button pressed) or is in manual operation
            case ST_LIFTER_NOT_CLIMBING:
                lifter.setAutoMode(false);
                break;

            case ST_LIFTER_PREPARE:
                drivetrain.setAutoDrive(true);
                lifter.setAutoMode(true);
                lifter.moveLifter(0.25);
                if(lifter.isAboveBar()) {
                    lifter.stopLifter();
                    iter = 0;
                    state = ST_ABOVE_BAR;
                }
                break;

            case ST_ABOVE_BAR:
                lifter.setAutoMode(false);
                if(isLinedUp()) {
                    drivetrain.setAutoDrive(false);
                    state = ST_LIFT_INITIAL;
                } else {
                    lineUp();
                }
                break;

            case ST_LIFT_INITIAL:
                lifter.setAutoMode(true);
                if(lifter.isHome()) {
                    lifter.setLifterHookState(false);  // Close Hook at Home position
                }
                if(lifter.getHeight() < -2 || lifter.isHookOpen()) {
                    lifter.stopLifter();               // Stop Motors just past Home
                    state = ST_LIFTER_ON_BAR;
                }
                lifter.moveLifter(-0.6);
                break;

            case ST_LIFTER_ON_BAR:
                lifter.setAutoMode(false);
                lifter.stopLifter();
                lifter.setLifterHookState(false);
                break;
            //case ST_TRAVERSE:
                
        }

    } /*

    
    The program to climb 
    public void climb(boolean isAuto) {
        SmartDashboard.putNumber("Lift St", state);
        //  System.out.println("Lifter State " + state);          

        if(IO.getHIDAxis(0)  >= 1) {      
            state = ST_LIFTER_RAISE;
        }
        if(IO.getHIDAxis(0) <= -1) {      
            state = ST_LIFTER_LOWER;
        }

        switch(state) {
            case ST_LIFTER_UNKNOWN: // unknown
                lifter.stopLifter();
                break;
            case ST_LIFTER_CALIBRATE:
                //calibrate things
            case ST_LIFTER_NOT_CLIMBING: // not climbing
                    lifter.moveLifter(0);
                    //if(IO.isHIDButtonPressed(HID_LIFTER_RAISE, false)) {  // maybe use positional joystick (up - down; left and right) - but not button
                    if(IO.getHIDAxis(0)  >= 1) { 
                        state = ST_LIFTER_RAISE; 
                    }
                    //if(IO.isHIDButtonPressed(HID_LIFTER_LOWER, false)) {
                    if(IO.getHIDAxis(0)  <= -1) {
                        state = ST_LIFTER_LOWER;
                    }
                    break;

            case ST_LIFTER_RAISE: // raise arms
                    lifter.moveLifter(.2);
                    if (lifter.isAboveBar()) {
                        state = ST_LIFTER_RAISED;
                    }
                    break;

            case ST_LIFTER_RAISED:
                    lifter.moveLifter(0);
                    if(IO.getHIDAxis(0)  <= -1) {
                        state = ST_LIFTER_LOWER;
                    }
                    break;          
                        
            case ST_LIFTER_LOWER: // lower arms
                    lifter.moveLifter(-.2);
                    if (lifter.isHome()) {
                        state = ST_LIFTER_HOME;
                    }
                    break;

            case ST_LIFTER_HOME:
                    lifter.moveLifter(0);
                    if(IO.getHIDAxis(0)  >= 1) { 
                        state = ST_LIFTER_RAISE; 
                    }
                    break;
            case ST_LINE_UP:
                lineUp();  
                break;      
                   
        }
    } */
}
