package oop.arkanoid.model;

import oop.arkanoid.Notifications;

class Brick extends Barrier implements Destroyable {

    private final int score;
    final Health health;

    Brick(Point position, Point size, Health health) {
        super(position, size);
        this.health = health;
        score = health.getValue() * 5;

        setTrajectories(position, size);
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

    @Override
    boolean hasVisibleCollisions(Trajectory trajectory, double radius) {
        return trajectories.get(StraightSides.BOTTOM_SIDE).hasIntersection(trajectory, radius) || trajectories.get(StraightSides.TOP_SIDE).hasIntersection(trajectory, radius) || trajectories.get(StraightSides.LEFT_SIDE).hasIntersection(trajectory, radius) || trajectories.get(StraightSides.RIGHT_SIDE).hasIntersection(trajectory, radius);
    }

    private void setTrajectories(Point position, Point size) {
        Trajectory bottomTrajectory = new Trajectory(new Point(1, 0), new Point(position.x(), position.x() + size.x()), new Point(position.y() + size.y(), position.y() + size.y()));
        bottomTrajectory.b = position.y() + size.y();
        trajectories.put(StraightSides.BOTTOM_SIDE, bottomTrajectory);

        Trajectory topTrajectory = new Trajectory(new Point(1, 0), new Point(position.x(), position.x() + size.x()), new Point(position.y(), position.y()));
        topTrajectory.b = position().y();
        trajectories.put(StraightSides.TOP_SIDE, topTrajectory);

        Trajectory leftSideTrajectory = new Trajectory(new Point(0, 1), new Point(position.x(), position.x()), new Point(position.y(), position.y() + size.y()));
        leftSideTrajectory.b = position.x();
        trajectories.put(StraightSides.LEFT_SIDE, leftSideTrajectory);

        Trajectory rightSideTrajectory = new Trajectory(new Point(0, 1), new Point(position.x() + size.x(), position.x() + size.x()), new Point(position.y(), position.y() + size.y()));
        rightSideTrajectory.b = position.x() + size.x();
        trajectories.put(StraightSides.RIGHT_SIDE, rightSideTrajectory);
    }

}