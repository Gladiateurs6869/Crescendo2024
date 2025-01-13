package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.ShuffleboardDisplay;
import frc.robot.commands.GamePieceCommandFactory;
import frc.robot.subsystems.DriveBase;

public class AutonomousCommandFactory {

    private final DriveBase m_driveBase;
    private final GamePieceCommandFactory m_gpCommandFactory;
    public final SendableChooser<Command> m_autoChooser = new SendableChooser<>();

    private static final double kAutonomousDriveOutput = -0.65;

    public AutonomousCommandFactory(DriveBase base, GamePieceCommandFactory gpCommandFactory) {
        m_driveBase = base;
        m_gpCommandFactory = gpCommandFactory;

        //autoChooser.addOption("test", test());
        m_autoChooser.setDefaultOption("shootSpeakerDriveOut", shootSpeakerDriveOut());
        //m_autoChooser.addOption("testArmRelease", testArmRelease());
        // m_autoChooser.addOption("testTurn", testTurn());
        m_autoChooser.addOption("testDrive", testDrive());
        m_autoChooser.addOption("shootSpeaker", shootSpeaker());
        m_autoChooser.addOption("shootSpeakerDriveOutCenter", shootSpeakerDriveOutCenter());
        m_autoChooser.addOption("shootSpeakerDriveOutAngleLeft", shootSpeakerDriveOutAngleLeft());
        m_autoChooser.addOption("shootSpeakerDriveOutAngleRight", shootSpeakerDriveOutAngleRight());
        m_autoChooser.addOption("shootSpeakerAngle", shootSpeakerAngle());
        //m_autoChooser.addOption("shootSpeakerDriveOut", shootSpeakerDriveOut());
        // m_autoChooser.addOption("testArmPosition2", testArmPosition2());
        // m_autoChooser.addOption("testArmPosition3", testArmPosition3());
        //m_autoChooser.addOption("shootSpeakerDriveOut", ShootSpeakerDriveOut());

        ShuffleboardDisplay.PrepLayout.add("Choix auto", m_autoChooser);
    } 

    public Command getSelected() {
        return m_autoChooser.getSelected();
    }

    // public Command testArmRelease() {
    //     return m_gpCommandFactory.armGoToReleasePosition()
    //         .withName("testArmRelease");
    // }
    
    // public Command testArmPosition1() {
    //     return m_gpCommandFactory.armPositionResetAtParked()
    //         .withName("testArmPosition1");
    // }

    // public Command testArmPosition2() {
    //     return m_gpCommandFactory.armPositionResetAtParked()
    //         .andThen(m_gpCommandFactory.armGoToSpeakerPosition())
    //         .withName("testArmPosition2");
    // }

    // public Command testArmPosition3() {
    //     return m_gpCommandFactory.armPositionResetAtParked()
    //         .andThen(m_gpCommandFactory.armGoToSpeakerPosition())
    //         .andThen(m_gpCommandFactory.launchNoteToSpeaker())
    //         .withName("testArmPosition3");
    // }

    public Command testDrive() {
        return new DrivingTimedCommand(2, kAutonomousDriveOutput, 0, m_driveBase)
            .withName("testDrive");
    }

    // public Command testTurn() {
    //     return new DrivingTurnCommand(kAutonomousDriveOutput, 180., m_driveBase)
    //         .withName("testTurn");
    // }

    // public Command testDriveThenTurn() {
    //     return new DrivingTimedCommand(0.5, kAutonomousDriveOutput, m_driveBase)
    //         .andThen(new DrivingTurnCommand(kAutonomousDriveOutput, 180., m_driveBase))
    //         .withName("testDriveAndTurn");
    // }

    public Command shootSpeaker() {
        return m_gpCommandFactory.armPositionResetAtParked()
            .andThen(m_gpCommandFactory.armGoToSpeakerPosition())
            .andThen(m_gpCommandFactory.launchNoteToSpeaker())
            .andThen(m_gpCommandFactory.armGoToGroundPosition())
            .andThen(m_gpCommandFactory.armGoDown())
            .withName("shootSpeaker");
    }

    public Command shootSpeakerAngle() {
        return m_gpCommandFactory.armPositionResetAtParked()
            .andThen(m_gpCommandFactory.armGoToSpeakerAnglePosition())
            .andThen(m_gpCommandFactory.launchNoteToSpeakerAngle())
            .andThen(m_gpCommandFactory.armGoToGroundPosition())
            .andThen(m_gpCommandFactory.armGoDown())
            .withName("shootSpeaker");
    }

    // public Command ShootSpeaker() {
    //     return m_gpCommandFactory.armGoToReleasePosition()
    //         .andThen(m_gpCommandFactory.launchNoteToSpeaker())
    //         .withName("shootSpeaker");
    // }

    public Command shootSpeakerDriveOut() {
        return shootSpeaker()
            .andThen(driveAndGrab(1, 0))
            .withName("ShootSpeakerDriveOut");
    }

    public Command driveAndGrab(double time, double rotation) {
        return new DrivingTimedCommand(time, kAutonomousDriveOutput, 0, m_driveBase) // away from speaker a little bit. 
            .raceWith(m_gpCommandFactory.intakeGrabNote())
            .withName("driveAndGrab");
    }

    public Command shootSpeakerDriveOutCenter() {
        return shootSpeaker()
            .andThen(driveAndGrab(1.1, 0))
            .andThen(ShootFromSecondaryIfNoteIntakeSuccessful())
            .withName("ShootSpeakerDriveOutCenter");
    }

    public Command shootSpeakerDriveOutAngleLeft() {
        return shootSpeakerAngle()
            .andThen(driveAndGrab(1.3, -0.2))
            .andThen(ShootFromSecondaryIfNoteIntakeSuccessful())
            .withName("ShootSpeakerDriveOutAngle");
    }
    
    public Command shootSpeakerDriveOutAngleRight() {
        return shootSpeakerAngle()
            .andThen(driveAndGrab(1.3, 0.2))
            .andThen(ShootFromSecondaryIfNoteIntakeSuccessful())
            .withName("ShootSpeakerDriveOutAngle");
    }

    public Command ShootFromSecondaryIfNoteIntakeSuccessful() {
        if (m_gpCommandFactory.getIntake().noteCaptured) {
            return m_gpCommandFactory.launchNoteToSpeakerAutoAway();
        }
        return DoNothing();
    }

    public Command DoNothing() {
        return Commands.runOnce(() -> {});
    }
}
