package frc.robot.wayzata2264.talonsrx;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TalonSrxConfig {
  public final int canAddress;
  public final boolean inverted;
  public final double minOutputForward;
  public final double minOutputReverse;
  public final double maxOutputForward;
  public final double maxOutputReverse;
  public final FeedbackDevice feedbackDevice;
  public final int sensorCountsPerRev;
  public final boolean sensorPhase;

  public final TalonPidSlotConfig[] pidSlotConfig;

  public final int accelRate;
  public final int cruiseVelocity;

  public TalonSrxConfig(int _canAddr, boolean _inverted, double _minOutputForward, double _minOutputReverse, double _maxOutputForward, double _maxOutputReverse,
                        FeedbackDevice _feedbackType, int _sensorCountsPerRev, boolean _encoderPhase, int _accelRate, int _maxVelocity,
                        TalonPidSlotConfig _slotConfig0, TalonPidSlotConfig _slotConfig1, TalonPidSlotConfig _slotConfig2, TalonPidSlotConfig _slotConfig3) {
      canAddress = _canAddr;
      inverted = _inverted;
      minOutputForward = _minOutputForward;
      minOutputReverse = _minOutputReverse;
      maxOutputForward = _maxOutputForward;
      maxOutputReverse = _maxOutputReverse;
      feedbackDevice = _feedbackType;
      sensorCountsPerRev = _sensorCountsPerRev;
      sensorPhase = _encoderPhase;
      accelRate = _accelRate;
      cruiseVelocity = _maxVelocity;

      pidSlotConfig = new TalonPidSlotConfig[4];
      pidSlotConfig[0] = _slotConfig0;
      pidSlotConfig[1] = _slotConfig1;
      pidSlotConfig[2] = _slotConfig2;
      pidSlotConfig[3] = _slotConfig3;
  }

  public boolean TalonSrcSetPidConfig(WPI_TalonSRX talon, int pidIdx) {
    talon.config_kF(pidIdx, pidSlotConfig[pidIdx].kF);
    talon.config_kP(pidIdx, pidSlotConfig[pidIdx].kP);
    talon.config_kI(pidIdx, pidSlotConfig[pidIdx].kI);
    talon.config_kD(pidIdx, pidSlotConfig[pidIdx].kD);

    return true;
  }
}