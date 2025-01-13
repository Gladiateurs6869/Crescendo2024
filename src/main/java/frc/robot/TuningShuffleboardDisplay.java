package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;


public class TuningShuffleboardDisplay {
    private static final int kBasicLayoutWidth = 4;
    private static final int kTotalHeight = 10;

    public static final ShuffleboardTab MainTab = Shuffleboard.getTab("Tuning");

    // Arm Column
    public static final ShuffleboardLayout ArmLayout = MainTab.getLayout("Arm", BuiltInLayouts.kList)
        .withSize(kBasicLayoutWidth, kTotalHeight)
        .withPosition(0, 0);

    // Intake Column
    public static final int kIntakeColumnPosition = kBasicLayoutWidth * 1;
    public static final ShuffleboardLayout IntakeLayout = MainTab.getLayout("Intake", BuiltInLayouts.kList)
        .withSize(kBasicLayoutWidth, kTotalHeight)
        .withPosition(kIntakeColumnPosition, 0);

    // Launcher Column
    public static final int kLauncherColumnPosition = kBasicLayoutWidth * 1;
    public static final ShuffleboardLayout LauncherLayout = MainTab.getLayout("Launcher", BuiltInLayouts.kList)
        .withSize(kBasicLayoutWidth, kTotalHeight)
        .withPosition(kLauncherColumnPosition, 0);

    // Commands Column
    public static final int kCommandsColumnPosition = kBasicLayoutWidth * 1;
    public static final ShuffleboardLayout CommandLayout = MainTab.getLayout("Commands", BuiltInLayouts.kList)
        .withSize(kBasicLayoutWidth, kTotalHeight)
        .withPosition(kCommandsColumnPosition, 0);

}
