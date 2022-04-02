// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
// import edu.wpi.first.wpilibj.GenericHID;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay;

//classes we make are imported here:
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Arduino;
import frc.robot.subsystems.Lifter;
import frc.robot.autonomous.*;
import static frc.robot.RobotMap.*;
import frc.robot.IO;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.*;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the manifest
 * file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
    public static final Drivetrain drivetrain = new Drivetrain();
    public static final Shooter shooter = new Shooter();
    public static final Collector collector = new Collector();
    public static final Arduino arduino = new Arduino();
    public static final Lifter lifter = new Lifter();
    public static final StartAutonomous auto = new StartAutonomous(shooter, drivetrain, collector, arduino);
    public static final Climb climbCommand = new Climb(lifter);
    public static final Relay led = new Relay(ID_LED);
    public static final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    public static final Drive driveCommand = new Drive(gyro, drivetrain);
    public static final Collect collectCommand = new Collect(collector, shooter, arduino, drivetrain, gyro, driveCommand);

    boolean isComplete;
    int rotateState;
    boolean doneDriving;
    boolean driven;

    boolean drive = false;
    double kP = 0.05;
    double integral = 0;
    double previousError;
    int iter = 0;

    /**
     * This function is run when the robot is first started up and should be used
     * for any
     * initialization code.
     */
    @Override
    public void robotInit() {

        // initiate subsystems
        drivetrain.init();
        shooter.init();
        collector.init();
        lifter.init();
        arduino.init();

        // Calibrate Gyro
        gyro.calibrate();

        // Define LED strings
        arduino.defineLEDString(LED_STRING_COLLECTOR, 24);
        arduino.defineLEDString(LED_STRING_LIFTER_LEFT, 24);
        arduino.defineLEDString(LED_STRING_LIFTER_RIGHT, 24);
        arduino.defineLEDString(LED_STRING_UNDERBODY, 60);
        arduino.defineLEDString(LED_STRING_UPPERBODY, 60);

        arduino.setLEDColor(LED_STRING_COLLECTOR, LED_COLOR_PURPLE);
        arduino.setLEDColor(LED_STRING_LIFTER_LEFT, LED_COLOR_YELLOW);
        arduino.setLEDColor(LED_STRING_LIFTER_RIGHT, LED_COLOR_RED);
        arduino.setLEDColor(LED_STRING_UNDERBODY, LED_COLOR_GREEN);
        if (IO.isAllianceBlue()) {
            arduino.setLEDColor(LED_STRING_UPPERBODY, LED_COLOR_BLUE);
        } else if (IO.isAllianceRed()) {
            arduino.setLEDColor(LED_STRING_UPPERBODY, LED_COLOR_RED);
        } else {
            arduino.setLEDColor(LED_STRING_UPPERBODY, LED_COLOR_BLACK);
        }

    }

    /** This function is run once each time the robot enters autonomous mode. */
    @Override
    public void autonomousInit() {
        collectCommand.setCollectorState(0);
        drivetrain.zeroEncoders();
        System.out.println("Init");
        auto.init();
        gyro.reset();
        // gyro.calibrate();
        isComplete = false;


        iter = 0; // Added by Mr. R. otherwise you can't run auto more than once in a row...
        integral = 0;
        doneDriving = false;

        // collectCommand.init();

    }
    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
      // switch  
        SmartDashboard.putNumber("Gyro ", gyro.getAngle());
        // Gyro occastionally fails to return values, causing an infinite spin. I'm not sure why.
        driveCommand.turnToAngle(45, 0.033); //I've only tested this version without an integral value
        driveCommand.turnToAngle(45, 0.033, 0.2, iter);
        iter++;
    }

    /**
     * This function is called once each time the robot enters teleoperated mode.
     */
    @Override
    public void teleopInit() {
        arduino.setLEDColor(0, LED_COLOR_RED);
        collectCommand.setCollectorState(1);
        collectCommand.init();
        led.setDirection(Relay.Direction.kForward);
        SmartDashboard.putNumber("Speed", 0);
        drivetrain.zeroEncoders();
        //SmartDashboard.putNumber("Alliance", IO.getAllianceColor());
        isComplete = false;
        rotateState = 0;
        iter = 0;
        gyro.reset();
        driven = false;
    }

    /** This function is called periodically during teleoperated mode. */
    @Override
    public void teleopPeriodic() {
        // TEMP
        SmartDashboard.putNumber("HID Ax 0", IO.getHIDAxis(0));
        SmartDashboard.putNumber("HID Ax 1", IO.getHIDAxis(1));
        climbCommand.climb(false);
        
        driveCommand.printAngle();

        collectCommand.collect(true);

        drivetrain.arcadeDrive();

        lifter.show();

        if (IO.xButtonIsPressed(true)) {
          collectCommand.setCollectorState(STATE_RESET);
        }

        SmartDashboard.putBoolean("Loaded?", collector.isBallLoaded());
        shooter.shooterPeriodic();

        /*
        if (collector.isBallLoaded()) {
          led.set(Relay.Value.kOn);
        } else {
          led.set(Relay.Value.kOff);
        }
        */

    }

    // TEST SHOULD NOW BE A MATCH SIMULATION ------------------------------------------------------------------------------------------------------------------

    /** This function is called once each time the robot enters test mode. */
    @Override
    public void testInit() {
        iter = 0;
        autonomousInit();
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {
        if(iter < 50 * 15) {
            SmartDashboard.putString("Match", "Autonomous");
            autonomousPeriodic();
        } else if(iter >= 50 * 15 && iter < 50 * 20) {
            teleopInit();
            SmartDashboard.putString("Match", "TeleOp");
        } else if (iter > 50 * 15 && iter < 50 * 135) {
            teleopPeriodic();
            SmartDashboard.putString("Match", "TeleOp");
        } else {
            SmartDashboard.putString("Match", "Over");
            drivetrain.stopMotors();
        }
        iter++;
    }
}
