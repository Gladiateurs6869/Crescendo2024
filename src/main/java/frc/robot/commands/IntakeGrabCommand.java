package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSystem;
import frc.robot.subsystems.LimitSwitchPointer;

public class IntakeGrabCommand extends Command {
    
    private final IntakeSystem m_intake;
    private LimitSwitchPointer m_limitSwitch;
    private LimitSwitchPointer m_colorSwitch;
    
    private static final double kIntakeGrabSpeed = 0.65;

    public IntakeGrabCommand(IntakeSystem intake) {
        m_intake = intake;
        addRequirements(m_intake);
    }

    @Override
    public void initialize() {
        m_colorSwitch = m_intake::noteColorDetected;
        m_limitSwitch = m_intake::limitSwitchActivated;
    }

    @Override
    public void execute() {
        m_intake.move(kIntakeGrabSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.stop();
    }

    @Override
    public boolean isFinished() {
        if (m_colorSwitch.isActivated() || m_limitSwitch.isActivated()) {
            m_intake.noteCaptured = true;
            return true;
        }
        return false;
    }
}