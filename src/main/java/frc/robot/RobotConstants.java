package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import frc.robot.wayzata2264.talonsrx.TalonPidSlotConfig;
import frc.robot.wayzata2264.talonsrx.TalonSrxConfig;

public class RobotConstants {
  public final static int TalonConfigTimeoutMs = 30;
  
  public final static TalonSrxConfig testbedTalonConfig = new TalonSrxConfig(0,                            // CAN address
                                                                             false,                        // Invert motor direction
                                                                             0.00,                         // Minimum forward duty cycle [0.0 - 1.0]
                                                                             -0.00,                        // Minimum reverse duty cycle [-1.0 - 0.0]
                                                                             1.0,                          // Maximum forward duty cycle [0.0 - 1.0]
                                                                             -1.0,                         // Maximum reverse duty cycle [0.0 - 1.0]
                                                                             FeedbackDevice.QuadEncoder,   // Feedback device type
                                                                             4096,                         // Sensor Counts Per rev (after quadrature)
                                                                             true,                         // Sensor "phase" true if sensor increments in same direction as motor
                                                                             0,                            // Motion profile acceleration rate (encoder counts per 100ms) 
                                                                             0,                            // Motion profile crusie velocity (encoder counts per 100ms)
                                                                             new TalonPidSlotConfig(0,        // PID configuration for slot 0
                                                                                                     0.094,    // Feed-forward gain
                                                                                                     0.350,    // kP: proportional gain
                                                                                                     0.001,    // kI: integral gain
                                                                                                     3.0),     // kD: derivitive gain
                                                                             new TalonPidSlotConfig(1),    // PID configuration for slot 1 (not currently used so set default values)
                                                                             new TalonPidSlotConfig(2),    // PID configuration for slot 2 (not currently used so set default values)
                                                                             new TalonPidSlotConfig(3));   // PID configuration for slot 3 (not currently used so set default values)
}