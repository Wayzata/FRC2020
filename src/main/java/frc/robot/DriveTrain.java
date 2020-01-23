package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class DriveTrain {

    WPI_TalonSRX leftRear;
    WPI_TalonSRX rightRear;
    WPI_TalonSRX leftFront;
    WPI_TalonSRX rightFront;

    Encoder encoder;

    MecanumDrive mDrive;

    public DriveTrain(WPI_TalonSRX _leftFront, WPI_TalonSRX _rightFront, WPI_TalonSRX _leftRear, WPI_TalonSRX _rightRear) {
        leftFront = _leftFront;
        rightFront = _rightFront;
        leftRear = _leftRear;
        rightRear = _rightRear;

        mDrive = new MecanumDrive(leftFront, leftRear, rightFront, rightRear);       
    }

    public void drive(double x, double y, double z) {
        mDrive.driveCartesian(x, y, z);

    }

    public void printThing() {
        System.out.println(leftFront.getSelectedSensorPosition());
    }

    public void resetThing() {
        leftFront.setSelectedSensorPosition(0);
    }

}