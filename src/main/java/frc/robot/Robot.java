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
    public static final Collect collectCommand = new Collect(collector, shooter, arduino);
    public static final StartAutonomous auto = new StartAutonomous(shooter, drivetrain, collector, arduino);
    public static final Relay led = new Relay(ID_LED);
    public static final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    public static final Drive driveCommand = new Drive(gyro, drivetrain);
    boolean isComplete;
    int rotateState;
    boolean doneDriving;
    boolean driven;


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
        collectCommand.setState(0);
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
        SmartDashboard.putNumber("Gyro ", gyro.getAngle());
        // Gyro occastionally fails to return values, causing an infinite spin. I'm not sure why.
        driveCommand.turnToAngle(90, 0.033); //I've only tested this version without an integral value
        driveCommand.turnToAngle(90, 0.033, 0.2, iter);
        iter++;
    }

    /**
     * This function is called once each time the robot enters teleoperated mode.
     */
    @Override
    public void teleopInit() {
        arduino.setLEDColor(0, LED_COLOR_RED);
        collectCommand.setState(1);
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

        SmartDashboard.putNumber("POV",IO.getPOV(false));

        double limelightMountAngleDegrees = -7;
        double limelightLensHeightInches = 18.5;
        double goalHeightInches = 33.5;

        driveCommand.printAngle();

        double distanceToGoalInches = IO.calculateDistance(limelightMountAngleDegrees, limelightLensHeightInches, goalHeightInches);
        SmartDashboard.putNumber("Goal Dist", distanceToGoalInches);
        SmartDashboard.putNumber("Goal Angle", IO.getLimelightYAngle());
        //System.out.println("Distance to Goal: " + distanceToGoalInches);

        collectCommand.collect(true);

        drivetrain.arcadeDrive();

        double targetOffsetAngleHorizontal = IO.getLimelightXAngle();
        SmartDashboard.putNumber("Target Offset", targetOffsetAngleHorizontal);
        //System.out.println(targetOffsetAngleHorizontal);
        SmartDashboard.putNumber("Drive Distance", drivetrain.getAverageDriveDistanceInches());

        //This is the code that should correct for distance and angle when the driver pushes "A"
        switch(rotateState) {
          case STATE_DRIVEROP:
            if(IO.aButtonIsPressed(true)) {
              gyro.reset();
              rotateState = STATE_CORRECT_DISTANCE;
            }
            break;
          case STATE_CORRECT_DISTANCE:
            if(!driven) {
              driven = drivetrain.autoDistDrive(-(distanceToGoalInches - 73), 0.3); //73 is the distance to goal in inches that shoots in low power mode to into the goal on the stools
            } else {
              rotateState = STATE_CORRECT_ANGLE;

            }
            break;
          case STATE_CORRECT_ANGLE:
            boolean isTurned = driveCommand.turnToAngle(-targetOffsetAngleHorizontal, 0.02); //Turn to angle has a +/- 10 degree window where it will stop
            System.out.println("Target Offset: " + targetOffsetAngleHorizontal);
            if(isTurned) {
              if(iter >= 50) {
                rotateState = STATE_RESET;
                iter = 0;
              } else {
                iter ++;
              }
            }
            break;
          case STATE_RESET:
            // gyro.reset();
            driveCommand.cancelTurn();
            rotateState = 0;
            break;
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
        teleopInit();  // ????? SHould this be autoInit(); ?
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {
        if(iter < 50 * 15) {
            SmartDashboard.putString("Match", "Autonomous");
            autonomousPeriodic();
        } else if(iter == 50 * 15) {
            teleopInit();
            SmartDashboard.putString("Match", "TeleOp");
        } else if (iter > 50 * 15 && iter < 50 * 135) {
            teleopPeriodic();
            SmartDashboard.putString("Match", "TeleOp");
        } else {
            SmartDashboard.putString("Match", "Over");
            drivetrain.stopMotors();
        }
    }
}
