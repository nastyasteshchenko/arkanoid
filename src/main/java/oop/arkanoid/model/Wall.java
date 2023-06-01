package oop.arkanoid.model;

import java.util.EnumMap;

public class Wall extends Barrier {

        private final LinearEquation linearEquation;
        private final CollisionPlace collisionPlace;

        Wall(Point position, Point size, CollisionPlace collisionPlace) {
            super(position, size);
            this.collisionPlace = collisionPlace;
            linearEquation = switch (collisionPlace) {
                case LEFT, RIGHT -> LinearEquation.xLinearMotionEquation(position.x(), new Point(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
                case BOTTOM -> LinearEquation.linearEquation(0, 0, new Point(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
                case TOP -> throw new UnsupportedOperationException();
            };
        }

        @Override
        EnumMap<CollisionPlace, LinearEquation> getLinearEquations() {
            var result = new EnumMap<CollisionPlace, LinearEquation>(CollisionPlace.class);
            result.put(collisionPlace, linearEquation);
            return result;
        }
    }