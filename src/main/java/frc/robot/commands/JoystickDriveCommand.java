// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveBase;

public class JoystickDriveCommand extends Command {

    private DriveBase m_driveBase;
    
    private DoubleSupplier m_forward;
    private DoubleSupplier m_rotation;

    public JoystickDriveCommand(DoubleSupplier forward, DoubleSupplier rotation, DriveBase base) {

        m_driveBase = base;
        m_forward = forward;
        m_rotation = rotation;
        addRequirements(m_driveBase);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        var forward = m_forward.getAsDouble();
        var rotation = m_rotation.getAsDouble();
        m_driveBase.mJoystickDrive(forward, rotation);
    }

    @Override
    public void end(boolean interrupted) {
        m_driveBase.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
