package oop.arkanoid.model;

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

    boolean hasIntersection(Trajectory trajectory) {
        if (dy == trajectory.dy && dx == trajectory.dx) {
            return false;
        }

        double x;
        double y;

        if (dy == 0) {
            y = b;
            x = trajectory.findX(y);
        } else if (dx == 0) {
            x = b;
            y = trajectory.findY(x);
        } else {
            x = (trajectory.b - b) / (trajectory.dy / trajectory.dx - dy / dx);
            y = trajectory.findY(x);
        }

        return inSegment(x, possibleValuesX.x(), possibleValuesX.y()) && inSegment(y, possibleValuesY.x(), possibleValuesY.y());
    }

    void recountB(Point position) {
        b = position.y() - dy * position.x() / dx;
    }

    double findY(double x) {
        return dy * x / dx + b;
    }

    double findX(double y) {
        return dx / dy * (y - b);
    }

    private boolean inSegment(double value, double min, double max) {
        return value <= max && value >= min;
    }
}
