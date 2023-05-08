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
    public void render() {
        try (FileInputStream fieldView = new FileInputStream("src/main/resources/oop/arkanoid/level2-view.properties")) {
            fieldParameters.load(fieldView);
        } catch (IOException e) {
//надо что-то написать потом
        }

        setParametersForLevelView();

        loadIndestructibleBricksParameters();
        loadStandardBricksParameters();

        root = new Pane(platform, ball, scoreLabel, scoreCountLabel, pauseButton, highScoreLabel, highScoreCountLabel);

        for (int i = 0; i < amountOfBricksLines - 1; i++) {
            for (int j = 0; j < amountOfBricksInLine; j++) {
                Rectangle brick = new Rectangle(startOfBricksX + j * (brickWidth + distanceBetweenBricks), startOfBricksY + i * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
                setParametersForStandardBrick(brick);
                bricks.add( brick);
                root.getChildren().add(brick);
            }
        }

        for (int i = 1; i < amountOfBricksInLine - 1; i++) {
            Rectangle brick = new Rectangle(startOfBricksX + i * (brickWidth + distanceBetweenBricks), startOfBricksY + (amountOfBricksLines - 1) * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            setParametersForIndestructibleBrick(brick);
            bricks.add(brick);
            root.getChildren().add(brick);
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
