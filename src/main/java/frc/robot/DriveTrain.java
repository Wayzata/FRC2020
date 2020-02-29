package frc.robot;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
// import edu.wpi.first.wpilibj.kinematics.MecanumDriveKinematics;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpiutil.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {

    double ty, tx, tv, ta, ts, zAdjust, xAdjust, yAdjust, integralZ, priorI, derivZ, priorEZ;
    final double kP_z = 0.009;
    final double kF_z = 0.1;
    final double kI_z = 0;
    final double kD_z = 0.001;

    final double kP_x = 0.0175;
    final double kF_x = 0.1;
    final double kI_x = 0.005;
    final double kD_x = 0.003;

    final double kP_y = 0.03125;
    final double kF_y = 0.1;
    final double kI_y = 0;
    final double kD_y = 0;

    final double speed = 0.7;

    long startShootTime = 0;
    public static long startBackUpTime = 0;

    WPI_TalonSRX backLeft;
    WPI_TalonSRX backRight;
    WPI_TalonSRX frontLeft;
    WPI_TalonSRX frontRight;

    PIDController pid_x = new PIDController(kP_x, kI_x, kD_x);
    PIDController pid_y = new PIDController(kP_y, kI_y, kD_y);
    PIDController pid_z = new PIDController(kP_z, kI_z, kD_z);

    Encoder encoder;

    MecanumDrive mDrive;

    NetworkTable limeTable;

    boolean shooting = false;

    public DriveTrain() {
        zAdjust = 0;
        xAdjust = 0;
        yAdjust = 0;
        integralZ = 0;
        priorI = 0;
        derivZ = 0;
        priorEZ = 0;

        frontLeft = new WPI_TalonSRX(Variables.frontLeftMotorPort);
        frontRight = new WPI_TalonSRX(Variables.frontRightMotorPort);
        backRight = new WPI_TalonSRX(Variables.backRightMotorPort);
        backLeft = new WPI_TalonSRX(Variables.backLeftMotorPort);

        // backLeft.setInverted(true);
        // backRight.setInverted(true);

        mDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
        mDrive.setSafetyEnabled(false);

        limeTable = NetworkTableInstance.getDefault().getTable("limelight");

        // PID Setup
       pid_x.setTolerance(5);
       pid_x.setSetpoint(0);
        pid_y.setTolerance(1);
        pid_y.setSetpoint(0);
        pid_z.setTolerance(1);
        pid_z.setSetpoint(0);
    }

    public void mecDrive(Joystick j) {
        mDrive.driveCartesian(0.7 * j.getX(), -0.6 * j.getY(), 0.6 * j.getZ());
        shooting = false;
    }

    public void fullStop() {
        mDrive.driveCartesian(0, 0, 0);
    }

    public void targetGoal(Joystick joy) {
        tv = limeTable.getEntry("tv").getDouble(0);
        tx = limeTable.getEntry("tx").getDouble(0);
        ty = limeTable.getEntry("ty").getDouble(0);
        ta = limeTable.getEntry("ta").getDouble(0);
        ts = limeTable.getEntry("ts").getDouble(0);

        Robot.intake.spinUpShooter(true, ty);
        if (tv == 1) {
            if (xIsAcceptable(tx)) {
                zAdjust = 0;
            } else { // Do the PID calculations for the spin-value
                integralZ += (tx * 0.027);
                derivZ = (tx - priorEZ) / 0.027;
                zAdjust = (kP_z * tx) + (kI_z * integralZ) + (kD_z * derivZ);
                priorEZ = tx;

                // Add feed-forward value
                if (zAdjust > 0) {
                    zAdjust += kF_z;
                } else {
                    zAdjust -= kF_z;
                }

                // Check max/min bounds
                if (zAdjust > speed) {
                    zAdjust = speed;
                } else if (zAdjust < -speed) {
                    zAdjust = -speed;
                }
            }

            if (yIsAcceptable(ty)) {
                yAdjust = 0;
            } else { // Do the PID calculations for the drive-value
                yAdjust = -(kP_y * ty);

                // Add feed-forward value
                if (yAdjust > 0) {
                    yAdjust += kF_y;
                } else {
                    yAdjust -= kF_y;
                }

                // Check max/min bounds
                if (yAdjust > speed) {
                    yAdjust = speed;
                } else if (yAdjust < -speed) {
                    yAdjust = -speed;
                }
            }
        } else { // If no target is in sight, spin until one is found
            zAdjust = 0;
            yAdjust = 0;
            xAdjust = 0;
        }

        // Set drivetrain to the calculated values ////NOTE: xAdjust is not currently
        // being used, it is always zero // maybe add back y :)
        mDrive.driveCartesian(0, 0, zAdjust);

        if(xIsAcceptable(tx)) { //  && yIsAcceptable(ty)
            if(shooting) {
                if(System.currentTimeMillis() - startShootTime > 5000) {
                    Robot.intake.setFullConvey(false);
                }
                else if(System.currentTimeMillis() - startShootTime > 1000) {
                    Robot.intake.setFullConvey(true);
                }
                else {
                    Robot.intake.setFullConvey(false);
                }
            }
            else {
                shooting = true;
                startShootTime = System.currentTimeMillis();
            }
        }
        else {
        }
    }

    // private void targetThenShoot(boolean bPressed) {
    //     tv = limeTable.getEntry("tv").getDouble(0);
    //     tx = limeTable.getEntry("tx").getDouble(0);
    //     ty = limeTable.getEntry("ty").getDouble(0);
    //     ta = limeTable.getEntry("ta").getDouble(0);
    //     ts = limeTable.getEntry("ts").getDouble(0);

    //     if (tv == 1) {
    //         mDrive.driveCartesian(MathUtil.clamp(calculatePlaneSpeed(), -0.8, 0.8),
    //                 MathUtil.clamp(pid_y.calculate(ty), -0.6, 0.6), MathUtil.clamp(pid_z.calculate(tx), -0.6, 0.6));
    //         if (pid_z.atSetpoint() && pid_x.atSetpoint() && planeAtSetpoint()) {

    //         }
    //     }

    // }

    // private double calculatePlaneSpeed() {
    //     return 0.0;
    // }

    // private boolean planeAtSetpoint() {
    //     return true;
    // }

    private boolean xIsAcceptable(double value) {
        return (value > -1.5) && (value < 1.5);
    }

    private boolean yIsAcceptable(double value) {
        return (value > -3) && (value < 3);
    }

    public void resetErrors() {
        priorI = 0;
        priorEZ = 0;
        pid_x.reset();
        pid_y.reset();
        pid_z.reset();
    }

    public void backUp() {
        if(System.currentTimeMillis() - startBackUpTime > 1000) {
            Robot.crossedLine = true;
            mDrive.driveCartesian(0, 0, 0);
        }
        else {
            mDrive.driveCartesian(0, -0.5, 0);
        }
    }

    public void setShooting(boolean value) {
        shooting = value;
    }

    public void putEncoders(){
        SmartDashboard.putNumber("Front Left Encoder", frontLeft.getSelectedSensorPosition());
        SmartDashboard.putNumber("Front Right Encoder", frontRight.getSelectedSensorPosition());
        SmartDashboard.putNumber("Back Left Encoder", backLeft.getSelectedSensorPosition());
        SmartDashboard.putNumber("Back Right Encoder", backRight.getSelectedSensorPosition());
    }
}