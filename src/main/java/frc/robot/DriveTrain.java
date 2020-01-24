package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class DriveTrain {

    double ty, tx, tv, steeringAdjust;
    final double kP = 0.3;

    WPI_TalonSRX backLeft;
    WPI_TalonSRX backRight;
    WPI_TalonSRX frontLeft;
    WPI_TalonSRX frontRight;

    Encoder encoder;

    MecanumDrive mDrive;

    NetworkTable limeTable;

    public DriveTrain() {
        // frontLeft = new TalonSRX(0);
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

        limeTable = NetworkTableInstance.getDefault().getTable("limelight");
       
    }

    public void mecDrive(Joystick j) {
        mDrive.driveCartesian(0.5 * j.getX(), -0.5 * j.getY(), 0.5 * j.getZ());

    }

    public void fullStop() {
        mDrive.driveCartesian(0, 0, 0);
    }

    public void printThing() {
        System.out.println(frontLeft.getSelectedSensorPosition());
    }

    public void resetThing() {
        frontLeft.setSelectedSensorPosition(0);
    }

    public void oneUpRafael() {
        tv = limeTable.getEntry("tv").getDouble(0);
        tx = limeTable.getEntry("tx").getDouble(0);

        if(xIsAcceptable(tx)) {
            //shoot
        }
        else {
            if(tv == 1) {
                if(tx < 0) {
                    steeringAdjust = -tx * kP;
                }
                else {
                    steeringAdjust = tx * kP;
                }
            }
            else {
                steeringAdjust = 0.3;
            }
        }

        mDrive.driveCartesian(0, 0, steeringAdjust);
    }

    private boolean xIsAcceptable(double value) {
        return (value > -10) && (value < 10);
    }
}