package oop.arkanoid.model;

import com.google.gson.JsonObject;
import oop.arkanoid.model.barriers.*;

import java.util.ArrayList;
import java.util.List;

import static oop.arkanoid.model.RangeChecker.checkRange;

public class GameLevel {
    private final Platform platform;
    private final List<Barrier> barriers;
    private final Ball ball;
    private final Point sceneSize;
    private int score = 0;
    private double speed = 1.5;

    GameLevel(Ball ball, Platform platform, List<Barrier> barriers, Point sceneSize) {
        this.ball = ball;
        this.platform = platform;
        this.barriers = barriers;
        this.sceneSize = sceneSize;
    }

    public Point nextBallPosition() {
        return ball.move(speed, barriers);
    }

    public double updatePlatformPosition(double x) {
        if (checkRange(0, platform.size.x() / 2, x)) {
            platform.update(0);
        } else if (checkRange(sceneSize.x() - platform.size.x() / 2, sceneSize.x(), x)) {
            platform.update(sceneSize.x() - platform.size.x());
        } else {
            platform.update(x - platform.size.x() / 2);
        }
        return platform.position.x();
    }

    public GameStates gameState() {
        if (barriers.stream().noneMatch(barrier -> barrier instanceof Brick brick && !(brick.health instanceof Health.Immortal))) {
            return GameStates.GAME_WIN;
        } else if (ball.getPosition().y() > sceneSize.y()) {
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

    public void increaseScore(int value) {
        score += value;
    }

    static class Builder {
        private final List<Barrier> barriers = new ArrayList<>();
        private Platform platform;
        private Ball ball;
        private final Point sceneSize;

        Builder(Point sceneSize) {
            this.sceneSize = sceneSize;
        }

        Builder platform(Point position, Point size) throws GeneratingGameException {
            platform = new Platform(position, size);
            platform.checkIfCollisions(barriers);
            platform.checkIfOutOfScene(sceneSize);
            barriers.add(platform);
            return this;
        }

        Builder ball(Point position, double radius) {
            ball = new Ball(radius, position);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        Builder addBrick(Point position, Point size, int health) throws GeneratingGameException {
            Brick brick = health == -1 ? new Brick(position, size, Health.createImmortal()) : new Brick(position, size, new Health(health));
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
            platform.isCollisionWithBall(new CircleEquation(ball.getPosition(), ball.radius + 2));
            checkUninitObjects();
            return new GameLevel(ball, platform, barriers, sceneSize);
        }

        private void checkUninitObjects() throws GeneratingGameException {
            if (ball == null || platform == null || sceneSize == null || barriers.isEmpty()) {
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