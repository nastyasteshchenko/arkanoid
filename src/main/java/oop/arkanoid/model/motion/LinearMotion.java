package oop.arkanoid.model.motion;

import oop.arkanoid.model.Point;
import oop.arkanoid.model.QuadraticEquation;

public class LinearMotion {

    public final MotionDirection direction;
    private Point currPosition;
    private final double step;
    private final BaseLinearEquation linearEquation;

    public LinearMotion(BaseLinearEquation linearEquation, MotionDirection direction, double step, Point position) {
        this.linearEquation = linearEquation;
        this.direction = direction;
        this.step = step;
        currPosition = position;
    }

    /**
     * Calculate next point of motion for ball by solving the system of equation:
     * <p>
     * { (x-centerX)^2 + (y - centerY)^2 = R^2<br>
     * { y=kx+b
     * <p>
     * In other words, by solving the quadratic equation:
     * <p>
     * [1+k^2]x^2 + [2(k(b-centerY) - centerX)]x + [centerX^2 - R^2 + (b - centerY)^2] = 0
     *
     * @return next point for ball motion
     */

    public Point nextPoint() {

        double a = 1 + linearEquation.k() * linearEquation.k();
        double b = 2 * (linearEquation.k() * (linearEquation.b() - currPosition.y()) - currPosition.x());
        double c = currPosition.x() * currPosition.x() - step * step + Math.pow(linearEquation.b() - currPosition.y(), 2);

        QuadraticEquation qEquation = new QuadraticEquation(a, b, c);

        double firstRoot = qEquation.roots.get(0);
        double secondRoot = qEquation.roots.get(1);

        currPosition = findNextPoint(firstRoot, secondRoot);

        return currPosition;
    }

    /**
     * Flips direction of ball.
     *
     * @return new linear motion with new direction
     */
    public LinearMotion flipDirection() {
        return new LinearMotion(linearEquation, direction.flip(), step, currPosition);
    }

    /**
     * Rotates ball motion linear equation.
     *
     * @param angle - angle of rotate
     * @return new linear motion with new linear equation according to new angle
     */
    public LinearMotion rotate(double angle) {
        return new LinearMotion(linearEquation.rotate(angle, currPosition), direction, step, currPosition);
    }

    public double getMotionAngle() {
        return linearEquation.angle();
    }

    public Point currPosition() {
        return currPosition;
    }

    private Point findNextPoint(double firstRoot, double secondRoot) {

        double newX;

        if (direction == MotionDirection.LEFT) {
            newX = firstRoot <= currPosition.x() ? firstRoot : secondRoot;
        } else {
            newX = firstRoot >= currPosition.x() ? firstRoot : secondRoot;
        }

        double newY = linearEquation.getY(newX);

        return new Point(newX, newY);
    }

}