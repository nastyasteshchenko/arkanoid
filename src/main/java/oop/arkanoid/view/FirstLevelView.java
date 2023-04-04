package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public final class FirstLevelView extends LevelView {

    @Override
    public void render() {

        amountOfBricks = Integer.parseInt(params.getProperty("level1.amount.of.bricks"));
        amountOfBreakableBricks = Integer.parseInt(params.getProperty("level1.amount.of.breakable.bricks"));

        pauseButton.setStyle("-fx-background-color: " + params.getProperty("level1.pause.button.color"));

        platform.setFill(Color.valueOf(params.getProperty("level1.platform.color")));

        ball.setFill(Color.valueOf(params.getProperty("level1.ball.color")));
        ball.setStroke(Color.valueOf(params.getProperty("level1.ball.stroke.color")));
        ball.setStyle("-fx-stroke-width: " + params.getProperty("level1.ball.stroke.width"));

        int numLine = 0;
        for (int i = 0, numColumn = 0; i < amountOfBricks - 5; numColumn++, i++) {
            if (i % 5 == 0 && i != 0) {
                ++numLine;
                numColumn = 0;
            }
            Rectangle brick = new Rectangle(startOfBricksX + numColumn * (brickWidth + distanceBetweenBricks), startOfBricksY + numLine * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            brick.setFill(Color.valueOf(params.getProperty("level1.standard.brick.color")));
            brick.setStroke(Color.valueOf(params.getProperty("level1.standard.brick.stroke.color")));
            brick.setStyle("-fx-stroke-width: " + params.getProperty("level1.standard.brick.stroke.width"));
            brick.setId(String.valueOf(i));
            bricks.put(brick.getId(), brick);
        }

        for (int i = amountOfBricks - 5, numColumn = 0; i < amountOfBricks; numColumn++, i++) {
            if (i % 5 == 0 && i != 0) {
                ++numLine;
            }
            Rectangle brick = new Rectangle(startOfBricksX + numColumn * (brickWidth + distanceBetweenBricks), startOfBricksY + numLine * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            brick.setFill(Color.valueOf(params.getProperty("level1.double.hit.brick.color")));
            brick.setStroke(Color.valueOf(params.getProperty("level1.double.hit.brick.stroke.color")));
            brick.setStyle("-fx-stroke-width: " + params.getProperty("level1.double.hit.brick.stroke.width"));
            brick.setId(String.valueOf(i));
            bricks.put(brick.getId(), brick);
        }

        root = new Pane(platform, ball, scoreLabel, scoreCountLabel, pauseButton, highScoreLabel, highScoreCountLabel);

        for (int i = 0; i < bricks.size(); i++) {
            root.getChildren().add(bricks.get(String.valueOf(i)));
        }

        gameScene = new Scene(root, sceneWidth, sceneHeight, Color.valueOf(params.getProperty("level1.scene.color")));

        gameScene.setOnMouseClicked(event -> {

            if (!isStartMovingBall) {
                isStartMovingBall = true;
            }

        });

        gameScene.setOnMouseMoved(event -> platformX = event.getX());
        root.setOpacity(0.5);
    }

}
