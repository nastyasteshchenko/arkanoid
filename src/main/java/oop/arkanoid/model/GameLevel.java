package oop.arkanoid.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

//GameLevel
public class GameLevel {
    private final List<Brick> bricks;
    private final Platform platform;
    private final List<Barrier> barriers;
    private final Ball ball;
    private final Point scene;
    private static int score;

    GameLevel(Ball ball, Platform platform, List<Brick> bricks, List<Barrier> barriers, Point scene) {
        this.ball = ball;
        this.platform = platform;
        this.bricks = bricks;
        this.barriers = barriers;
        this.scene = scene;
        score = 0;
    }

    static class Builder {
        private final List<Brick> bricks = new ArrayList<>();
        private final List<Barrier> barriers = new ArrayList<>();
        private Platform platform;
        private Ball ball;
        private Point scene;

        Builder platform(Point position, Point size) {
            platform = new Platform(position, size);
            barriers.add(platform);
            return this;
        }

        Builder ball(Point position, double radius) {
            ball = new Ball(position, radius);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        Builder addDestroyableBrick(Point position, Point size, int health) {
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
        Builder addImmortalBrick(Point position, Point size) {
            Brick brick = new Brick(position, size, Health.createImmortal());
            if (bricks.stream().anyMatch(b -> hasCollision(brick, b))) {
                //TODO the same
                System.out.println("Collision");
            }
            barriers.add(brick);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        Builder addWall(Point position, Point size) {
            barriers.add(new Wall(position, size));
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        Builder scene(Point size) {
            scene = size;
            return this;
        }

        //не должно быть возможности выйти за стенки
        GameLevel build() {
            //коллизия с барьером общий метод использовать
            //TODO added exceptions(?)
            if (!isBallOnPlatform()) {
                System.out.println("Ball is not on the platform");
            }
            if (ball == null || platform == null || scene == null || bricks.isEmpty() || barriers.isEmpty()) {
                System.out.println("Game can't start");
            }
            //в самом начале генерации всей игры проходить по всем файлам и проверять нормально загружается игра или нет
            return new GameLevel(ball, platform, bricks, barriers, scene);
        }

        private boolean isBallOnPlatform() {
            return inSegment(platform.position.y() - ball.radius - 2, platform.position.y() - ball.radius, ball.position.y()) &&
                    platform.position.x() + platform.size.x() / 2 == ball.position.x();
        }

        private boolean inSegment(double a, double b, double x) {
            return a <= x && x <= b;
        }

        private boolean hasCollision(Brick b1, Brick b2) {
            return (inSegment(b2.position.x(), b2.position.x() + b2.size.x(), b1.position.x()) || inSegment(b2.position.x(), b2.position.x() + b2.size.x(), b1.position.x() + b1.size.x()))
                    && (inSegment(b2.position.y(), b2.position.y() + b2.size.y(), b1.position.y()) || inSegment(b2.position.y(), b2.position.y() + b2.size.y(), b1.position.y() + b1.size.y()));
        }
    }

    static void increaseScore(int value) {
        score += value;
    }

    public Point nextBallPosition() {
        Point newBallPos = ball.move(barriers);
        ArrayList<Brick> toRemove = new ArrayList<>();
        bricks.stream().filter(brick -> !barriers.contains(brick)).forEach(toRemove::add);
        toRemove.forEach(bricks::remove);
        return newBallPos;
    }

    public double updatePlatformPosition(double x) {
        platform.position.setX(x - platform.size.x() / 2);
        return platform.position.x();
    }

    public GameStates gameState(){
        if(bricks.isEmpty()){
            return GameStates.GAME_WIN;
        } else if (ball.position.y()>scene.y()){
            return GameStates.GAME_LOSE;
        } else {
            return GameStates.GAME_IN_PROCESS;
        }
    }
    //enum с состояниями игры
    public static GameLevel initLevel(JsonObject object) {
        return new LevelInitiator(object).initLevel();
    }

    private void updateSpeed(int speed) {
        ball.speed = speed;
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

}