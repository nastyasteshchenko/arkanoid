package oop.arkanoid.model;

public class GeneratingGameException extends Exception {

    private GeneratingGameException(String message) {
        super(message);
    }

    static GeneratingGameException collisionWithOtherObjects() {
        return new GeneratingGameException("Detected collision with other object");
    }

    static GeneratingGameException outOfScene() {
        return new GeneratingGameException("Detected object is out of scene");
    }

    static GeneratingGameException ballIsNotOnPlatform() {
        return new GeneratingGameException("Ball is not on the platform");
    }

    static GeneratingGameException uninitObjects() {
        return new GeneratingGameException("Some objects are uninitialized");
    }

}
