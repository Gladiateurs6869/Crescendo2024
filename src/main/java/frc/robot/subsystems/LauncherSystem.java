// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.ShuffleboardDisplay;

public class LauncherSystem extends SubsystemBase {

    private CANSparkMax m_motor = new CANSparkMax(Constants.MotorPort.LaunchMotorNeo, MotorType.kBrushless);
    private SparkPIDController m_pidController;
    private RelativeEncoder m_encoder;
    
    // Choose based on what direction you want to be positive,
    // this does not affect motor invert.
    private static final boolean kMotorInvert = false;

    // Position Launches out. 
    private static final double kMaxOutput = 0.95;

    // No reason to have turing in the wrong direction 
    private static final double kMinOutput = 0;

    // PID coefficients
    private static final double kP = 0.001; 
    private static final double kI = 0.;
    private static final double kD = 0.; 
    private static final double kIz = 0.; 
    private static final double kFF = 0.; 

    // Set a Maximum Rpm for security. 
    // Neo maximum free RPM is approx 5700 
    // We probably want approximately 2500-3000 rpm for launching note. 
    private static final double kMaxRpm = 4000;

    // // Increase/decrease speed delta for testing. 
    // private static final double kVelocityChangeDelta = 100;    // rpm

    public LauncherSystem() {
        initNeo();
        
        ShuffleboardDisplay.LauncherLayout.addDouble("Velocity (RPM)", m_encoder::getVelocity);
        ShuffleboardDisplay.LauncherLayout.addDouble("Max Velocity (RPM)", () -> kMaxRpm);
        ShuffleboardDisplay.LauncherLayout.addDouble("Output", m_motor::getAppliedOutput);
        ShuffleboardDisplay.LauncherLayout.addDouble("Max Output", () -> kMaxOutput);
    }

    @Override
    public void periodic() {
    }

    public void resetEncoders() {
        m_encoder.setPosition(0);
    }

    public double getPosition() {
        return m_encoder.getPosition();
    }

    public double getVelocity() {
        return m_encoder.getVelocity();
    }

    // public void changeVelocity(VelocityDeltaType direction) {
    //     var currentVelocity = getVelocity();
    //     var velocityDelta = kVelocityChangeDelta;
    //     if(direction == VelocityDeltaType.Negative)  {
    //         velocityDelta = -1 * velocityDelta;
    //     }

    //     setVelocity(currentVelocity + velocityDelta);
    // }

    // public void moveToPosition(double targetPosition)
    // {
    //     m_pidController.setReference(targetPosition, CANSparkMax.ControlType.kPosition);
    // }

    public void setVelocity(double targetVelocity) {
        
        // if (MathHelp.isZero(targetVelocity, kVelocityChangeDelta/2.)) {
        //     stop();
        // } else {
            if (Math.abs(targetVelocity) > kMaxRpm) {
                targetVelocity = kMaxRpm * Math.signum((targetVelocity));
            }
           m_pidController.setReference(targetVelocity, CANSparkMax.ControlType.kVelocity);
        //}
    }

    public void move(double speed) {
        m_motor.set(speed);
    }

    public void stop() {
        m_motor.stopMotor();
    }

    private void initNeo()
    {
        // The restoreFactoryDefaults method can be used to reset the configuration parameters
        // in the SPARK MAX to their factory default state. If no argument is passed, these
        // parameters will not persist between power cycles
        m_motor.restoreFactoryDefaults();

        m_motor.setInverted(kMotorInvert);
        
        m_motor.setIdleMode(IdleMode.kBrake);        
        
        // In order to use PID functionality for a controller, a SparkMaxPIDController object
        // is constructed by calling the getPIDController() method on an existing
        // CANSparkMax object
        m_pidController = m_motor.getPIDController();

        // Encoder object created to display position values
        m_encoder = m_motor.getEncoder();

        // set PID coefficients
        m_pidController.setP(kP);
        m_pidController.setI(kI);
        m_pidController.setD(kD);
        m_pidController.setIZone(kIz);
        m_pidController.setFF(kFF);
        m_pidController.setOutputRange(kMinOutput, kMaxOutput);        
    }
}
