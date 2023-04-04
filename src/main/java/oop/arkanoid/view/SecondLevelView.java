package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public final class SecondLevelView extends LevelView {
    @Override
    public void render() {

        amountOfBricks = Integer.parseInt(params.getProperty("level2.amount.of.bricks"));
        amountOfBreakableBricks = Integer.parseInt(params.getProperty("level2.amount.of.breakable.bricks"));

        pauseButton.setStyle("-fx-background-color: " + params.getProperty("level2.pause.button.color"));

        platform.setFill(Color.valueOf(params.getProperty("level2.platform.color")));

        ball.setFill(Color.valueOf(params.getProperty("level2.ball.color")));
        ball.setStroke(Color.valueOf(params.getProperty("level2.ball.stroke.color")));
        ball.setStyle("-fx-stroke-width: "+ params.getProperty("level2.ball.stroke.width"));

        for (int i = 0, numLine = 0, numColumn = 0; i < amountOfBreakableBricks; numColumn++, i++) {
            if (i % 5 == 0 && i != 0) {
                ++numLine;
                numColumn = 0;
            }
            Rectangle brick = new Rectangle(startOfBricksX + numColumn * (brickWidth + distanceBetweenBricks), startOfBricksY + numLine * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            brick.setFill(Color.valueOf(params.getProperty("level2.standard.brick.color")));
            brick.setStroke(Color.valueOf(params.getProperty("level2.standard.brick.stroke.color")));
            brick.setStyle("-fx-stroke-width: "+ params.getProperty("level2.standard.brick.stroke.width"));
            brick.setId(String.valueOf(i));
            bricks.put(brick.getId(), brick);
        }

        for (int i = amountOfBreakableBricks, numColumn = 1; i < amountOfBricks; numColumn++, i++) {
            Rectangle brick = new Rectangle(startOfBricksX + numColumn * (brickWidth + distanceBetweenBricks), startOfBricksY + 3 * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            brick.setFill(Color.valueOf(params.getProperty("level2.indestructible.brick.color")));
            brick.setStroke(Color.valueOf(params.getProperty("level2.indestructible.brick.stroke.color")));
            brick.setStyle("-fx-stroke-width: "+ params.getProperty("level2.indestructible.brick.stroke.width"));
            brick.setId(String.valueOf(i));
            bricks.put(brick.getId(), brick);
        }

        root = new Pane(platform, ball, scoreLabel, scoreCountLabel, pauseButton, highScoreLabel, highScoreCountLabel);

        for (int i = 0; i < bricks.size(); i++) {
            root.getChildren().add(bricks.get(String.valueOf(i)));
        }

        gameScene = new Scene(root, sceneWidth, sceneHeight, Color.valueOf(params.getProperty("level2.scene.color")));

        gameScene.setOnMouseClicked(event -> {

            if (!isStartMovingBall) {
                isStartMovingBall = true;
            }

        });

        gameScene.setOnMouseMoved(event -> platformX = event.getX());
        root.setOpacity(0.5);
    }

}
