package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class DriveTrain {

    WPI_TalonSRX backLeft;
    WPI_TalonSRX backRight;
    WPI_TalonSRX frontLeft;
    WPI_TalonSRX frontRight;

    MecanumDrive mDrive;

    public DriveTrain() {
        // frontLeft = new TalonSRX(0);
        // frontRight = new TalonSRX(1);
        // backRight = new TalonSRX(2);
        // backLeft = new TalonSRX(3);
        frontLeft = new WPI_TalonSRX(6);
        frontRight = new WPI_TalonSRX(7);
        backRight = new WPI_TalonSRX(9);
        backLeft = new WPI_TalonSRX(8);

        // frontLeft.setInverted(true);
        // backRight.setInverted(true);
       
        mDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
       
        
    }

    public void mecDrive(Joystick j) {
        mDrive.driveCartesian(0.5 * j.getX(), -0.5 * j.getY(), 0.5 * j.getZ());

    }

}