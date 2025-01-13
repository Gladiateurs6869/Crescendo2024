// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.ShuffleboardDisplay;

public class DriveBase extends SubsystemBase {

    private final WPI_TalonSRX m_leftMaster = new WPI_TalonSRX(Constants.MotorPort.DriveLeftMaster);
    private final WPI_VictorSPX m_leftSlave = new WPI_VictorSPX(Constants.MotorPort.DriveLeftSlave);

    private final WPI_TalonSRX m_rightMaster = new WPI_TalonSRX(Constants.MotorPort.DriveRightMaster);
    private final WPI_VictorSPX m_rightSlave = new WPI_VictorSPX(Constants.MotorPort.DriveRightSlave);

    private final DifferentialDrive m_driveBase = new DifferentialDrive(m_leftMaster, m_rightMaster);

    // private final MotorControllerGroup m_mcgRight = new MotorControllerGroup(m_rightMaster, m_rightSlave);
    // private final MotorControllerGroup m_mcgLeft = new MotorControllerGroup(m_leftMaster, m_leftSlave);
    // private final DifferentialDrive m_driveBase = new DifferentialDrive(m_mcgLeft, m_mcgRight);

    private ADXRS450_Gyro m_gyro;

    private double m_currentForward;
    private double m_currentRotation;

    /** Creates a new DriveBase. */
    public DriveBase() {
        m_gyro = new ADXRS450_Gyro();
        m_gyro.calibrate();

        initMotorControllers();

        ShuffleboardDisplay.DriveLayout.addNumber("Forward", () -> m_currentForward);
        ShuffleboardDisplay.DriveLayout.addNumber("Rotate", () -> m_currentRotation);
        ShuffleboardDisplay.DriveLayout.addNumber("Gyro Angle", m_gyro::getAngle);
    }

    @Override
    public void periodic() {
    }

    public double getCurrentAngle() {
        return m_gyro.getAngle();
    }

    public void mJoystickDrive(double forwardSpeed, double rotationSpeed) {
        m_currentForward = forwardSpeed;
        m_currentRotation = rotationSpeed;
        m_driveBase.arcadeDrive(forwardSpeed, rotationSpeed);
    }

    public void stop() {
        m_driveBase.stopMotor();
     }

     private void initMotorControllers() {
        m_leftMaster.configFactoryDefault();
        m_leftSlave.configFactoryDefault();
        m_rightMaster.configFactoryDefault();
        m_rightSlave.configFactoryDefault();

        m_leftSlave.follow(m_leftMaster);
        m_leftMaster.setNeutralMode(NeutralMode.Brake);
        m_leftSlave.setNeutralMode(NeutralMode.Brake);

        m_leftMaster.setInverted(true);
        m_leftSlave.setInverted(InvertType.FollowMaster);


        m_rightSlave.follow(m_rightMaster);
        m_rightMaster.setNeutralMode(NeutralMode.Brake);
        m_rightSlave.setNeutralMode(NeutralMode.Brake);
        m_rightMaster.setInverted(false);
        m_rightSlave.setInverted(InvertType.FollowMaster);
     }

    //  private void initMotorControllersLegacy() {
    //     m_rightMaster.setNeutralMode(NeutralMode.Brake);
    //     m_leftMaster.setNeutralMode(NeutralMode.Brake);
    //     m_rightSlave.setNeutralMode(NeutralMode.Brake);
    //     m_leftSlave.setNeutralMode(NeutralMode.Brake);
    //     m_mcgLeft.setInverted(true);
    //  }
}