package oop.arkanoid.model;

import oop.arkanoid.model.barrier.*;
import oop.arkanoid.notifications.EventType;
import oop.arkanoid.notifications.Notifications;

import java.util.ArrayList;
import java.util.List;

import static oop.arkanoid.model.ModelUtils.isInRange;

public class GameLevel {
    private final Platform platform;
    private final List<Barrier> barriers;
    private final Ball ball;
    private final Point sceneSize;
    private int score = 0;

    GameLevel(Ball ball, Platform platform, List<Barrier> barriers, Point sceneSize) {
        Notifications.getInstance().subscribe(EventType.DESTROY, this, b -> {
            Brick brick = (Brick) b;
            score += brick.score();
        });

        this.ball = ball;
        this.platform = platform;
        this.barriers = barriers;
        this.sceneSize = sceneSize;
    }

    public Point nextBallPosition() {
        return ball.move(barriers);
    }

    public double updatePlatformPosition(double x) {
        if (isInRange(0, platform.size.x() / 2, x)) {
            platform.update(0);
        } else if (isInRange(sceneSize.x() - platform.size.x() / 2, sceneSize.x(), x)) {
            platform.update(sceneSize.x() - platform.size.x());
        } else {
            platform.update(x - platform.size.x() / 2);
        }
        return platform.position().x();
    }

    public GameState gameState() {
        if (barriers.stream().filter(barrier -> barrier instanceof Brick).allMatch(brick -> ((Brick) brick).isImmortal())) {
            Notifications.getInstance().unsubscribe(EventType.DESTROY, this);
            return GameState.WIN;
        } else if (ball.position().y() > sceneSize.y()) {
            return GameState.LOSE;
        } else {
            return GameState.PROCESS;
        }
    }

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public Platform getPlatform() {
        return platform;
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

    public static class Builder {
        private final List<Barrier> barriers = new ArrayList<>();
        private Platform platform;
        private Ball ball;
        private final Point sceneSize;

        public Builder(Point sceneSize) {
            this.sceneSize = sceneSize;
        }

        public Builder platform(Point position, Point size) throws GeneratingGameException {
            platform = new Platform(position, size);
            platform.checkIfCollisionsWithOtherBarrier(barriers);
            platform.checkIfOutOfScene(sceneSize);
            barriers.add(platform);
            return this;
        }

        public Builder ball(Point position, double radius) {
            ball = new Ball(radius, position);
            return this;
        }

        public Builder addBrick(Point position, Point size, int health) throws GeneratingGameException {
            Brick brick = switch (health) {
                case -1 -> new Brick(position, size, Health.createImmortal());
                case 1, 2 -> new Brick(position, size, new Health(health));
                default -> throw GeneratingGameException.unsupportedHealth();
            };

            brick.checkIfOutOfScene(sceneSize);
            brick.checkIfCollisionsWithOtherBarrier(barriers);
            barriers.add(brick);
            return this;
        }

        public Builder addWall(Point position, Point size, CollisionPlace place) throws GeneratingGameException {
            Wall wall = new Wall(position, size, place);
            wall.checkWall(sceneSize);
            barriers.add(wall);
            return this;
        }

        public GameLevel build() throws GeneratingGameException {
            platform.checkCollisionWithBall(new CircleEquation(ball.position(), ball.radius + 2));
            checkUninitializedObjects();
            return new GameLevel(ball, platform, barriers, sceneSize);
        }

        private void checkUninitializedObjects() throws GeneratingGameException {
            if (ball == null || platform == null || sceneSize == null || barriers.isEmpty()) {
                throw GeneratingGameException.uninitializedObjects();
            }
        }
    }
}