package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import oop.arkanoid.Point;

import java.util.ArrayList;
import java.util.HashMap;

public sealed class LevelView permits FirstLevelView, SecondLevelView {
    protected double platformX = PLATFORM_START_X + PLATFORM_WIDTH / 2;
    protected boolean isStartMovingBall = false;
    protected boolean isPause = false;
    public static final double SCENE_WIDTH = 600;
    public static final double SCENE_HEIGHT = 900;

    protected static final double PLATFORM_WIDTH = 124;
    protected static final double PLATFORM_HEIGHT = 22;
    protected static final double PLATFORM_START_X = 236;
    protected static final double PLATFORM_START_Y = 756;

    protected static final double BALL_START_X = 297.5;
    protected static final double BALL_START_Y = 740;
    protected static final double BALL_RADIUS = 15;

    protected static final double BLOCK_WIDTH = 114;
    protected static final double BLOCK_HEIGHT = 30;
    protected static final double DISTANCE_BETWEEN_BLOCKS = 3;

    protected static final double START_OF_BLOCKS_X = 9;
    protected static final double START_OF_BLOCKS_Y = 81;

    protected Label scoreCountLabel = new Label(String.valueOf(0));
    protected Label scoreLabel = new Label("Score:");

    protected Label highScoreCountLabel = new Label();
    protected Label highScoreLabel = new Label("High Score:");
    protected Button pauseButton = new Button("Pause");
    protected Rectangle platform;
    protected Circle ball;
    protected Scene gameScene;
    protected Pane root;

    protected final HashMap<String, Rectangle> blocks = new HashMap<>();

    public void render() {
    }

    public LevelView() {

        pauseButton.setOnMouseClicked(event -> isPause = !isPause);

        pauseButton.setTranslateX(520);
        pauseButton.setTranslateY(840);
        pauseButton.setPrefSize(60, 40);
        pauseButton.setStyle("-fx-font-size: 15");
        pauseButton.setFont(Font.font("Droid Sans Mono"));
        pauseButton.setTextFill(Color.WHITE);

        highScoreLabel.setLayoutX(125);
        highScoreLabel.setLayoutY(860);
        highScoreLabel.setFont(Font.font("Droid Sans Mono"));
        highScoreLabel.setStyle("-fx-font-size: 20");

        highScoreCountLabel.setLayoutX(230);
        highScoreCountLabel.setLayoutY(860);
        highScoreCountLabel.setFont(Font.font("Droid Sans Mono"));
        highScoreCountLabel.setStyle("-fx-font-size: 20");

        scoreLabel.setLayoutX(15);
        scoreLabel.setLayoutY(860);
        scoreLabel.setFont(Font.font("Droid Sans Mono"));
        scoreLabel.setStyle("-fx-font-size: 20");

        scoreCountLabel.setLayoutX(80);
        scoreCountLabel.setLayoutY(860);
        scoreCountLabel.setFont(Font.font("Droid Sans Mono"));
        scoreCountLabel.setStyle("-fx-font-size: 20");
    }

    public double getPlatformX() {
        return platformX;
    }

    public void movePlatform(double x) {
        platform.setX(x);
    }

    public void moveBall(Point point) {
        ball.setCenterX(point.x());
        ball.setCenterY(point.y());
    }

    public void changeScore(int points) {
        scoreCountLabel.setText(String.valueOf(points));
    }

    public void deleteBlock(ArrayList<String> indexes) {
        for (String index : indexes) {
            root.getChildren().remove(blocks.get(index));
            blocks.remove(index);
        }
    }

    public boolean isStartMovingBall() {
        return isStartMovingBall;
    }

    public void clear() {
        scoreCountLabel.setText("0");
        isStartMovingBall = false;
        platformX = PLATFORM_START_X + PLATFORM_WIDTH / 2;
        blocks.clear();
    }

    public Rectangle getPlatform() {
        return platform;
    }

    public Scene getGameScene() {
        return gameScene;
    }

    public Circle getBall() {
        return ball;
    }

    public HashMap<String, Rectangle> getBlocks() {
        return blocks;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setHighScoreCountLabel(String num) {
        highScoreCountLabel.setText(num);
    }
}
