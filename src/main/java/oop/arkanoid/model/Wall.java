package oop.arkanoid.model;

import java.util.EnumMap;

public class Wall extends Barrier {

        private final LinearEquation linearEquation;
        private final CollisionPlace collisionPlace;

        Wall(Point position, Point size, CollisionPlace collisionPlace, double sceneWidth) {
            super(position, size);
            this.collisionPlace = collisionPlace;
            linearEquation = switch (collisionPlace) {
                case LEFT -> LinearEquation.xlinearMotionEquation(size.x());
                case RIGHT -> LinearEquation.xlinearMotionEquation(sceneWidth - 0.5);
                case TOP -> LinearEquation.linearEquation(0, 0.5);
                case BOTTOM -> throw new UnsupportedOperationException();
            };
        }

        @Override
        EnumMap<CollisionPlace, LinearEquation> getLinearEquations() {
            var result = new EnumMap<CollisionPlace, LinearEquation>(CollisionPlace.class);
            result.put(collisionPlace, linearEquation);
            return result;
        }
    }