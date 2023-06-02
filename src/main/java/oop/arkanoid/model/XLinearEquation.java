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
    public LinearEquation rotate(Point currPoint, CollisionPlace place) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LinearEquation rotate(Point currPoint, double diffX){
        throw new UnsupportedOperationException();
    }
}
