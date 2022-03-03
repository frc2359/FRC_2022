package frc.robot.autonomous;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import static frc.robot.RobotMap.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;

public class StartAutonomous {
    Shooter shooter;
    Drivetrain drivetrain;
    
    /** Creates a new AutoShootBuilder. */
    public StartAutonomous(Shooter shooter, Drivetrain drivetrain) {
      this.shooter = shooter;
      this.drivetrain = drivetrain;

    }
  
    private void find(){
        //code to run when in finding balls mode
    }

    private void pickUp() {
        //code to run when in pick up mode

    }

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
