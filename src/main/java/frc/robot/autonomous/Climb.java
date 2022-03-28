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
        lifter.moveLifter(-1); // Move Lifter to Home Position
    }

    public int getState() {
        return state;
    }

 
    /** The program to climb */
    public void climb(boolean isAuto) {
        SmartDashboard.putNumber("Lift St", state);
        System.out.println("Lifter State " + state);
        SmartDashboard.putNumber("L. Height",lifter.getHeight());


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
                   
        }
    }
}
