package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class FirstLevelView extends LevelView {

    private static final Integer amountOfLinesBricks=3;
    private static final Integer amountOfBricksInLine=3;

    private static final Rectangle[][] reg = new Rectangle[0][];

    @Override
    public void render() throws IOException {

        Properties fieldParameters = new Properties();

        fieldParameters.load(new FileInputStream("src/main/resources/oop/arkanoid/level1-view.properties"));

//TODO: разделить на методы
        amountOfBricks = Integer.parseInt(fieldParameters.getProperty("amount.of.bricks"));
        amountOfBreakableBricks = Integer.parseInt(fieldParameters.getProperty("amount.of.breakable.bricks"));

        pauseButton.setStyle("-fx-background-color: " + fieldParameters.getProperty("pause.button.color"));

        platform.setFill(Color.valueOf(fieldParameters.getProperty("platform.color")));

        ball.setFill(Color.valueOf(fieldParameters.getProperty("ball.color")));
        ball.setStroke(Color.valueOf(fieldParameters.getProperty("ball.stroke.color")));
        ball.setStyle("-fx-stroke-width: " + fieldParameters.getProperty("ball.stroke.width"));

        int numLine = 0;
        for (int i = 0, numColumn = 0; i < amountOfBricks - 5; numColumn++, i++) {
            if (i % 5 == 0 && i != 0) {
                ++numLine;
                numColumn = 0;
            }
            Rectangle brick = new Rectangle(startOfBricksX + numColumn * (brickWidth + distanceBetweenBricks), startOfBricksY + numLine * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            brick.setFill(Color.valueOf(fieldParameters.getProperty("standard.brick.color")));
            brick.setStroke(Color.valueOf(fieldParameters.getProperty("standard.brick.stroke.color")));
            brick.setStyle("-fx-stroke-width: " + fieldParameters.getProperty("standard.brick.stroke.width"));
            brick.setId(String.valueOf(i));
            bricks.put(brick.getId(), brick);
        }

        for (int i = amountOfBricks - 5, numColumn = 0; i < amountOfBricks; numColumn++, i++) {
            if (i % 5 == 0 && i != 0) {
                ++numLine;
            }
            Rectangle brick = new Rectangle(startOfBricksX + numColumn * (brickWidth + distanceBetweenBricks), startOfBricksY + numLine * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            brick.setFill(Color.valueOf(fieldParameters.getProperty("double.hit.brick.color")));
            brick.setStroke(Color.valueOf(fieldParameters.getProperty("double.hit.brick.stroke.color")));
            brick.setStyle("-fx-stroke-width: " + fieldParameters.getProperty("double.hit.brick.stroke.width"));
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
