package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class ShuffleboardDisplay {

    public static final ShuffleboardDisplay Instance = new ShuffleboardDisplay();

    public static ShuffleboardTab MainTab;

    public static ShuffleboardLayout PrepLayout;
    public static ShuffleboardLayout ArmLayout;
    public static ShuffleboardLayout CommandLayout;
    public static ShuffleboardLayout DriveLayout;
    public static ShuffleboardLayout IntakeLayout;
    public static ShuffleboardLayout LauncherLayout;
    public static ShuffleboardLayout VideoLayout;
    
    private ShuffleboardDisplay() {
        var kBasicLayoutWidth = 3;
        var kTotalWidth = 20;
        var kTotalHeight = 10;

        MainTab = Shuffleboard.getTab("Crescendo");

        var columnIndex = 0;
        // First Column
        PrepLayout = MainTab.getLayout("Prep", BuiltInLayouts.kList)
                .withSize(kBasicLayoutWidth, kTotalHeight/2)
                .withPosition(columnIndex, 0);
        CommandLayout = MainTab.getLayout("Commands", BuiltInLayouts.kList)
                .withSize(kBasicLayoutWidth, kTotalHeight/2)
                .withPosition(columnIndex, kTotalHeight/2);

        // Second Column
        columnIndex += kBasicLayoutWidth;
        DriveLayout = MainTab.getLayout("Drive", BuiltInLayouts.kList)
                .withSize(kBasicLayoutWidth, kTotalHeight/2)
                .withPosition(columnIndex, 0);
        IntakeLayout = MainTab.getLayout("Intake", BuiltInLayouts.kList)
                .withSize(kBasicLayoutWidth, kTotalHeight/2)
                .withPosition(columnIndex, kTotalHeight/2);

        // Third Column
        columnIndex += kBasicLayoutWidth;
        ArmLayout = MainTab.getLayout("Arm", BuiltInLayouts.kList)
                .withSize(kBasicLayoutWidth, kTotalHeight)
                .withPosition(columnIndex, 0);

        // Fourth Column
        columnIndex += kBasicLayoutWidth;
        LauncherLayout = MainTab.getLayout("Launcher", BuiltInLayouts.kList)
                .withSize(kBasicLayoutWidth, kTotalHeight)
                .withPosition(columnIndex, 0);

        // Video
        columnIndex += kBasicLayoutWidth;
        var videoSize = Math.max(kTotalWidth - columnIndex, kTotalHeight);
        VideoLayout = MainTab.getLayout("Video", BuiltInLayouts.kList)
                .withSize(videoSize, videoSize)
                .withPosition(columnIndex, 0);
    }

}
