package oop.arkanoid.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

abstract class Barrier {

    final Point position;
    final Point size;

    Barrier(Point position, Point size) {
        this.position = position;
        this.size = size;
    }

    final HashMap<CollisionPlace, LinearEquation> findCollision(CircleEquation circleEquation) {
        HashMap<CollisionPlace, LinearEquation> collisionPlaces = new HashMap<>();
        var linearEquations = getLinearEquations();
        for (var entry : linearEquations.entrySet()) {
            if (entry.getValue().hasIntersection(circleEquation)) {
                collisionPlaces.put(entry.getKey(), entry.getValue());
            }
        }
        return collisionPlaces;
    }

    abstract EnumMap<CollisionPlace, LinearEquation> getLinearEquations();

}