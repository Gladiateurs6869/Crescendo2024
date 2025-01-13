// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.ShuffleboardDisplay;
import frc.robot.TuningShuffleboardDisplay;

public class ArmSystem extends SubsystemBase {

    private final WPI_TalonSRX m_motorMaster = new WPI_TalonSRX(Constants.MotorPort.ArmMotorMaster);
    private final WPI_TalonSRX m_motorSlave = new WPI_TalonSRX(Constants.MotorPort.ArmMotorSlave);

    private final DigitalInput m_bottomRightLimitSwitch = new DigitalInput(Constants.DigitalInputPort.ArmBottomRightLimitSwitch);
    private final DigitalInput m_bottomLeftLimitSwitch = new DigitalInput(Constants.DigitalInputPort.ArmBottomLeftLimitSwitch);
    private final DigitalInput m_topLimitSwitch = new DigitalInput(Constants.DigitalInputPort.ArmTopLimitSwitch);

    // Arm Constants
    // Encoder reset to zero when we turn on the robot. 
    // We'll proabably need to manually reset it at some known position. 
    //
    // Lets assume encoder position value 0 will be the lowest value when the arm is nearest the ground. 
    // So that we can reset the encoder when we hit the limit switch. 
    //
    // Encoder is on the motor axis before the final two gears reductions. 
    // so we need to convert arm angle to the ground into encoder revolutions.
    // Actual values will depend on the final gear ratios. 
    // 
    // For now -> lets assume this is 45 degrees from horizontal for basic calculations. 
    public static final double kGearRatio = (50. / 24.) * (84. / 16.);   // approx 2.083 * 5.25 = 10.938

    // public static final double kParkedAngle = 90;
    // public static final double kGroundArmAngle = (10 - kParkedAngle) / 360. * kGearRatio;
    // public static final double kSpeakerArmAngle = (45 - kParkedAngle) / 360. * kGearRatio;
    // public static final double kSpeakerAutoAwayArmAngle = (55 - kParkedAngle) / 360. * kGearRatio;
    // public static final double kChainArmAngle = (115 - kParkedAngle) / 360. * kGearRatio;
    // public static final double kAmpArmAngle =  (75 - kParkedAngle)  / 360. * kGearRatio;

    // At its lowest the arm is at an angle to the ground (i.e. not quite horizontal)
    // Below we want the angle defined as from the horizontal plane. 
    public static final double kInitialParkedAngleDegrees = 90;
    public static final double kLowestArmAngleToGroundDegrees = 20.;

    public static final double kInitialParkedAngle = (kInitialParkedAngleDegrees - kLowestArmAngleToGroundDegrees) / 360. * kGearRatio;
    // By defintion, we want ground arm angle to be encoder position 0.
    public static final double kGroundArmAngle = 0; 
    
    public static final double kSpeakerArmAngle = (43 /* degrees */ - kLowestArmAngleToGroundDegrees) / 360. * kGearRatio;
    public static final double kSpeakerArmAngleSides = (45 /* degrees */ - kLowestArmAngleToGroundDegrees) / 360. * kGearRatio;

    public static final double kSpeakerInterArmAngle = (66 /* degrees */ - kLowestArmAngleToGroundDegrees) / 360. * kGearRatio;
    public static final double kSpeakerAutoAwayArmAngle = (48 - kLowestArmAngleToGroundDegrees) / 360. * kGearRatio;
    //public static final double kReleaseArmAngle = (60 - kParkedArmAngleToGround) / 360. * kGearRatio;
    //public static final double kChainArmAngle = (105 - kLowestArmAngleToGround) / 360. * kGearRatio;
    public static final double kAmpArmAngle =  (75 - kLowestArmAngleToGroundDegrees)  / 360. * kGearRatio;

    public static final double kMinArmAngle = kGroundArmAngle;
    public static final double kMaxArmAngle = kAmpArmAngle;

    // private static final double kMaxCurrent = 30; // Amps, Cannot be more than 40 as we use 40A fuses. 
    // private boolean m_MaxCurrentTripped = false;

