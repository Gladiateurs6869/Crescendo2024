package frc.robot.util;

public class MathHelp {
    
    private final static double kEpsilon = 0.01;

    public static boolean isZero(double value)
    {
        return isZero(value, kEpsilon);
    }

    public static boolean isZero(double value, double epsilon)
    {
        return (Math.abs(value) < epsilon);
    }
}
