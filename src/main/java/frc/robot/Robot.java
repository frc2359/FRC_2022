// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Relay;

//classes we make are imported here:
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Lifter;
import frc.robot.autonomous.*;
import static frc.robot.RobotMap.*;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

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
    public static final Lifter lifter = new Lifter();
    public static final StartAutonomous auto = new StartAutonomous(shooter, drivetrain, collector);
    public static final Climb climbCommand = new Climb(lifter, drivetrain);
    public static final Relay led = new Relay(ID_LED);
    public static final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    public static final Drive driveCommand = new Drive(drivetrain);
    public static final Collect collectCommand = new Collect(collector, shooter, drivetrain, gyro, driveCommand);

    boolean isComplete;
    int rotateState;
    boolean driven;
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

        // Calibrate Gyro
        gyro.calibrate();
    }

    
    /** This function is run once each time the robot enters autonomous mode. */
    @Override
    public void autonomousInit() {
        drivetrain.zeroEncoders();
        auto.init();
        gyro.reset();
    }
    
    
    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {}

    /**
     * This function is called once each time the robot enters teleoperated mode.
     */
    @Override
    public void teleopInit() {
        collectCommand.setCollectorState(1);
        led.setDirection(Relay.Direction.kForward);
        SmartDashboard.putNumber("Speed", 0);
        drivetrain.zeroEncoders();
        SmartDashboard.putNumber("Alliance", IO.getAllianceColor());
        isComplete = false;
        rotateState = 0;
        iter = 0;
        gyro.reset();
        driven = false;

        //FOR TESTING --------------------------------
        collectCommand.setAutoShootConstraints(73, 3);
    }

    /** This function is called periodically during teleoperated mode. */
    @Override
    public void teleopPeriodic() {
        // TEMP
        SmartDashboard.putNumber("HID Ax 0", IO.getHIDAxis(0));
        SmartDashboard.putNumber("HID Ax 1", IO.getHIDAxis(1));
        climbCommand.climb(false);
        

        collectCommand.correctDistance();
        collectCommand.setCorrectState(STATE_DRIVEROP);

        collectCommand.collect(true);

        if(IO.startButtonIsPressed(true)) {
          drivetrain.setAutoDrive(false);
        }
        
        if(!drivetrain.getAutoDrive()) {
          drivetrain.arcadeDrive();
        }


        if (IO.xButtonIsPressed(true)) {
          collectCommand.setCollectorState(STATE_RESET);
        }
        SmartDashboard.putBoolean("Loaded?", collector.isBallLoaded());

    }
}