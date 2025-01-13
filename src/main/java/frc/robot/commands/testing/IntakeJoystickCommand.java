package frc.robot.commands.testing;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSystem;

public class IntakeJoystickCommand extends Command {

    private IntakeSystem m_intake;
    
    private DoubleSupplier m_controlValue;

    public IntakeJoystickCommand(DoubleSupplier controlValue, IntakeSystem intake) {

        m_intake = intake;
        m_controlValue = controlValue;
        addRequirements(m_intake);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        var forward = m_controlValue.getAsDouble();
        m_intake.move(forward);
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}