    // Convention should be position voltage moves arm up. 
    // Security factor. Limiting max output, max speed. may need to be adjusted after testing. 
    private double m_maxOutput = 0.4;
    public double getMaxOutput() { return m_maxOutput; }
    private GenericEntry m_tuneMaxOutput;

    // PID values. 
    private double m_pidP = 0.4;
    public double getPidP() { return m_pidP; }
    private GenericEntry m_tunePidP;

    private double m_pidI = 0.;
    public double getPidI() { return m_pidI; }
    private GenericEntry m_tunePidI;

    private double m_pidD = 0.00001;
    public double getPidD() { return m_pidD; }
    private GenericEntry m_tunePidD;

    private double m_pidIz = 0;
    public double getPidIz() { return m_pidIz; }
    private GenericEntry m_tunePidIz;

    // Manual fixed speeds (voltage%) for moving the arm. 
    private double m_angleDownSpeed = -0.4;
    public double getAngleDownSpeed() { return m_angleDownSpeed; }
    private GenericEntry m_tuneAngleDownSpeed;

    private double m_angleUpSpeed = 0.4;
    public double getAngleUpSpeed() { return m_angleUpSpeed; }
    private GenericEntry m_tuneAngleUpSpeed;

    // Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
    // now we just want the primary one.
    private static final int kPIDLoopIdx = 0;

    // Set to zero to skip waiting for confirmation, set to nonzero to wait and
    // report to DS if action fails.
    private static final int kTimeoutMs = 30;

    // Choose so that Talon does not report sensor out of phase
    private static final boolean kSensorPhase = true;

    // Choose based on what direction you want to be positive,
    // Convention should be position voltage moves arm up. 
    private static final boolean kMotorInvert = true;

    // Number of encoder ticks per revolution. 
    private static final double kMagEncoderConversionFactor = 4096;

    public ArmSystem() {
        initMotorControllers();

        ShuffleboardDisplay.ArmLayout.addDouble("Position", this::getPosition);
        ShuffleboardDisplay.ArmLayout.addDouble("Arm Angle", this::getPositionAsArmAngle);
        ShuffleboardDisplay.ArmLayout.addDouble("Current", m_motorMaster::getStatorCurrent);
        ShuffleboardDisplay.ArmLayout.addDouble("Voltage", m_motorMaster::getMotorOutputVoltage);
        ShuffleboardDisplay.ArmLayout.addBoolean("Top LS", this::topLimitActivated);
        ShuffleboardDisplay.ArmLayout.addBoolean("Bottom LS", this::bottomLimitActivated);
        //ShuffleboardDisplay.ArmLayout.addBoolean("MaxCurrent Tripped", () -> this.m_MaxCurrentTripped);

        // ShuffleboardDisplay.ArmLayout.addDouble("MaxOutput", this::getMaxOutput);
        // ShuffleboardDisplay.ArmLayout.addDouble("PidP", this::getPidP);
        // ShuffleboardDisplay.ArmLayout.addDouble("PidI", this::getPidI);
        // ShuffleboardDisplay.ArmLayout.addDouble("PidD", this::getPidD);
        // ShuffleboardDisplay.ArmLayout.addDouble("PidIz", this::getPidIz);
        // ShuffleboardDisplay.ArmLayout.addDouble("AngleUpSpeed", this::getAngleUpSpeed);
        // ShuffleboardDisplay.ArmLayout.addDouble("AngleDownSpeed", this::getAngleDownSpeed);

        m_tunePidP = TuningShuffleboardDisplay.ArmLayout.add("PID P", m_pidP).getEntry();
        m_tunePidI = TuningShuffleboardDisplay.ArmLayout.add("PID I", m_pidI).getEntry();
        m_tunePidD = TuningShuffleboardDisplay.ArmLayout.add("PID D", m_pidD).getEntry();
        m_tunePidIz = TuningShuffleboardDisplay.ArmLayout.add("PID IZ", m_pidIz).getEntry();
        m_tuneMaxOutput = TuningShuffleboardDisplay.ArmLayout.add("Max Output", m_maxOutput).getEntry();
        m_tuneAngleDownSpeed = TuningShuffleboardDisplay.ArmLayout.add("AngleDownSpeed", m_angleDownSpeed).getEntry();
        m_tuneAngleUpSpeed = TuningShuffleboardDisplay.ArmLayout.add("AngleUpSpeed", m_angleUpSpeed).getEntry();
    }

