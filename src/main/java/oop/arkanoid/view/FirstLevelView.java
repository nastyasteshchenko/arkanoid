package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.util.Properties;

public final class FirstLevelView extends LevelView {
    public static int amountOfBlocks;

    public static int amountOfBreakableBlocks;


    @Override
    public void render() {

        amountOfBlocks = Integer.parseInt(params.getProperty("level1.amount.of.blocks"));
        amountOfBreakableBlocks = Integer.parseInt(params.getProperty("level1.amount.of.breakable.blocks"));

        pauseButton.setStyle("-fx-background-color: " + params.getProperty("level1.pause.button.color"));

        platform.setFill(Color.valueOf(params.getProperty("level1.platform.color")));

        ball.setFill(Color.valueOf(params.getProperty("level1.ball.color")));
        ball.setStroke(Color.valueOf(params.getProperty("level1.ball.stroke.color")));
        ball.setStyle("-fx-stroke-width: "+ params.getProperty("level1.ball.stroke.width"));

        for (int i = 0, numLine = 0, numColumn = 0; i < amountOfBlocks; numColumn++, i++) {
            if (i % 5 == 0 && i != 0) {
                ++numLine;
                numColumn = 0;
            }
            Rectangle block = new Rectangle(startOfBlocksX + numColumn * (blockWidth + distanceBetweenBlocks), startOfBlocksY + numLine * (blockHeight + distanceBetweenBlocks), blockWidth, blockHeight);
            block.setFill(Color.valueOf(params.getProperty("level1.block.color")));
            block.setStroke(Color.valueOf(params.getProperty("level1.block.stroke.color")));
            block.setStyle("-fx-stroke-width: "+ params.getProperty("level1.block.stroke.width"));
            block.setId(String.valueOf(i));
            blocks.put(block.getId(), block);
        }

        root = new Pane(platform, ball, scoreLabel, scoreCountLabel, pauseButton, highScoreLabel, highScoreCountLabel);

        for (int i = 0; i < blocks.size(); i++) {
            root.getChildren().add(blocks.get(String.valueOf(i)));
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
