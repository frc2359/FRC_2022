package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import static frc.robot.RobotMap.*;


public class Collector {
    private DigitalInput sensorBall = new DigitalInput(1);
    //private Compressor airCompressor = new Compressor(1);
    //private Solenoid solLifter = new Solenoid(7);


    public boolean isBallLoaded() {
        return !sensorBall.get();
    }




    
}
