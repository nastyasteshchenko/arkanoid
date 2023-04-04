package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import oop.arkanoid.Point;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public sealed class LevelView permits FirstLevelView, SecondLevelView {

    protected static int amountOfBricks;
    protected static int amountOfBreakableBricks;
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

    protected static double brickWidth;
    protected static double brickHeight;
    protected static double distanceBetweenBricks;

    protected static double startOfBricksX;
    protected static double startOfBricksY;

    protected static Label scoreCountLabel = new Label(String.valueOf(0));
    protected static Label scoreLabel = new Label("Score:");

    protected static Label highScoreCountLabel = new Label();
    protected static Label highScoreLabel = new Label("High Score:");
    protected static Button pauseButton = new Button("Pause");
    protected static Rectangle platform;
    protected static Circle ball;
    protected static Scene gameScene;

    protected static Pane root;
    protected static Properties fieldParameters = new Properties();
    protected static final HashMap<String, Rectangle> bricks = new HashMap<>();

    public void render() {
    }

    public static void downloadField() throws IOException {

        fieldParameters.load(new FileInputStream("src/main/resources/oop/arkanoid/field-view.properties"));

        sceneHeight = Double.parseDouble(fieldParameters.getProperty("scene.height"));
        sceneWidth = Double.parseDouble(fieldParameters.getProperty("scene.width"));

        platformWidth = Double.parseDouble(fieldParameters.getProperty("platform.width"));
        platformHeight = Double.parseDouble(fieldParameters.getProperty("platform.height"));
        platformStartX = Double.parseDouble(fieldParameters.getProperty("platform.start.x"));
        platformStartY = Double.parseDouble(fieldParameters.getProperty("platform.start.y"));

        ballStartX = Double.parseDouble(fieldParameters.getProperty("ball.start.x"));
        ballStartY = Double.parseDouble(fieldParameters.getProperty("ball.start.y"));
        ballRadius = Double.parseDouble(fieldParameters.getProperty("ball.radius"));

        brickHeight = Double.parseDouble(fieldParameters.getProperty("brick.height"));
        brickWidth = Double.parseDouble(fieldParameters.getProperty("brick.width"));
        distanceBetweenBricks = Double.parseDouble(fieldParameters.getProperty("distance.between.bricks"));

        startOfBricksX = Double.parseDouble(fieldParameters.getProperty("start.of.bricks.x"));
        startOfBricksY = Double.parseDouble(fieldParameters.getProperty("start.of.bricks.y"));

        pauseButton.setOnMouseClicked(event -> isPause = !isPause);

        pauseButton.setTranslateX(Double.parseDouble(fieldParameters.getProperty("pause.button.x")));
        pauseButton.setTranslateY(Double.parseDouble(fieldParameters.getProperty("pause.button.y")));
        pauseButton.setPrefSize(Double.parseDouble(fieldParameters.getProperty("pause.button.pref.width")), Double.parseDouble(fieldParameters.getProperty("pause.button.pref.height")));
        pauseButton.setStyle("-fx-font-size: " + fieldParameters.getProperty("pause.button.font.size"));
        pauseButton.setFont(Font.font(fieldParameters.getProperty("pause.button.font")));
        pauseButton.setTextFill(Color.valueOf(fieldParameters.getProperty("pause.button.text.color")));

        highScoreLabel.setLayoutX(Double.parseDouble(fieldParameters.getProperty("high.score.label.x")));
        highScoreLabel.setLayoutY(Double.parseDouble(fieldParameters.getProperty("high.score.label.y")));
        highScoreLabel.setFont(Font.font(fieldParameters.getProperty("high.score.label.font")));
        highScoreLabel.setStyle("-fx-font-size: " + fieldParameters.getProperty("high.score.label.font.size"));

        highScoreCountLabel.setLayoutX(Double.parseDouble(fieldParameters.getProperty("high.score.count.label.x")));
        highScoreCountLabel.setLayoutY(Double.parseDouble(fieldParameters.getProperty("high.score.count.label.y")));
        highScoreCountLabel.setFont(Font.font(fieldParameters.getProperty("high.score.count.label.font")));
        highScoreCountLabel.setStyle("-fx-font-size: " + fieldParameters.getProperty("high.score.count.label.font.size"));

        scoreLabel.setLayoutX(Double.parseDouble(fieldParameters.getProperty("score.label.x")));
        scoreLabel.setLayoutY(Double.parseDouble(fieldParameters.getProperty("score.label.y")));
        scoreLabel.setFont(Font.font(fieldParameters.getProperty("score.label.font")));
        scoreLabel.setStyle("-fx-font-size: " + fieldParameters.getProperty("score.label.font.size"));

        scoreCountLabel.setLayoutX(Double.parseDouble(fieldParameters.getProperty("score.count.label.x")));
        scoreCountLabel.setLayoutY(Double.parseDouble(fieldParameters.getProperty("score.count.label.y")));
        scoreCountLabel.setFont(Font.font(fieldParameters.getProperty("score.count.label.font")));
        scoreCountLabel.setStyle("-fx-font-size: " + fieldParameters.getProperty("score.count.label.font.size"));

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

    public void deleteBrick(ArrayList<String> indexes) {
        for (String index : indexes) {
            root.getChildren().remove(bricks.get(index));
            bricks.remove(index);
        }
    }

    public boolean isStartMovingBall() {
        return isStartMovingBall;
    }

    public void setRecordText(Text text, String level) throws IOException {
        fieldParameters.load(new FileInputStream("src/main/resources/oop/arkanoid/field-view.properties"));
        text.setX(Double.parseDouble(fieldParameters.getProperty("level" + level + ".score.x")));
        text.setY(Double.parseDouble(fieldParameters.getProperty("level" + level + ".score.y")));
        text.setFont(Font.font(fieldParameters.getProperty("level" + level + ".score.font")));
        text.setFill(Color.valueOf(fieldParameters.getProperty("level" + level + ".score.color")));
        text.setStyle("-fx-font-size: " + fieldParameters.getProperty("level" + level + ".score.font.size"));
    }

    public void clear() {
        ball.setCenterX(ballStartX);
        ball.setCenterY(ballStartY);
        scoreCountLabel.setText("0");
        isStartMovingBall = false;
        platformX = platformStartX + platformWidth / 2;
        platform.setX(platformStartX);
        bricks.clear();
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

    public HashMap<String, Rectangle> getBricks() {
        return bricks;
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

    public int getAmountOfBricks() {
        return amountOfBricks;
    }

    public int getAmountOfBreakableBricks() {
        return amountOfBreakableBricks;
    }

}
