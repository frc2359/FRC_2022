package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import static frc.robot.RobotMap.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.SerialPort;


public class Arduino {
    
    private DigitalInput sensorBallRed = new DigitalInput(3);
    private DigitalInput sensorBallBlue = new DigitalInput(4);

    private SerialPort arduinoUSB;
    private Timer timer; 

    private String cmd;
    private boolean arduinoFound;
    
    
    /** Arduino Init */
    public void init() {
        // Establish Connection to Arduino
        try {
            arduinoUSB = new SerialPort(9600, SerialPort.Port.kUSB);
            System.out.println("Connected on kUSB!");
            arduinoFound = true;
        } catch (Exception e) {
            System.out.println("Failed to connect on kUSB, trying kUSB1");
            try {
                arduinoUSB = new SerialPort(9600, SerialPort.Port.kUSB1);
                System.out.println("Connected on kUSB1!");
                arduinoFound = true;
            } catch (Exception e1) {
                System.out.println("Failed to connect on kUSB1, trying kUSB2");
                try {
                    arduinoUSB = new SerialPort(9600, SerialPort.Port.kUSB2);
                    System.out.println("Connected on kUSB2!");
                    arduinoFound = true;
                } catch (Exception e2) {
                    System.out.println("Failed to connect on kUSB2, all attempts failed.");
                    arduinoFound = false;
                }
            }
        }
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

    public boolean isArduinoFound() {
        return arduinoFound;
    }

    /**  LED CONTROL */
    public void defineLEDString (int port, int numLEDs) {
       // send to Arduino the port and numLEDs
        cmd = "D"+Integer.toString(port)+","+Integer.toString(numLEDs);
        System.out.println(cmd);
        if (arduinoFound) {
            arduinoUSB.writeString(cmd);
            arduinoUSB.write(new byte[]{0x0A}, 1);
        }
     }
    
    public void setLEDColor (int port, int colorCode) {
        // send port and colorCode to Arduino
        cmd = "S"+Integer.toString(port)+","+Integer.toString(colorCode);
        System.out.println(cmd);
        if (arduinoFound) {
            arduinoUSB.writeString(cmd);
            arduinoUSB.write(new byte[]{0x0A}, 1);
        }
    }
}
