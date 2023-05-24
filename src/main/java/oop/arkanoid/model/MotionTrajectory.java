package oop.arkanoid.model;

class MotionTrajectory {

    final Trajectory trajectory;
    final Point position;
    private final double speed;

    MotionTrajectory(Point diffs, Point startPosition) {
        this.trajectory = new Trajectory(diffs, new Point(0, 0), new Point(0,0));
        trajectory.recountB(startPosition);
        this.position = startPosition;
        speed = Math.sqrt(trajectory.dx * trajectory.dx + trajectory.dy * trajectory.dy);
    }

    void changeAngle(double dx) {
        trajectory.dx = dx;
        double newDY = Math.sqrt(speed * speed - dx * dx);
        if (trajectory.dy < 0) {
            trajectory.dy = -newDY;
        } else {
            trajectory.dy = newDY;
        }
    }

    Point nextPoint() {
        position.setX(position.x() + trajectory.dx);
        position.setY(position.y() + trajectory.dy);
        return position;
    }
}
