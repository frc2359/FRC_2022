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
import frc.robot.autonomous.*;
import static frc.robot.RobotMap.*;
import frc.robot.IO;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;




/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
  private final Timer m_timer = new Timer();
  public static final Drivetrain drivetrain = new Drivetrain();
  public static final Shooter shooter = new Shooter();
  public static final Collector collector = new Collector();
  public static final Collect collectCommand = new Collect(collector, shooter);
  public static final StartAutonomous auto = new StartAutonomous(shooter, drivetrain, collector);
  public static final Relay led = new Relay(ID_LED);

  //This is proactive - I'm not sure we'll end up NEEDING this, but I'm guessing it will be nescessary
  public static final double DRIVE_SENSITIVITY_MULT = 1;

  ADXRS450_Gyro gyro = new ADXRS450_Gyro();
  double kP = 0.05;
  int iter = 0;

  

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    

    //initiate subsystems 
    drivetrain.init();
    shooter.init();
    collector.init();
    


  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
    collectCommand.setState(0);
    drivetrain.zeroEncoders();
    System.out.println("Init");
    auto.init();
    //repeat = -10;
    gyro.reset();
    gyro.calibrate();
    


    // collectCommand.init();

  }
  
  

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    // auto.autoRun();
    // shooter.shooterPeriodic();

    if(gyro.getAngle() < 90) {
      drivetrain.turn(0.05, -0.05);

    }
    
    // Ahmad stuff start
    // Find the heading error; setpoint is 90
    // if(iter == 0) {
    //   double error = 90 - gyro.getAngle();
    //   // Turns the robot to face the desired direction
    //   SmartDashboard.putNumber("angle", error);
    //   drivetrain.turn(0.05 * error, -0.05 * error);
    //   SmartDashboard.putString("Turned?", "Yes");
    //   iter++;
    // }
    
    // Ahmad stuff end
    
  }

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {
    collectCommand.setState(1);
    collectCommand.init();
    led.setDirection(Relay.Direction.kForward);
    SmartDashboard.putNumber("Speed", 0);

  }

  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {
    // collector.runPneumatics();
    

    collectCommand.collect(true);

    if(collector.isBallLoaded()) {
        led.set(Relay.Value.kOn);
    } else {
      led.set(Relay.Value.kOff);
    }

    drivetrain.arcadeDrive();

    // shooter.shooterControl();
    SmartDashboard.putBoolean("Ball Loaded?", collector.isBallLoaded());
    shooter.shooterPeriodic();
    //IO.putNumberToSmartDashboard("Lidar Distance", IO.getLidarDistance());
    //IO.putNumberToSmartDashboard("Vision Distance", IO.getVisionDistance());

    

  }

  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}


}
