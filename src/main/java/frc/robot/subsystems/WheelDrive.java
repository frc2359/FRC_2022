package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.CANCoder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.AnalogInput;

public class WheelDrive {
    private WPI_TalonFX angleMotor;
    private WPI_TalonFX speedMotor;
    private PIDController pidController;
    private CANCoder encoder;

    public WheelDrive (int angleMotor, int speedMotor, int encoder) {
        this.angleMotor = new WPI_TalonFX (angleMotor);
        this.speedMotor = new WPI_TalonFX (speedMotor);
        this.encoder = new CANCoder(encoder);
        pidController = new PIDController (1, 0, 0);
        pidController.enableContinuousInput(-1.0, 1.0);
    }

    public void setTo(double setpoint, double target) {
        angleMotor.set(pidController.calculate(target, setpoint));
    }

    public WPI_TalonFX getAngleMotor(){
        return angleMotor;
    }

    public WPI_TalonFX getSpeedMotor(){
        return speedMotor;
    }

    public CANCoder getEncoder() {
        return encoder;
    }
}
