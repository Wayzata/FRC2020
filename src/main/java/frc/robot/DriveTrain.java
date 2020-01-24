package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class DriveTrain {

    WPI_TalonSRX backLeft;
    WPI_TalonSRX backRight;
    WPI_TalonSRX frontLeft;
    WPI_TalonSRX frontRight;

    Encoder encoder;

    MecanumDrive mDrive;

    public DriveTrain() {
        // frontLeft = new WPI_TalonSRX(0);
        // frontRight = new TalonSRX(1);
        // backRight = new TalonSRX(2);
        // backLeft = new TalonSRX(3);
        frontLeft = new WPI_TalonSRX(1);
        frontRight = new WPI_TalonSRX(0);
        backRight = new WPI_TalonSRX(3);
        backLeft = new WPI_TalonSRX(2);

        // frontLeft.setInverted(true);
        // backRight.setInverted(true);
       
        mDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
        mDrive.setSafetyEnabled(false);
        
    }

    public void mecDrive(Joystick j) {
        mDrive.driveCartesian(0.5 * j.getX(), -0.5 * j.getY(), 0.5 * j.getZ());

    }

    public void printThing() {
        System.out.println(frontLeft.getSelectedSensorPosition());
    }

    public void resetThing() {
        frontLeft.setSelectedSensorPosition(0);
    }

}