package oop.arkanoid.model.motion;

import oop.arkanoid.model.CircleEquation;

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


}
