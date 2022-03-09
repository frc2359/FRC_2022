package frc.robot.autonomous;

import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.IO;

public class Collect {
    private Collector collector;
    private Shooter shooter;
    private int state = 0;
    private int counterTimer = 0; //counter that we use to count time. TeleOP periodic runs 50 times per second, so checking for a count of 50 = 1s

    public Collect(Collector col, Shooter shoot) {
        collector = col;
        shooter = shoot;
    }

    /** Sets a new state of the collect command. */
    public void setState(int setPoint) {
        state = setPoint;
    }

    /** The program to collect balls. Each state represents a different part of the process to shoot. */
    public void collect() {
        SmartDashboard.putNumber("State", state);
        System.out.println("State " + state);
        switch(state) {
            case 0: // unknown
                    collector.setIntakeSpeed(0);
                    shooter.pickBallUp(state);
                    System.out.println("State " + state);
                    break;
            case 1: // not collectiong
                    collector.setBallLifterState(true);
                    collector.setIntakeSpeed(0);
                    System.out.println("State " + state);
                    shooter.pickBallUp(state);
                    if(IO.aButtonIsPressed(false)) {
                       state = 2; 
                    }
                    else { 
                        break;
                    } 
            case 2: // Looking for ball
                    collector.setBallLifterState(false);
                    collector.setIntakeSpeed(.4);
                    System.out.println("State " + state);
                    shooter.pickBallUp(state);
                    if(collector.isBallLoaded()) {
                        state = 3; 
                    }
                    else {
                        break;
                    }
            case 3: // Secure ball
                    collector.setIntakeSpeed(0);
                    System.out.println("State " + state);
                    shooter.pickBallUp(state);
                    collector.setBallLifterState(true);
                    if(IO.bButtonIsPressed(false)) { 
                        state = 4; 
                    } 
                    else {
                        break;
                    }
            case 4: // Prepare to Shoot
                    if(counterTimer == 0) {
                    collector.setBallLifterState(false);
                    }
                    collector.setIntakeSpeed(0);
                    System.out.println("State " + state);
                    shooter.pickBallUp(state);
                    //Add the code to pull and correct distance here
                    if(counterTimer == 50) {
                        state = 5;
                        counterTimer = 0;
                    }
                    else {
                        counterTimer++;
                        break;
                    }
            case 5: // Shooting
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
                        break;
                    }      
        }
    }
}
