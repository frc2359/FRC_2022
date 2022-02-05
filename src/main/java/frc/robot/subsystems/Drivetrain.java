package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import static frc.robot.RobotMap.*;
import frc.robot.IO;


public class Drivetrain implements Subsystem {
    //The Falcon 500s are a unit that include Talon FXs as their base motors, over which there is an encoder built in.
    WPI_TalonFX frontLeft = new WPI_TalonFX(ID_DRIVE_FL);
    WPI_TalonFX frontRight = new WPI_TalonFX(ID_DRIVE_FR);
    // WPI_TalonFX backLeft = new WPI_TalonFX(ID_DRIVE_BR);
    // WPI_TalonFX backRight = new WPI_TalonFX(ID_DRIVE_BL);
    Timer timer = new Timer(); //for timing autonomous functions
    private DifferentialDrive drive = new DifferentialDrive(frontLeft, frontRight); //front motors are masters & control inputs for both front and back
    

    /** drive function that can be called without having to pass in private vairables **/
    public void arcadeDrive() {
        if ((IO.getDriveTrigger() - IO.getReverseTrigger()) > 1 || (IO.getDriveTrigger() - IO.getReverseTrigger()) < -1) {
            System.out.println("out of bounds drive value. go to Drivetrain.java line 34 and edit to an in-bounds expression");
        } else {
            // drive.arcadeDrive(IO.getThrottle() * DRIVE_SPEED_MULT, IO.getLeftXAxis() * DRIVE_SPEED_MULT);
            if(IO.getThrottle() < 0){
                drive.arcadeDrive(Math.pow(IO.getThrottle(), 2) / 10, IO.getLeftXAxis() * DRIVE_SPEED_MULT);
                System.out.println("Throttle: " + (Math.pow(IO.getThrottle(), 2) / 10));

            } else {
                drive.arcadeDrive((IO.getThrottle() * IO.getThrottle()) / 10, IO.getLeftXAxis() * DRIVE_SPEED_MULT);
            }
            System.out.println("R: " + frontLeft.getSelectedSensorPosition());
            System.out.println("L: " + frontRight.getSelectedSensorPosition());
            System.out.println("A: " + IO.getDriveDistance(frontRight.getSelectedSensorPosition(), frontLeft.getSelectedSensorPosition(), true) +"\n");
        }
    }

    /**  automated drive function that can be called and executed without direct input from a controller **/
    public void autoTimeDrive(double dist, double speed, double turn) {
        if (IO.getDriveDistance(frontLeft.getSelectedSensorPosition(), frontRight.getSelectedSensorPosition(), true) > dist && speed < 1 && speed > -1 && turn < 1 && turn > -1) {
            drive.arcadeDrive(speed * DRIVE_SPEED_MULT, turn);
        } else {
            this.stopMotors();
        }
    }
    

    /** drive a distance at a speed (uses encoder data) -- NEEDS UPDATING*/
    public void autoDistDrive(double dist, double speed) {
        //code
    }


    /**initialize the drivetrain**/
    public void init() {
        /* Motor controllers default motor safety OFF.
            WPI drive trains default motor safety ON.
            Experiment with different enables below.... */
        //frontLeft.setSafetyEnabled(true);
        //frontRight.setSafetyEnabled(true);
        //drive.setSafetyEnabled(false);


        //Reset Motor Controllers to Factory Configuration
        frontLeft.configFactoryDefault();
        frontRight.configFactoryDefault();
        // backLeft.configFactoryDefault();
        // backRight.configFactoryDefault();
        
        //Set motors that are on the same side to follow each other (both left together, both right together)
        // backLeft.follow(frontLeft);
        // backRight.follow(frontRight);


        //Set Motor Direction and Encoder Sensor Phase
        frontLeft.setInverted(false);      // Positive is forward
        // backLeft.setInverted(InvertType.FollowMaster);     
        frontRight.setInverted(true);      // Invert so positive is forward
        // backRight.setInverted(InvertType.FollowMaster);

        frontLeft.setSensorPhase(false); // Check
        frontRight.setSensorPhase(true); // Check

        //init sensor position
        frontLeft.setSelectedSensorPosition(0);
        frontRight.setSelectedSensorPosition(0);

        //Set Brake/Coast Options
        frontLeft.setNeutralMode(BRAKE_MODE_DRIVE ? NeutralMode.Brake : NeutralMode.Coast);
        // backLeft.setNeutralMode(BRAKE_MODE_DRIVE ? NeutralMode.Brake : NeutralMode.Coast);
        frontRight.setNeutralMode(BRAKE_MODE_DRIVE ? NeutralMode.Brake : NeutralMode.Coast);
        // backRight.setNeutralMode(BRAKE_MODE_DRIVE ? NeutralMode.Brake : NeutralMode.Coast);
        

        //Set Math.clamp Switch Positions
        final int kTimeoutMs = 30;  // Move to RobotMap??

        frontLeft.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.Disabled, kTimeoutMs);
        frontRight.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.Disabled, kTimeoutMs);
        
        /*
        * diff drive assumes (by default) that right side must be negative to move
        * forward. Change to 'false' so positive/green-LEDs moves robot forward
        */
        // drive.setRightSideInverted(false); // do not change this
        //1.13377687664 circ 8.667 ratio
    }

    public void initDefaultCommand() {}

    /**more diverse drive function. This is a work in progress.**/ //WE NEED TO GET THIS WORKING 
    public void drive() { 
        //convert the x-axis value given by the controller into a multiplier
        double lMult = 1; //speed multiplier
        double rMult = 1; //speed multiplier
        if(IO.getLeftXAxis() < 0) { //This is for steering. We will need to check the functionality of this 
            rMult = 0.5;
        } else {
            lMult = 0.5; 
        }
        if(IO.reverseTriggerIsPressed() && IO.throttleTriggerIsPressed()) { 
            //When both triggers are pressed, stop the robot. This is mostly to avoid potential issues that arise if there is no conditional here.
            frontLeft.stopMotor();
            frontRight.stopMotor();
        } else if(IO.throttleTriggerIsPressed()) {
            frontLeft.set(DRIVE_CONTROL_MODE, IO.getDriveTrigger() * lMult);
            frontRight.set(DRIVE_CONTROL_MODE,IO.getDriveTrigger() * rMult);
        } else if(IO.reverseTriggerIsPressed()) {
            frontLeft.set(DRIVE_CONTROL_MODE,IO.getReverseTrigger() * lMult);
            frontRight.set(DRIVE_CONTROL_MODE,IO.getReverseTrigger() * rMult);
        }
    }
    /**stops motors manually**/
    public void stopMotors() {
        frontLeft.stopMotor();
        frontRight.stopMotor();
        //should not be needed
        // backRight.stopMotor();
        // backLeft.stopMotor();
    }
}

