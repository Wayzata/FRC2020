package frc.robot.wayzata2264.talonsrx;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.RobotConstants;

public class TalonPidSlotConfig {
  public final boolean inUse;
  public final int index;
  public final double kF;
  public final double kP;
  public final double kI;
  public final double kD;
  public final double peakOutput;

  public final int closedLoopPeriodMs;
  public final int allowableClosedLoopError;
  public final int integralZone;
  public final double integralMaxAccmulator;

  public TalonPidSlotConfig(int _index, double _kF, double _kP, double _kI, double _kD, double _peakOutput, int _periodMs, int _maxError, int _iZone, double _iMax)  {
    inUse = true;
    index = _index;
    kF = _kF;
    kP = _kP;
    kI = _kI;
    kD = _kD;
    peakOutput = 1.0;

    closedLoopPeriodMs = _periodMs;
    allowableClosedLoopError = _maxError;
    integralZone = _iZone;
    integralMaxAccmulator = _iMax;
}

  public TalonPidSlotConfig(int _index, double _kF, double _kP, double _kI, double _kD)  {
    // TODO: The integral zone and max i accumulator values may need to be tweaked. NMP 1/19/2020
    this(_index, _kF, _kP, _kI, _kD,
         1.0,     // _peakOutput
         1,       // _periodMs
         0,       // _maxError
         300,     // _iZone
         100.0);  // _iMax
  }

  public TalonPidSlotConfig(int _index)  {
    inUse = false;
    index = _index;
    kF = 0;
    kP = 0;
    kI = 0;
    kD = 0;
    peakOutput = 1.0;

    closedLoopPeriodMs = 0;
    allowableClosedLoopError = 0;
    integralZone = 0;
    integralMaxAccmulator = 0;
  }

  public boolean setConfig(WPI_TalonSRX talon) {
    if (!inUse)
      return true;

    var err = talon.configAllowableClosedloopError(index, allowableClosedLoopError, RobotConstants.TalonConfigTimeoutMs);
    if (err != ErrorCode.OK) {
      System.out.println(String.format("Error %d while writing closed loop error limit to slot %d", err, index));
      return false;
    }

    err = talon.config_kF(index, kF, RobotConstants.TalonConfigTimeoutMs);
    if (err != ErrorCode.OK) {
      System.out.println(String.format("Error %d while writing kF to slot %d", err, index));
      return false;
    }

    err = talon.config_kP(index, kP, RobotConstants.TalonConfigTimeoutMs);
    if (err != ErrorCode.OK) {
      System.out.println(String.format("Error %d while writing kP to slot %d", err, index));
      return false;
    }
      
    err = talon.config_kI(index, kI, RobotConstants.TalonConfigTimeoutMs);
    if (err != ErrorCode.OK) {
      System.out.println(String.format("Error %d while writing kI to slot %d", err, index));
      return false;
    }
      
    err = talon.config_kD(index, kD, RobotConstants.TalonConfigTimeoutMs);
    if (err != ErrorCode.OK) {
      System.out.println(String.format("Error %d while writing kD to slot %d", err, index));
      return false;
    }

    err = talon.configClosedLoopPeakOutput(index, peakOutput, RobotConstants.TalonConfigTimeoutMs);
    if (err != ErrorCode.OK) {
      System.out.println(String.format("Error %d while writing peak output to slot %d", err, index));
      return false;
    }

    err = talon.configClosedLoopPeriod(index, closedLoopPeriodMs, RobotConstants.TalonConfigTimeoutMs);
    if (err != ErrorCode.OK) {
      System.out.println(String.format("Error %d while writing loop period to slot %d", err, index));
      return false;
    }

    err = talon.config_IntegralZone(index, integralZone, RobotConstants.TalonConfigTimeoutMs);
    if (err != ErrorCode.OK) {
      System.out.println(String.format("Error %d while writing iZone to slot %d", err, index));
      return false;
    }

    err = talon.configMaxIntegralAccumulator(index, integralMaxAccmulator, RobotConstants.TalonConfigTimeoutMs);
    if (err != ErrorCode.OK) {
      System.out.println(String.format("Error %d while writing closed iMaxAccum to slot %d", err, index));
      return false;
    }


    // Zero the encoder position.
    err = talon.setSelectedSensorPosition(0, index, RobotConstants.TalonConfigTimeoutMs);
    if (err != ErrorCode.OK) {
      System.out.println(String.format("Error %d resetting encoder position for slot %d", err, index));
      return false;
    }

    return true;
  }
}