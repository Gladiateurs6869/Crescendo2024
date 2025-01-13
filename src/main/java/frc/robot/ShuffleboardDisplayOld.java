// package frc.robot;

// import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
// import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
// import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
// import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

// public class ShuffleboardDisplayOld {
//     private static final int kBasicLayoutWidth = 3;
//     private static final int kTotalWidth = 20;
//     private static final int kTotalHeight = 10;

//     public static final ShuffleboardTab MainTab = Shuffleboard.getTab("Crescendo");

//     // First Column
//     public static final int kFirstColumnTopSize = 3;
//     public static final ShuffleboardLayout PrepLayout = MainTab.getLayout("Prep", BuiltInLayouts.kList)
//         .withSize(kBasicLayoutWidth, kFirstColumnTopSize)
//         .withPosition(0, 0);

//     // Second Column
//     public static final int kSecondColumnTopSize = 5;
//     public static final int kSecondColumnPosition = kBasicLayoutWidth * 1;
//     public static final ShuffleboardLayout DriveLayout = MainTab.getLayout("Drive", BuiltInLayouts.kList)
//         .withSize(kBasicLayoutWidth, kSecondColumnTopSize)
//         .withPosition(kSecondColumnPosition, 0);
//     public static final ShuffleboardLayout LauncherLayout = MainTab.getLayout("Launcher", BuiltInLayouts.kList)
//         .withSize(kBasicLayoutWidth, kTotalHeight - kSecondColumnTopSize)
//         .withPosition(kSecondColumnPosition, kSecondColumnTopSize);

//     // Third Column
//     public static final int kThirdColumnTopSize = 4;
//     public static final int kThirdColumnPostition = kBasicLayoutWidth * 2;
//     public static final ShuffleboardLayout CommandLayout = MainTab.getLayout("Commands", BuiltInLayouts.kList)
//         .withSize(kBasicLayoutWidth, kThirdColumnTopSize)
//         .withPosition(kThirdColumnPostition, 0);
//     public static final ShuffleboardLayout IntakeLayout = MainTab.getLayout("Intake", BuiltInLayouts.kList)
//         .withSize(kBasicLayoutWidth, kTotalHeight - kThirdColumnTopSize)
//         .withPosition(kThirdColumnPostition, kThirdColumnTopSize);

//     // Fourth Column 
//     public static final int kFourthColumnPostition = kBasicLayoutWidth * 2;
//     public static final ShuffleboardLayout ArmLayout = MainTab.getLayout("Arm", BuiltInLayouts.kList)
//         .withSize(kBasicLayoutWidth, kTotalHeight)
//         .withPosition(kFourthColumnPostition, kFirstColumnTopSize);

//     // Video
//     public static final int kVideoColumnPostition = kBasicLayoutWidth * 4;
//     public static final int kVideoSize = Math.max(kTotalWidth-kVideoColumnPostition, kTotalHeight);
//     public static final ShuffleboardLayout VideoLayout = MainTab.getLayout("Video", BuiltInLayouts.kList)
//         .withSize(kVideoSize, kVideoSize)
//         .withPosition(kVideoColumnPostition, 0);



// }