    @Override
    public void periodic() {
        //monitorStatorCurrent();
        updateParametersFromShuffleboard();
    }

    // Resets encoders current position value to 0
    public void resetEncoders() {
        resetEncoders(0);
    }

    public void resetEncoders(double currentPosition) {
        currentPosition = currentPosition * kMagEncoderConversionFactor;
        m_motorMaster.setSelectedSensorPosition(currentPosition, kPIDLoopIdx, kTimeoutMs);
    }

    // Returns current encoder position value 
    public double getPosition() {
        return m_motorMaster.getSelectedSensorPosition(kPIDLoopIdx) / kMagEncoderConversionFactor;
    }

    public double getPositionAsArmAngle() {
        return ((getPosition() / kGearRatio) * 360.) + kLowestArmAngleToGroundDegrees;
    }

    // Set the arm moving, value is voltage%
    public void move(double value) {
        if (value > 0 && topLimitActivated()) { // up
            m_motorMaster.set(0);
            return;
        } else if (value < 0 && bottomLimitActivated()) { // down
            m_motorMaster.set(0);
            resetEncoders(0);
            return;
        }

        m_motorMaster.set(value);
    }
 
    // Make arm move up using predefined up move speed.
    public void moveUp() {
        if (topLimitActivated()) {
            m_motorMaster.set(0);
        } else {
            m_motorMaster.set(m_angleUpSpeed);
        }
    }

    // Make arm move down using predefined down move speed.
    public void moveDown() {
        if (bottomLimitActivated()) {
            m_motorMaster.set(0);
            resetEncoders(0);
        } else {
            m_motorMaster.set(m_angleDownSpeed);
        }
    }

    // Move arm to a specific encoder position. 
    public void moveToPosition(double targetPosition) {
        var encoderPosition = targetPosition * kMagEncoderConversionFactor;
        m_motorMaster.set(ControlMode.Position, encoderPosition);
    }

    // Stop arm motor. 
    public void stop() {
        m_motorMaster.stopMotor();
    }

    // Returns status of the arm's bottom limit switch. 
    public boolean bottomLimitActivated() {
        var value = (m_bottomRightLimitSwitch.get()  ||  !m_bottomLeftLimitSwitch.get());
        if (value) { resetEncoders(0); }
        return value;
    }

    // Returns status of the arm's top limit switch. 
    public boolean topLimitActivated()  {
        return !m_topLimitSwitch.get();
    }
    
    // Switches the motors to brake neutral mode for more resistance. 
    public void switchBrake() {
        m_motorMaster.setNeutralMode(NeutralMode.Brake);
        m_motorSlave.setNeutralMode(NeutralMode.Brake);
    }

    // Switches the motors to coast neutral mode for no resistance. 
    public void switchCoast() {
        m_motorMaster.setNeutralMode(NeutralMode.Coast);
        m_motorSlave.setNeutralMode(NeutralMode.Coast);
    }

    // private void monitorStatorCurrent() {
    //     double currentM = m_motorMaster.getStatorCurrent();
    //     //double currentS = m_motorSlave.getStatorCurrent();

    //     if (currentM > kMaxCurrent) { // || currentS > kMaxCurrent) {
    //         stop();
    //         m_MaxCurrentTripped = true;
    //     }
    // }

