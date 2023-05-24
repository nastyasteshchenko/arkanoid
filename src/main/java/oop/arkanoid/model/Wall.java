package oop.arkanoid.model;

public class Wall extends Barrier {

    wallType type;

    Wall(Point position, Point size, wallType type) {
        super(position, size);
        this.type = type;

        if (type == wallType.TOP) {
            Trajectory topTrajectory = new Trajectory(new Point(1, 0), new Point(position.x(), position.x() + size.x()), new Point(position.y(), position.y() + size.y()));
            topTrajectory.recountB(new Point(position.x(), position.y()));
            trajectories.put(TrajectoryType.BOTTOM_SIDE, topTrajectory);

        } else {
            Trajectory trajectory = new Trajectory(new Point(0, 1), new Point(position.x(), position.x() + size.x()), new Point(position.y(), position.y() + size.y()));
            trajectory.b = position.x();
            if (type == wallType.RIGHT) {
                trajectories.put(TrajectoryType.LEFT_SIDE, trajectory);
            } else {
                trajectories.put(TrajectoryType.RIGHT_SIDE, trajectory);
            }
        }
    }

    @Override
    CollisionResult hasVisibleCollisions(Trajectory trajectory) {
        switch (type) {
            case RIGHT -> {
                if (trajectories.get(TrajectoryType.LEFT_SIDE).hasIntersection(trajectory)) {
                    return CollisionResult.NO_ANGLE_CHANGE;
                }
            }
            case LEFT -> {
                if (trajectories.get(TrajectoryType.RIGHT_SIDE).hasIntersection(trajectory)) {
                    return CollisionResult.NO_ANGLE_CHANGE;
                }
            }
            case TOP -> {
                if (trajectories.get(TrajectoryType.BOTTOM_SIDE).hasIntersection(trajectory)) {
                    return CollisionResult.NEED_TO_CHANGE_ANGLE;
                }
            }
        }
        return null;
    }
}