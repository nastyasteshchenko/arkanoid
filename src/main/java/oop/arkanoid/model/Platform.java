package oop.arkanoid.model;

class Platform extends Barrier {
    Platform(Point position, Point size) {
        super(position, size);
        Trajectory trajectory = new Trajectory(new Point(1, 0), new Point(position.x(), position.x() + size.x()), new Point(position.y(), position.y()));
        trajectory.recountB(new Point(position.x(), position.y()));
        trajectories.put(TrajectoryType.TOP_SIDE, trajectory);
    }

    @Override
    CollisionResult hasVisibleCollisions(Trajectory trajectory) {
        if (trajectories.get(TrajectoryType.TOP_SIDE).hasIntersection(trajectory)) {
            return CollisionResult.NEED_TO_CHANGE_ANGLE;
        }
        return null;
    }

}