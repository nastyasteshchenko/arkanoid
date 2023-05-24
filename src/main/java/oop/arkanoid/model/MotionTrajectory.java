package oop.arkanoid.model;

class MotionTrajectory {

    final Trajectory trajectory;
    final Point position;

    final double speed;

    MotionTrajectory(Point diffs, Point startPosition, Point possibleValuesX, Point possibleValuesY) {
        this.trajectory = new Trajectory(diffs, possibleValuesX, possibleValuesY);
        trajectory.recountB(startPosition);
        this.position = startPosition;
        speed = Math.sqrt(trajectory.dx * trajectory.dx + trajectory.dy * trajectory.dy);
    }

    void changeAngle(double dx) {
        trajectory.dx = dx;
        if (trajectory.dy < 0) {
            trajectory.dy = -Math.sqrt(speed * speed - trajectory.dx * trajectory.dx);
        } else {
            trajectory.dy = Math.sqrt(speed * speed - trajectory.dx * trajectory.dx);
        }

    }

    Point nextPoint() {
        position.setX(position.x() + trajectory.dx);
        position.setY(position.y() + trajectory.dy);
        return position;
    }
}
