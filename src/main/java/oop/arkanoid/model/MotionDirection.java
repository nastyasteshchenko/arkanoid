package oop.arkanoid.model;

enum MotionDirection {
    LEFT,
    RIGHT;

    MotionDirection flip() {
        return this == LEFT ? RIGHT : LEFT;
    }

    MotionDirection flipByPlatform(double diffBetweenBallAndCenterPlatform) {
        return diffBetweenBallAndCenterPlatform <= 0 ? LEFT : RIGHT;
    }
}
