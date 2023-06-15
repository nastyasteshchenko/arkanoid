package oop.arkanoid.model.motion;

public enum MotionDirection {
    LEFT,
    RIGHT;

    MotionDirection flip() {
        return this == LEFT ? RIGHT : LEFT;
    }

    //TODO: подумать как избавиться от diffXBetweenBallAndCenterPlatform
    MotionDirection flipByPlatform(double diffXBetweenBallAndCenterPlatform) {
        return diffXBetweenBallAndCenterPlatform > 0 ? LEFT : RIGHT;
    }
}
