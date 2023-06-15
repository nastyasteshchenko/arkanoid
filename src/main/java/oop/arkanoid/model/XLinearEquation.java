package oop.arkanoid.model;

import oop.arkanoid.model.barrier.CollisionPlace;

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
