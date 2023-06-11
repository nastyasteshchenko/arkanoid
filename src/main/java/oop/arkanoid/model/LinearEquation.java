package oop.arkanoid.model;

import oop.arkanoid.model.barriers.CollisionPlace;

import java.util.List;

public interface LinearEquation {

    List<Double> getIntersectionPoints(CircleEquation circleEquation);

    double getY(double x);

    LinearEquation rotate(Point currPoint, CollisionPlace place);

    LinearEquation rotate(Point currPoint, double diffBetweenBallAndCenterPlatform);

    static LinearEquation linearEquation(double angle, double b) {
        return new BaseLinearEquation(angle, b);
    }

    static LinearEquation xLinearMotionEquation(double x) {
        return new XLinearEquation(x);
    }

}
