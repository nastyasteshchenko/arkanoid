package oop.arkanoid.model;

import java.util.EnumMap;

public class Wall extends Barrier {

        private final LinearEquation linearEquation;
        private final CollisionPlace collisionPlace;

        Wall(Point position, Point size, CollisionPlace collisionPlace, double sceneWidth) {
            super(position, size);
            this.collisionPlace = collisionPlace;
            linearEquation = switch (collisionPlace) {
                case LEFT -> LinearEquation.xlinearMotionEquation(size.x(), new Point(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
                case RIGHT -> LinearEquation.xlinearMotionEquation(sceneWidth, new Point(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
                case TOP -> LinearEquation.linearEquation(0, 0, new Point(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
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