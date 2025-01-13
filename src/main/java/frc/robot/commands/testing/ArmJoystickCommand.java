package frc.robot.commands.testing;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmSystem;

public class ArmJoystickCommand extends Command {

    private ArmSystem m_arm;
    
    private DoubleSupplier m_controlValue;

    public ArmJoystickCommand(DoubleSupplier controlValue, ArmSystem arm) {

        m_arm = arm;
        m_controlValue = controlValue;
        addRequirements(m_arm);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        var forward = m_controlValue.getAsDouble();
        m_arm.move(forward);
    }

    @Override
    public void end(boolean interrupted) {
        m_arm.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
