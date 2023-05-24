package oop.arkanoid.model;

import oop.arkanoid.Notifications;

class Brick extends Barrier implements Destroyable {

    private final int score;
    final Health health;

    Brick(Point position, Point size, Health health) {
        super(position, size);
        this.health = health;
        score = health.getValue() * 5;

        Trajectory bottomTrajectory = new Trajectory(new Point(1, 0), new Point(position.x(), position.x() + size.x()), new Point(position.y() + size.y(), position.y() + size.y()));
        bottomTrajectory.b = position.y() + size.y();
        trajectories.put(TrajectoryType.BOTTOM_SIDE, bottomTrajectory);

        Trajectory topTrajectory = new Trajectory(new Point(1, 0), new Point(position.x(), position.x() + size.x()), new Point(position.y(), position.y()));
        topTrajectory.b = position().y();
        trajectories.put(TrajectoryType.TOP_SIDE, topTrajectory);

        Trajectory leftSideTrajectory = new Trajectory(new Point(0, 1), new Point(position.x(), position.x()), new Point(position.y(), position.y() + size.y()));
        leftSideTrajectory.b = position.x();
        trajectories.put(TrajectoryType.LEFT_SIDE, leftSideTrajectory);

        Trajectory rightSideTrajectory = new Trajectory(new Point(0, 1), new Point(position.x() + size.x(), position.x() + size.x()), new Point(position.y(), position.y() + size.y()));
        rightSideTrajectory.b = position.x() + size.x();
        trajectories.put(TrajectoryType.RIGHT_SIDE, rightSideTrajectory);
    }

    @Override
    public boolean isAlive() {
        return health.isAlive();
    }

    @Override
    public Point position() {
        return position;
    }

    @Override
    public void onHit() {
        health.decrease();
        if (!isAlive()) {
            GameLevel.increaseScore(score);
            Notifications.getInstance().publish(Notifications.EventType.DESTROY, this);
        }
    }

    //TODO подумать над вверх/вниз
    @Override
    boolean hasVisibleCollisions(Trajectory trajectory, double radius) {
        return trajectories.get(TrajectoryType.BOTTOM_SIDE).hasIntersection(trajectory, radius) || trajectories.get(TrajectoryType.TOP_SIDE).hasIntersection(trajectory, radius) || trajectories.get(TrajectoryType.LEFT_SIDE).hasIntersection(trajectory, radius) || trajectories.get(TrajectoryType.RIGHT_SIDE).hasIntersection(trajectory, radius);
    }

}