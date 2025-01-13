// package frc.robot.commands.testing;

// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.subsystems.LauncherSystem;

// public class LauncherSpeedDeltaCommand extends Command {

//     private LauncherSystem m_launcher;
    
//     private VelocityDeltaType m_direction;
    
//     public LauncherSpeedDeltaCommand(VelocityDeltaType direction, LauncherSystem launcher) {
//         m_direction = direction;
//         m_launcher = launcher;
//         addRequirements(m_launcher);
//     }
//     @Override
//     public void initialize() {
//     }

//     @Override
//     public void execute() {
//         SmartDashboard.putString("SpeedDelta", "Accelerate");
//         m_launcher.changeVelocity(m_direction);
//     }

//     @Override
//     public void end(boolean interrupted) {
//         // stopMotor();
//     }
    
//     @Override
//     public boolean isFinished() {
//         return true;
//     }
// }
