package oop.arkanoid.model;

import java.util.List;

class BaseLinearEquation implements LinearEquation {

    final double k;
    final double angle;
    final double b;

    final Point xBorders;

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
            if (GameLevel.Builder.inSegment(xBorders.x(), xBorders.y(), root)) {
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
    public double getX(double y) {
        return (y - b) / k;
    }

    @Override
    public LinearEquation rotate(Point currPoint, CollisionPlace place) {
        if (place.needToChangeDirection) {
            return new BaseLinearEquation(-180 - angle, recountB(180 - angle, currPoint), xBorders);
        } else {
            return new BaseLinearEquation(-angle, recountB(-angle, currPoint), xBorders);
        }
    }

    @Override
    public LinearEquation rotate(Point currPoint, double diffBetweenBallAndCenterPlatform) {
        double angle = -90 - diffBetweenBallAndCenterPlatform;
        if (diffBetweenBallAndCenterPlatform == 0) {
            angle = -91;
        }
        return new BaseLinearEquation(angle, recountB(angle, currPoint), xBorders);
    }

    static double recountB(double angle, Point position) {
        return position.y() - position.x() * Math.tan(Math.toRadians(angle));
    }

    @Override
    public double findDistance(CircleEquation circleEquation, CollisionPlace place) {
        List<Double> xs = circleEquation.getX(b);
        if (place == CollisionPlace.LEFT) {
            return Math.abs(xs.get(0) - xBorders.x());
        } else {
            return Math.abs(xs.get(0) - xBorders.y());
        }
    }
}

