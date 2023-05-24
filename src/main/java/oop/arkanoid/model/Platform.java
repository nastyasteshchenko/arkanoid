package oop.arkanoid.model;

class Platform extends Barrier {
    Platform(Point position, Point size) {
        super(position, size);
        Trajectory trajectory = new Trajectory(new Point(1, 0), new Point(position.x(), position.x() + size.x()), new Point(position.y(), position.y()));
        trajectory.recountB(new Point(position.x(), position.y()));
        trajectories.put(StraightSides.TOP_SIDE, trajectory);
    }

    @Override
    boolean hasVisibleCollisions(Trajectory trajectory, double radius) {
        return trajectories.get(StraightSides.TOP_SIDE).hasIntersection(trajectory, radius);
    }

}