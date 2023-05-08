package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.IOException;

public final class FirstLevelView extends LevelView {
    private Color standardBrickColor;
    private Color standardBrickStrokeColor;
    private String standardBrickStrokeWidth;

    private Color doubleHitBrickColor;
    private Color doubleHitBrickStrokeColor;
    private String doubleHitBrickStrokeWidth;

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
    public void render() {

        try (FileInputStream fieldView = new FileInputStream("src/main/resources/oop/arkanoid/level1-view.properties")) {
            fieldParameters.load(fieldView);
        } catch (IOException e) {
//надо что-то написать потом
        }

        setParametersForLevelView();

        loadStandardBricksParameters();
        loadDoubleHitBricksParameters();

        root = new Pane(platform, ball, scoreLabel, scoreCountLabel, pauseButton, highScoreLabel, highScoreCountLabel);

        for (int i = 0; i < amountOfBricksLines - 1; i++) {
            for (int j = 0; j < amountOfBricksInLine; j++) {
                Rectangle brick = new Rectangle(startOfBricksX + j * (brickWidth + distanceBetweenBricks), startOfBricksY + i * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
                setParametersForStandardBrick(brick);
                bricks.add(brick);
                root.getChildren().add(brick);
            }
        }

        for (int i = 0; i < amountOfBricksInLine; i++) {
            Rectangle brick = new Rectangle(startOfBricksX + i * (brickWidth + distanceBetweenBricks), startOfBricksY + (amountOfBricksLines - 1) * (brickHeight + distanceBetweenBricks), brickWidth, brickHeight);
            setParametersForDoubleHitBrick(brick);
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
