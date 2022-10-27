package frc.robot.autonomous;


//import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj2.command.Command;
//import edu.wpi.first.wpilibj2.command.CommandBase;
//import edu.wpi.first.wpilibj2.command.WaitCommand;
import static frc.robot.RobotMap.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Arduino;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import frc.robot.autonomous.*;

public class StartAutonomous {
    Shooter shooter;
    Drivetrain drivetrain;
    Collector collector;
    Arduino arduino;
    Collect collect;
        int counterTimer = 0;
        int state = 0;

    
    /** Creates a new AutoShootBuilder. */
    public StartAutonomous(Shooter shooter, Drivetrain drivetrain, Collector collector, Arduino arduino) {
      this.shooter = shooter;
      this.drivetrain = drivetrain;
      this.collector = collector;
      this.arduino = arduino;
        collect = new Collect(collector, shooter, arduino);
    }
  
    private void find(){
        //code to run when in finding balls mode
    }

    private void pickUp() {
        //code to run when in pick up mode

    }

    /*
    public void multiBallAuto() {
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
      //       //driveCommand.turnToAngle(45, 0.033); //I've only tested this version without an integral value
      //       //driveCommand.turnToAngle(45, 0.033, 0.2, iter);
      //       //iter ++;
      //       //autoCase = 1;
      //       //if(iter >= 50) {
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
    */

    public void init() {
        collect.setCollectorState(0);
        collect.collect(true);
        drivetrain.zeroEncoders();
        state = 0;
        counterTimer = 0;
    }


    public void autoRun(){
        SmartDashboard.putNumber("Autonomous State", state);

        switch(state) {
            case ST_AUTO_START:
                drivetrain.zeroEncoders();
                collect.setCollectorState(3);
                System.out.println("In Case 0");
                collect.setCollectorState(STATE_SECURE_BALL);
                state = ST_AUTO_DRIVE_BACK1;
                break;
            case ST_AUTO_DRIVE_BACK1: //drive back
                if(counterTimer < 70) {
                        drivetrain.driveAuto(0.55);
                        counterTimer++;
                        System.out.println("driving back...");
                        break;
                    } 
                    /*
                    // SmartDashboard.putBoolean("is dist acheived?", (drivetrain.getAverageDriveDistanceFeet() < 6));
                    // System.out.println("is dist acheived? " + (drivetrain.getAverageDriveDistanceFeet() < 6));
                    // System.out.println("dist " + drivetrain.getAverageDriveDistanceFeet());
                    // SmartDashboard.putNumber("dist", drivetrain.getAverageDriveDistanceFeet());
                    // System.out.println("Rotations: " + drivetrain.getRotations());

                    // if(drivetrain.getAverageDriveDistanceFeet() < 7) {
                    //     drivetrain.driveAuto(0.6);
                    //     System.out.println("In Case 1");
                    //     break;
                    //} */

                else {
                    shooter.setShotPower(SHOOT_AUTO);
                    SmartDashboard.putNumber("Shooter Power", 1);
                    state=ST_AUTO_SHOOT1;
                    break;
                }
            case ST_AUTO_SHOOT1: //shoot
                drivetrain.driveAuto(0);
                drivetrain.zeroEncoders();  
                collect.setCollectorState(STATE_PREPARE_TO_SHOOT);
                state = 3;
                break;
            case 3: // done
                drivetrain.driveAuto(0);
                break;
        }
        collect.collect(true);
        shooter.shooterPeriodic();
    }

