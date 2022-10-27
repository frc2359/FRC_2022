package frc.robot;

// some other stuff Siven gotta import

private WPI_TalonFX angleMotor;
private  WPI_TalonFX speedMotor;
private PIDController pidController;


public class WheelDrive{

    public WheelDrive (int angleMotor, int speedMotor, int encoder) {
        this.angleMotor = new  WPI_TalonFX(angleMotor);
        this.speedMotor = new  WPI_TalonFX(speedMotor);
        pidController = new PIDController (1, 0, 0, new AnalogInput (encoder), this.angleMotor);

        pidController.setOutputRange (-1, 1);
        pidController.setContinuous ();
        pidController.enable ();
    }

    //  need to set a constant for the max voltage the swerve modules take
    private final double MAX_VOLTS = ;

    public void drive (double speed, double angle) {

        speedMotor.set (speed);
    

        double setpoint = angle * (MAX_VOLTS * 0.5) + (MAX_VOLTS * 0.5); // Optimization offset can be calculated here.
        if (setpoint < 0) {
            setpoint = MAX_VOLTS + setpoint;
        }
        if (setpoint > MAX_VOLTS) {
            setpoint = setpoint - MAX_VOLTS;
        }

        pidController.setSetpoint (setpoint);

    }
}    