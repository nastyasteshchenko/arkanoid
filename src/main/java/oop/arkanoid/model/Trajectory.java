package oop.arkanoid.model;

import static oop.arkanoid.model.GameLevel.Builder.inSegment;

class Trajectory {

    double dx;
    double dy;
    double b;
    private final Point possibleValuesX;
    private final Point possibleValuesY;

    Trajectory(Point diffs, Point possibleValuesX, Point possibleValuesY) {
        this.dx = diffs.x();
        this.dy = diffs.y();
        this.possibleValuesX = possibleValuesX;
        this.possibleValuesY = possibleValuesY;
    }

    boolean hasIntersection(Trajectory trajectory, double radius) {
        if (dy == trajectory.dy && dx == trajectory.dx) {
            return false;
        }
        return dy == 0 ? inSegment(possibleValuesX.x() - radius, possibleValuesX.y() + radius, trajectory.findX(b)) : inSegment( possibleValuesY.x() - radius, possibleValuesY.y() + radius, trajectory.findY(b));
    }

    void recountB(Point position) {
        b = position.y() - dy * position.x() / dx;
    }

    private double findY(double x) {
        return dy * x / dx + b;
    }

    private double findX(double y) {
        return dx / dy * (y - b);
    }

}
