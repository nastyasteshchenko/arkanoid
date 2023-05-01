
//
//    public void addDoubleHitBrickBrick(double x, double y, double width, double height, String id) {
//        bricks.put(id, new DoubleHitBrick(x, y, width, height));
//    }
//
//    public void addIndestructibleBrick(double x, double y, double width, double height, String id) {
//        bricks.put(id, new IndestructibleBrick(x, y, width, height));
//    }
//
//    public Point recountBallCoordinates() {
//
//        if (ball.getCenterY() >= sceneHeight + 2 * ball.getRadius()) {
//            isGameOver = true;
//        }
//
//        ball.setAngle(countReboundingAngle());
//
//        ball.renewCoordinates();
//
//        return new Point(ball.getCenterX(), ball.getCenterY());
//    }
//
//    public int getAmountOfBreakableBricks() {
//        return amountOfBreakableBricks;
//    }
//
//    public int getScore() {
//        return score;
//    }
//
//    public boolean isGameOver() {
//        return isGameOver;
//    }
//}

package oop.arkanoid.model;

import java.util.List;

// TODO the model should be self-sufficient, no need tons of API for controller/presenter
// TODO use custom event notifications, if possible, e.g. for destroying the bricks
public class Game {
    private List<Wall> walls;
    private List<Brick> bricks;
    private Platform platform;

    private List<Barrier> barriers;

    private Ball ball;

    Game(Ball ball, Platform platform, List<Brick> bricks, List<Barrier> barriers, List<Wall> walls) {
        this.ball = ball;
        this.platform = platform;
        this.bricks = bricks;
        this.barriers = barriers;
        this.walls = walls;
    }

    int score;

    Point nextBallPosition() {
        return ball.nextPosition(barriers);
    }

    // TODO proper synchronization if two timers are initialized
    void updatePlatformPosition(int newX) {
 platform.position.setX(newX);
    }

    boolean gameOver() {
        return bricks.isEmpty();
    }

    private void updateSpeed(int speed) {
        ball.speed = speed;
    }

    static class Builder {

        private List<Wall> walls;
        private List<Brick> bricks;
        private Platform platform;

        private List<Barrier> barriers;

        private Ball ball;

        Builder platform(double x, double y, double width, double height) {

            platform = new Platform(createPoint(x, y), createPoint(width, height));

            return this;
        }

        Builder ball(double radius, double centerX, double centerY) {
            ball = new Ball(radius, createPoint(centerX, centerY));
            return this;
        }

        Builder addBrick(Point position, Point size, Health health) {
            // TODO check collisions
            return this;
        }

        Builder addWall(double x, double y, double width, double height) {
            walls.add(new Wall(createPoint(x, y), createPoint(width, height)));
            return this;
        }

        Game build() {
            // TODO check that ball in on the platform
            // TODO check if Game is correctly set up
            return new Game(ball, platform, bricks, barriers, walls);
        }

        private Point createPoint(double x, double y) {
            return new Point(x, y);
        }
    }


}