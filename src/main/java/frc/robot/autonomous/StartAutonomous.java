package frc.robot.autonomous;

import static frc.robot.RobotMap.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Arduino;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StartAutonomous {
    Shooter shooter;
    Drivetrain drivetrain;
    Collector collector;
    Collect collect;

    
    /** Creates a new AutoShootBuilder. */
    public StartAutonomous(Shooter shooter, Drivetrain drivetrain, Collector collector) {
        this.shooter = shooter;
        this.drivetrain = drivetrain;
        this.collector = collector;
        collect = new Collect(collector, shooter);
    }

    public void init() {
        collect.setCollectorState(0);
        collect.collect(true);
        drivetrain.zeroEncoders();
    }
}