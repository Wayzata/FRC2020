package frc.robot;

public class Variables {

    // Motor Ports
    public static final int frontLeftMotorPort = 1;
    public static final int frontRightMotorPort = 4;
    public static final int backLeftMotorPort = 2;
    public static final int backRightMotorPort = 3;
    public static final int conveyorMotorPort = 10;
    public static final int feedMotorPort = 13;
    public static final int shooterMotorOnePort = 5;
    public static final int shooterMotorTwoPort = 6;
    public static final int intakeMotorOnePort = 9;
    //public static final int intakeMotorTwoPort = 4;
    public static final int raiseMotorPort = 8;
    public static final int climbMotorPort = 7;
    public static final int controlMotorPort = 11;
    public static final int colorArmPort = 12;

    // Index Speed Amounts
    public static final double intakeMotorSpeed = -0.55;
    public static final double conveyorIndexSpeed = 0.90;
    public static final double feedIndexSpeed = 1.0;

    // Climbing Motor Speeds
    public static final double raiseMotorUpSpeed = 1;
    public static final double raiseMotorDownSpeed = -0.65;
    public static final double climbMotorUpSpeed = -1.0;
    public static final double climbMotorDownSpeed = 1.0;

    // Shooter and Control Panel speeds
    public static final double shooterSpeed = 0.55;
    public static final double conveyorShootSpeed = 0.95;
    public static final double feedShootSpeed = 1.0;
    public static final double controlPanelSpeed = 0.3;

    // Sensor Ports
    public static final int frontStartBeamPort = 3;
    public static final int frontEndBeamPort = 4;
    public static final int conveyorBeamPort = 5;
    public static final int feedBeamPort = 6;
    public static final int colorSenseDownPort = 0;
    public static final int colorSenseUpPort = 1;

    // Shooter Control Loop Voltages
    public static final double shooterNominalVoltage = 6.1; // 6.1 Update this value once tuned - NOT TUNED

    // Left Shooter Gains
    public static final double leftShooter_kP = 0;
    public static final double leftShooter_kI = 0;
    public static final double leftShooter_kD = 0;
    public static final double leftShooter_kF = 0;

    // Right Shooter Gains
    public static final double rightShooter_kP = 0.5;
    public static final double rightShooter_kI = 0;
    public static final double rightShooter_kD = 0;
    public static final double rightShooter_kF = 0;

    // Shooter Speed Gain
    //public static final double shooterDistanceNear_kP = -(1.0 / 11.0); //Fix value?
    public static final double shooterDistanceMiddle_kP = -(1.0 / 12.8);
    public static final double shooterDistanceFar_kP = -(1.0 / 11.);
    //
}