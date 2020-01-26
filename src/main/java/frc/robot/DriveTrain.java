package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {
    private final double kP_Z = 0.0175;
    private final double kF_Z = 0.1;
    private final double kI_Z = 0.005;
    private final double kD_Z = 0.3;
    private final double maxSpeedX = 0.5;
    private final double maxSpeedZ = 0.5;

    private double priorIntegralZ = 0;
    private double priorErrorZ = 0;

    private WPI_TalonSRX backLeft;
    private WPI_TalonSRX backRight;
    private WPI_TalonSRX frontLeft;
    private WPI_TalonSRX frontRight;

    private MecanumDrive mDrive;

    private NetworkTable limeTable;

    public DriveTrain() {
        frontLeft = new WPI_TalonSRX(1);
        frontRight = new WPI_TalonSRX(0);
        backRight = new WPI_TalonSRX(3);
        backLeft = new WPI_TalonSRX(2);
       
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
        double errorZ = 0;
        double integralZ = 0;
        double derivitiveZ = 0;

        double driveX = 0;
        double driveY = 0;
        double driveZ = 0;

        double tv = limeTable.getEntry("tv").getDouble(0);
        double tx = limeTable.getEntry("tx").getDouble(0);
        double ty = limeTable.getEntry("ty").getDouble(0);
        double ta = limeTable.getEntry("ta").getDouble(0);
        SmartDashboard.putNumber("Area", ta);

        // check for GT or LT than 0.5 since comparing directly to 0.0 or 1.0 can fail due to rounding error.
        if (tv > 0.5) {
            if (xIsAcceptable(tx)) {
                errorZ = 0;
                integralZ = 0;
                derivitiveZ = 0;
            }
            else {
                // If the camera is reporting an X error rotate to correct the error.
                errorZ = tx;

                // TODO: Should really add a integral limit. NMP 1/25/2020
                integralZ = (tx * 0.027) + priorIntegralZ;
                derivitiveZ = (tx - priorErrorZ) / 2.7;
            }

            if (yIsAcceptable(ty)) {
                driveX = 0;
            }
            else {
                // For the Y adjustment just move at a constant speed until we are in range.
                // NOTE: set the X drive as the camera's Y error is vertical on the camera which corresponds
                //       to moving the robot forward/backwards, which is the X motion axis.
                // NOTE: invert the drive value vs. the Y error since the error is positive if the crosshair
                //       is above the calibrated position, which means that we need to move further from the target.
                // TODO: Should this be changed to use PID for adjustment? NMP 1/25/2020
                driveX = maxSpeedX;
                if (ty > 0)
                    driveX *= -1;
            }

            // Compute the speed to spin at, apply a Feed-Forward if moving slow but clamp it to the spin speed.
            driveZ = (kP_Z * errorZ) + (kI_Z * integralZ) + (kD_Z * derivitiveZ);
            if (driveZ > 0.00001) {
                driveZ += kF_Z;
            }
            else if (driveZ < 0.00001) {
                driveZ -= kF_Z;
            }

            if (driveZ > maxSpeedZ) {
                driveZ = maxSpeedZ;
            }
            else if (driveZ < -maxSpeedZ) {
                driveZ = -maxSpeedZ;
            }

            System.out.printf("%0.4f, %0.4f, %0.4f", driveX, driveY, driveZ);
            SmartDashboard.putNumber("Steering Adjust", driveZ);
        }
        else { // No valid target, zero out the PID terms and rotate to try and acquire a target.
            errorZ = 0;
            integralZ = 0;

            driveX = 0;
            driveY = 0;
            // TODO: Consider adding a gyro to see if we should spin CW or CCW? NMP 1/25/2020
            driveZ = maxSpeedZ;
        }

        priorErrorZ = errorZ;
        priorIntegralZ = integralZ;

        mDrive.driveCartesian(driveY, driveX, driveZ);
    }

    public void resetErrors() {
        priorIntegralZ = 0;
        priorErrorZ = 0;
    }


    private boolean xIsAcceptable(double value) {
        return (value > -1) && (value < 1);
    }

    private boolean yIsAcceptable(double value) {
        return (value > -3) && (value < 3);
    }
}