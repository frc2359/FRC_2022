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
    int rotateState;
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
    boolean shootingAuto;
    boolean isTurnComplete;

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
        System.out.println("Init");
        auto.init();
        gyro.reset();
        collectCommand.correctDistance();
        // gyro.calibrate();
        isComplete = false;


        iter = 0; // Added by Mr. R. otherwise you can't run auto more than once in a row...
        integral = 0;
        doneDriving = false;
        nextAutoCase = 0;

        // collectCommand.init();

    }
    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
      auto.autoRun();
      // switch (autoCase){
      //   case ST_AUTO_START:
      //     drivetrain.zeroEncoders();
      //     collectCommand.setCollectorState(STATE_SECURE_BALL);
      //     System.out.println("In Case 0");
      //     autoCase = ST_AUTO_DRIVE_BACK1;
      //     break;

      //   case AUTO_LEAVE_TARMAC:
      //     collectCommand.setCorrectState(STATE_PREPARE);
      //     if(collectCommand.getCorrectState() == STATE_CORRECT_DONE) {
      //       collectCommand.setCorrectState(STATE_DRIVEROP);
      //       shooter.setShotPower(0.6);
      //       SmartDashboard.putNumber("Shooter Power", 1);
      //       autoCase = AUTO_SHOOT_1;
      //     }
      //     break;

      //   case AUTO_SHOOT_1:
      //     collectCommand.setCollectorState(STATE_PREPARE_TO_SHOOT);
      //     break;  

      //   case AUTO_TURN:
      //     SmartDashboard.putNumber("Gyro ", gyro.getAngle());
      //     boolean isTurnComplete = driveCommand.turnToAngle(130, 0.043);
      //     if (isTurnComplete) {
      //       // Gyro occastionally fails to return values, causing an infinite spin. I'm not sure why.
      //       /*driveCommand.turnToAngle(45, 0.033); //I've only tested this version without an integral value
      //       driveCommand.turnToAngle(45, 0.033, 0.2, iter);
      //       iter ++;
      //       autoCase = 1;*/
      //       if(iter >= 50) {
      //         drivetrain.zeroEncoders();
      //         nextAutoCase = AUTO_DRIVE_BACK;
      //         autoCase = AUTO_CANCEL_TURN;
      //         iter = 0;
      //       } else {
      //         iter ++;
      //       }
      //     }
      //     break;
        
      //   case AUTO_DRIVE_BACK:
      //     if (drivetrain.getAverageDriveDistanceInches() < 36) {
      //       drivetrain.driveAuto(0.5);
      //       collectCommand.setCollectorState(STATE_COLLECTING);
      //     } else {
      //       gyro.reset();
      //       autoCase = AUTO_TURN_BACK;
      //     }
      //     break;
        
      //   case AUTO_TURN_BACK:
      //     isTurnComplete = driveCommand.turnToAngle(100, 0.043);
      //     if(iter >= 50) {
      //       drivetrain.zeroEncoders();
      //       nextAutoCase = AUTO_LIMELIGHT_DRIVE;
      //       autoCase = AUTO_CANCEL_TURN;
      //       iter = 0;
      //     } else {
      //       iter ++;
      //     }
      //   case AUTO_LIMELIGHT_DRIVE:
      //     collectCommand.correctDistance();
      //     collectCommand.setCorrectState(STATE_PREPARE);
      //     break;

      //   case AUTO_CANCEL_TURN:
      //     drivetrain.driveAuto(0);
      //     driveCommand.cancelTurn();
      //     autoCase = nextAutoCase;
      //     nextAutoCase = 0;
      //     break;
        
          
      // }

    }

    /**
     * This function is called once each time the robot enters teleoperated mode.
     */
    @Override
    public void teleopInit() {
        //String testString = "Teleop";
        //System.out.print("Telop nessage - ");
        //System.out.println(arduino.writeArduino(testString));
        //arduino.setLEDColor(0, LED_COLOR_RED);
        collectCommand.init();
        collectCommand.setCollectorState(1);
        led.setDirection(Relay.Direction.kForward);
        //SmartDashboard.putNumber("Speed", 0);
        drivetrain.zeroEncoders();
        //SmartDashboard.putNumber("Alliance", IO.getAllianceColor());
        isComplete = false;
        collectCommand.setAutoShootConstraints(125, 5);
        rotateState = 0;
        collectCommand.setCorrectState(STATE_DRIVEROP);
        iter = 0;
        gyro.reset();
        driven = false;
        lhc = false;

        shootingAuto = IO.isHIDButtonPressed(HID_SHOOT_AUTO_MODE, false);


        //FOR TESTING --------------------------------
        // collectCommand.setAutoShootConstraints(73, 2);
    }

    /** This function is called periodically during teleoperated mode. */
    @Override
    public void teleopPeriodic() {
        // drivetrain.setAutoDrive(true);
        // drivetrain.driveAuto(0.25);
        // TEMP
        //SmartDashboard.putNumber("HID Ax 0", IO.getHIDAxis(0));
        //SmartDashboard.putNumber("HID Ax 1", IO.getHIDAxis(1));
        // climbCommand.climb(false);
        
        driveCommand.printAngle();



        // shooter.setShotPower(0.6);
        // shooter.pickBallUp(STATE_SHOOT);

        collectCommand.correctDistance();
        // collectCommand.setCorrectState(STATE_DRIVEROP);

        if(IO.aButtonIsPressed(true)){
          collectCommand.setCorrectState(STATE_PREPARE);
        }

        if(IO.yButtonIsPressed(true)){
          collectCommand.setCorrectState(STATE_DRIVEROP);

        }

        // if(collectCommand.getCorrectState() == STATE_CORRECT_DONE) {
        //   collectCommand.setCollectorState(STATE_PREPARE_TO_SHOOT);
        // }

        collectCommand.collect(true);

        if(collectCommand.getCorrectState() != STATE_CORRECT_DISTANCE || climbCommand.getState() <= ST_LIFTER_UNKNOWN) {
          drivetrain.arcadeDrive();
        }

        if(IO.isHIDButtonPressed(HID_SHOOT_AUTO_MODE, false)) {
          shootingAuto = !shootingAuto;
          collectCommand.setAutoShoot(shootingAuto);
        }

        /*
        if(IO.startButtonIsPressed(true)) {
          drivetrain.setAutoDrive(false);
        }
        
        if(!drivetrain.getAutoDrive()) {
          
        } */

        /*
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
        */
        

        SmartDashboard.putBoolean("Arms Homed", lifter.isArmsHome());


        if (IO.xButtonIsPressed(true)) {
          collectCommand.setCollectorState(STATE_RESET);
        }

        //SmartDashboard.putBoolean("lifter hook state", lhc);


        // if(IO.aButtonIsPressed(true)) {
        //   //lhc = !lhc;
        //   lifter.setLifterHookState(true);
        // }

        if(IO.bButtonIsPressed(true)) {
          lifter.setLifterHookState(false);
        }

        SmartDashboard.putBoolean("Loaded?", collector.isBallLoaded());
        shooter.shooterPeriodic();

        
        if (collector.isBallLoaded()) {
          led.set(Relay.Value.kOn);
        } else {
          led.set(Relay.Value.kOff);
        }
        

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
      
    }
}
