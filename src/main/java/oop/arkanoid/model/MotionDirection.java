package oop.arkanoid.model;

enum MotionDirection {
    LEFT,
    RIGHT;

    MotionDirection flip() {
        return this == LEFT ? RIGHT : LEFT;
    }

    MotionDirection flipByPlatform(double diffXBetweenBallAndCenterPlatform) {
        return diffXBetweenBallAndCenterPlatform > 0 ? LEFT : RIGHT;
    }
}
