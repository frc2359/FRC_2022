package frc.robot.subsystems;

//import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import static frc.robot.RobotMap.*;

//import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import frc.robot.IO;


public class Collector {
    private DigitalInput sensorBall1 = new DigitalInput(DIO_BALL_SENSOR_1);
    //private DigitalInput sensorBall2 = new DigitalInput(DIO_BALL_SENSOR_2);
    private Compressor airCompressor = new Compressor(ID_PNEUMATIC_HUB, PneumaticsModuleType.REVPH);
    private Solenoid solBallLifter = new Solenoid(PneumaticsModuleType.REVPH, ID_SOL_BALL_LIFTER);
    private WPI_VictorSPX intakeMotor = new WPI_VictorSPX(ID_INTAKE_MOTOR);


    /** Gets sensor data of whether the ball is loaded in the shooter or not */
    public boolean isBallLoaded() {        
        return !sensorBall1.get(); // || !sensorBall2.get();
    }

    public void init() {
        airCompressor.enableDigital();
        //solLifter.set(true);
        // intakeMotor.set(.4);
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


    // ******************* Can probably delete this function... *********************
    /*
    public void runPneumatics() {
        if(IO.aButtonIsPressed(false)) {
            setBallLifterState(true);       
          }
          else if(IO.bButtonIsPressed(false)){
            setBallLifterState(false);
          }
      
          SmartDashboard.putBoolean("Compressor", compressorStatus());
          SmartDashboard.putBoolean("Pressure Sw", pressureSwitchStatus());
      
          if(IO.xButtonIsPressed(false)) {
            enableCompressor(true);
            SmartDashboard.putBoolean("X Button", true);      
          }
          else if(IO.yButtonIsPressed(false)){
            enableCompressor(false);
            SmartDashboard.putBoolean("X Button", false); 
          }
    }
    */
    
       
}
