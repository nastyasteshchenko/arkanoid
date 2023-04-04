package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public final class SecondLevelView extends LevelView {
    public static int amountOfBlocks;
    public static int amountOfBreakableBlocks;

    @Override
    public void render() {

        amountOfBlocks = Integer.parseInt(params.getProperty("level2.amount.of.blocks"));
        amountOfBreakableBlocks = Integer.parseInt(params.getProperty("level2.amount.of.breakable.blocks"));

        pauseButton.setStyle("-fx-background-color: " + params.getProperty("level2.pause.button.color"));

        platform.setFill(Color.valueOf(params.getProperty("level2.platform.color")));

        ball.setFill(Color.valueOf(params.getProperty("level2.ball.color")));
        ball.setStroke(Color.valueOf(params.getProperty("level2.ball.stroke.color")));
        ball.setStyle("-fx-stroke-width: "+ params.getProperty("level2.ball.stroke.width"));

        for (int i = 0, numLine = 0, numColumn = 0; i < amountOfBreakableBlocks; numColumn++, i++) {
            if (i % 5 == 0 && i != 0) {
                ++numLine;
                numColumn = 0;
            }
            Rectangle block = new Rectangle(startOfBlocksX + numColumn * (blockWidth + distanceBetweenBlocks), startOfBlocksY + numLine * (blockHeight + distanceBetweenBlocks), blockWidth, blockHeight);
            block.setFill(Color.valueOf(params.getProperty("level2.standard.block.color")));
            block.setStroke(Color.valueOf(params.getProperty("level2.standard.block.stroke.color")));
            block.setStyle("-fx-stroke-width: "+ params.getProperty("level2.standard.block.stroke.width"));
            block.setId(String.valueOf(i));
            blocks.put(block.getId(), block);
        }

        for (int i = amountOfBreakableBlocks, numColumn = 1; i < amountOfBlocks; numColumn++, i++) {
            Rectangle block = new Rectangle(startOfBlocksX + numColumn * (blockWidth + distanceBetweenBlocks), startOfBlocksY + 3 * (blockHeight + distanceBetweenBlocks), blockWidth, blockHeight);
            block.setFill(Color.valueOf(params.getProperty("level2.indestructible.block.color")));
            block.setStroke(Color.valueOf(params.getProperty("level2.indestructible.block.stroke.color")));
            block.setStyle("-fx-stroke-width: "+ params.getProperty("level2.indestructible.block.stroke.width"));
            block.setId(String.valueOf(i));
            blocks.put(block.getId(), block);
        }

        root = new Pane(platform, ball, scoreLabel, scoreCountLabel, pauseButton, highScoreLabel, highScoreCountLabel);

        for (int i = 0; i < blocks.size(); i++) {
            root.getChildren().add(blocks.get(String.valueOf(i)));
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
