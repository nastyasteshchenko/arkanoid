package oop.arkanoid.model;

import oop.arkanoid.view.LevelView;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// TODO the model should be self-sufficient, no need tons of API for controller/presenter
// TODO use custom event notifications, if possible, e.g. for destroying the bricks
public class Game {
    private final List<Brick> bricks;
    private final Platform platform;
    private final List<Barrier> barriers;

    private boolean gameOver = false;
    private final Ball ball;

    Game(Ball ball, Platform platform, List<Brick> bricks, List<Barrier> barriers) {
        this.ball = ball;
        this.platform = platform;
        this.bricks = bricks;
        this.barriers = barriers;
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

    public boolean gameWin() {
        return bricks.isEmpty();
    }

    public boolean gameOver() {
        return gameOver;
    }

    protected static Double getPropertyInDouble(String key, Properties properties) {
        return Double.parseDouble(properties.getProperty(key));
    }

    public static Game initLevel(Properties properties) {
        return new LevelInitor(properties).initLevel();
    }

    private void updateSpeed(int speed) {
        ball.speed = speed;
    }

    private Point createPoint(double x, double y) {
        return new Point(x, y);
    }

    public static class Builder {
        private final List<Brick> bricks = new ArrayList<>();
        private Platform platform;

        private final List<Barrier> barriers = new ArrayList<>();

        private Ball ball;

        public Builder platform(Point position, Point size) {
            platform = new Platform(position, size);
            barriers.add(platform);
            return this;
        }

        public Builder ball(Point position, double radius) {
            ball = new Ball(position, radius);
            return this;
        }

        public Builder addDestroyableBrick(Point position, Point size, int health) {
            Brick barrier = new Brick(position, size, new Health(health));
            bricks.add(barrier);
            barriers.add(barrier);
            return this;
        }

        public Builder addImmortalBrick(Point position, Point size) {
            Barrier barrier = new Brick(position, size, Health.createImmortal());
            barriers.add(barrier);
            // TODO check collisions
            return this;
        }

        public Builder addWall(Point position, Point size) {
            barriers.add(new Wall(position, size));
            return this;
        }

        public Game build() {
            // TODO check that ball in on the platform
            // TODO check if Game is correctly set up
            return new Game(ball, platform, bricks, barriers);
        }
    }


}