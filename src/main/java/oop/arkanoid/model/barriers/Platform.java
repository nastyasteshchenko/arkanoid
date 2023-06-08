package oop.arkanoid.model.barriers;

import oop.arkanoid.model.*;

import java.util.EnumMap;

public final class Platform extends Barrier {

    private final EnumMap<CollisionPlace, LinearEquation> linearEquations = new EnumMap<>(CollisionPlace.class);

    public Platform(Point position, Point size) {
        super(position, size);

        linearEquations.put(CollisionPlace.TOP, LinearEquation.linearEquation(0, position.y(), new Point(position.x(), position.x() + size.x())));
        linearEquations.put(CollisionPlace.LEFT, LinearEquation.xLinearMotionEquation(position.x(), new Point(position.y(), position.y() + size.y())));
        linearEquations.put(CollisionPlace.RIGHT, LinearEquation.xLinearMotionEquation(position.x() + size.x(), new Point(position.y(), position.y() + size.y())));
    }


    public void update(double x) {
        position.setX(x);
        linearEquations.clear();

        linearEquations.put(CollisionPlace.TOP, LinearEquation.linearEquation(0, position.y(), new Point(position.x(), position.x() + size.x())));
        linearEquations.put(CollisionPlace.LEFT, LinearEquation.xLinearMotionEquation(position.x(), new Point(position.y(), position.y() + size.y())));
        linearEquations.put(CollisionPlace.RIGHT, LinearEquation.xLinearMotionEquation(position.x() + size.x(), new Point(position.y(), position.y() + size.y())));

    }

    public void isCollisionWithBall(CircleEquation circleEquation) throws GeneratingGameException {
        if (!linearEquations.get(CollisionPlace.TOP).hasIntersection(circleEquation)){
            throw GeneratingGameException.ballIsNotOnPlatform();
        }
    }

    @Override
    EnumMap<CollisionPlace, LinearEquation> getLinearEquations() {
        return linearEquations;
    }
}