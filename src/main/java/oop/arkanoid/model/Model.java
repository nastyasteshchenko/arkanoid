package oop.arkanoid.model;

import oop.arkanoid.Point;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {

    private boolean isGameOver = false;
    private int score = 0;
    private int amountOfBreakableBlocks;
    private int amountOfBlocks;
    private final double sceneWidth;
    private final double sceneHeight;
    private final HashMap<String, Block> blocks = new HashMap<>();
    private Platform platform;
    private Ball ball;

    public void restartModel(int amountOfBlocks, int amountOfBreakableBlocks) {
        this.amountOfBlocks = amountOfBlocks;
        this.amountOfBreakableBlocks = amountOfBreakableBlocks;
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

    public void addStandartBlock(double x, double y, double width, double height, String id) {
        blocks.put(id, new StandardBlock(x, y, width, height));
    }

    public void addIndestructibleBlock(double x, double y, double width, double height, String id) {
        blocks.put(id, new IndestructibleBlock(x, y, width, height));
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

    public ArrayList<String> detectCollisionsWithBlocks() {

        ArrayList<String> indexesCollisionBlocks = new ArrayList<>();

        for (int i = 0; i < amountOfBlocks; i++) {
            Block block = blocks.get(String.valueOf(i));
            if (block != null) {
                if (ball.isCollisionWithBlockBottom(block) || ball.isCollisionWithBlockTop(block)) {
                    ball.detectCollisionY();
                    score += block.getPoints();
                    if (!(block instanceof IndestructibleBlock)) {
                        indexesCollisionBlocks.add(String.valueOf(i));
                    }
                } else {
                    if (ball.isCollisionWithBlockLeftSide(block) || ball.isCollisionWithBlockRightSide(block)) {
                        ball.detectCollisionX();
                        score += block.getPoints();
                        if (!(block instanceof IndestructibleBlock)) {
                            indexesCollisionBlocks.add(String.valueOf(i));
                        }
                    }
                }
            }
        }
        for (String i : indexesCollisionBlocks) {
            blocks.remove(i);
        }
        amountOfBreakableBlocks -= indexesCollisionBlocks.size();
        return indexesCollisionBlocks;
    }

    public int getAmountOfBreakableBlocks() {
        return amountOfBreakableBlocks;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}