package oop.arkanoid.model;

public class ModelUtils {
    public static boolean isInRange(double min, double max, double value) {
        return min <= value && value <= max;
    }

    public static double tan(double angle) {
        return Math.tan(Math.toRadians(angle));
    }

}
