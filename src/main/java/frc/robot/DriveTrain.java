package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {

    double ty, tx, tv, ta, steeringAdjust, forwardAdjust;
    final double kP = 0.03;
    final double speed = 0.3;

    WPI_TalonSRX backLeft;
    WPI_TalonSRX backRight;
    WPI_TalonSRX frontLeft;
    WPI_TalonSRX frontRight;

    Encoder encoder;

    MecanumDrive mDrive;

    NetworkTable limeTable;

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
        ta = limeTable.getEntry("ta").getDouble(0);
        SmartDashboard.putNumber("Area", ta);

        if(tv == 1) {
            if (xIsAcceptable(tx)) {
                steeringAdjust = 0.0;
            }
            else {
                steeringAdjust = kP * tx;
            }
        }
        else {
            mDrive.driveCartesian(0, 0, speed);
            return;
        }

        if(steeringAdjust > speed) {
            steeringAdjust = speed;
        }
        else if(steeringAdjust < -speed) {
            steeringAdjust = -speed;
        }

        System.out.println(steeringAdjust);
        SmartDashboard.putNumber("Steering Adjust", steeringAdjust);
        
        if (ta < 0.5 && tv == 1){
            mDrive.driveCartesian(0, 0.3, steeringAdjust);
        } else {
            mDrive.driveCartesian(0, 0, steeringAdjust);
        }
    }

    private boolean xIsAcceptable(double value) {
        return (value > -8) && (value < 8);
    }
}