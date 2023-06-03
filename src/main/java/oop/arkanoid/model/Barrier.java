package oop.arkanoid.model;

import javafx.util.Pair;

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

    final CollisionPlace findCollision(CircleEquation circleEquation) {
        CollisionPlace collisionPlace = null;

        Pair<CollisionPlace, LinearEquation> vertical = null;
        Pair<CollisionPlace, LinearEquation> horizontal = null;

        var linearEquations = getLinearEquations();
        for (var entry : linearEquations.entrySet()) {
            if (entry.getValue().hasIntersection(circleEquation)) {
                if (entry.getKey() == CollisionPlace.LEFT || CollisionPlace.RIGHT == entry.getKey()) {
                    vertical = new Pair<>(entry.getKey(), entry.getValue());
                } else {
                    horizontal = new Pair<>(entry.getKey(), entry.getValue());
                }
                collisionPlace = entry.getKey();
            }
        }

        if (collisionPlace==null) {
            return null;
        }

        if (vertical == null || horizontal == null) {
            return collisionPlace;
        }

        double distanceY = vertical.getValue().findDistance(circleEquation, horizontal.getKey());
        double distanceX = horizontal.getValue().findDistance(circleEquation, vertical.getKey());

        return distanceX > distanceY ? vertical.getKey() : horizontal.getKey();
    }

    abstract EnumMap<CollisionPlace, LinearEquation> getLinearEquations();

}