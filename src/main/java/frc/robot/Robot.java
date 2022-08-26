// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//classes we make are imported here:
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Collector;
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
    public static final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    public static final Drive driveCommand = new Drive(drivetrain);
    public static final Collect collectCommand = new Collect(collector, shooter, drivetrain, gyro, driveCommand);

    int rotateState;

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {

        // initiate subsystems
        drivetrain.init();
        shooter.init();
        collector.init();

        // Calibrate Gyro
        gyro.calibrate();
    }

    
    /** This function is run once each time the robot enters autonomous mode. */
    @Override
    public void autonomousInit() {
        //zero encoders and gyros
        drivetrain.zeroEncoders();
        gyro.reset();
    }
    
    
    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {}

    /** This function is called once each time the robot enters teleoperated mode. */
    @Override
    public void teleopInit() {
        collectCommand.setCollectorState(1);
        SmartDashboard.putNumber("Speed", 0);
        drivetrain.zeroEncoders();
        rotateState = 0;
        gyro.reset();
    }

    /** This function is called periodically during teleoperated mode. */
    @Override
    public void teleopPeriodic() {
        collectCommand.collect(true);
        drivetrain.arcadeDrive();
        
        if (IO.xButtonIsPressed(true)) {
          collectCommand.setCollectorState(STATE_RESET);
        }
        
    }
}