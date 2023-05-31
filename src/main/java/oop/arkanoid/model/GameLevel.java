package oop.arkanoid.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class GameLevel {
    private final List<Brick> bricks;
    private final Platform platform;
    private final List<Barrier> barriers;
    private final Ball ball;
    private final Point scene;
    private static int score;

    private int step;

    GameLevel(Ball ball, Platform platform, List<Brick> bricks, List<Barrier> barriers, Point scene) {
        this.ball = ball;
        this.platform = platform;
        this.bricks = bricks;
        this.barriers = barriers;
        this.scene = scene;
        score = 0;
        step = 2;
    }

    public Point nextBallPosition() {
        Point newBallPos = ball.move(step, barriers);
        ArrayList<Brick> toRemove = new ArrayList<>();
        bricks.stream().filter(brick -> !barriers.contains(brick)).forEach(toRemove::add);
        toRemove.forEach(bricks::remove);
        return newBallPos;
    }

    public double updatePlatformPosition(double x) {
        platform.position.setX(x - platform.size.x() / 2);
        return platform.position.x();
    }

    public GameStates gameState() {
        if (bricks.isEmpty()) {
            return GameStates.GAME_WIN;
        } else if (ball.position.y() > scene.y()) {
            return GameStates.GAME_LOSE;
        } else {
            return GameStates.GAME_IN_PROCESS;
        }
    }

    public static GameLevel initLevel(JsonObject object) throws GeneratingGameException {
        return new LevelInitiator(object).initLevel();
    }

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

    public int getScore() {
        return score;
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

    static void increaseScore(int value) {
        score += value;
    }

    static class Builder {
        private final List<Brick> bricks = new ArrayList<>();
        private final List<Barrier> barriers = new ArrayList<>();
        private Platform platform;
        private Ball ball;
        private final Point scene;

        Builder(Point scene) {
            this.scene = scene;
        }

        Builder platform(Point position, Point size) {
            platform = new Platform(position, size);
            barriers.add(platform);
            return this;
        }

        Builder ball(Point position, double radius) {
            //TODO think about step
            BaseLinearEquation ballLineEquation = new BaseLinearEquation(Math.random() * 60 - 120, BaseLinearEquation.recountB(-45, position), new Point(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
            ball = new Ball(radius, position, new LinearMotion(ballLineEquation, MotionDirection.RIGHT, 2, position));
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        Builder addDestroyableBrick(Point position, Point size, int health) throws GeneratingGameException {
            Brick brick = new Brick(position, size, new Health(health));
            checkIfOutOfScene(brick.position, size);
            checkIfCollisions(brick);
            bricks.add(brick);
            barriers.add(brick);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        Builder addImmortalBrick(Point position, Point size) throws GeneratingGameException {
            Brick brick = new Brick(position, size, Health.createImmortal());
            checkIfOutOfScene(brick.position, size);
            checkIfCollisions(brick);
            barriers.add(brick);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        Builder addWall(Point position, Point size, CollisionPlace place) {
            Wall wall = new Wall(position, size, place, scene.x());
            barriers.add(wall);
            return this;
        }

        GameLevel build() throws GeneratingGameException {

          //  checkIfBallOnPlatform();
            checkUninitObjects();

            return new GameLevel(ball, platform, bricks, barriers, scene);
        }

//        private void checkIfBallOnPlatform() throws GeneratingGameException {
//            if (!ball.motion(platform)) {
//                throw GeneratingGameException.ballIsNotOnPlatform();
//            }
//        }

        private void checkUninitObjects() throws GeneratingGameException {
            if (ball == null || platform == null || scene == null || bricks.isEmpty() || barriers.isEmpty()) {
                throw GeneratingGameException.uninitObjects();
            }
        }

        static boolean inSegment(double a, double b, double x) {
            return a <= x && x <= b;
        }

        private void checkIfOutOfScene(Point position, Point size) throws GeneratingGameException {
            if (!inSegment(0, scene.x(), position.x()) || !inSegment(0, scene.y(), position.y()) || !inSegment(0, scene.x(), position.x() + size.x()) || !inSegment(0, scene.y(), position.y() + size.y())) {
                throw GeneratingGameException.outOfScene();
            }
        }

        private void checkIfCollisions(Barrier barrier) throws GeneratingGameException {
            if (barriers.stream().anyMatch(b -> hasCollision(barrier, b))) {
                throw GeneratingGameException.collisionWithOtherObjects();
            }
        }

        private boolean hasCollision(Barrier b1, Barrier b2) {
            return (inSegment(b2.position.x(), b2.position.x() + b2.size.x(), b1.position.x()) || inSegment(b2.position.x(), b2.position.x() + b2.size.x(), b1.position.x() + b1.size.x()))
                    && (inSegment(b2.position.y(), b2.position.y() + b2.size.y(), b1.position.y()) || inSegment(b2.position.y(), b2.position.y() + b2.size.y(), b1.position.y() + b1.size.y()));
        }
    }
}