    public void newAutoRun() {
        SmartDashboard.putNumber("Autonomous State", state);

        switch(state) {
            case ST_AUTO_START:
                drivetrain.zeroEncoders();
                collect.setCollectorState(3);
                System.out.println("In Case 0");
                collect.setCollectorState(STATE_SECURE_BALL);
                shooter.setShotPower(0.5);
                state = ST_AUTO_SHOOT1;
                break;
            case ST_AUTO_SHOOT1:
                collect.setCollectorState(STATE_PREPARE_TO_SHOOT);
                counterTimer++;
                if(counterTimer > 65) {
                    counterTimer = 0;
                    state = ST_AUTO_DRIVE_BACK1;
                }
                break;
            case ST_AUTO_DRIVE_BACK1: //drive back
                if(counterTimer < 70) {
                        drivetrain.driveAuto(0.55);
                        collect.setCollectorState(STATE_COLLECTING);
                        counterTimer++;   
                        System.out.println("driving back...");
                        break;
                    } 

                else {
                    shooter.setShotPower(SHOOT_AUTO);
                    // SmartDashboard.putNumber("Shooter Power", 1);
                    if(collect.getCollectorState() == STATE_SECURE_BALL){
                        state = ST_AUTO_SHOOT2;
                    } else {
                        state = ST_AUTO_DONE;
                    }
                    break;
                }
            case ST_AUTO_SHOOT2: //shoot
                drivetrain.driveAuto(0);
                drivetrain.zeroEncoders();  
                collect.setCollectorState(STATE_PREPARE_TO_SHOOT);
                state = 3;
                break;
            case ST_AUTO_DONE: // done
                drivetrain.driveAuto(0);
                break;
        }
        collect.collect(true);
        shooter.shooterPeriodic();
    }


/*
    public void autoRun(){
        SmartDashboard.putNumber("Autonomous State", state);

        switch(state) {
            case ST_AUTO_START:
                drivetrain.zeroEncoders();
                collect.setCollectorState(3);
                System.out.println("In Case 0");
                state = ST_AUTO_DRIVE_BACK1;
                break;

            case ST_AUTO_DRIVE_BACK1: //drive back
                if(counterTimer < 30) {
                        drivetrain.driveAuto(0.5);
                        counterTimer++;
                        System.out.println("herere");
                        break;
                    } 
                    // SmartDashboard.putBoolean("is dist acheived?", (drivetrain.getAverageDriveDistanceFeet() < 6));
                    // System.out.println("is dist acheived? " + (drivetrain.getAverageDriveDistanceFeet() < 6));
                    // System.out.println("dist " + drivetrain.getAverageDriveDistanceFeet());
                    // SmartDashboard.putNumber("dist", drivetrain.getAverageDriveDistanceFeet());
                    // System.out.println("Rotations: " + drivetrain.getRotations());

                    // if(drivetrain.getAverageDriveDistanceFeet() < 7) {
                    //     drivetrain.driveAuto(0.6);
                    //     System.out.println("In Case 1");
                    //     break;
                    //}

                else {
                    drivetrain.driveAuto(0);
                    counterTimer = 0;
                    state=ST_AUTO_SHOOT1;
                    break;
                }

            case ST_AUTO_SHOOT1: //shoot
                if (counterTimer == 0) {
                    collect.setCollectorState(4);
                    counterTimer++;
                    break;
                }
                else if(counterTimer < 100){
                    counterTimer++;
                    break;
                }
                else {
                    state = ST_AUTO_DRIVE_BACK2;
                    counterTimer = 0;
                    break;
                }
                
            case ST_AUTO_DRIVE_BACK2: //drive back a little more
                if(counterTimer == 0) {
                    collect.setCollectorState(2);
                }
                else if(counterTimer < 25) {
                    drivetrain.driveAuto(0.5);
                    //System.out.println("In Case 3");
                    counterTimer++;
                } else {
                    drivetrain.driveAuto(0);
                    counterTimer = 0;
                    if(collector.isBallLoaded()){
                        state = ST_AUTO_PAUSE;
                    } else {
                        collect.setCollectorState(4);
                        state = ST_AUTO_DONE;
                    }
                }
                break;

            case ST_AUTO_PAUSE:
                if(counterTimer < 100){
                    counterTimer++;
                }
                else {
                    counterTimer = 0;
                    state = ST_AUTO_DRIVE_FORWARD;
                }
                break;
                
            case ST_AUTO_DRIVE_FORWARD:
                if(counterTimer < 25) {
                    drivetrain.driveAuto(-0.5);
                    counterTimer++;
                    System.out.println("herere");
                } else {
                    drivetrain.driveAuto(0);
                    drivetrain.zeroEncoders();
                    counterTimer = 0; 
                    state=ST_AUTO_SHOOT2;
                }
                break;
       
            case ST_AUTO_SHOOT2:
                if (counterTimer == 0) {
                    drivetrain.driveAuto(0);
                    collect.setCollectorState(4);
                    counterTimer++;   
                }
                if(counterTimer < 50) {
                    counterTimer++;
                } else {
                    counterTimer = 0;
                    state = ST_AUTO_DONE;
                }
                break;

            case ST_AUTO_DONE:
                drivetrain.driveAuto(0);
                collect.setCollectorState(4);
                break;
             
        }

        collect.collect(true);
        shooter.shooterPeriodic();
    }
*/
    
    private void shoot() {
        //code to run when in shoot mode
    }

    public void run() {
        find();
        pickUp();
        shoot();
        //code to run periodically in autonomous
    }
}
