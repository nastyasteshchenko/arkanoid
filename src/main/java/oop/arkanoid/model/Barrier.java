//package oop.arkanoid.model;
//
//import java.util.HashMap;
//import java.util.Map;
//
//enum StraightSides {
//    LEFT_SIDE,
//    RIGHT_SIDE,
//    TOP_SIDE,
//    BOTTOM_SIDE
//}
//
//abstract class Barrier {
//
//    final Point position;
//    final Point size;
//    final Map<StraightSides, Trajectory> trajectories = new HashMap<>();
//
//    Barrier(Point position, Point size) {
//        this.position = position;
//        this.size = size;
//
//    }
//
//    abstract boolean hasVisibleCollisions(Trajectory trajectory, double radius);
//
//}

package oop.arkanoid.model;

import java.util.EnumMap;

abstract class Barrier {

    final Point position;
    final Point size;


    Barrier(Point position, Point size) {
        this.position = position;
        this.size = size;

    }

    final CollisionPlace findCollision(CircleEquation circleEquation) {
        var linearEquations = getLinearEquations();
        // TODO detect one place if there are several collisions
        for (var entry : linearEquations.entrySet()) {
            if (entry.getValue().hasIntersection(circleEquation)) {
                return entry.getKey();
            }
        }
        return null;
    }

    abstract EnumMap<CollisionPlace, LinearEquation> getLinearEquations();

}