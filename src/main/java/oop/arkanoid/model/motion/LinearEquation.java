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

    static BaseLinearEquation linearEquation(double angle, Point position) {
        double k = tan(angle);
        double b = position.y() - position.x() * k;
        return new BaseLinearEquation(angle, b, k);
    }

    static XLinearEquation xLinearMotionEquation(double x) {
        return new XLinearEquation(x);
    }

}
