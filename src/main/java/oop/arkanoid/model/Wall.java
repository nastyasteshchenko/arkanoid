package oop.arkanoid.model;

public class Wall extends Barrier {

    private final WallType type;

    Wall(Point position, Point size, WallType type) {
        super(position, size);
        this.type = type;
        setTrajectories(type);
    }

    @Override
    boolean hasVisibleCollisions(Trajectory trajectory, double radius) {
        switch (type) {
            case RIGHT -> {
                return trajectories.get(StraightSides.LEFT_SIDE).hasIntersection(trajectory, radius);
            }
            case LEFT -> {
                return trajectories.get(StraightSides.RIGHT_SIDE).hasIntersection(trajectory, radius);
            }
            case TOP -> {
                return trajectories.get(StraightSides.BOTTOM_SIDE).hasIntersection(trajectory, radius);
            }
            default -> {
                return false;
            }
        }
    }

    private void setTrajectories(WallType type) {
        if (type == WallType.TOP) {
            Trajectory topTrajectory = new Trajectory(new Point(1, 0), new Point(position.x(), position.x() + size.x()), new Point(position.y(), position.y() + size.y()));
            topTrajectory.b = position.y();
            trajectories.put(StraightSides.BOTTOM_SIDE, topTrajectory);
        } else {
            Trajectory trajectory = new Trajectory(new Point(0, 1), new Point(position.x(), position.x() + size.x()), new Point(position.y(), position.y() + size.y()));
            trajectory.b = position.x();
            if (type == WallType.RIGHT) {
                trajectories.put(StraightSides.LEFT_SIDE, trajectory);
            } else {
                trajectories.put(StraightSides.RIGHT_SIDE, trajectory);
            }
        }
    }
}