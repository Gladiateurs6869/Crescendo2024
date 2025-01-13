// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.ShuffleboardDisplay;

public class IntakeSystem extends SubsystemBase {

    private final WPI_TalonSRX m_motor = new WPI_TalonSRX(Constants.MotorPort.IntakeMotorTalon);
    private final DigitalInput m_limitSwitch = new DigitalInput(Constants.DigitalInputPort.IntakeNoteSwitch);

    //  Change the I2C port below to match the connection of your color sensor
    private final I2C.Port i2cPort = I2C.Port.kOnboard;

    // A Rev Color Sensor V3 object is constructed with an I2C port as a
    // parameter. The device will be automatically initialized with default
    // parameters.
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

    private static final double kMaxOutput = 0.75;

    private final ColorMatch m_colorMatcher = new ColorMatch();
    private Color m_lastDetectedColor = new Color(); 
    private ColorMatchResult m_lastColorMatch = new ColorMatchResult(m_lastDetectedColor, 0);

    private static final Color kNoteTarget = new Color(0.6, 0.35, 0.);  // Note Orange
    private static final Color kBackgroundTarget1 = new Color(0.25, 0.35, 0.5); // Some value of gray
    // private static final Color kBackgroundTarget2 = new Color(1., 0., 0.); // Red
    // private static final Color kBackgroundTarget3 = new Color(0., 1., 0.); // Green

    // Set to zero to skip waiting for confirmation, set to nonzero to wait and
    // report to DS if action fails.
    private static final int kTimeoutMs = 30;

    // Choose based on what direction you want to be positive,
    // this does not affect motor invert.
    private static final boolean kMotorInvert = false;

    public boolean noteCaptured = false;
    private double m_currentSpeed = 0;
    
    public double getCurrentSpeed() {
        return m_currentSpeed;
    }

    public IntakeSystem() {
        initTalon();
        m_colorMatcher.addColorMatch(kNoteTarget);
        m_colorMatcher.addColorMatch(kBackgroundTarget1);
        // m_colorMatcher.addColorMatch(kBackgroundTarget2);
        // m_colorMatcher.addColorMatch(kBackgroundTarget3);

        ShuffleboardDisplay.IntakeLayout.addDouble("Current", m_motor::getStatorCurrent);
        ShuffleboardDisplay.IntakeLayout.addDouble("Voltage", m_motor::getMotorOutputVoltage);
        ShuffleboardDisplay.IntakeLayout.addDouble("Red", () -> m_lastDetectedColor.red);
        ShuffleboardDisplay.IntakeLayout.addDouble("Green", () -> m_lastDetectedColor.green);
        ShuffleboardDisplay.IntakeLayout.addDouble("Blue", () -> m_lastDetectedColor.blue);
        //ShuffleboardDisplay.IntakeLayout.addDouble("IR", m_colorSensor::getIR);
        ShuffleboardDisplay.IntakeLayout.addBoolean("Note Detected", this::noteColorDetected);
        ShuffleboardDisplay.IntakeLayout.addBoolean("Note LS", this::limitSwitchActivated);
    }

    @Override
    public void periodic() {
        m_lastDetectedColor = m_colorSensor.getColor();
        m_lastColorMatch = m_colorMatcher.matchClosestColor(m_lastDetectedColor);
    }

    public boolean limitSwitchActivated() {
        return !m_limitSwitch.get();
    }

    public void switchBrake() {
        m_motor.setNeutralMode(NeutralMode.Brake);
    }

    // public void switchCoast() {
    //     m_motor.setNeutralMode(NeutralMode.Coast);
    // }

    public void move(double speed) {
        m_currentSpeed = speed;
        m_motor.set(speed);
    }

    public void stop() {
        m_motor.stopMotor();
        m_currentSpeed = 0;
    }

    public boolean noteColorDetected() {
        return (m_lastColorMatch.color == kNoteTarget);
    }

    private void initTalon() {
        // Factory Default all hardware to prevent unexpected behaviour
        m_motor.configFactoryDefault();

        // Set based on what direction you want forward/positive to be.
        // This does not affect sensor phase.
        m_motor.setInverted(kMotorInvert);

        // Config the peak and nominal outputs, 12V means full
        m_motor.configNominalOutputForward(0, kTimeoutMs);
        m_motor.configNominalOutputReverse(0, kTimeoutMs);
        m_motor.configPeakOutputForward(kMaxOutput, kTimeoutMs);
        m_motor.configPeakOutputReverse(-1 * kMaxOutput, kTimeoutMs);

        switchBrake();
    }
}
