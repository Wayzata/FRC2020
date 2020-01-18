/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;

//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

  Joystick joy;
  Shooter shooter;
  WPI_TalonSRX t;

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

    //driveTrain = new DriveTrain();
    //ultrasonic = new Ultrasonic(9, 8);
    shooter = new Shooter();

    joy = new Joystick(0);

    t = new WPI_TalonSRX(0);

    gyro = new ADXRS450_Gyro();
    

    //ultrasonic.setAutomaticMode(true);
    //t.set(ControlMode.Position, 5000);

    t.configFactoryDefault();

    t.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);

    t.setSensorPhase(true);

    t.setInverted(false);
    
    t.configNominalOutputForward(0, 30);
    t.configNominalOutputReverse(0, 30);
    t.configPeakOutputForward(1, 30);
    t.configPeakOutputReverse(-1, 30);

    t.configMotionAcceleration(0, 500);
    t.configMotionCruiseVelocity(0, 500);
    
    t.configAllowableClosedloopError(0, 0, 30);

    //First parameter is PID_loop_id
    t.config_kF(0, 0.094);
    t.config_kP(0, .35);
    t.config_kI(0, 0.001);
    t.config_kD(0, 3);

    t.setSelectedSensorPosition(0, 0, 30);
    //t.set(ControlMode.Position, 100);

    //t.clearMotionProfileTrajectories();
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
    //SmartDashboard.putNumber("Ultrasonic Reading", ultrasonic.getRangeInches());
    //System.out.println(t.getSelectedSensorPosition());
    //System.out.println("Velocity: " + t.getSelectedSensorVelocity() + "       Position: " + t.getSelectedSensorPosition(0));
    System.out.println(gyro.getAngle());
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
    //driveTrain.mecDrive(joy);
    //t.set(joy.getY());

    if(joy.getRawButtonPressed(5)) {
      t.setSelectedSensorPosition(0, 0, 30);
      //t.set(ControlMode.Position, 10000);
    }
    else if(joy.getRawButtonPressed(3)) {
      t.set(ControlMode.Velocity, 1024);
    }
    else if(joy.getRawButtonPressed(6)) {
      t.set(ControlMode.Velocity, -1024);
    }
    else if(joy.getRawButtonPressed(4)) {
      t.set(ControlMode.Velocity, 0);
    }
    else if(joy.getRawButtonPressed(11)) {
      t.set(ControlMode.Position, t.getSelectedSensorPosition(0) + 1024);
    }

    //shooter.spinnyBoi2k(joy.getRawButton(3));
  }

  

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}