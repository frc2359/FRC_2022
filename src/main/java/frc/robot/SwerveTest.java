package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.IO;

//We need to make this more cool. This is how: https://github.com/SwerveDriveSpecialties/swerve-template

//another really cool link to look at https://github.com/SwerveDriveSpecialties/swerve-lib

public class SwerveTest {
    WPI_TalonFX speed = new WPI_TalonFX(0);
    WPI_TalonFX rotate = new WPI_TalonFX(1);

    public void drive() {
        double rot = IO.getLeftXAxis(true);
        double sp = IO.getLeftYAxis(true);
        speed.set(sp);
        rotate.set(rot);
    }
}
