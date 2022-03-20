package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import static frc.robot.RobotMap.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Arduino {
    
    private DigitalInput sensorBallRed = new DigitalInput(3);
    private DigitalInput sensorBallBlue = new DigitalInput(4);
    
    
    /** Arduino Init */
    public void init() {
        // Establish Connection to Arduino
    }  
    
    /** Gets sensor data of ball color */
    public boolean isBallRed() {        
        return sensorBallRed.get();
    }

    public boolean isBallBlue() {        
        return sensorBallBlue.get();
    }

    public int getBallColor() {        
        if (isBallBlue()) {
            return COLOR_BLUE;
        }
        else if (isBallRed()) {
            return COLOR_RED;
        }
        else {
            return COLOR_UNKNOWN;
        }
    }

    /**  LED CONTROL */
    public void defineLEDString (int port, int numLEDs) {
        // send to Arduino the port and numLEDs
    }
    
    public void setLEDColor (int port, int colorCode) {
        // send port and colorCode to Arduino
    }
}
