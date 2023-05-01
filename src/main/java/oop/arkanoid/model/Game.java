
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

import java.util.ArrayList;
import java.util.List;

// TODO the model should be self-sufficient, no need tons of API for controller/presenter
// TODO use custom event notifications, if possible, e.g. for destroying the bricks
public class Game {
    private List<Brick> bricks;
    private Platform platform;

    private List<Barrier> barriers;

    private Ball ball;

    Game(Ball ball, Platform platform, List<Brick> bricks, List<Barrier> barriers) {
        this.ball = ball;
        this.platform = platform;
        this.bricks = bricks;
        this.barriers = barriers;
    //    this.walls = walls;
    }

    int score;

    public Point nextBallPosition() {
        return ball.nextPosition(barriers);
    }

    // TODO proper synchronization if two timers are initialized
    public double updatePlatformPosition(double x) {


            platform.position.setX(x - platform.size.x() / 2);


        return platform.position.x();
    }

   public boolean gameOver() {
        return bricks.isEmpty();
    }

    private void updateSpeed(int speed) {
        ball.speed = speed;
    }

    public static class Builder {

        private final List<Wall> walls = new ArrayList<>();
        private final List<Brick> bricks = new ArrayList<>();
        private Platform platform;

        private final List<Barrier> barriers = new ArrayList<>();

        private Ball ball;

        public Builder() {
        }

        public Builder platform(double x, double y, double width, double height) {
            platform = new Platform(createPoint(x, y), createPoint(width, height));
            barriers.add(platform);
            return this;
        }

        public Builder ball(double radius, double centerX, double centerY) {
            ball = new Ball(radius, createPoint(centerX, centerY));
            return this;
        }

        public Builder addDestroyableBrick(double x, double y, double width, double height, int health) {
            Brick barrier = new Brick(createPoint(x,y), createPoint(width, height), new Health(health));
            bricks.add(barrier);
            barriers.add(barrier);
            return this;
        }

        public Builder addImmortalBrick(double x, double y, double width, double height) {
            Barrier barrier = new Brick(createPoint(x,y), createPoint(width, height), new Health.Immortal());
            barriers.add(barrier);
            // TODO check collisions
            return this;
        }

        public Builder addWall(double x, double y, double width, double height) {
            barriers.add(new Wall(createPoint(x, y), createPoint(width, height)));
            return this;
        }

        public Game build() {
            // TODO check that ball in on the platform
            // TODO check if Game is correctly set up
            return new Game(ball, platform, bricks, barriers);
        }

        private Point createPoint(double x, double y) {
            return new Point(x, y);
        }
    }


}