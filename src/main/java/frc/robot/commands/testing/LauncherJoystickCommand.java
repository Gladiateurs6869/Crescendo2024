package frc.robot.commands.testing;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LauncherSystem;

public class LauncherJoystickCommand extends Command {

    private LauncherSystem m_launcher;
    
    private DoubleSupplier m_controlValue;

    public LauncherJoystickCommand(DoubleSupplier controlValue, LauncherSystem launcher) {

        m_launcher = launcher;
        m_controlValue = controlValue;
        addRequirements(m_launcher);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        var forward = m_controlValue.getAsDouble();
        m_launcher.move(forward);
    }

    @Override
    public void end(boolean interrupted) {
        m_launcher.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}