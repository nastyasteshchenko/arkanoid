package oop.arkanoid.model;

import oop.arkanoid.Point;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {

    private boolean isGameOver = false;
    private int score = 0;
    private int amountOfBreakableBricks;
    private int amountOfBricks;
    private final double sceneWidth;
    private final double sceneHeight;
    private final HashMap<String, Brick> bricks = new HashMap<>();
    private Platform platform;
    private Ball ball;

    public void restartModel(int amountOfBricks, int amountOfBreakableBricks) {
        this.amountOfBricks = amountOfBricks;
        this.amountOfBreakableBricks = amountOfBreakableBricks;
        score = 0;
        isGameOver = false;
    }

    public Model(double sceneHeight, double sceneWidth) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
    }

    public void setBall(double radius, double centerX, double centerY) {
        this.ball = new Ball(radius, centerX, centerY);
    }

    public void setPlatform(double x, double y, double width, double height) {
        platform = new Platform(x, y, width, height);
    }

    public void addStandardBrick(double x, double y, double width, double height, String id) {
        bricks.put(id, new StandardBrick(x, y, width, height));
    }

    public void addDoubleHitBrickBrick(double x, double y, double width, double height, String id) {
        bricks.put(id, new DoubleHitBrick(x, y, width, height));
    }

    public void addIndestructibleBrick(double x, double y, double width, double height, String id) {
        bricks.put(id, new IndestructibleBrick(x, y, width, height));
    }

    public Point recountBallCoordinates() {

        if (ball.getCenterY() >= sceneHeight + 2 * ball.getRadius()) {
            isGameOver = true;
        }

        ball.setAngle(countReboundingAngle());

        ball.renewCoordinates();

        return new Point(ball.getCenterX(), ball.getCenterY());
    }

    public double recountPlatformX(double x) {

        if (x <= platform.getWidth() / 2) {

            platform.setX(0);

        } else if (x >= sceneWidth - platform.getWidth() / 2) {

            platform.setX(sceneWidth - platform.getWidth());

        } else {

            platform.setX(x - platform.getWidth() / 2);

        }

        return platform.getX();
    }

    private double countReboundingAngle() {

        if (ball.isCollisionWithPlatform(platform)) {
            return -90 - (platform.getX() + platform.getWidth() / 2 - ball.getCenterX());
        }

        if (ball.getCenterX() >= sceneWidth - ball.getRadius() || ball.getCenterX() < ball.getRadius() || ball.isToChangeXDirection()) {
            return -180 - ball.getAngle();

        }

        if (ball.getCenterY() <= ball.getRadius() || ball.isToChangeYDirection()) {
            return -ball.getAngle();

        }
        return ball.getAngle();

    }

    public ArrayList<String> detectCollisionsWithBricks() {

        ArrayList<String> indexesCollisionBricks = new ArrayList<>();

        for (int i = 0; i < amountOfBricks; i++) {
            Brick brick = bricks.get(String.valueOf(i));
            if (brick != null) {
                if (ball.isCollisionWithBrickBottom(brick) || ball.isCollisionWithBrickTop(brick)) {
                    ball.detectCollisionY();
                    brick.decreaseCountOfHits();
                    if (!(brick instanceof IndestructibleBrick) && brick.getCountOfHits() == 0) {
                        score += brick.getPoints();
                        indexesCollisionBricks.add(String.valueOf(i));
                    }
                } else {
                    if (ball.isCollisionWithBrickLeftSide(brick) || ball.isCollisionWithBrickRightSide(brick)) {
                        ball.detectCollisionX();
                        brick.decreaseCountOfHits();
                        if (!(brick instanceof IndestructibleBrick) && brick.getCountOfHits()==0) {
                            score += brick.getPoints();
                            indexesCollisionBricks.add(String.valueOf(i));
                        }
                    }
                }
            }
        }
        for (String i : indexesCollisionBricks) {
            bricks.remove(i);
        }
        amountOfBreakableBricks -= indexesCollisionBricks.size();
        return indexesCollisionBricks;
    }

    public int getAmountOfBreakableBricks() {
        return amountOfBreakableBricks;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}