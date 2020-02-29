package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
//import com.revrobotics.Rev2mDistanceSensor;
//import com.revrobotics.Rev2mDistanceSensor.Port;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {
    // Motors
    WPI_TalonSRX intakeMotorOne;
    WPI_TalonSRX intakeMotorTwo;
    WPI_TalonSRX conveyorMotor;
    WPI_TalonSRX feedMotor;
    WPI_TalonSRX shooterOne;
    WPI_TalonSRX shooterTwo;
    WPI_TalonSRX colorFlap;

    // Beam Break Sensors for Indexing Power Cells
    DigitalInput frontStartBeam;
    DigitalInput frontEndBeam;
    DigitalInput conveyorBeam;
    DigitalInput feedBeam;
    DigitalInput colorSenseDown;
    DigitalInput colorSenseUp;

    //Rev2mDistanceSensor dSenseHigh;
    //Rev2mDistanceSensor dSenseLow;



    public Intake() {
        intakeMotorOne = new WPI_TalonSRX(Variables.intakeMotorOnePort); 
        conveyorMotor = new WPI_TalonSRX(Variables.conveyorMotorPort);
        feedMotor = new WPI_TalonSRX(Variables.feedMotorPort);
        frontStartBeam = new DigitalInput(Variables.frontStartBeamPort);
        frontEndBeam = new DigitalInput(Variables.frontEndBeamPort);
        conveyorBeam = new DigitalInput(Variables.conveyorBeamPort);
        feedBeam = new DigitalInput(Variables.feedBeamPort);
        shooterOne = new WPI_TalonSRX(Variables.shooterMotorOnePort);
        shooterTwo = new WPI_TalonSRX(Variables.shooterMotorTwoPort);
        colorFlap = new WPI_TalonSRX(Variables.colorArmPort);
        //dSenseHigh = new Rev2mDistanceSensor(Port.kOnboard);
        //dSenseLow = new Rev2mDistanceSensor(Port.kMXP);
        colorSenseDown = new DigitalInput(Variables.colorSenseDownPort);
        colorSenseUp = new DigitalInput(Variables.colorSenseUpPort);
    }

    public void setSpeed(double speed) {
        intakeMotorOne.set(ControlMode.PercentOutput, speed);
        //intakeMotorTwo.set(ControlMode.PercentOutput, speed);
    }

    public void checkIntake(boolean bPressed, boolean expellPressed){
        SmartDashboard.putBoolean("FrontBeam", frontStartBeam.get());
        SmartDashboard.putBoolean("EndBeam", frontEndBeam.get());
        SmartDashboard.putBoolean("ConveyorBeam", conveyorBeam.get());
        SmartDashboard.putBoolean("FeederBeam", feedBeam.get());
        if (bPressed){
            // Set Intake to run
            if (frontEndBeam.get()){
                setSpeed(Variables.intakeMotorSpeed);
            } else {
                setSpeed(0);
            }
            
            // Front beam sensors for indexing
            if ((!frontStartBeam.get() || !frontEndBeam.get()) && feedBeam.get()){ //
                conveyorMotor.set(ControlMode.PercentOutput, Variables.conveyorIndexSpeed);
            } else {
                conveyorMotor.set(ControlMode.PercentOutput, 0);
            }

            // End beam break check for indexing
            if (!conveyorBeam.get() && feedBeam.get()){ // && feedBeam.get()
                feedMotor.set(ControlMode.PercentOutput, Variables.feedIndexSpeed);
            } else {
                feedMotor.set(ControlMode.PercentOutput, 0);
            }

        } else if (expellPressed){
            intakeMotorOne.set(-1 * Variables.intakeMotorSpeed);
            conveyorMotor.set(-1 * Variables.conveyorIndexSpeed);
            feedMotor.set(-0.50 * Variables.feedIndexSpeed);
        } else {
            setSpeed(0);
            feedMotor.set(ControlMode.PercentOutput, 0);
            conveyorMotor.set(ControlMode.PercentOutput, 0);
        }
    
    }

    public void setFullShoot(boolean bPressed){
        if (bPressed){
            spinUpShooter(true, 0);
            setFullConvey(true);
            intakeMotorOne.set(ControlMode.PercentOutput, Variables.intakeMotorSpeed);
        } else {
            spinUpShooter(false, 0);
            setFullConvey(false);
            intakeMotorOne.set(ControlMode.PercentOutput, 0);
        }
    }

    public void setFullConvey(boolean bPressed){
        if (bPressed){
            feedMotor.set(ControlMode.PercentOutput, Variables.feedShootSpeed);
            conveyorMotor.set(ControlMode.PercentOutput, Variables.conveyorShootSpeed);
        } else {
            conveyorMotor.set(ControlMode.PercentOutput, 0);
            feedMotor.set(ControlMode.PercentOutput, 0);
        }
    }

    public void setColorFlap(double speed){
        colorFlap.set(ControlMode.PercentOutput, speed);
    }

    public void setColorFlapUp() {

        if(!colorSenseUp.get()) {
            colorFlap.set(0);
        }
        else {
            colorFlap.set(-0.6);
        }
        
    }

    public void setColorFlapDown() {

        if(!colorSenseDown.get()) {
            colorFlap.set(0);
        }
        else {
            colorFlap.set(0.2);
        }
    }

    /**
     * Controls the shooter speed accurately regardless the battery that is being used
     * Uses current control mode on the Talons.
     * Not as accurate as encoders but since it is a shooter we can align ourselves to adjust
     * for a certain amperage with our vision.
     */
    public void spinUpShooter(boolean bPressed, double ty) {
        if (bPressed){
            //shooterOne.set(ControlMode.PercentOutput, Variables.shooterSpeed); 
            //shooterTwo.set(ControlMode.PercentOutput, -1 * Variables.shooterSpeed);
            if(ty >= -6) {
                shooterOne.setVoltage(Variables.shooterNominalVoltage);
                shooterTwo.setVoltage(-1*(Variables.shooterNominalVoltage));
            }
            else if(ty >= -12) {
                shooterOne.setVoltage(Variables.shooterNominalVoltage + Variables.shooterDistanceMiddle_kP*ty);
                shooterTwo.setVoltage(-1*(Variables.shooterNominalVoltage + Variables.shooterDistanceMiddle_kP*ty));
            }
            else {
                shooterOne.setVoltage(Variables.shooterNominalVoltage + Variables.shooterDistanceFar_kP*ty);
                shooterTwo.setVoltage(-1*(Variables.shooterNominalVoltage + Variables.shooterDistanceFar_kP*ty));
            }
            
            // Below for tuning amperage for current control loop
            SmartDashboard.putNumber("Shooter Two Voltage", shooterTwo.getMotorOutputVoltage());
            SmartDashboard.putNumber("Shooter One Voltage", shooterOne.getMotorOutputVoltage());
        } else {
            shooterOne.set(ControlMode.PercentOutput, 0);
            shooterTwo.set(ControlMode.PercentOutput, 0);
        }
    }
    /**
     * Returns if the shooters are actually at the set amperage +- kTolerance AMPs
     */
    public boolean isReadyForShot(){
        double kTolerance = 0.25; // Tolerance in amps for shooter motors
        if (shooterOne.getMotorOutputVoltage() <= (Variables.shooterNominalVoltage + kTolerance) && shooterOne.getMotorOutputVoltage() >= Variables.shooterNominalVoltage - kTolerance){
            if (shooterTwo.getMotorOutputVoltage() <= (Variables.shooterNominalVoltage + kTolerance) && shooterTwo.getMotorOutputVoltage() >= Variables.shooterNominalVoltage - kTolerance){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * Configures and sets the shooter motor PID values
     * Called on RobotInit
     */
    public void setupShooterMotorConfigs(){
        // Left Shooter Config - DONT KNOW IF SHOOTERONE IS LEFT SHOOTER MAKE SURE TO CHECK
        shooterOne.configFactoryDefault();
        shooterOne.configPeakOutputForward(1);
        shooterOne.configPeakOutputReverse(-1);
        shooterOne.config_kP(0, Variables.leftShooter_kP);
        shooterOne.config_kI(0, Variables.leftShooter_kI);
        shooterOne.config_kD(0, Variables.leftShooter_kD);
        shooterOne.config_kF(0, Variables.leftShooter_kF);

        // Right Shooter Config - DONT KNOW IF SHOOTERTWO IS RIGHT SHOOTER MAKE SURE TO CHECK
        shooterTwo.configFactoryDefault();
        shooterTwo.configPeakOutputForward(1);
        shooterTwo.configPeakOutputReverse(-1);
        shooterTwo.config_kP(0, Variables.leftShooter_kP);
        shooterTwo.config_kI(0, Variables.leftShooter_kI);
        shooterTwo.config_kD(0, Variables.leftShooter_kD);
        shooterTwo.config_kF(0, Variables.leftShooter_kF);
    }


    public void setFeedMotor(double speed) {
        feedMotor.set(ControlMode.PercentOutput, speed);
    }
}