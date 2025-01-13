// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveBase;
import frc.robot.util.MathHelp;

public class DrivingTurnCommand extends Command {

    private DriveBase m_driveBase;

    private double m_degreesToTurn;
    private double m_targetDegrees;
    private double m_speed;

    private static final double kP = 0.1;
    private static final double kAngleTolerance = 5;
    private static final double kMinSpeed = 0.25;
    private static final double kMaxSpeed = 0.5;

    public DrivingTurnCommand(double speed, double degreesTurn, DriveBase base) {
        m_driveBase = base;
        m_degreesToTurn = degreesTurn;
        m_speed = speed;
        m_targetDegrees = m_driveBase.getCurrentAngle() + m_degreesToTurn;

        addRequirements(m_driveBase);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        m_driveBase.mJoystickDrive(0, getTargetSpeed());
    }

    @Override
    public void end(boolean interrupted) {
        m_driveBase.stop();
    }

    @Override
    public boolean isFinished() {
        return (MathHelp.isZero(m_driveBase.getCurrentAngle() - m_targetDegrees, kAngleTolerance));
    }

    private double getTargetSpeed() {
        double currentAngle = m_driveBase.getCurrentAngle();
        var error = m_targetDegrees - currentAngle;
        var output = error * kP * m_speed;
        if (Math.abs(output) < kMinSpeed) {
            output = kMinSpeed * Math.signum(output);
        }
        if (Math.abs(output) > kMaxSpeed) {
            output = kMaxSpeed * Math.signum(output);
        }
        return output;
    }
}
