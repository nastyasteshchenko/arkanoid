package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.IOException;

public final class SecondLevelView extends LevelView {

    private Color standardBrickColor;
    private Color standardBrickStrokeColor;
    private String standardBrickStrokeWidth;

    private Color indestructibleBrickColor;
    private Color indestructibleBrickStrokeColor;
    private String indestructibleBrickStrokeWidth;

    private void loadStandardBricksParameters() {
        standardBrickColor = Color.valueOf(getPropertyInString("standard.brick.color"));
        standardBrickStrokeColor = Color.valueOf(getPropertyInString("standard.brick.stroke.color"));
        standardBrickStrokeWidth = "-fx-stroke-width: " + getPropertyInString("standard.brick.stroke.width");
    }

    private void loadIndestructibleBricksParameters() {
        indestructibleBrickColor = Color.valueOf(getPropertyInString("indestructible.brick.color"));
        indestructibleBrickStrokeColor = Color.valueOf(getPropertyInString("indestructible.brick.stroke.color"));
        indestructibleBrickStrokeWidth = "-fx-stroke-width: " + getPropertyInString("indestructible.brick.stroke.width");
    }

    private void setParametersForStandardBrick(Rectangle brick) {
        brick.setFill(standardBrickColor);
        brick.setStroke(standardBrickStrokeColor);
        brick.setStyle(standardBrickStrokeWidth);
    }

    private void setParametersForIndestructibleBrick(Rectangle brick) {
        brick.setFill(indestructibleBrickColor);
        brick.setStroke(indestructibleBrickStrokeColor);
        brick.setStyle(indestructibleBrickStrokeWidth);
    }

    @Override
    public void render() throws IOException {
        fieldParameters.load(new FileInputStream("src/main/resources/oop/arkanoid/level2-view.properties"));

       setParametersForLevelView();

       loadIndestructibleBricksParameters();
       loadStandardBricksParameters();

        for (int i = 0, numLine = 0, numColumn = 0; i < amountOfBreakableBricks; numColumn++, i++) {
            if (i % 5 == 0 && i != 0) {
                ++numLine;
                numColumn = 0;
            }
            Rectangle brick = new Rectangle(startOfBricksX + numColumn * (brickWidth + distanceBetweenBricks), startOfBricksY + numLine * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            setParametersForStandardBrick(brick);
            brick.setId(String.valueOf(i));
            bricks.put(brick.getId(), brick);
        }

        for (int i = amountOfBreakableBricks, numColumn = 1; i < amountOfBricks; numColumn++, i++) {
            Rectangle brick = new Rectangle(startOfBricksX + numColumn * (brickWidth + distanceBetweenBricks), startOfBricksY + 3 * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            setParametersForIndestructibleBrick(brick);
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
