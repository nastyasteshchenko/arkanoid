package oop.arkanoid.model.barrier;

import oop.arkanoid.model.*;
import oop.arkanoid.model.motion.LinearEquation;

import java.util.EnumMap;

public final class Platform extends Barrier {

    private final EnumMap<CollisionPlace, LinearEquation> linearEquations = new EnumMap<>(CollisionPlace.class);

    public Platform(Point position, Point size) {
        super(position, size);

        linearEquations.put(CollisionPlace.TOP, LinearEquation.linearEquation(0, position));
        linearEquations.put(CollisionPlace.LEFT, LinearEquation.xLinearMotionEquation(position.x()));
        linearEquations.put(CollisionPlace.RIGHT, LinearEquation.xLinearMotionEquation(position.x() + size.x()));
    }


    public void update(double x) {
        position = new Point(x, position.y());
        linearEquations.clear();

        linearEquations.put(CollisionPlace.TOP, LinearEquation.linearEquation(0, position));
        linearEquations.put(CollisionPlace.LEFT, LinearEquation.xLinearMotionEquation(position.x()));
        linearEquations.put(CollisionPlace.RIGHT, LinearEquation.xLinearMotionEquation(position.x() + size.x()));

    }

    public void checkCollisionWithBall(CircleEquation circleEquation) throws GeneratingGameException {
        if (linearEquations.get(CollisionPlace.TOP).getIntersectionPoints(circleEquation).isEmpty()){
            throw GeneratingGameException.ballIsNotOnPlatform();
        }
    }

    @Override
    EnumMap<CollisionPlace, LinearEquation> getLinearEquations() {
        return linearEquations;
    }
}