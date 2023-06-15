package oop.arkanoid.model;

public class ModelUtils {
    public static boolean isInRange(double min, double max, double value) {
        return min <= value && value <= max;
    }

}
