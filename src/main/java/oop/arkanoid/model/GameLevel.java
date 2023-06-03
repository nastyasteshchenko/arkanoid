package oop.arkanoid.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import static oop.arkanoid.model.Barrier.inSegment;

public class GameLevel {
    private final List<Brick> destroyableBricks;
    private final Platform platform;
    private final List<Barrier> barriers;
    private final Ball ball;
    private final Point sceneSize;
    private static int score;
    private double speed;

    GameLevel(Ball ball, Platform platform, List<Brick> bricks, List<Barrier> barriers, Point scene) {
        this.ball = ball;
        this.platform = platform;
        this.destroyableBricks = bricks;
        this.barriers = barriers;
        this.sceneSize = scene;
        score = 0;
        speed = 1.5;
    }

    public Point nextBallPosition() {
        Point newBallPos = ball.move(speed, barriers);

        ArrayList<Brick> toRemove = new ArrayList<>();
        destroyableBricks.stream().filter(brick -> !barriers.contains(brick)).forEach(toRemove::add);
        toRemove.forEach(destroyableBricks::remove);

        return newBallPos;
    }

    public double updatePlatformPosition(double x) {
        if (inSegment(0, platform.size.x() / 2, x)) {
            platform.update(0);
        } else if (inSegment(sceneSize.x() - platform.size.x() / 2, sceneSize.x(), x)) {
            platform.update(sceneSize.x() - platform.size.x());
        } else {
            platform.update(x - platform.size.x() / 2);
        }
        return platform.position.x();
    }

    public GameStates gameState() {
        if (destroyableBricks.isEmpty()) {
            return GameStates.GAME_WIN;
        } else if (ball.position.y() > sceneSize.y()) {
            return GameStates.GAME_LOSE;
        } else {
            return GameStates.GAME_IN_PROCESS;
        }
    }

    public static GameLevel initLevel(JsonObject object) throws GeneratingGameException {
        return new LevelInitiator(object).initLevel();
    }

    public void updateSpeed(double speed) {
        this.speed = speed;
    }

    static void increaseScore(int value) {
        score += value;
    }

    static class Builder {
        private final List<Brick> bricks = new ArrayList<>();
        private final List<Barrier> barriers = new ArrayList<>();
        private Platform platform;
        private Ball ball;
        private final Point sceneSize;

        Builder(Point sceneSize) {
            this.sceneSize = sceneSize;
        }

        Builder platform(Point position, Point size) {
            platform = new Platform(position, size);
            barriers.add(platform);
            return this;
        }

        Builder ball(Point position, double radius) {
            ball = new Ball(radius, position);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        Builder addDestroyableBrick(Point position, Point size, int health) throws GeneratingGameException {
            Brick brick = new Brick(position, size, new Health(health));
            brick.checkIfOutOfScene(sceneSize);
            brick.checkIfCollisions(barriers);
            bricks.add(brick);
            barriers.add(brick);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        Builder addImmortalBrick(Point position, Point size) throws GeneratingGameException {
            Brick brick = new Brick(position, size, Health.createImmortal());
            brick.checkIfOutOfScene(sceneSize);
            brick.checkIfCollisions(barriers);
            barriers.add(brick);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        Builder addWall(Point position, Point size, CollisionPlace place) throws GeneratingGameException {
            Wall wall = new Wall(position, size, place);
            wall.checkWall(sceneSize, position);
            barriers.add(wall);
            return this;
        }

        GameLevel build() throws GeneratingGameException {

            platform.isCollisionWithBall(new CircleEquation(ball.position, ball.radius + 2));
            checkUninitObjects();

            return new GameLevel(ball, platform, bricks, barriers, sceneSize);
        }

        private void checkUninitObjects() throws GeneratingGameException {
            if (ball == null || platform == null || sceneSize == null || bricks.isEmpty() || barriers.isEmpty()) {
                throw GeneratingGameException.uninitObjects();
            }
        }
    }

    public ArrayList<Point> getStandardBricks() {
        ArrayList<Point> bricks = new ArrayList<>();
        this.destroyableBricks.stream().filter(brick -> brick.health.getValue() == 1).forEach(brick -> bricks.add(brick.position));
        return bricks;
    }

    public ArrayList<Point> getDoubleHitBricks() {
        ArrayList<Point> bricks = new ArrayList<>();
        this.destroyableBricks.stream().filter(brick -> brick.health.getValue() == 2).forEach(brick -> bricks.add(brick.position));
        return bricks;
    }

    public ArrayList<Point> getImmortalBricks() {
        ArrayList<Point> bricks = new ArrayList<>();
        barriers.stream().filter(barrier -> barrier instanceof Brick && ((Brick) barrier).health instanceof Health.Immortal).forEach(brick -> bricks.add(brick.position));
        return bricks;
    }

    public int getScore() {
        return score;
    }

    public Point getBrickSize() {
        return destroyableBricks.get(0).size;
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
        return sceneSize;
    }

}