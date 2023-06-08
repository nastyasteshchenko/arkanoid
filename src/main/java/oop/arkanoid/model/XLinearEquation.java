package oop.arkanoid.model;

import oop.arkanoid.model.barriers.CollisionPlace;

import java.util.List;

import static oop.arkanoid.model.RangeChecker.checkRange;

record XLinearEquation(double x, Point yBorders) implements LinearEquation {

    @Override
    public boolean hasIntersection(CircleEquation circleEquation) {
        List<Double> ys = circleEquation.getY(x);
        for (Double y : ys) {
            if (checkRange(yBorders.x(), yBorders.y(), y)) {
                return true;
            }
        }
        return false;
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

    @Override
    public double getDistanceBallCrossingLine(CircleEquation circleEquation, CollisionPlace place) {
        List<Double> ys = circleEquation.getY(x);
        for (Double y : ys) {
            if (checkRange(yBorders.x(), yBorders.y(), y)) {
                if (place == CollisionPlace.TOP) {
                    return Math.abs(y - yBorders.x());
                } else {
                    return Math.abs(y - yBorders.y());
                }
            }
        }
        return 0;
    }

}
