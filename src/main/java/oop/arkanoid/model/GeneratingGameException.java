package oop.arkanoid.model;

public class GeneratingGameException extends Exception {

    private GeneratingGameException(String message) {
        super(message);
    }

    public static GeneratingGameException collisionWithOtherObjects() {
        return new GeneratingGameException("Detected collision with other object");
    }

    public static GeneratingGameException outOfScene() {
        return new GeneratingGameException("Detected object is out of scene");
    }

    public static GeneratingGameException ballIsNotOnPlatform() {
        return new GeneratingGameException("Ball is not on the platform");
    }

    public static GeneratingGameException wrongWallPosition() {
        return new GeneratingGameException("Wrong wall position");
    }

    public static GeneratingGameException addingBottomWall() {
        return new GeneratingGameException("Can't add bottom wall");
    }

    public static GeneratingGameException unsupportedHealth() {
        return new GeneratingGameException("Unsupported health");
    }

    static GeneratingGameException uninitializedObjects() {
        return new GeneratingGameException("Some objects are uninitialized");
    }

}
