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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public sealed class LevelView permits FirstLevelView, SecondLevelView {
    protected double platformX = platformStartX + platformWidth / 2;
    protected boolean isStartMovingBall = false;
    protected static boolean isPause = false;
    protected static double sceneWidth;
    protected static double sceneHeight;

    protected static double platformWidth;
    protected static double platformHeight;
    protected static double platformStartX;
    protected static double platformStartY;

    protected static double ballStartX;
    protected static double ballStartY;
    protected static double ballRadius;

    protected static double blockWidth;
    protected static double blockHeight;
    protected static double distanceBetweenBlocks;

    protected static double startOfBlocksX;
    protected static double startOfBlocksY;

    protected static Label scoreCountLabel = new Label(String.valueOf(0));
    protected static Label scoreLabel = new Label("Score:");

    protected static Label highScoreCountLabel = new Label();
    protected static Label highScoreLabel = new Label("High Score:");
    protected static Button pauseButton = new Button("Pause");
    protected static Rectangle platform;
    protected static Circle ball;
    protected static Scene gameScene;
    protected static Pane root;
    protected static Properties params = new Properties();
    protected static final HashMap<String, Rectangle> blocks = new HashMap<>();

    public void render() {
    }

    public static void downloadField() throws IOException {

        params.load(new FileInputStream("src/main/resources/oop/arkanoid/field-view.properties"));

        sceneHeight = Double.parseDouble(params.getProperty("scene.height"));
        sceneWidth = Double.parseDouble(params.getProperty("scene.width"));

        platformWidth = Double.parseDouble(params.getProperty("platform.width"));
        platformHeight = Double.parseDouble(params.getProperty("platform.height"));
        platformStartX = Double.parseDouble(params.getProperty("platform.start.x"));
        platformStartY = Double.parseDouble(params.getProperty("platform.start.y"));

        ballStartX = Double.parseDouble(params.getProperty("ball.start.x"));
        ballStartY = Double.parseDouble(params.getProperty("ball.start.y"));
        ballRadius = Double.parseDouble(params.getProperty("ball.radius"));

        blockHeight = Double.parseDouble(params.getProperty("block.height"));
        blockWidth = Double.parseDouble(params.getProperty("block.width"));
        distanceBetweenBlocks = Double.parseDouble(params.getProperty("distance.between.blocks"));

        startOfBlocksX = Double.parseDouble(params.getProperty("start.of.blocks.x"));
        startOfBlocksY = Double.parseDouble(params.getProperty("start.of.blocks.y"));

        pauseButton.setOnMouseClicked(event -> isPause = !isPause);

        pauseButton.setTranslateX(Double.parseDouble(params.getProperty("pause.button.x")));
        pauseButton.setTranslateY(Double.parseDouble(params.getProperty("pause.button.y")));
        pauseButton.setPrefSize(Double.parseDouble(params.getProperty("pause.button.pref.width")), Double.parseDouble(params.getProperty("pause.button.pref.height")));
        pauseButton.setStyle("-fx-font-size: " + params.getProperty("pause.button.font.size"));
        pauseButton.setFont(Font.font(params.getProperty("pause.button.font")));
        pauseButton.setTextFill(Color.valueOf(params.getProperty("pause.button.text.color")));

        highScoreLabel.setLayoutX(Double.parseDouble(params.getProperty("high.score.label.x")));
        highScoreLabel.setLayoutY(Double.parseDouble(params.getProperty("high.score.label.y")));
        highScoreLabel.setFont(Font.font(params.getProperty("high.score.label.font")));
        highScoreLabel.setStyle("-fx-font-size: " + params.getProperty("high.score.label.font.size"));

        highScoreCountLabel.setLayoutX(Double.parseDouble(params.getProperty("high.score.count.label.x")));
        highScoreCountLabel.setLayoutY(Double.parseDouble(params.getProperty("high.score.count.label.y")));
        highScoreCountLabel.setFont(Font.font(params.getProperty("high.score.count.label.font")));
        highScoreCountLabel.setStyle("-fx-font-size: " + params.getProperty("high.score.count.label.font.size"));

        scoreLabel.setLayoutX(Double.parseDouble(params.getProperty("score.label.x")));
        scoreLabel.setLayoutY(Double.parseDouble(params.getProperty("score.label.y")));
        scoreLabel.setFont(Font.font(params.getProperty("score.label.font")));
        scoreLabel.setStyle("-fx-font-size: " + params.getProperty("score.label.font.size"));

        scoreCountLabel.setLayoutX(Double.parseDouble(params.getProperty("score.count.label.x")));
        scoreCountLabel.setLayoutY(Double.parseDouble(params.getProperty("score.count.label.y")));
        scoreCountLabel.setFont(Font.font(params.getProperty("score.count.label.font")));
        scoreCountLabel.setStyle("-fx-font-size: " + params.getProperty("score.count.label.font.size"));

        platform = new Rectangle(platformStartX, platformStartY, platformWidth, platformHeight);
        ball = new Circle(ballStartX, ballStartY, ballRadius);

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
        ball.setCenterX(ballStartX);
        ball.setCenterY(ballStartY);
        scoreCountLabel.setText("0");
        isStartMovingBall = false;
        platformX = platformStartX + platformWidth / 2;
        platform.setX(platformStartX);
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

    public double getSceneWidth() {
        return sceneWidth;
    }

    public double getSceneHeight() {
        return sceneHeight;
    }
}
