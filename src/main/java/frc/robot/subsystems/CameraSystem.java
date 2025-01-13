package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.util.PixelFormat;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.ShuffleboardDisplay;

// public class CameraSystem extends SubsystemBase {
//     private enum CurrentCamera {
//         LaunchCamera,
//         IntakeCamera
//     }

//     private CurrentCamera m_current = CurrentCamera.IntakeCamera;

//     private final UsbCamera m_cameraIntake;
//     private final UsbCamera m_cameraLaunch;
//     private final VideoSink m_server;

//     private static final int kIntakeCameraUsbPort = 0;
//     private static final int kLaunchCameraUsbPort = 1;
//     private static final int kHorizontalResolution = 320;
//     private static final int kVerticalResolution = 240;
//     private static final int kFPS = 30;

//     public CameraSystem() {
//         m_cameraIntake = new UsbCamera("Intake", kIntakeCameraUsbPort);
//         CameraServer.addCamera(m_cameraIntake);

//         m_cameraLaunch = new UsbCamera("Launch", kLaunchCameraUsbPort);
//         CameraServer.addCamera(m_cameraLaunch);

//         m_server = CameraServer.addServer("MainCameraServer");
//         m_server.setSource(m_cameraIntake);
//         m_cameraIntake.setVideoMode(PixelFormat.kMJPEG, kHorizontalResolution, kVerticalResolution, kFPS);
//         m_cameraLaunch.setVideoMode(PixelFormat.kMJPEG, kHorizontalResolution, kVerticalResolution, kFPS);

//         // adjustVideoMode(m_cameraIntake);
//         // adjustVideoMode(m_cameraLaunch);

//         // May do: HUD indicators for the pilot: https://docs.wpilib.org/en/stable/docs/software/vision-processing/roborio/using-the-cameraserver-on-the-roborio.html

//         //ShuffleboardDisplay.MainTab.add("Video", m_server.getSource()).withSize(10, 10).withPosition(9, 0);
//         ShuffleboardDisplay.VideoLayout.add("Video", m_server.getSource());
//     }

//     // private void adjustVideoMode(UsbCamera camera) {
//     //     var succeeded = false;

//     //     while (!succeeded) {
//     //         succeeded = camera.setVideoMode(PixelFormat.kMJPEG, kHorizontalResolution, kVerticalResolution, kFPS);
//     //         try {
//     //             Thread.sleep(20);
//     //         } catch(InterruptedException ie) {
//     //             // Do nothing
//     //         }
//     //     }
//     // }

//     public Command switchCameraIntakeSide() {
//         return runOnce(() -> {
//             m_cameraLaunch.setConnectionStrategy(ConnectionStrategy.kForceClose);
//             m_cameraIntake.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
//             m_server.setSource(m_cameraIntake);
//             m_current = CurrentCamera.IntakeCamera;
//         }).ignoringDisable(true);
//     }

//     public Command switchCameraLaunchSide() {
//         return runOnce(() -> {
//             m_cameraLaunch.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
//             m_cameraIntake.setConnectionStrategy(ConnectionStrategy.kForceClose);
//             m_server.setSource(m_cameraLaunch);
//             m_current = CurrentCamera.LaunchCamera;
//         }).ignoringDisable(true);
//     }

//     public Command cameraToggle() {
//         return Commands.either(switchCameraLaunchSide(), switchCameraIntakeSide(),() -> m_current == CurrentCamera.IntakeCamera)
//         .withName("CameraToggeling")
//         .ignoringDisable(true);
//     }
// }

public class CameraSystem extends SubsystemBase {

    public Command switchCameraIntakeSide() {
        return DoNothing();
    }

    public Command switchCameraLaunchSide() {
        return DoNothing();
    }

    public Command cameraToggle() {
        return DoNothing();
    }

    public Command DoNothing() {
        return Commands.runOnce(() -> {});
    }
}