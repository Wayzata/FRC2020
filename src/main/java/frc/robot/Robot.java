/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.wayzata2264.talonsrx.TalonSrxFactory;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  DriveTrain driveTrain;
  Ultrasonic ultrasonic;

  Joystick joystick;
  Shooter shooter;
  WPI_TalonSRX testbedTalon;

  ADXRS450_Gyro gyro;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // Create all the motor drivers/controllers
    if (RobotConstants.enableTestbedMotor) {
      testbedTalon = TalonSrxFactory.createAndInit(RobotConstants.testbedTalonConfig);
    }

    // Create the input sensors/devices
    joystick = new Joystick(RobotConstants.joystickPort);

    if (RobotConstants.enableGyro) {
      gyro = new ADXRS450_Gyro();
    }
    ultrasonic = new Ultrasonic(RobotConstants.ultrasonicSensorPingCh, RobotConstants.ultrasonicSensorEchoCh);

    // Create the subsystem components
    if (RobotConstants.enableDrivetrain) {
      driveTrain = new DriveTrain(TalonSrxFactory.createAndInit(RobotConstants.drivetrainLFTalonConfig),
                                  TalonSrxFactory.createAndInit(RobotConstants.drivetrainRFTalonConfig),
                                  TalonSrxFactory.createAndInit(RobotConstants.drivetrainLRTalonConfig),
                                  TalonSrxFactory.createAndInit(RobotConstants.drivetrainRRTalonConfig));
      }

    if (RobotConstants.enableShooter) {
      shooter = new Shooter(TalonSrxFactory.createAndInit(RobotConstants.shooter0TalonConfig), TalonSrxFactory.createAndInit(RobotConstants.shooter1TalonConfig));
    }

    // Initialize/start any sensors that need to be started.
    if (RobotConstants.enableUltrasonicSensor) {
      ultrasonic.setAutomaticMode(true);
    }
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    if (RobotConstants.enableUltrasonicSensor) {
      SmartDashboard.putNumber("Ultrasonic Reading", ultrasonic.getRangeInches());
    }

    if (RobotConstants.enableTestbedMotor) {
      System.out.println("Velocity: " + testbedTalon.getSelectedSensorVelocity() + "       Position: " + testbedTalon.getSelectedSensorPosition(0));
    }
    
    if (RobotConstants.enableGyro) {
      System.out.println(gyro.getAngle());
    }
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    if (RobotConstants.enableDrivetrain) {
      driveTrain.drive(joystick.getX() * RobotConstants.drivetrainSpeedScaleFactor, 
                      -joystick.getY() * RobotConstants.drivetrainSpeedScaleFactor,
                      joystick.getZ() * RobotConstants.drivetrainSpeedScaleFactor);
    }

    if (RobotConstants.enableShooter) {
      shooter.setEnable(joystick.getRawButton(3));
    }

    if (RobotConstants.enableTestbedMotor) {
      if(joystick.getRawButtonPressed(5)) {
        testbedTalon.setSelectedSensorPosition(0, 0, RobotConstants.TalonConfigTimeoutMs);
        //testbedTalon.set(ControlMode.Position, 10000);
      }
      else if(joystick.getRawButtonPressed(3)) {
        testbedTalon.set(ControlMode.Velocity, 1024);
      }
      else if(joystick.getRawButtonPressed(6)) {
        testbedTalon.set(ControlMode.Velocity, -1024);
      }
      else if(joystick.getRawButtonPressed(4)) {
        testbedTalon.set(ControlMode.Velocity, 0);
      }
      else if(joystick.getRawButtonPressed(11)) {
        testbedTalon.set(ControlMode.Position, testbedTalon.getSelectedSensorPosition(0) + 1024);
      }
    }
  }

  

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}