
package frc.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;




public class Drive{
    
    double P = 0.05;
    double I;
    double D;
    int integral, previous_error, setpoint = 0;
    ADXRS450_Gyro gyro;
    DifferentialDrive robotDrive;
    double rcw;


    public Drive(ADXRS450_Gyro gyro) {
        this.gyro = gyro;
        double P = 0.05;
        SmartDashboard.putNumber("Drive P", P);
        double I = 0.0;
        SmartDashboard.putNumber("Drive I", I);
        double D = 0.0;
        SmartDashboard.putNumber("Drive D", D);
        System.out.println("here hello");
    }

    public void setSetpoint(int setpoint) {
        this.setpoint = setpoint;
    }
/*
    public void changePIDValues() {
        P = SmartDashboard.getNumber("Drive P", 0.225);
        I = SmartDashboard.getNumber("Drive I", 0.0675);
        D = SmartDashboard.getNumber("Drive D", 0.0);


    }
*/
    public void PID(){
        double realAngle = (gyro.getAngle() / 150) * 360;
        //double P = 0.05;
       
        double error = 60 - gyro.getAngle(); // Error = Target - Actual
        this.integral += (error * .02); // Integral is increased by the error*time (which is .02 seconds using normal IterativeRobot)
        double derivative = (error - this.previous_error) / .02;
        System.out.println("P: " + P);
        System.out.println("I: " + I);
        System.out.println("D: " + D);

        rcw = P * error + I * this.integral + D * derivative;
        System.out.println("rcw: " + rcw);

    }

    public void execute(DifferentialDrive robotDrive)
    {
        System.out.println("rcw: " + rcw);
        PID();
        System.out.println("Executing...");
        robotDrive.arcadeDrive(0, rcw);
    }
}
