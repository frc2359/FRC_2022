package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import static frc.robot.RobotMap.*;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.IO;


public class Lifter {

    private CANSparkMax lifterLeft = new CANSparkMax(CAN_ID_LIFTER_LEFT, MotorType.kBrushless);
    private CANSparkMax lifterRight = new CANSparkMax(CAN_ID_LIFTER_RIGHT, MotorType.kBrushless);
    private TalonSRX lifterArm = new TalonSRX(CAN_ID_LIFTER_ARM);
 
    private DigitalInput sensorPaddleLeft = new DigitalInput(DIO_LIFTER_PADDLE_LEFT);
    private DigitalInput sensorPaddleRight = new DigitalInput(DIO_LIFTER_PADDLE_RIGHT);
    private DigitalInput sensorHomeLeft = new DigitalInput(DIO_LIFTER_HOME_LEFT);
    private DigitalInput sensorHomeRight = new DigitalInput(DIO_LIFTER_HOME_RIGHT);
    private DigitalInput sensorHookOpenLeft = new DigitalInput(DIO_LIFTER_HOOK_OPEN_LEFT);
    private DigitalInput sensorHookOpenRight = new DigitalInput(DIO_LIFTER_HOOK_OPEN_RIGHT);
    private DigitalInput sensorHookClosedLeft = new DigitalInput(DIO_LIFTER_HOOK_CLOSED_LEFT);
    private DigitalInput sensorHookClosedRight = new DigitalInput(DIO_LIFTER_HOOK_CLOSED_RIGHT);
 
    private Solenoid solLifterHook = new Solenoid(PneumaticsModuleType.REVPH, ID_SOL_LIFTER_HOOK);

    public void init() {
        
    }

       
}
