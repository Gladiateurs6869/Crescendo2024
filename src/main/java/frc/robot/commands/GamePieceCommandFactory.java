package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.LauncherSetSpeedCommand.LauncherTarget;
import frc.robot.subsystems.ArmSystem;
import frc.robot.subsystems.CameraSystem;
import frc.robot.subsystems.IntakeSystem;
import frc.robot.subsystems.LauncherSystem;

public class GamePieceCommandFactory {
    
    private final ArmSystem m_arm;
    private final IntakeSystem m_intake;
    private final LauncherSystem m_launcher;
    private final CameraSystem m_cameras;

    // Intake Constants 
    public static final double kIntakeFeedSpeed = 0.80;
    public static final double kIntakeFeedTime = 0.4;

    public static final double kIntakeFeedAmpSpeed = 0.2;
    public static final double kIntakeFeedAmpTime = 0.5;

    public static final double kIntakeBackOffSpeed = -0.20;
    public static final double kIntakeBackOffTime = 0.3;

    public static final double kIntakeRejectSpeed = -0.80;
    public static final double kIntakeRejectTime = 0.5;

    public GamePieceCommandFactory(ArmSystem arm, IntakeSystem intake, LauncherSystem launcher, CameraSystem cameras) {
        this.m_arm = arm;
        this.m_intake = intake;
        this.m_launcher = launcher;
        this.m_cameras = cameras;

        // PilotShuffleboardLayout.PREP_LAYOUT.add(prepareForCone());
        // PilotShuffleboardLayout.CMD_LAYOUT.addString("Mode repos", () -> brake ? "FREIN" : "LIBRE");
    } 

    public IntakeSystem getIntake() { 
        return m_intake;
    }

    public Command armPositionResetAtParked() {
        // Set initial encoder position for what should be the arm at the top starting position. 
        return Commands.runOnce(() -> m_arm.resetEncoders(ArmSystem.kInitialParkedAngle))
            .withName("testArmPositionResetAtParked");
    }

    public Command testArmPositionReset() {
        return armGoDown()
            .andThen(armResetEncoderPosition())
            .withName("testArmPositionReset");
    }

    /* ****************************************************
    // Complex Commands
    ***************************************************** */
    // public Command launchNoteToSpeaker() {
    //     return armGoToSpeakerPosition()
    //         .alongWith(launcherSetForSpeakerLaunch())
    //         .andThen(intakeFeedLauncher())
    //         .andThen(launcherStop())
    //         .andThen(m_cameras.switchCameraIntakeSide())
    //         .withName("launchToSpeaker");
    // }
    
    public Command launchNoteToSpeaker() {
        return launcherSetForSpeakerLaunch()
            .andThen(intakeFeedLauncher())
            .andThen(launcherStop())
            .andThen(m_cameras.switchCameraIntakeSide())
            .withName("launchToSpeaker");
    }

        
    public Command launchNoteToSpeakerAngle() {
        return launcherSetForAutoAngle()
            .andThen(intakeFeedLauncher())
            .andThen(launcherStop())
            .andThen(m_cameras.switchCameraIntakeSide())
            .withName("launchToSpeaker");
    }

    public Command launchNoteToSpeakerAutoAway() {
        return launcherSetForSpeakerAutoAwayLaunch()
            .andThen(intakeFeedLauncher())
            .andThen(launcherStop())
            .andThen(m_cameras.switchCameraIntakeSide())
            .withName("launchNoteToSpeakerAutoAway");
    }

    // public Command launchNoteToAmp() {
    //     return armGoToAmpPosition()
    //         .andThen(intakeShootToAmp())
    //         .andThen(launcherStop())
    //         .andThen(m_cameras.switchCameraIntakeSide())
    //         .withName("launchToSpeaker");
    // }

    
    public Command launchNoteToAmp() {
        return launcherSetForAmpLaunch()
            .alongWith(intakeShootToAmp())
            .andThen(launcherStop())
            .andThen(m_cameras.switchCameraIntakeSide())
            .withName("launchToSpeaker");
    }

    // public Command launchNoteToAmp() {
    //     return intakeShootToAmp()
    //         .andThen(launcherStop())
    //         .andThen(m_cameras.switchCameraIntakeSide())
    //         .withName("launchToSpeaker");
    // }

    public Command grabNoteFromGround() {
        return armGoToGroundPosition()
            .alongWith(intakeGrabNote())
            .andThen(m_cameras.switchCameraLaunchSide())
            .withName("granNoteFromGround");
    }

    /* ****************************************************
    // Intake Commands
    ***************************************************** */
    public Command intakeFeedLauncher() {
        return new IntakeTimedCommand(kIntakeFeedSpeed, kIntakeFeedTime, m_intake)
            .withName("intakeFeedLauncher");
    }

    public Command intakeGrabNote() {
        return new IntakeGrabCommand(m_intake)
            .andThen(new IntakeTimedCommand(0.6, 0.33, m_intake))
            .withName("intakeGrabNote");
    }

