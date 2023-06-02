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
        for (var entry : linearEquations.entrySet()) {
            if (entry.getValue().hasIntersection(circleEquation)) {
                return entry.getKey();
            }
        }
        return null;
    }

    abstract EnumMap<CollisionPlace, LinearEquation> getLinearEquations();

}