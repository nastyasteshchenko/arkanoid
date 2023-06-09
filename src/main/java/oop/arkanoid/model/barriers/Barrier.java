package oop.arkanoid.model.barriers;

import javafx.util.Pair;
import oop.arkanoid.model.*;

import java.util.EnumMap;
import java.util.List;

import static oop.arkanoid.model.RangeChecker.checkRange;


public abstract sealed class Barrier permits Wall, Brick, Platform {

    public final Point position;
    public final Point size;

    Barrier(Point position, Point size) {
        this.position = position;
        this.size = size;
    }

    public CollisionPlace findCollision(CircleEquation circleEquation) {

        Pair<CollisionPlace, Double> vertical = null;
        Pair<CollisionPlace, Double> horizontal = null;

        var linearEquations = getLinearEquations();
        for (var entry : linearEquations.entrySet()) {
            List<Double> intersectionPoints = entry.getValue().findIntersectionPoints(circleEquation);
            if (!intersectionPoints.isEmpty()) {
                if (entry.getKey() == CollisionPlace.LEFT || CollisionPlace.RIGHT == entry.getKey()) {
                    for (Double root : intersectionPoints) {
                        if (checkRange(position.y(), position.y() + size.y(), root)) {
                            vertical = new Pair<>(entry.getKey(), root);
                        }
                    }
                } else {
                    for (Double root : intersectionPoints) {
                        if (checkRange(position.x(), position.x() + size.x(), root)) {
                            horizontal = new Pair<>(entry.getKey(), root);
                        }
                    }
                }
            }
        }

        if (vertical==null && horizontal == null) {
            return null;
        }

        if (vertical == null || horizontal == null) {
            if (vertical!= null){
                return vertical.getKey();
            }
            return horizontal.getKey();
        }

        double distanceX;
        if (horizontal.getKey() == CollisionPlace.LEFT) {
            distanceX = Math.abs(horizontal.getValue() - position.x());
        } else {
            distanceX = Math.abs(horizontal.getValue() - position.x() - size.x());
        }

        double distanceY;
        if (vertical.getKey() == CollisionPlace.TOP) {
            distanceY = Math.abs(vertical.getValue() - position.y());
        } else {
            distanceY = Math.abs(vertical.getValue() - position.y() - size.y());
        }

       return distanceX <= distanceY ? vertical.getKey() : horizontal.getKey();
    }

    abstract EnumMap<CollisionPlace, LinearEquation> getLinearEquations();

    public void checkIfCollisions(List<Barrier> barriers) throws GeneratingGameException {
        if (barriers.stream().anyMatch(this::hasCollisionWithObject)) {
            throw GeneratingGameException.collisionWithOtherObjects();
        }
    }

    public void checkIfOutOfScene(Point scene) throws GeneratingGameException {
        if (!checkRange(0, scene.x(), position.x()) || !checkRange(0, scene.y(), position.y()) || !checkRange(0, scene.x(), position.x() + size.x()) || !checkRange(0, scene.y(), position.y() + size.y())) {
            throw GeneratingGameException.outOfScene();
        }
    }

    private boolean hasCollisionWithObject(Barrier barrier) {
        return (checkRange(barrier.position.x(), barrier.position.x() + barrier.size.x(), position.x()) || checkRange(barrier.position.x(), barrier.position.x() + barrier.size.x(), position.x() + size.x()))
                && (checkRange(barrier.position.y(), barrier.position.y() + barrier.size.y(), position.y()) || checkRange(barrier.position.y(), barrier.position.y() + barrier.size.y(), position.y() + size.y()));
    }
}