    public Command intakeShootToAmp() {
        return new IntakeTimedCommand(kIntakeFeedAmpSpeed, kIntakeFeedAmpTime, m_intake)
            .withName("intakeShootToAmp"); 
    }

    public Command intakeReject() {
        if (Math.abs(m_intake.getCurrentSpeed()) > 0.1) {
            return intakeStop();
        }
        return new IntakeTimedCommand(kIntakeRejectSpeed, kIntakeRejectTime, m_intake)
            .withName("intakeReject");
    }

    // command to make sure the intake backs off just a little to ensure the launcher can accelerate before shooting the note. 
    public Command intakeBackoff() {
        return new IntakeTimedCommand(kIntakeBackOffSpeed, kIntakeBackOffTime, m_intake)
            .withName("intakeReject");
    }

    public Command intakeStop() {
        return Commands.runOnce(() -> { m_intake.stop(); })
            .withName("intakeStop");
    }

    /* ****************************************************
    // Launcher Commands
    ***************************************************** */
    public Command launcherSetForSpeakerLaunch() {
        return new LauncherSetSpeedCommand(LauncherTarget.Speaker, m_launcher)
            .withName("setForSpeakerLaunch");
    }

    public Command launcherSetForSpeakerAutoAwayLaunch() {
        return new LauncherSetSpeedCommand(LauncherTarget.SpeakerAway, m_launcher)
            .withName("launcherSetForSpeakerAutoAwayLaunch");
    }
    
    public Command launcherSetForAmpLaunch() {
        return new LauncherSetSpeedCommand(LauncherTarget.Amp, m_launcher)
            .withName("setForAmpLaunch");
    }

    public Command launcherSetForAutoAngle() {
        return new LauncherSetSpeedCommand(LauncherTarget.SpeakerAngleAuto, m_launcher)
            .withName("launcherSetForAutoAngle");
    }

    public Command launcherStop() {
        return Commands.runOnce(() -> m_launcher.stop(), m_launcher)
            .withName("launcherStop");
    }

    /* ****************************************************
    // Arm Commands
    // ***************************************************** */
    // public Command armGoToPosition(double position) {
    //     if (position < ArmSystem.kMinArmAngle) { position = ArmSystem.kMinArmAngle; };
    //     if (position > ArmSystem.kMaxArmAngle) { position = ArmSystem.kMaxArmAngle; };
    //     return new ArmGoToAngleCommand(position, m_arm)
    //         .withName("armGoToPosition");
    // }

    public Command armGoToSpeakerAutoAwayPosition() {
        return new ArmGoToAngleCommand(ArmSystem.kSpeakerAutoAwayArmAngle, m_arm)
            .withName("armGoToSpeakerAutoAwayPosition");
    }

    public Command armGoToAmpPosition() {
        return new ArmGoToAngleCommand(ArmSystem.kAmpArmAngle, m_arm)
            .withName("armGoToAmpPosition");
    }

    // public Command armGoToChainPosition() {
    //     return new ArmGoToAngleCommand(kChainArmAngle, m_arm)
    //         .withName("armGoToChainPosition");
    // }

    public Command armGoToGroundPosition() {
        return new ArmGoToAngleCommand(ArmSystem.kGroundArmAngle, m_arm)
            .withName("armGoToGroundPosition");
    }

    public Command armGoToIntermediateSpeakerPosition() {
        return new ArmGoToAngleCommand(ArmSystem.kSpeakerInterArmAngle, m_arm)
            .withName("armGoToSpeakerPosition");
    }

    public Command armGoToSpeakerPosition() {
        return new ArmGoToAngleCommand(ArmSystem.kSpeakerArmAngle, m_arm)
            .withName("armGoToSpeakerPosition");
    }

    public Command armGoToSpeakerAnglePosition() {
        return new ArmGoToAngleCommand(ArmSystem.kSpeakerArmAngleSides, m_arm)
            .withName("armGoToSpeakerAnglePosition");
    }

    // public Command armGoToReleasePosition() {
    //     return Commands.runOnce(() -> m_arm.resetEncoders(), m_arm)
    //         .andThen(new ArmGoToAngleCommand(kReleaseArmAngle, m_arm))
    //         .withName("armGoToReleasePosition");
    // }

    public Command armGoUp() {
        return new ArmMoveCommand(ArmMoveDirection.Up, m_arm)
            .withName("armGoUp");
    }

    public Command armGoDown() {
        return new ArmMoveCommand(ArmMoveDirection.Down, m_arm)
            .withName("armGoDown");
    }

    public Command armStop() {
        return Commands
                .runOnce(() -> m_arm.stop(), m_arm)
                .withName("armStop");
    }

    public Command armResetEncoderPosition() {
        return Commands.runOnce(() -> m_arm.resetEncoders())
            .withName("armResetEncoderPosition");
    }
}
