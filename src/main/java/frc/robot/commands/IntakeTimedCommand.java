package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSystem;

public class IntakeTimedCommand extends Command {

    private final IntakeSystem m_intake;
    
    private double m_delay;
    private double m_speed;
    private Timer m_timer;
    private boolean m_timerIsStarted = false;

    public IntakeTimedCommand(double speed, double delay, IntakeSystem intake) {
        m_intake = intake;
        m_speed = speed;
        m_delay = delay;
        m_timer = new Timer();
        addRequirements(this.m_intake);
    }

    @Override
    public void initialize() {
        m_timerIsStarted = false;
    }

    @Override
    public void execute() {
        startTimer();
        m_intake.move(m_speed);
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.noteCaptured = false;
        m_timer.stop();
        m_timerIsStarted = false;
        m_intake.stop();
    }

    @Override
    public boolean isFinished() {
        return m_timer.get() > m_delay;
    }

    private void startTimer() {
        if (!m_timerIsStarted) {
            m_timer.reset();
            m_timer.start();
            m_timerIsStarted = true;
        }
    }
}