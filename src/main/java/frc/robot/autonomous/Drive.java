package frc.robot.autonomous;

import frc.robot.subsystems.Drivetrain;

public class Drive{
    Drivetrain drivetrain;

    public Drive(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    public boolean turnToAngle(double angle, double P, double I) {
        return true;
    }

    public void cancelTurn() {
        
    }

    public void goForwardPID(double desiredDist, double P, double I){

    }
    
}