package oop.arkanoid.model;

import java.util.EnumMap;

//Сделать чтобы не выходила за стенки
class Platform extends Barrier {

    private final EnumMap<CollisionPlace, LinearEquation> linearEquations = new EnumMap<>(CollisionPlace.class);

    Platform(Point position, Point size) {
        super(position, size);

        linearEquations.put(CollisionPlace.TOP, LinearEquation.linearEquation(0, position.y()));
        linearEquations.put(CollisionPlace.LEFT, LinearEquation.xlinearMotionEquation(position.x()));
        linearEquations.put(CollisionPlace.RIGHT, LinearEquation.xlinearMotionEquation(position.x() + size.x()));
    }

    @Override
    EnumMap<CollisionPlace, LinearEquation> getLinearEquations() {
        return linearEquations;
    }
}