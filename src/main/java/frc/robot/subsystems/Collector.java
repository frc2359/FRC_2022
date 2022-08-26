package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import static frc.robot.RobotMap.*;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;


public class Collector {
    private DigitalInput sensorBall1 = new DigitalInput(DIO_BALL_SENSOR_1);
    private Compressor airCompressor = new Compressor(ID_PNEUMATIC_HUB, PneumaticsModuleType.REVPH);
    private Solenoid solBallLifter = new Solenoid(PneumaticsModuleType.REVPH, ID_SOL_BALL_LIFTER);
    private WPI_VictorSPX intakeMotor = new WPI_VictorSPX(ID_INTAKE_MOTOR);


    /** Gets sensor data of whether the ball is loaded in the shooter or not */
    public boolean isBallLoaded() {        
        return !sensorBall1.get();
    }

    public void init() {
        airCompressor.enableDigital();
    }

    /** Sets whether the flippers are up or not */
    public void setBallLifterState(boolean up) {
        solBallLifter.set(up);
    }

    
    public boolean compressorStatus() {
        return airCompressor.enabled();
    }

    public boolean pressureSwitchStatus() {
        return airCompressor.getPressureSwitchValue();
    }

    public void enableCompressor(boolean on) {
        if (on) {
            airCompressor.enableDigital();
        }
        else {
            airCompressor.disable();
        }
    }

    public void setIntakeSpeed(double spd){
        intakeMotor.set(spd);
    }       
}
