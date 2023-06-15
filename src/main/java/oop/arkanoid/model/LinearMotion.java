package oop.arkanoid.model;

import oop.arkanoid.model.barrier.CollisionPlace;

class LinearMotion {

    final BaseLinearEquation linearEquation;
    final MotionDirection direction;
    final double step;
    Point currPoint;

    LinearMotion(BaseLinearEquation linearEquation, MotionDirection direction, double step, Point startPos) {
        this.linearEquation = linearEquation;
        this.direction = direction;
        this.step = step;
        currPoint = startPos;
    }

    Point nextPoint() {
    /*
         { (x-centerX)^2 + (y - centerY)^2 = R^2
         { y=kx+b

        [1+k^2]x^2 + [2(k(b-centerY) - centerX)]x + [centerX^2 - R^2 + (b - centerY)^2] = 0
    */

        double a = 1 + linearEquation.k * linearEquation.k;
        double b = 2 * (linearEquation.k * (linearEquation.b - currPoint.y()) - currPoint.x());
        double c = currPoint.x() * currPoint.x() - step * step + Math.pow(linearEquation.b - currPoint.y(), 2);

        QuadraticEquation qEquation = new QuadraticEquation(a, b, c);

        Double firstRoot = qEquation.roots.get(0);
        Double secondRoot = qEquation.roots.get(1);

        if (direction == MotionDirection.LEFT) {
            currPoint.setX(firstRoot <= currPoint.x() ? firstRoot : secondRoot);
        } else {
            currPoint.setX(firstRoot >= currPoint.x() ? firstRoot : secondRoot);
        }
        currPoint.setY(linearEquation.getY(currPoint.x()));

        return currPoint;
    }

    //TODO: delete or unused
    LinearMotion changeStepIfNeeded(double newStep) {
        if (step == newStep) {
            return this;
        }
        return new LinearMotion(linearEquation, direction, newStep, currPoint);
    }

    LinearMotion flipDirection() {
        return new LinearMotion(linearEquation, direction.flip(), step, currPoint);
    }

    LinearMotion flipDirection(double diffXBetweenBallAndCenterPlatform) {
        return new LinearMotion(linearEquation, direction.flipByPlatform(diffXBetweenBallAndCenterPlatform), step, currPoint);
    }

    LinearMotion rotate(CollisionPlace place) {
        return new LinearMotion((BaseLinearEquation) linearEquation.rotate(currPoint, place), direction, step, currPoint);
    }

    LinearMotion rotate(double diffXBetweenBallAndCenterPlatform) {
        return new LinearMotion((BaseLinearEquation) linearEquation.rotate(currPoint, diffXBetweenBallAndCenterPlatform), direction, step, currPoint);
    }
}