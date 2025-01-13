// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.GamePieceCommandFactory;
import frc.robot.commands.JoystickDriveCommand;
import frc.robot.subsystems.DriveBase;
import frc.robot.subsystems.IntakeSystem;
import frc.robot.subsystems.LauncherSystem;
import frc.robot.util.GamepadAxisButton;
import frc.robot.subsystems.ArmSystem;
import frc.robot.subsystems.CameraSystem;
import frc.robot.commands.autonomous.*;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;


/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

    // The robot's subsystems and commands are defined here...
    private DriveBase m_drivebase = new DriveBase();
    private LauncherSystem m_launcher = new LauncherSystem();
    private IntakeSystem m_intake = new IntakeSystem();
    private ArmSystem m_arm = new ArmSystem();
    private CameraSystem m_cameras = new CameraSystem();
    
    private Joystick m_joystick = new Joystick(Constants.ControllerPort.Joystick);
    private XboxController m_manette = new XboxController(Constants.ControllerPort.Manette);

    private GamePieceCommandFactory m_gpCommandFactory = new GamePieceCommandFactory(m_arm, m_intake, m_launcher, m_cameras);
    private AutonomousCommandFactory m_autoCommandFactory = new AutonomousCommandFactory(m_drivebase, m_gpCommandFactory);

    // The container for the robot. Contains subsystems, OI devices, and commands.
    public RobotContainer() {

        CameraServer.startAutomaticCapture();
        CameraServer.startAutomaticCapture();
        //CameraServer.startAutomaticCapture();

        // Configure the trigger bindings
        m_drivebase.setDefaultCommand(new JoystickDriveCommand(()->m_joystick.getY(), ()->m_joystick.getX(), m_drivebase));
        // m_launcher.setDefaultCommand(new LauncherJoystickCommand(()->m_joystick.getY(), m_launcher));
        // m_intake.setDefaultCommand(new IntakeJoystickCommand(() -> m_joystick.getX(), m_intake));
        //m_arm.setDefaultCommand(new ArmFreeMoveCommand(()->m_manette.getLeftY(), m_arm));
        //m_chooser.setDefaultOption("TestCommand", new TestCommand(m_drivebase));

        ShuffleboardDisplay.CommandLayout.add(CommandScheduler.getInstance());
        configureBindings();
    }

    private void configureBindings() {

        // TESTING COMMAND MAPPING
        // *************************************************************************************************************************
        new JoystickButton(m_manette, Constants.Xbox360ButtonPort.YellowY).onTrue(m_gpCommandFactory.launchNoteToSpeaker());
        new JoystickButton(m_manette, Constants.Xbox360ButtonPort.RedB).onTrue(m_gpCommandFactory.intakeBackoff());
        new JoystickButton(m_manette, Constants.Xbox360ButtonPort.BlueX).onTrue(m_gpCommandFactory.intakeReject());
        new JoystickButton(m_manette, Constants.Xbox360ButtonPort.GreenA).onTrue(m_gpCommandFactory.intakeGrabNote());

        new JoystickButton(m_manette, Constants.Xbox360ButtonPort.LeftBumper).onTrue(m_gpCommandFactory.launcherStop());
        new JoystickButton(m_manette, Constants.Xbox360ButtonPort.RightBumper).onTrue(m_gpCommandFactory.intakeStop());

        // new JoystickButton(m_manette, Constants.Xbox360ButtonPort.LeftTrigger).onTrue(m_gpCommandFactory.launchNoteToAmp());
        // new JoystickButton(m_manette, Constants.Xbox360ButtonPort.RightTrigger).onTrue(m_gpCommandFactory.launchNoteToSpeaker());

        new GamepadAxisButton(this::leftTriggerAxisBoolean).onTrue(m_gpCommandFactory.intakeStop());
        new GamepadAxisButton(this::rightTriggerAxisBoolean).onTrue(m_gpCommandFactory.intakeGrabNote());

        new POVButton(m_manette, Constants.Xbox360ButtonPort.PovUp, 0).whileTrue(m_gpCommandFactory.armGoUp());
        new POVButton(m_manette, Constants.Xbox360ButtonPort.PovRight, 0).whileTrue(m_gpCommandFactory.armGoToGroundPosition());
        new POVButton(m_manette, Constants.Xbox360ButtonPort.PovDown, 0).whileTrue(m_gpCommandFactory.armGoDown());
        new POVButton(m_manette, Constants.Xbox360ButtonPort.PovLeft, 0).whileTrue(m_gpCommandFactory.armGoToSpeakerPosition());

        new JoystickButton(m_joystick, Constants.JoystickButtonPort.TopLeftHigh).onTrue(m_gpCommandFactory.intakeGrabNote());
        new JoystickButton(m_joystick, Constants.JoystickButtonPort.TopLeftLow).onTrue(m_gpCommandFactory.intakeFeedLauncher());
        new JoystickButton(m_joystick, Constants.JoystickButtonPort.TopRightHigh).onTrue(m_gpCommandFactory.launcherSetForSpeakerLaunch());
        //new JoystickButton(m_joystick, Constants.JoystickButtonPort.TopRightLow).onTrue(m_gpCommandFactory.launcherSetForAmpLaunch());

        // new JoystickButton(m_joystick, Constants.JoystickButtonPort.TriggerThumb).onTrue(m_gpCommandFactory.launcherStop());
        // new JoystickButton(m_joystick, Constants.JoystickButtonPort.TriggerIndex).onTrue(m_gpCommandFactory.intakeStop());

        
        
        // GAME PLAY COMMAND MAPPING
        // *************************************************************************************************************************
        // new JoystickButton(m_manette, Constants.Xbox360ButtonPort.YellowY).onTrue(m_gpCommandFactory.intakeGrabNote());
        // new JoystickButton(m_manette, Constants.Xbox360ButtonPort.RedB).onTrue(m_gpCommandFactory.armGoToGroundPosition());
        // new JoystickButton(m_manette, Constants.Xbox360ButtonPort.BlueX).onTrue(m_gpCommandFactory.intakeReject());
        // new JoystickButton(m_manette, Constants.Xbox360ButtonPort.GreenA).onTrue(m_gpCommandFactory.intakeGrabNote());

        // new JoystickButton(m_manette, Constants.Xbox360ButtonPort.LeftBumper).onTrue(m_gpCommandFactory.launcherStop());
        // new JoystickButton(m_manette, Constants.Xbox360ButtonPort.RightBumper).onTrue(m_gpCommandFactory.intakeStop());

        // new JoystickButton(m_manette, Constants.Xbox360ButtonPort.LeftTrigger).onTrue(m_gpCommandFactory.launchNoteToAmp());
        // new JoystickButton(m_manette, Constants.Xbox360ButtonPort.RightTrigger).onTrue(m_gpCommandFactory.launchNoteToSpeaker());

        // new POVButton(m_manette, Constants.Xbox360ButtonPort.PovUp, 0).whileTrue(m_gpCommandFactory.armGoUp());
        // new POVButton(m_manette, Constants.Xbox360ButtonPort.PovDown, 0).whileTrue(m_gpCommandFactory.armGoDown());
    }

    public Command getAutonomousCommand() {
        SmartDashboard.putString("Automous", "getAutonomousCommand");
        return m_autoCommandFactory.getSelected();
    }

    
  public boolean leftTriggerAxisBoolean(){
    return Math.abs(m_manette.getRawAxis(2)) > .5;
  }

  public boolean rightTriggerAxisBoolean(){
    return Math.abs(m_manette.getRawAxis(3)) > .5;
  }
}