package oop.arkanoid.model;

public class RangeChecker {
    public static boolean checkRange(double min, double max, double value) {
        return min <= value && value <= max;
    }

}
