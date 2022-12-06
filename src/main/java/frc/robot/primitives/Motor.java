package frc.robot.primitives;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class Motor {
    public MotorController m;

    public void Motor(char type, int port) {
        switch(type) {
            case 't':   m = new WPI_TalonFX(port);
            case 's':   m = new CANSparkMax(port, MotorType.kBrushless);
        }
    }

    
}
