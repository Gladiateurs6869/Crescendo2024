// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveBase;

public class DrivingTimedCommand extends Command {

    private DriveBase m_driveBase;
    
    private double m_time;
    private double m_forwardSpeed;
    private double m_rotateSpeed;
    private boolean m_isFinished = false;
    private Timer m_timer;
    private boolean m_timerIsStarted = false;

    public DrivingTimedCommand(double time, double forwardSpeed, double rotateSpeed, DriveBase base) {
        m_driveBase = base;
        m_time = time;
        m_forwardSpeed = forwardSpeed;
        m_rotateSpeed = rotateSpeed;
        m_timer = new Timer();
        addRequirements(m_driveBase);
    }

    @Override
    public void initialize() {
        m_isFinished = false;
        m_timer.reset();
        m_timerIsStarted = false;
    }

    @Override
    public void execute() {
        StartTimer();
        m_driveBase.mJoystickDrive(m_forwardSpeed, m_rotateSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        m_driveBase.stop();
        m_timer.stop();
        m_timerIsStarted = false;
    }

    @Override
    public boolean isFinished() {
        if (m_timer.get() < m_time) {
            return m_isFinished;
        } else {
            return true;
        }
    }
    
    private void StartTimer() {
        if (!m_timerIsStarted) {
            m_timer.reset();
            m_timer.start();
            m_timerIsStarted = true;
        }
    }
}
