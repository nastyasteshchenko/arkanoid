package oop.arkanoid.model;

import java.util.List;

class LinearMotion {

    final BaseLinearEquation linearEquation;
    final MotionDirection direction;
    final double step;
    Point currPoint;

    LinearMotion(BaseLinearEquation linearEquation, MotionDirection horizontalDirection, double step, Point startPos) {
        this.linearEquation = linearEquation;
        this.direction = horizontalDirection;
        this.step = step;
        currPoint = startPos;
    }

    Point nextPoint() {
        /*
         TODO move to ctor
         { (x-centerX)^2 + (y - centerY)^2 = R^2
         { y=kx+b
        [1+k^2]x^2 + [2(k(b-centerY) - centerX)]x + [centerX^2 - R^2 + (b - centerY)^2] = 0
        */
        QuadraticEquation qEquation = new QuadraticEquation(1 + linearEquation.k * linearEquation.k, 2 * (linearEquation.k * (linearEquation.b - currPoint.y()) - currPoint.x()),
                currPoint.x() * currPoint.x() - step * step + Math.pow(linearEquation.b - currPoint.y(), 2));
        List<Double> roots = qEquation.roots;

        if (direction == MotionDirection.LEFT) {
            if (roots.get(0) <= currPoint.x()) {
                currPoint.setX(roots.get(0));
            } else {
                currPoint.setX(roots.get(1));
            }
        } else {
            if (roots.get(0) >= currPoint.x()) {
                currPoint.setX(roots.get(0));
            } else {
                currPoint.setX(roots.get(1));
            }
        }
        currPoint.setY(linearEquation.getY(currPoint.x()));
        return currPoint;
    }

    LinearMotion changeStepIfNeeded(double newStep) {
        if (step == newStep) {
            return this;
        }
        return new LinearMotion(linearEquation, direction, newStep, currPoint);
    }

    LinearMotion flipDirection() {
        return new LinearMotion(linearEquation, direction.flip(), step, currPoint);
    }
    LinearMotion flipDirection(double diffX) {
        return new LinearMotion(linearEquation, direction.flipByPlatform(diffX), step, currPoint);
    }

    LinearMotion rotate(CollisionPlace place) {
        return new LinearMotion((BaseLinearEquation) linearEquation.rotate(currPoint, place), direction, step, currPoint);
    }

    LinearMotion rotate(double diffX) {
        return new LinearMotion((BaseLinearEquation) linearEquation.rotate(currPoint, diffX), direction, step, currPoint);
    }
}