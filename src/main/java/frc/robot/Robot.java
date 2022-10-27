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

import org.ejml.dense.row.linsol.AdjustableLinearSolver_FDRM;

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
    public static final Climb climbCommand = new Climb(lifter, drivetrain);
    public static final Relay led = new Relay(ID_LED);
    public static final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    public static final Drive driveCommand = new Drive(gyro, drivetrain);

    public static final Collect collectCommand = new Collect(collector, shooter, arduino, drivetrain, gyro, driveCommand);
    

    int Timer = 0;
    boolean isComplete;
    int rotateState = 0;
    boolean doneDriving;
    boolean driven;
    boolean lhc; //for testing

    boolean drive = false;
    double kP = 0.05;
    int autoCase = 0;
    int nextAutoCase = 0;
    double integral = 0;
    double previousError;
    int iter = 0;
    int rumbleTimer = 0;
    boolean shootingAuto;
    boolean isTurnComplete;
    boolean isShooting;

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
        
        String testString = "Init2";
        System.out.print("Init2 nessage - ");
        System.out.println(arduino.writeArduino(testString));
    }

    /** This function is run once each time the robot enters autonomous mode. */
    @Override
    public void autonomousInit() {
        collectCommand.setCollectorState(0);
        drivetrain.zeroEncoders();
        auto.init();
        collectCommand.correctDistance();
        isComplete = false;
        iter = 0;
        integral = 0;
        doneDriving = false;
        nextAutoCase = 0;

        /*
        // gyro.calibrate();
        // gyro.reset();
        // collectCommand.init();
        */

    }
    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
      auto.autoRun();
      // auto.newAutoRun();
    }

    /**
     * This function is called once each time the robot enters teleoperated mode.
     */
    @Override
    public void teleopInit() {
      /*
        //String testString = "Teleop";
        //System.out.print("Telop nessage - ");
        //System.out.println(arduino.writeArduino(testString));
        //arduino.setLEDColor(0, LED_COLOR_RED);
        //SmartDashboard.putNumber("Speed", 0);
        //SmartDashboard.putNumber("Alliance", IO.getAllianceColor());
        
        collectCommand.init();*/

        

        collectCommand.setCollectorState(1);
        led.setDirection(Relay.Direction.kForward);
        gyro.reset();
        drivetrain.zeroEncoders();
        isComplete = false;
        collectCommand.setAutoShootConstraints(125, 5);
        collectCommand.setCorrectState(STATE_DRIVEROP);
        iter = 0;
        driven = false;
        lhc = false;

        rotateState = STATE_DRIVEROP;


        shootingAuto = IO.isHIDButtonPressed(HID_SHOOT_AUTO_MODE, false);
        
        isShooting = false;
    }


    public void correctDist() {
      double distanceToGoalInches = IO.calculateDistance(LIMELIGHT_MOUNT_ANGLE, LIMELIGHT_MOUNT_HEIGHT, HIGH_GOAL_DISTANCE);
        SmartDashboard.putNumber("Goal Dist", distanceToGoalInches);
        SmartDashboard.putNumber("Goal Angle", IO.getLimelightYAngle());
        //System.out.println("Distance to Goal: " + distanceToGoalInches);
        SmartDashboard.putNumber("Rotate State", rotateState);
        int maxDistance = 170;
        int desiredDistance = 150;
        int minDistance = 130;
        SmartDashboard.putNumber("Max", minDistance);
        SmartDashboard.putNumber("Min", maxDistance);
        
        
        double targetOffsetAngleHorizontal = IO.getLimelightXAngle();
        SmartDashboard.putNumber("Target Offset", targetOffsetAngleHorizontal);

        switch(rotateState) {
          case STATE_DRIVEROP:
            isShooting = false;

            //reset stuff 
            break;
          case STATE_PREPARE:
            gyro.reset();
            drivetrain.zeroEncoders();
            rotateState = STATE_CORRECT_DISTANCE;
            break;
          case STATE_CORRECT_DISTANCE: 
            if(distanceToGoalInches > minDistance && distanceToGoalInches < maxDistance) {
              drivetrain.driveAuto(0);
              rotateState = STATE_CORRECT_ANGLE;
            } else {
              drivetrain.driveAuto(0.015 * (desiredDistance - distanceToGoalInches));
            }
            /*else if(distanceToGoalInches > maxDistance) { 
              drivetrain.driveAuto(-0.7); 
            } else if (distanceToGoalInches < minDistance) {
              drivetrain.driveAuto(0.7);
            } */

            
            SmartDashboard.putNumber("Desired", desiredDistance);
            SmartDashboard.putNumber("TO Goal", distanceToGoalInches);

            /*drivetrain.driveAuto(5, (desiredDistance - distanceToGoalInches), 0);
            if(distanceToGoalInches > minDistance && distanceToGoalInches < maxDistance) {
                rotateState = STATE_CORRECT_ANGLE;
            }*/
            break;
          case STATE_CORRECT_ANGLE:
            // driveCommand.turnToAngle(driveCommand.getRotationalPower(0.043, -targetOffsetAngleHorizontal)); //Turn to angle has a +/- 10 degree window where it will stop
            double rotateRCW = 0.001 * -targetOffsetAngleHorizontal;
            SmartDashboard.putNumber("RCW", rotateRCW);
            if(0 >= (targetOffsetAngleHorizontal - 2) && 0 <= (targetOffsetAngleHorizontal + 2)) {
                driveCommand.turnToAngle(rotateRCW);
                iter++;
            } else {
                rotateState = STATE_RESET;
                iter = 0;
            }
            break;
          case STATE_RESET:
            drivetrain.driveAuto(0);
            driveCommand.cancelTurn();
            rotateState = STATE_DRIVEROP;
            break;
          case STATE_CORRECT_DONE:
            break;
        }

    }


    /** This function is called periodically during teleoperated mode. */
    @Override
    public void teleopPeriodic() {
        driveCommand.printAngle();
        double distanceToGoalInches = IO.calculateDistance(LIMELIGHT_MOUNT_ANGLE, LIMELIGHT_MOUNT_HEIGHT, HIGH_GOAL_DISTANCE);
        SmartDashboard.putNumber("Goal Dist", distanceToGoalInches);

        if (IO.getHIDAxis(0) >= 1) {
          collector.rotateArms(.4);
        } else if (IO.getHIDAxis(0) <= -1) {
          collector.rotateArms(-.4);
        } else {
          collector.rotateArms(0);
        }

        // collectCommand.correctDistance();

        // if(IO.aButtonIsPressed(true)){
        //   collectCommand.setCorrectState(STATE_PREPARE);
        // }

        // if(IO.yButtonIsPressed(true)){
        //   collectCommand.setCorrectState(STATE_DRIVEROP);
        // }

        // // if(collectCommand.getCorrectState() == STATE_CORRECT_DONE) {
        // //   collectCommand.setCollectorState(STATE_PREPARE_TO_SHOOT);
        // // }

        collectCommand.collect(true);

        correctDist();

        if(IO.bButtonIsPressed(true)) {
          isShooting = true;
          rotateState = STATE_PREPARE;
        }

        // if(IO.isHIDButtonPressed(HID_SHOOT_AUTO_MODE, false)) {
        //   shootingAuto = !shootingAuto;
        //   collectCommand.setAutoShoot(shootingAuto);
        // }

        // if(collectCommand.getCollectorState() == STATE_NOT_COLLECTING || collectCommand.getCollectorState() == STATE_COLLECTING ||
        //    IO.isHIDButtonPressed(HID_COLLECTOR_OFF, false)  || collectCommand.getCorrectState() < STATE_CORRECT_DISTANCE 
        //    || collectCommand.getCorrectState() > STATE_CORRECT_ANGLE || climbCommand.getState() <= ST_LIFTER_UNKNOWN) {
        //   isShooting = false;
        // }
        
        // if(collectCommand.getCollectorState() > STATE_PREPARE_TO_SHOOT) {
        //   isShooting = true;
        //   rumbleTimer = 0;

        // }

        if(!isShooting) {
          if(rumbleTimer < 20){
            IO.setRumble(true, 1);
            rumbleTimer++;
          } else {
            IO.setRumble(true, 0);
          }
          drivetrain.arcadeDrive();
        }

        //This is the code that should correct for distance and angle
        
        // if (IO.xButtonIsPressed(true)) {
        //   collectCommand.setCollectorState(STATE_RESET);
        // }
        
        // SmartDashboard.putBoolean("Loaded?", collector.isBallLoaded());
        // shooter.shooterPeriodic();

        
        // if (collector.isBallLoaded()) {
        //   led.set(Relay.Value.kOn);
        // } else {
        //   led.set(Relay.Value.kOff);
        // }

        

        /*
        // TEMP
        // SmartDashboard.putBoolean("Arms Homed", lifter.isArmsHome());
        //SmartDashboard.putNumber("HID Ax 0", IO.getHIDAxis(0));
        //SmartDashboard.putNumber("HID Ax 1", IO.getHIDAxis(1));
        // climbCommand.climb(false);

        lifter.show();
        // Manual Lifter
        if (IO.getHIDAxis(0) >= 1) {
          lifter.moveLifter(.4);
        } else if (IO.getHIDAxis(0) <= -1) {
          lifter.moveLifter(-.4);
        } else {
          if (lifter.isManual()) {
            lifter.stopLifter();
          }
        }
        // Manual Arms
        if (IO.getHIDAxis(1) >= 1) {
          lifter.spin(.7);
        } else if (IO.getHIDAxis(1) <= -1) {
          lifter.spin(-.7);
        } else {
          if (lifter.isManual()) {
            lifter.stopSpin();
          }
        }
        //SmartDashboard.putBoolean("lifter hook state", lhc);


        // if(IO.aButtonIsPressed(true)) {
        //   //lhc = !lhc;
        //   lifter.setLifterHookState(true);
        // }

        if(IO.bButtonIsPressed(true)) {
          lifter.setLifterHookState(false);
        }
        */
        // lifter.rollLifter(IO.getHIDAxis(1));

    }


    /** This function is called once each time the robot enters test mode. */
    @Override
    public void testInit() {
        iter = 0;
        autonomousInit();
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {
      
    }
}