    private void updateParametersFromShuffleboard() {
        if (m_tunePidP.getDouble(m_pidP) != m_pidP) { 
            m_pidP = m_tunePidP.getDouble(m_pidP);
            m_motorMaster.config_kP(kPIDLoopIdx, m_pidP, kTimeoutMs); 
        }

        if (m_tunePidI.getDouble(m_pidI) != m_pidI) { 
            m_pidI = m_tunePidI.getDouble(m_pidI);
            m_motorMaster.config_kI(kPIDLoopIdx, m_pidI, kTimeoutMs); 
        }

        if (m_tunePidD.getDouble(m_pidD) != m_pidD) { 
            m_pidD = m_tunePidD.getDouble(m_pidD);
            m_motorMaster.config_kD(kPIDLoopIdx, m_pidD, kTimeoutMs); 
        }

        if (m_tunePidIz.getDouble(m_pidIz) != m_pidIz) { 
            m_pidIz = m_tunePidIz.getDouble(m_pidIz);
            m_motorMaster.config_IntegralZone(kPIDLoopIdx, m_pidIz, kTimeoutMs); 
        }

        // m_tuneMaxOutput
        if (m_tuneMaxOutput.getDouble(m_maxOutput) != m_maxOutput) { 
            m_maxOutput = m_tuneMaxOutput.getDouble(m_maxOutput);
        }

        // m_tuneAngleDownSpeed
        if (m_tuneAngleDownSpeed.getDouble(m_angleDownSpeed) != m_angleDownSpeed) { 
            m_angleDownSpeed = m_tuneAngleDownSpeed.getDouble(m_angleDownSpeed);
        }

        // m_tuneAngleUpSpeed
        if (m_tuneAngleUpSpeed.getDouble(m_angleUpSpeed) != m_angleUpSpeed) { 
            m_angleUpSpeed = m_tuneAngleUpSpeed.getDouble(m_angleUpSpeed);
        }
    }
    
    // Initializes the motor controllers 
    private void initMotorControllers() {
        // Factory Default all hardware to prevent unexpected behaviour
        m_motorMaster.configFactoryDefault();
        m_motorSlave.configFactoryDefault();

        m_motorSlave.follow(m_motorMaster, FollowerType.PercentOutput);

        // Set based on what direction you want forward/positive to be.
        // This does not affect sensor phase.
        m_motorMaster.setInverted(kMotorInvert);
        m_motorSlave.setInverted(InvertType.FollowMaster);

        // Config the sensor used for Primary PID and sensor direction
        m_motorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);

        // Ensure sensor is positive when output is positive
        m_motorMaster.setSensorPhase(kSensorPhase);

        // Config the peak and nominal outputs, 12V means full
        m_motorMaster.configNominalOutputForward(0, kTimeoutMs);
        m_motorMaster.configNominalOutputReverse(0, kTimeoutMs);
        m_motorMaster.configPeakOutputForward(m_maxOutput, kTimeoutMs);
        m_motorMaster.configPeakOutputReverse(-1 * m_maxOutput, kTimeoutMs);

        // // Config the allowable closed-loop error, Closed-Loop output will be
        // // neutral within this range. See Table in Section 17.2.1 for native
        // // units per rotation.
        // motor.configAllowableClosedloopError(0, kPIDLoopIdx, kTimeoutMs);

        // FeedForward corresponds basically to a minimum ouput required to overcome
        // static resistance/friction

        // Config Position Closed Loop gains in slot0, typically kF stays zero.
        m_motorMaster.config_kF(kPIDLoopIdx, 0, kTimeoutMs);
        m_motorMaster.config_kP(kPIDLoopIdx, m_pidP, kTimeoutMs);
        m_motorMaster.config_kI(kPIDLoopIdx, m_pidI, kTimeoutMs);
        m_motorMaster.config_IntegralZone(kPIDLoopIdx, m_pidIz, kTimeoutMs);
        m_motorMaster.config_kD(kPIDLoopIdx, m_pidD, kTimeoutMs);

        // motor.configMotionAcceleration(1000, kTimeoutMs);
        // motor.configMotionCruiseVelocity(1000, kTimeoutMs);

        // // Grab the 360 degree position of the MagEncoder's absolute
        // // position, and intitally set the relative sensor to match.
        // int absolutePosition = motor.getSensorCollection().getPulseWidthPosition();

        // // Mask out overflows, keep bottom 12 bits */
        // absolutePosition &= 0xFFF;
        // if (kSensorPhase) { absolutePosition *= -1; }
        // if (kMotorInvert) { absolutePosition *= -1; }

        // // Set the quadrature (relative) sensor to match absolute
        // motor.setSelectedSensorPosition(absolutePosition, kPIDLoopIdx, kTimeoutMs);
    }
}
