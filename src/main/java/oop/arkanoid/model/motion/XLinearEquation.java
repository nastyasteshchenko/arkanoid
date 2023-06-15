package oop.arkanoid.model.motion;

import oop.arkanoid.model.CircleEquation;
import oop.arkanoid.model.Point;
import oop.arkanoid.model.barrier.CollisionPlace;
import oop.arkanoid.model.motion.LinearEquation;

import java.util.List;

public record XLinearEquation(double x) implements LinearEquation {

    @Override
    public List<Double> getIntersectionPoints(CircleEquation circleEquation) {
        return circleEquation.getY(x);
    }

    @Override
    public double getY(double x) {
        return Double.NaN;
    }

    @Override
    public LinearEquation rotate(Point currPoint, CollisionPlace place) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LinearEquation rotate(Point currPoint, double diffBetweenBallAndCenterPlatform) {
        throw new UnsupportedOperationException();
    }

}