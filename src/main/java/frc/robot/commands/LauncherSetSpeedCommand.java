package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LauncherSystem;

public class LauncherSetSpeedCommand extends Command {

    public enum LauncherTarget {
        Amp,
        Speaker, 
        SpeakerAway,
        SpeakerAngleAuto,
    }

    private LauncherSystem m_launcher;
    
    // private double m_targetRpm;
    
    // // Launcher Constants
    // // Speaker launch calculated required speed approx 8.7 m/s => 2200 rpm *= 1.20 => 2650
    // public static final double kLauncherSpeakerSpeed = 2650;         // rpm
    // // Speaker launch from away in auto calculated required speed approx 10.5 m/s => 2500rpm *= 1.20 => 2650
    // public static final double kLauncherSpeakerAutoAwaySpeed = 3000; // rpm
    // public static final double kLauncherAmpSpeed = 500;       
    // public static final double RpmTolerance = 300.;   // +/- rpm

    private LauncherTarget m_target;
    private double m_delay;
    private Timer m_timer;
    private boolean m_timerIsStarted = false;
    private static final double kLauncherSpeakerValue = 0.925;
    private static final double kLauncherSpeakerAngleAutoValue = 0.925;
    private static final double kLauncherSpeakerAutoAwayValue = 0.95;
    private static final double kLauncherAmpValue = 0.05;

    public LauncherSetSpeedCommand(LauncherTarget target, LauncherSystem launcher) {
        m_target = target;
        m_launcher = launcher;
        m_timer = new Timer();
        addRequirements(m_launcher);
    }

    @Override
    public void initialize() {
        m_timerIsStarted = false;
    }

    @Override
    public void execute() {
        startTimer();
        if (m_target == LauncherTarget.Speaker) {
            m_launcher.move(kLauncherSpeakerValue);
            //m_launcher.setVelocity(kLauncherSpeakerSpeed);
        } else if (m_target == LauncherTarget.SpeakerAngleAuto) {
            m_launcher.move(kLauncherSpeakerAngleAutoValue); 
        } else if (m_target == LauncherTarget.SpeakerAway) {
            m_launcher.move(kLauncherSpeakerAutoAwayValue);
            //m_launcher.setVelocity(kLauncherSpeakerAutoAwaySpeed);
        } else if (m_target == LauncherTarget.Amp) {
            m_launcher.move(kLauncherAmpValue);
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_timer.stop();
        m_timerIsStarted = false;
    }

    @Override
    public boolean isFinished() {
        return m_timer.get() > m_delay;
        // var error = Math.abs(m_targetRpm - m_launcher.getVelocity());
        // var isFinished = MathHelp.isZero(error, RpmTolerance); 
        // return isFinished;
    }

    private void startTimer() {
        if (!m_timerIsStarted) {
            if (m_target == LauncherTarget.Speaker) {
                m_delay = 0.5;
            } else if (m_target == LauncherTarget.SpeakerAway) {
                m_delay = 0.5;
            } else if (m_target == LauncherTarget.Amp) {
                m_delay = 0.5;
            }

            m_timer.reset();
            m_timer.start();
            m_timerIsStarted = true;
        }
    }
}
