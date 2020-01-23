package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Shooter {
  WPI_TalonSRX shooterMotor0;
  WPI_TalonSRX shooterMotor1;

  public Shooter(WPI_TalonSRX talon0, WPI_TalonSRX talon1) {
    shooterMotor0 = talon0;
    shooterMotor1 = talon1;
  }

  public void setEnable(boolean enable) {    
    if (enable == true) {
        shooterMotor0.set(RobotConstants.shooterSpeed);
        shooterMotor1.set(RobotConstants.shooterSpeed);
    } else {
        shooterMotor0.set(0);
        shooterMotor1.set(0);
    }
    
  }
}