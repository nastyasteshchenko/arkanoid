package oop.arkanoid.model.motion;

import oop.arkanoid.model.CircleEquation;
import oop.arkanoid.model.Point;

import java.util.List;

import static oop.arkanoid.model.ModelUtils.tan;

public interface LinearEquation {

    List<Double> getIntersectionPoints(CircleEquation circleEquation);

    double getY(double x);

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
