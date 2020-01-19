package frc.robot.wayzata2264.talonsrx;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.RobotConstants;

public class TalonSrxFactory {
    public static WPI_TalonSRX createAndInit(TalonSrxConfig config) {
        var t = new WPI_TalonSRX(config.canAddress);
    
        t.configFactoryDefault();
    
        t.configSelectedFeedbackSensor(config.feedbackDevice, 0, RobotConstants.TalonConfigTimeoutMs);
        t.setSensorPhase(config.sensorPhase);
        t.setInverted(config.inverted);
        
        t.configNominalOutputForward(config.minOutputForward, RobotConstants.TalonConfigTimeoutMs);
        t.configNominalOutputReverse(config.minOutputReverse, RobotConstants.TalonConfigTimeoutMs);
        t.configPeakOutputForward(config.maxOutputForward, RobotConstants.TalonConfigTimeoutMs);
        t.configPeakOutputReverse(config.maxOutputReverse, RobotConstants.TalonConfigTimeoutMs);
    
        t.configMotionAcceleration(config.accelRate, RobotConstants.TalonConfigTimeoutMs);
        t.configMotionCruiseVelocity(config.cruiseVelocity, RobotConstants.TalonConfigTimeoutMs);
    
        for (var slotConfig : config.pidSlotConfig) {
          slotConfig.setConfig(t);
        }
        
        t.setSelectedSensorPosition(0, 0, RobotConstants.TalonConfigTimeoutMs);
    
        t.clearMotionProfileTrajectories();
    
        return t;
      }
}