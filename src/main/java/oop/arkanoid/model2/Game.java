package oop.arkanoid.model2;

import java.util.List;

// TODO the model should be self-sufficient, no need tons of API for controller/presenter
// TODO use custom event notifications, if possible, e.g. for destroying the bricks
public class Game {
    private List<Wall> walls;
    private List<Brick> bricks;
    private Platform platform;

    private List<Barrier> barriers;

    private Ball ball;

    int speed;
    int score;

    Point nextBallPosition() {
        return ball.nextPosition(speed, barriers);
    }

    // TODO proper synchronization if two timers are initialized
    void updatePlatformPosition(int newX) {

    }

    boolean gameOver() {
        return bricks.isEmpty();
    }

    private void updateSpeed(int speed) {
        // TODO check correctness
        this.speed = speed;
    }

    static class Builder {

        Builder platform() {
            return this;
        }

        Builder ball() {
            return this;
        }

        Builder addBrick(Point position, Point size, Health health) {
            // TODO check collisions
            return this;
        }

        Game build() {
            // TODO check that ball in on the platform
            // TODO check if Game is correctly set up
            return new Game();
        }
    }

}
