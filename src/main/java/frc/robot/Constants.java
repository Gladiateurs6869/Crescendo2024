// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    
    // Liste de constantes pour les ports analogs
    // Toutes nos elements branches sur un port de "AnalogInput" devraient etre definis ici.
    // i.e. Sensors
    public static class AnalogInputPort {
        // Port pour le Ultrasonic Sensor en mode test.
        public static int kUltrasonicSensor1 = 0;
    }

    // Liste des constantes pour les ports de controlleurs (joysticks)
    public static class ControllerPort {
        public static final int Joystick = 0;
        public static final int Manette = 1;
    }

    public static class Xbox360ButtonPort {
        public static final int YellowY = 4;
        public static final int BlueX = 3;
        public static final int GreenA = 1;
        public static final int RedB = 2;
        public static final int LeftBumper = 5;
        public static final int RightBumper = 6;
        public static final int LeftTrigger = 7;
        public static final int RightTrigger = 8;

        public static final int PovLeft = 270;
        public static final int PovRight = 90;
        public static final int PovUp = 0;
        public static final int PovDown = 180;
    }

    public static class JoystickButtonPort {
        public static final int TriggerIndex = 1;
        public static final int TriggerThumb = 2;
        public static final int TopLeftLow = 3;
        public static final int TopRightLow = 4;
        public static final int TopLeftHigh = 5;
        public static final int TopRightHigh = 6;
    }

    // Liste de constantes pour les ports digitaux
    // Tout nos elements branches sur un port de "DigitalInput" devraient etre definis ici.
    // i.e. LimitSwitch
    public static class DigitalInputPort {
        public static final int ArmBottomLeftLimitSwitch = 0;
        public static final int ArmBottomRightLimitSwitch = 3;
        public static final int ArmTopLimitSwitch = 1;
        public static final int IntakeNoteSwitch = 2;
    }

    // Liste des ports CAN pour les controlleurs de moteurs
    public static class MotorPort {
        // do not use port 1. CAN port 1 is reserved for the PDP (Power Distribution Panel)
        
        // Tank Drive
        public static final int DriveLeftMaster = 5;  // TalonSRX
        public static final int DriveLeftSlave = 4;   // VictorSPX
        public static final int DriveRightMaster = 2; // TalonSRX
        public static final int DriveRightSlave = 7;  // VictorSPX

        // Systems
        public static final int ArmMotorMaster = 8;   // TalonSRX
        public static final int ArmMotorSlave = 3;    // VictorSPX
        public static final int LaunchMotorNeo = 6;   // SparkMax/Neo
        public static final int IntakeMotorTalon = 9; // TalonSRX
        //public static final int HatchetGrabMotor = 10;
    }
}
