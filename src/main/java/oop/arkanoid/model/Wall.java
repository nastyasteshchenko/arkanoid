package oop.arkanoid.model;

public class Wall extends Barrier {

    WallType type;

    Wall(Point position, Point size, WallType type) {
        super(position, size);
        this.type = type;

        if (type == WallType.TOP) {
            Trajectory topTrajectory = new Trajectory(new Point(1, 0), new Point(position.x(), position.x() + size.x()), new Point(position.y(), position.y() + size.y()));
            topTrajectory.b = position.y();
            trajectories.put(TrajectoryType.BOTTOM_SIDE, topTrajectory);

        } else {
            Trajectory trajectory = new Trajectory(new Point(0, 1), new Point(position.x(), position.x() + size.x()), new Point(position.y(), position.y() + size.y()));
            trajectory.b = position.x();
            if (type == WallType.RIGHT) {
                trajectories.put(TrajectoryType.LEFT_SIDE, trajectory);
            } else {
                trajectories.put(TrajectoryType.RIGHT_SIDE, trajectory);
            }
        }
    }

    @Override
    boolean hasVisibleCollisions(Trajectory trajectory, double radius) {
        switch (type) {
            case RIGHT -> {
                return trajectories.get(TrajectoryType.LEFT_SIDE).hasIntersection(trajectory, radius);
            }
            case LEFT -> {
                return trajectories.get(TrajectoryType.RIGHT_SIDE).hasIntersection(trajectory, radius);
            }
            case TOP -> {
                return trajectories.get(TrajectoryType.BOTTOM_SIDE).hasIntersection(trajectory, radius);
            }
            default -> {
                return false;
            }
        }
    }
}