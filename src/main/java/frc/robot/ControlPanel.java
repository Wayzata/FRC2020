package frc.robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

public class ControlPanel {
    WPI_TalonSRX controlPanel;
    Encoder encoder;
    Color detectedColor;
    String seenColor;
    ColorMatchResult match;
    String targetColor;
    ColorSensorV3 sensyBoi;
    ColorMatch matchyMan;

    I2C.Port i2cPort;
    ColorSensorV3 m_colorSensor;

    final Color kBlueTarget;
    final Color kGreenTarget;
    final Color kRedTarget;
    final Color kYellowTarget;
    boolean findingColor;

    public ControlPanel() {
        controlPanel = new WPI_TalonSRX(Variables.controlMotorPort);
        i2cPort = I2C.Port.kOnboard;
        m_colorSensor = new ColorSensorV3(i2cPort);
        sensyBoi = new ColorSensorV3(i2cPort);
        matchyMan = new ColorMatch();

        kBlueTarget = ColorMatch.makeColor(0.252, 0.473, 0.275);
        kGreenTarget = ColorMatch.makeColor(0.247, 0.541, 0.212);
        kRedTarget = ColorMatch.makeColor(0.320, 0.462, 0.218);
        kYellowTarget = ColorMatch.makeColor(0.297, 0.534, 0.168);
        // private final Color kBlueTarget = ColorMatch.makeColor(0.177, 0.455, 0.367);
        // private final Color kGreenTarget = ColorMatch.makeColor(0.226, 0.529, 0.245);
        // private final Color kRedTarget = ColorMatch.makeColor(0.386, 0.419, 0.194);
        // private final Color kYellowTarget = ColorMatch.makeColor(0.317, 0.517, 0.165);

        matchyMan.addColorMatch(kBlueTarget);
        matchyMan.addColorMatch(kGreenTarget);
        matchyMan.addColorMatch(kRedTarget);
        matchyMan.addColorMatch(kYellowTarget);

        colorPIDSetup();
    }
    public void colorPIDSetup() {
        controlPanel.configFactoryDefault();
    
        controlPanel.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
    
        controlPanel.setSensorPhase(false);
    
        controlPanel.setInverted(false);
        
        controlPanel.configNominalOutputForward(0, 30);
        controlPanel.configNominalOutputReverse(0, 30);
        controlPanel.configPeakOutputForward(0.5, 30);
        controlPanel.configPeakOutputReverse(-0.5, 30);
        controlPanel.configMotionAcceleration(0, 75);
        controlPanel.configMotionCruiseVelocity(0, 75);
        
        controlPanel.configAllowableClosedloopError(0, 0, 30);
    
        controlPanel.config_kF(0, 0);
        controlPanel.config_kP(0, 0.25);
        controlPanel.config_kI(0, 0);
        controlPanel.config_kD(0, 0);
    
        controlPanel.setSelectedSensorPosition(0, 0, 30);
    }
    public void getColor(String gameData){
        detectedColor = m_colorSensor.getColor();

        match = matchyMan.matchClosestColor(detectedColor);

        switch(gameData.charAt(0)) {
          case 'B':
            targetColor = "Red";
            break;
          case 'Y':
            targetColor = "Green";
            break;
          case 'R':
            targetColor = "Blue";
            break;
          case 'G':
            targetColor = "Yellow";
            break;
          default:
            System.out.println("You messed up");
            break;
        }

        if (match.color == kBlueTarget) {
        seenColor = "Blue";
        } else if (match.color == kRedTarget) {
        seenColor = "Red";
        } else if (match.color == kGreenTarget) {
        seenColor = "Green";
        } else if (match.color == kYellowTarget) {
        seenColor = "Yellow";
        } else {
        seenColor = "Unknown";
        }

        SmartDashboard.putString("Colors", sensyBoi.getRawColor().toString());
        SmartDashboard.putNumber("Confidence", match.confidence);
        SmartDashboard.putString("Color Seen", seenColor);
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);  
        //maybe uh oh no worky

        controlPanel.set(Variables.controlPanelSpeed);

    }

    public void randomColor(){
  
            int betterRand = (int)(4*Math.random()+1);
            findingColor = true;
            
            switch(betterRand) {
              case 1:
                targetColor = "Blue";
                break;
              case 2:
                targetColor = "Green";
                break;
              case 3:
                targetColor = "Red";
                break;
              case 4:
                targetColor = "Yellow";
                break;
            }

            SmartDashboard.putString("Required Color", targetColor);
            //SmartDashboard.putNumber("randy", betterRand);
            controlPanel.set(0.2);
    }
    public void stopOnColor(){
      if(seenColor.equals(targetColor)){
        controlPanel.set(0);
      }
    }
    public void encoderReset(){
          controlPanel.setSelectedSensorPosition(0);
          controlPanel.set(ControlMode.Position, 0);
        }
    
    public void spinFourTimes(){
         controlPanel.set(ControlMode.Position, controlPanel.getSelectedSensorPosition() + 3800);
    }

    public void encoder(){  
        SmartDashboard.putNumber("Enocoder Positron", controlPanel.getSelectedSensorPosition());
    }

    public void setTargeting(boolean b) {
     
    }

    public void printColorEncoder() {
      SmartDashboard.putNumber("Color Encoder", controlPanel.getSelectedSensorPosition());
    }

    public void setColorWheel(double speed) {
      controlPanel.set(ControlMode.PercentOutput, speed);
    }

    public void resetThing() {
      controlPanel.setSelectedSensorPosition(0);
    }
}