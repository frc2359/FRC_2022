// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
// import edu.wpi.first.wpilibj.GenericHID;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj.command.Scheduler;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.Timer;


//classes we make are imported here:
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
// import frc.robot.IO;




/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
  // private final DifferentialDrive m_robotDrive = new DifferentialDrive(new PWMSparkMax(0), new PWMSparkMax(1));
  // private final Joystick m_stick = new Joystick(0);
  private final Timer m_timer = new Timer();
  // public static final Drivetrain drivetrain = new Drivetrain();
  public static final Shooter shooter = new Shooter();

  //These were on the FRC_2021 project - I'm not sure if they have to do with the radio, so I just added them in:
  public static NetworkTableInstance rpi3;
  //public static NetworkTableEntry to_the_right;
  //public static NetworkTableEntry to_the_left;
  public static NetworkTableEntry ultrasonicReading;

  //This is proactive - I'm not sure we'll end up NEEDING this, but I'm guessing it will be nescessary
  public static final double DRIVE_SENSITIVITY_MULT = 1;
  

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    //instantiate network interface for Raspberry pi
    final NetworkTableInstance robotNetInst = NetworkTableInstance.getDefault();
		final NetworkTable robotNet = robotNetInst.getTable("obs");

    //initiate subsystems 
    // drivetrain.init();
    shooter.init();
  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
    //repeat = -10;
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
      // drivetrain.autoDistDrive(1, 0.2);
  }

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {
    // drivetrain.arcadeDrive();
    shooter.shooterPeriodic();
  }

  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}


}
