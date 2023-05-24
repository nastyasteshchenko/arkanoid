package oop.arkanoid.model;

import java.util.HashMap;
import java.util.Map;

enum TrajectoryType {
    LEFT_SIDE,
    RIGHT_SIDE,
    TOP_SIDE,
    BOTTOM_SIDE
}

abstract class Barrier {

    final Point position;
    final Point size;

    Map<TrajectoryType, Trajectory> trajectories = new HashMap<>();

    Barrier(Point position, Point size) {
        this.position = position;
        this.size = size;

    }

    abstract boolean hasVisibleCollisions(Trajectory trajectory, double radius);

}
