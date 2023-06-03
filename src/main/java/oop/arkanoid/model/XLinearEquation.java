package oop.arkanoid.model;

import java.util.List;

class XLinearEquation implements LinearEquation {

    private final double x;
    final Point yBorders;

    XLinearEquation(double x, Point yBorders) {
        this.x = x;
        this.yBorders = yBorders;
    }

    @Override
    public boolean hasIntersection(CircleEquation circleEquation) {
        List<Double> ys = circleEquation.getY(x);
        for (Double y : ys) {
            if (GameLevel.Builder.inSegment(yBorders.x(), yBorders.y(), y)) {
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
    public double getX(double y) {
        return x;
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
    public double findDistance(CircleEquation circleEquation, CollisionPlace place) {
        List<Double> ys = circleEquation.getY(x);
        for (Double y : ys) {
            if (GameLevel.Builder.inSegment(yBorders.x(), yBorders.y(), y)) {
                if (place == CollisionPlace.TOP) {
                    return Math.abs(y - yBorders.x());
                } else {
                    return Math.abs(y - yBorders.y());
                }
            }
        }
        return 0;
    }

    @Override
    public double findHordaSize(CircleEquation circleEquation) {
        List<Double> ys = circleEquation.getY(x);
        return Math.abs(ys.get(0) - ys.get(1));
    }

}
