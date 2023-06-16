package oop.arkanoid.model.motion;

import oop.arkanoid.model.CircleEquation;
import oop.arkanoid.model.Point;

import java.util.List;

import static oop.arkanoid.model.ModelUtils.tan;

public interface LinearEquation {

    /**
     * Calculates circle and line intersection points.
     *
     * @param circleEquation equation of circle
     * @return points of circle and line intersection
     */

    List<Double> getIntersectionPoints(CircleEquation circleEquation);

    double getY(double x);

    /**
     * Rotates ball motion linear equation.
     *
     * @param angle     angle to rotate
     * @param currPoint position relative to which to rotate
     * @return new linear motion with new linear equation according to new angle
     */
    LinearEquation rotate(double angle, Point currPoint);

    static LinearEquation linearEquation(double angle, Point position) {
        double k = tan(angle);
        double b = position.y() - position.x() * k;
        return new BaseLinearEquation(angle, b, k);
    }

    static LinearEquation xLinearMotionEquation(double x) {
        return new XLinearEquation(x);
    }

}
