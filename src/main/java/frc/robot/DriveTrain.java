package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {

    double ty, tx, tv, ta, steeringAdjust, forwardAdjust, integral, priorI = 0, deriv=0, priorE=0;
    final double kP = 0.0175;
    final double kF = 0.1;
    final double kI = 0.005;
    final double kD = 0.3;
    final double speed = 0.5;

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
        mDrive.driveCartesian(0.4 * j.getX(), -0.4 * j.getY(), 0.4 * j.getZ());

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
        ty = limeTable.getEntry("ty").getDouble(0);
        ta = limeTable.getEntry("ta").getDouble(0);
        SmartDashboard.putNumber("Area", ta);

        if(tv == 1) {
            if(xIsAcceptable(tx)) {
                mDrive.driveCartesian(0, 0, 0);
                return;
            }
            else {
                integral = (priorI + (tx*0.027));
                deriv = (tx - priorE) / 2.7;
                steeringAdjust = (kP * tx) + (kI * integral) + (kD * deriv);
                priorE = tx;
                priorI = integral;
            }
        }
        else {
            mDrive.driveCartesian(0, 0, speed);
            return;
        }

        if(steeringAdjust > 0) {
            steeringAdjust += kF;
        }
        else {
            steeringAdjust -= kF;
        }

        if(steeringAdjust > speed) {
            steeringAdjust = speed;
        }
        else if(steeringAdjust < -speed) {
            steeringAdjust = -speed;
        }

        System.out.println(steeringAdjust);
        SmartDashboard.putNumber("Steering Adjust", steeringAdjust);
        
        if (!yIsAcceptable(ty) && tv == 1){
            if (ty > -3)
                mDrive.driveCartesian(0, -0.5, steeringAdjust);
            else if (ty < 3)
                mDrive.driveCartesian(0, 0.5, steeringAdjust);
        } else if (yIsAcceptable(ty) && xIsAcceptable(tx)){
            fullStop();
        } else {
            mDrive.driveCartesian(0, 0, steeringAdjust);
        } 
    }

    private boolean xIsAcceptable(double value) {
        return (value > -1) && (value < 1);
    }

    private boolean yIsAcceptable(double value) {
        return (value > -3) && (value < 3);
    }

    public void resetErrors() {
        priorI = 0;
        priorE = 0;
    }
}