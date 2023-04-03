package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public final class FirstLevelView extends LevelView {
    public static final int AMOUNT_OF_BLOCKS = 20;

    public static final int AMOUNT_OF_BREAKABLE_BLOCKS = 20;

    @Override
    public void render() {

        pauseButton.setStyle("-fx-background-color: #ff0000");

        platform = new Rectangle(PLATFORM_START_X, PLATFORM_START_Y, PLATFORM_WIDTH, PLATFORM_HEIGHT);

        ball = new Circle(BALL_START_X, BALL_START_Y, BALL_RADIUS);

        platform.setFill(Color.DARKRED);

        ball.setFill(Color.RED);
        ball.setStroke(Color.RED);
        ball.setStyle("-fx-stroke-width: 3");

        for (int i = 0, numLine = 0, numColumn = 0; i < AMOUNT_OF_BLOCKS; numColumn++, i++) {
            if (i % 5 == 0 && i != 0) {
                ++numLine;
                numColumn = 0;
            }
            Rectangle block = new Rectangle(START_OF_BLOCKS_X + numColumn * (BLOCK_WIDTH + DISTANCE_BETWEEN_BLOCKS), START_OF_BLOCKS_Y + numLine * (BLOCK_HEIGHT + DISTANCE_BETWEEN_BLOCKS), BLOCK_WIDTH, BLOCK_HEIGHT);
            block.setFill(Color.TOMATO);
            block.setId(String.valueOf(i));
            block.setStroke(Color.RED);
            block.setStyle("-fx-stroke-width: 3");
            blocks.put(block.getId(), block);
        }

        root = new Pane(platform, ball, scoreLabel, scoreCountLabel, pauseButton, highScoreLabel, highScoreCountLabel);

        for (int i = 0; i < blocks.size(); i++) {
            root.getChildren().add(blocks.get(String.valueOf(i)));
        }

        gameScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.PEACHPUFF);

        gameScene.setOnMouseClicked(event -> {

            if (!isStartMovingBall) {
                isStartMovingBall = true;
            }

        });

        gameScene.setOnMouseMoved(event -> platformX = event.getX());
        root.setOpacity(0.5);
    }

}
