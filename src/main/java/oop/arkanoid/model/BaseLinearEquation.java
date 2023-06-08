package oop.arkanoid.model;

import oop.arkanoid.model.barriers.CollisionPlace;

import java.util.List;

import static oop.arkanoid.model.barriers.Barrier.inSegment;

class BaseLinearEquation implements LinearEquation {

    final double k;
    final double b;

    private final double angle;
    private final Point xBorders;

    BaseLinearEquation(double angle, double b, Point xBorders) {
        this.angle = angle;
        this.k = Math.tan(Math.toRadians(angle));
        this.b = b;
        this.xBorders = xBorders;
    }

    /*
        { (x-centerX)^2 + (y - centerY)^2 = R^2
        { y=kx+b

       [1+k^2]x^2 + [2(k(b-centerY) - centerX)]x + [centerX^2 - R^2 + (b - centerY)^2] = 0

    */

    @Override
    public boolean hasIntersection(CircleEquation circleEquation) {
        QuadraticEquation qEquation = new QuadraticEquation(1 + k * k, 2 * (k * (b - circleEquation.center().y()) - circleEquation.center().x()),
                circleEquation.center().x() * circleEquation.center().x() - circleEquation.radius() * circleEquation.radius() + Math.pow(b - circleEquation.center().y(), 2));

        for (Double root : qEquation.roots) {
            if (inSegment(xBorders.x(), xBorders.y(), root)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getY(double x) {
        return k * x + b;
    }

    @Override
    public LinearEquation rotate(Point currPoint, CollisionPlace place) {
        if (place.needToChangeDirection) {
            return new BaseLinearEquation(-180 - angle, countB(-180 - angle, currPoint), xBorders);
        } else {
            return new BaseLinearEquation(-angle, countB(-angle, currPoint), xBorders);
        }
    }

    @Override
    public LinearEquation rotate(Point currPoint, double diffXBetweenBallAndCenterPlatform) {
        return new BaseLinearEquation(-90 - diffXBetweenBallAndCenterPlatform, countB(-90 - diffXBetweenBallAndCenterPlatform, currPoint), xBorders);
    }

    @Override
    public double getDistanceBallCrossingLine(CircleEquation circleEquation, CollisionPlace place) {
        List<Double> xs = circleEquation.getX(b);

        for (Double x : xs) {
            if (inSegment(xBorders.x(), xBorders.y(), x)) {
                if (place == CollisionPlace.LEFT) {
                    return Math.abs(x - xBorders.x());
                } else {
                    return Math.abs(x - xBorders.y());
                }
            }
        }
        return 0;
    }

    static double countB(double angle, Point position) {
        return position.y() - position.x() * Math.tan(Math.toRadians(angle));
    }
}

