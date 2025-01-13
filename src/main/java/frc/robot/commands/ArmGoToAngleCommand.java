package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmSystem;
import frc.robot.subsystems.LimitSwitchPointer;
import frc.robot.util.MathHelp;

public class ArmGoToAngleCommand extends Command {

    private ArmSystem m_arm;
    private LimitSwitchPointer m_limitSwitch;

    private boolean m_areWeGoingUp;
    private double m_angle;

    private static final double kAngleTolerance = 0.1;  // 1 degree = 0.03

    public ArmGoToAngleCommand(double angle, ArmSystem arm) {
        m_angle = angle;
        m_arm = arm;
        addRequirements(m_arm);
    }

    @Override
    public void initialize() {
        m_areWeGoingUp = (m_angle - m_arm.getPosition()) > 0;
        m_limitSwitch = m_areWeGoingUp ? m_arm::topLimitActivated : m_arm::bottomLimitActivated;
    }

    @Override
    public void execute() {
        m_arm.moveToPosition(m_angle);
    }

    @Override
    public void end(boolean interrupted) {
        // in case we are stopping because we reach the limit switch.
        // stop 
        if (m_limitSwitch.isActivated()) {
            m_arm.stop();
        }
    }

    @Override
    public boolean isFinished() {
        var error = Math.abs(m_angle - m_arm.getPosition());
        var isFinished = MathHelp.isZero(error, kAngleTolerance);
        return isFinished || m_limitSwitch.isActivated();
    }
}
