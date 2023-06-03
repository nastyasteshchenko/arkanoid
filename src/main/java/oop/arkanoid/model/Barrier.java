package oop.arkanoid.model;

import javafx.util.Pair;

import java.util.EnumMap;
import java.util.List;


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

        if (collisionPlace == null) {
            return null;
        }

        if (vertical == null || horizontal == null) {
            return collisionPlace;
        }

        double distanceX = horizontal.getValue().getDistanceBallCrossingLine(circleEquation, vertical.getKey());
        double distanceY = vertical.getValue().getDistanceBallCrossingLine(circleEquation, horizontal.getKey());

        return distanceX <= distanceY ? vertical.getKey() : horizontal.getKey();
    }

    abstract EnumMap<CollisionPlace, LinearEquation> getLinearEquations();

    static boolean inSegment(double min, double max, double value) {
        return min <= value && value <= max;
    }

    void checkIfCollisions(List<Barrier> barriers) throws GeneratingGameException {
        if (barriers.stream().anyMatch(this::hasCollisionWithObject)) {
            throw GeneratingGameException.collisionWithOtherObjects();
        }
    }

    void checkIfOutOfScene(Point scene) throws GeneratingGameException {
        if (!inSegment(0, scene.x(), position.x()) || !inSegment(0, scene.y(), position.y()) || !inSegment(0, scene.x(), position.x() + size.x()) || !inSegment(0, scene.y(), position.y() + size.y())) {
            throw GeneratingGameException.outOfScene();
        }
    }

    private boolean hasCollisionWithObject(Barrier barrier) {
        return (inSegment(barrier.position.x(), barrier.position.x() + barrier.size.x(), position.x()) || inSegment(barrier.position.x(), barrier.position.x() + barrier.size.x(), position.x() + size.x()))
                && (inSegment(barrier.position.y(), barrier.position.y() + barrier.size.y(), position.y()) || inSegment(barrier.position.y(), barrier.position.y() + barrier.size.y(), position.y() + size.y()));
    }
}