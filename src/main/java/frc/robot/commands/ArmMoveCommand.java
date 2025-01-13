package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmSystem;
import frc.robot.subsystems.LimitSwitchPointer;

public class ArmMoveCommand extends Command { 

    private ArmSystem m_arm;
    private LimitSwitchPointer m_topLimitSwitch;
    private LimitSwitchPointer m_bottomLimitSwitch;

    private ArmMoveDirection m_direction;
    private boolean isFinished = false;

    public ArmMoveCommand(ArmMoveDirection direction, ArmSystem arm) {
        m_direction = direction;
        m_arm = arm;
        addRequirements(m_arm);
    }

    @Override
    public void initialize() {
        m_topLimitSwitch = m_arm::topLimitActivated;
        m_bottomLimitSwitch = m_arm::bottomLimitActivated;
        isFinished = false;
    }
   
    @Override
    public void execute() {
        if (m_direction == ArmMoveDirection.Up &&  !m_topLimitSwitch.isActivated()){
            m_arm.moveUp();
        } else if (m_direction == ArmMoveDirection.Down && !m_bottomLimitSwitch.isActivated()) {
            m_arm.moveDown();
        } else {
            isFinished = true;
            m_arm.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_arm.stop();
        isFinished = false;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
