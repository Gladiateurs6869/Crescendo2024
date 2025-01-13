// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.commands;

// import java.nio.file.WatchEvent;

// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.subsystems.ArmSystem;
// import frc.robot.subsystems.HatchetGrabSystem;
// import frc.robot.util.MathHelp;

// public class ClimbCommand extends Command {
//   /** Creates a new ClimbCommand. */

//   private HatchetGrabSystem hatchetGrabSystem;
//   private ArmSystem armSystem;

//   public ClimbCommand(ArmSystem aS, HatchetGrabSystem hG) {
//     hatchetGrabSystem = hG;
//     armSystem = aS;
//     addRequirements(hG);
//     addRequirements(aS);
//   }

//   // Called when the command is initially scheduled.
//   @Override
//   public void initialize() {}

//   // Called every time the scheduler runs while the command is scheduled.
//   @Override
//   public void execute() {
//     hatchetGrabSystem.move(0);
//   }

//   public boolean isAtWantedAngle(double wantedAngle){
//     if(MathHelp.isZero(armSystem.getPositionAsArmAngle() - wantedAngle, 0.01 )){
//       return true;
//     }
//     else{
//       return false;
//     }
//   }

//   // Called once the command ends or is interrupted.
//   @Override
//   public void end(boolean interrupted) {}

//   // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     return false;
//   }
// }
