package oop.arkanoid.model;

import com.google.gson.JsonObject;
import oop.arkanoid.model.barriers.*;

import java.util.ArrayList;
import java.util.List;

import static oop.arkanoid.model.barriers.Barrier.inSegment;

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

    public static void increaseScore(int value) {
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
        Builder addBrick(Point position, Point size, int health) throws GeneratingGameException {
            Brick brick;
            if (health == -1) {
                brick = new Brick(position, size, Health.createImmortal());
            } else {
                brick = new Brick(position, size, new Health(health));
            }
            brick.checkIfOutOfScene(sceneSize);
            brick.checkIfCollisions(barriers);
            if (health != -1) {
                bricks.add(brick);
            }
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

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public int getScore() {
        return score;
    }

    public Ball getBall() {
        return ball;
    }

    public Point getSceneSize() {
        return sceneSize;
    }

}