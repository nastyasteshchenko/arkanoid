package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public final class SecondLevelView extends LevelView {
    @Override
    public void render() throws IOException {

        fieldParameters.load(new FileInputStream("src/main/resources/oop/arkanoid/level2-view.properties"));

       setParametersForLevelView();

        for (int i = 0, numLine = 0, numColumn = 0; i < amountOfBreakableBricks; numColumn++, i++) {
            if (i % 5 == 0 && i != 0) {
                ++numLine;
                numColumn = 0;
            }
            Rectangle brick = new Rectangle(startOfBricksX + numColumn * (brickWidth + distanceBetweenBricks), startOfBricksY + numLine * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            brick.setFill(Color.valueOf(fieldParameters.getProperty("standard.brick.color")));
            brick.setStroke(Color.valueOf(fieldParameters.getProperty("standard.brick.stroke.color")));
            brick.setStyle("-fx-stroke-width: "+ fieldParameters.getProperty("standard.brick.stroke.width"));
            brick.setId(String.valueOf(i));
            bricks.put(brick.getId(), brick);
        }

        for (int i = amountOfBreakableBricks, numColumn = 1; i < amountOfBricks; numColumn++, i++) {
            Rectangle brick = new Rectangle(startOfBricksX + numColumn * (brickWidth + distanceBetweenBricks), startOfBricksY + 3 * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            brick.setFill(Color.valueOf(fieldParameters.getProperty("indestructible.brick.color")));
            brick.setStroke(Color.valueOf(fieldParameters.getProperty("indestructible.brick.stroke.color")));
            brick.setStyle("-fx-stroke-width: "+ fieldParameters.getProperty("indestructible.brick.stroke.width"));
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
