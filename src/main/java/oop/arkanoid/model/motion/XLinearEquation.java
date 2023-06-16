package oop.arkanoid.model.motion;

import oop.arkanoid.model.CircleEquation;
import oop.arkanoid.model.Point;

import java.util.List;

public record XLinearEquation(double x) implements LinearEquation {

    /**
     * Calculates circle and line intersection points
     *
     * @param circleEquation equation of circle
     * @return points of circle and line intersection
     */

    @Override
    public List<Double> getIntersectionPoints(CircleEquation circleEquation) {
        return circleEquation.getY(x);
    }

    @Override
    public double getY(double x) {
        return Double.NaN;
    }

    @Override
    public LinearEquation rotate(double angle, Point currPoint) {
        throw new UnsupportedOperationException();
    }

}
