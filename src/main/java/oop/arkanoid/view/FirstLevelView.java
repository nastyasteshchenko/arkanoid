package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.IOException;

public final class FirstLevelView extends LevelView {

    private static final Integer amountOfLinesBricks = 3;
    private static final Integer amountOfBricksInLine = 3;

    private Color standardBrickColor;
    private Color standardBrickStrokeColor;
    private String standardBrickStrokeWidth;

    private Color doubleHitBrickColor;
    private Color doubleHitBrickStrokeColor;
    private String doubleHitBrickStrokeWidth;

    private static final Rectangle[][] reg = new Rectangle[0][];

    private void loadStandardBricksParameters() {
        standardBrickColor = Color.valueOf(getPropertyInString("standard.brick.color"));
        standardBrickStrokeColor = Color.valueOf(getPropertyInString("standard.brick.stroke.color"));
        standardBrickStrokeWidth = "-fx-stroke-width: " + getPropertyInString("standard.brick.stroke.width");
    }

    private void loadDoubleHitBricksParameters() {
        doubleHitBrickColor = Color.valueOf(getPropertyInString("double.hit.brick.color"));
        doubleHitBrickStrokeColor = Color.valueOf(getPropertyInString("double.hit.brick.stroke.color"));
        doubleHitBrickStrokeWidth = "-fx-stroke-width: " + getPropertyInString("double.hit.brick.stroke.width");
    }

    private void setParametersForStandardBrick(Rectangle brick) {
        brick.setFill(standardBrickColor);
        brick.setStroke(standardBrickStrokeColor);
        brick.setStyle(standardBrickStrokeWidth);
    }

    private void setParametersForDoubleHitBrick(Rectangle brick) {
        brick.setFill(doubleHitBrickColor);
        brick.setStroke(doubleHitBrickStrokeColor);
        brick.setStyle(doubleHitBrickStrokeWidth);
    }

    @Override
    public void render() throws IOException {

        fieldParameters.load(new FileInputStream("src/main/resources/oop/arkanoid/level1-view.properties"));

        setParametersForLevelView();

        loadStandardBricksParameters();
        loadDoubleHitBricksParameters();

        int numLine = 0;
        for (int i = 0, numColumn = 0; i < amountOfBricks - 5; numColumn++, i++) {
            if (i % 5 == 0 && i != 0) {
                ++numLine;
                numColumn = 0;
            }
            Rectangle brick = new Rectangle(startOfBricksX + numColumn * (brickWidth + distanceBetweenBricks), startOfBricksY + numLine * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            setParametersForStandardBrick(brick);
            brick.setId(String.valueOf(i));
            bricks.put(brick.getId(), brick);
        }

        for (int i = amountOfBricks - 5, numColumn = 0; i < amountOfBricks; numColumn++, i++) {
            if (i % 5 == 0 && i != 0) {
                ++numLine;
            }
            Rectangle brick = new Rectangle(startOfBricksX + numColumn * (brickWidth + distanceBetweenBricks), startOfBricksY + numLine * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            setParametersForDoubleHitBrick(brick);
            brick.setId(String.valueOf(i));
            bricks.put(brick.getId(), brick);
        }

        root = new Pane(platform, ball, scoreLabel, scoreCountLabel, pauseButton, highScoreLabel, highScoreCountLabel);

        for (int i = 0; i < bricks.size(); i++) {
            root.getChildren().add(bricks.get(String.valueOf(i)));
        }

        gameScene = new Scene(root, sceneWidth, sceneHeight, Color.valueOf(fieldParameters.getProperty("scene.color")));

        gameScene.setOnMouseClicked(event -> {

            if (!isStartMovingBall) {
                isStartMovingBall = true;
            }

        });

        gameScene.setOnMouseMoved(event -> platformX = event.getX());
        root.setOpacity(0.5);
    }

}
