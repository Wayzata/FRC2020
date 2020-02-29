package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climbing {
    // Motors
    WPI_TalonSRX raiseMotor;
    WPI_TalonSRX climbMotor;

    DigitalInput downLimit;
    DigitalInput upperLimit;

    boolean lowering = false;

    boolean lowered = true;

    public Climbing() {
        raiseMotor = new WPI_TalonSRX(Variables.raiseMotorPort);
        climbMotor = new WPI_TalonSRX(Variables.climbMotorPort);
        downLimit = new DigitalInput(8);

    }

    public void checkClimb(int pov) {
        if(pov == 180) {
            climbMotor.set(ControlMode.PercentOutput, Variables.climbMotorUpSpeed);
        }
        else if(pov == 0) {
            climbMotor.set(ControlMode.PercentOutput, Variables.climbMotorDownSpeed);
        }
        else {
            climbMotor.set(ControlMode.PercentOutput, 0);
        }
    }

    public void setRaiserUp() {
        raiseMotor.set(ControlMode.PercentOutput, Variables.raiseMotorUpSpeed);
    }

    public void setRaiserDown() {
        raiseMotor.set(ControlMode.PercentOutput, Variables.raiseMotorDownSpeed);
    }

    public void stopRaiser() {
        raiseMotor.set(ControlMode.PercentOutput, 0);
    }

    public void climberUp() {
        climbMotor.set(ControlMode.PercentOutput, Variables.climbMotorUpSpeed);
    }

    public void climberDown() {
        climbMotor.set(ControlMode.PercentOutput, Variables.climbMotorDownSpeed);
    }

    public void stopClimber() {
        climbMotor.set(ControlMode.PercentOutput, 0);
    }

    public void printClimbThing() {
        SmartDashboard.putNumber("Arm Encoder", raiseMotor.getSelectedSensorPosition());
    }

    public void raiseArmToPosition() {
        raiseMotor.set(ControlMode.Position, 1190);
    }

    public void beginArmReset() {
        raiseMotor.set(ControlMode.PercentOutput, Variables.raiseMotorDownSpeed);
        lowering = true;
    }

    public void checkArmLowering() {

    }

    public void PIDSetup() {
        raiseMotor.configFactoryDefault();
    
        raiseMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
    
        raiseMotor.setSensorPhase(false);
    
        raiseMotor.setInverted(false);
        
        raiseMotor.configNominalOutputForward(0, 30);
        raiseMotor.configNominalOutputReverse(0, 30);
        raiseMotor.configPeakOutputForward(0.75, 30);
        raiseMotor.configPeakOutputReverse(-0.5, 30);
        // raiseMotor.configMotionAcceleration(0, 75);
        // raiseMotor.configMotionCruiseVelocity(0, 75);
        
        raiseMotor.configAllowableClosedloopError(0, 0, 30);
    
        raiseMotor.config_kF(0, 0.2);
        raiseMotor.config_kP(0, 1.25);
        raiseMotor.config_kI(0, 0.0);
        raiseMotor.config_kD(0, 0.1);
    
        raiseMotor.setSelectedSensorPosition(0, 0, 30);
    }

    public void armReset(){
        if(!downLimit.get()){
            System.out.println("limit switch already active");
            raiseMotor.set(ControlMode.PercentOutput, 0);
        } else {
            raiseMotor.set(ControlMode.PercentOutput, Variables.raiseMotorDownSpeed);
        }
    }
    
    public void checkEncoderReset() {
        if(!downLimit.get()) {
            raiseMotor.setSelectedSensorPosition(0);
        }
    }
}

/*


this is epic and no one knows what we're doing
but were epic and they


*/