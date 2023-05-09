package oop.arkanoid.model;

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

    private final Point scene;

    public ArrayList<Point> getStandardBricks() {
        ArrayList<Point> bricks = new ArrayList<>();
        this.bricks.stream().filter(brick -> brick.health.getValue() == 1).forEach(brick -> bricks.add(brick.position));
        return bricks;
    }

    public ArrayList<Point> getDoubleHitBricks() {
        ArrayList<Point> bricks = new ArrayList<>();
        this.bricks.stream().filter(brick -> brick.health.getValue() == 2).forEach(brick -> bricks.add(brick.position));
        return bricks;
    }

    public ArrayList<Point> getImmortalBricks() {
        ArrayList<Point> bricks = new ArrayList<>();
        barriers.stream().filter(barrier -> barrier instanceof Brick && ((Brick) barrier).health instanceof Health.Immortal).forEach(brick -> bricks.add(brick.position));
        return bricks;
    }

    public Point getBrickSize() {
        return bricks.get(0).size;
    }

    public Point getBallPosition() {
        return ball.position;
    }

    public double getBallRadius() {
        return ball.radius;
    }

    public Point getPlatformSize() {
        return platform.size;
    }

    public Point getPlatformPosition() {
        return platform.position;
    }

    public Point getSceneSize() {
        return scene;
    }


    Game(Ball ball, Platform platform, List<Brick> bricks, List<Barrier> barriers, Point scene) {
        this.ball = ball;
        this.platform = platform;
        this.bricks = bricks;
        this.barriers = barriers;
        this.scene = scene;
    }

    int score;

    public Point nextBallPosition() {
        Point newBallPos = ball.nextPosition(barriers);
        if (newBallPos.y() > scene.y()) {
            gameOver = true;
        }
        ArrayList<Brick> toRemove = new ArrayList<>();
        bricks.stream().filter(brick -> !barriers.contains(brick)).forEach(toRemove::add);
        toRemove.forEach(bricks::remove);
        return newBallPos;
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
        return new LevelInitiator(properties).initLevel();
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

        private Point scene;

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
            Brick brick = new Brick(position, size, new Health(health));
            if (bricks.stream().anyMatch(b -> hasCollision(brick, b))) {
                //TODO think about
                System.out.println("Collision");
            }
            bricks.add(brick);
            barriers.add(brick);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder addImmortalBrick(Point position, Point size) {
            Brick brick = new Brick(position, size, Health.createImmortal());
            if (bricks.stream().anyMatch(b -> hasCollision(brick, b))) {
                //TODO the same
                System.out.println("Collision");
            }
            barriers.add(brick);
            return this;
        }

        public Builder addWall(Point position, Point size) {
            barriers.add(new Wall(position, size));
            return this;
        }

        public Builder scene(Point size) {
            scene = size;
            return this;
        }

        public Game build() {
            // TODO check that ball in on the platform
            // TODO check if Game is correctly set up
            return new Game(ball, platform, bricks, barriers, scene);
        }

        private boolean inSegment(double a, double b, double x) {
            return a <= x && x <= b;
        }

        private boolean hasCollision(Brick b1, Brick b2) {
            return (inSegment(b2.position.x(), b2.position.x() + b2.size.x(), b1.position.x()) || inSegment(b2.position.x(), b2.position.x() + b2.size.x(), b1.position.x() + b1.size.x()))
                    && (inSegment(b2.position.y(), b2.position.y() + b2.size.y(), b1.position.y()) || inSegment(b2.position.y(), b2.position.y() + b2.size.y(), b1.position.y() + b1.size.y()));
        }
    }


}