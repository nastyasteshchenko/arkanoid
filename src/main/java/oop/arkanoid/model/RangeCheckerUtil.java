package oop.arkanoid.model;

public class RangeCheckerUtil {
    //TODO isInRange?
    public static boolean checkRange(double min, double max, double value) {
        return min <= value && value <= max;
    }

}
