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
    protected static final HashMap<String, Rectangle> bricks = new HashMap<>();

    private static final Properties fieldParameters = new Properties();

    public void render() throws IOException {
    }

    private static Double getDigitProperty(String key) {
        return Double.parseDouble(LevelView.fieldParameters.getProperty(key));
    }

    private static String getStringProperty(String key) {
        return LevelView.fieldParameters.getProperty(key);
    }
    private static void loadPlatformParameters() {
        platformWidth = getDigitProperty("platform.width");
        platformHeight = getDigitProperty("platform.height");
        platformStartX = getDigitProperty("platform.start.x");
        platformStartY = getDigitProperty("platform.start.y");
    }

    private static void loadBallParameters() {
        ballStartX = getDigitProperty("ball.start.x");
        ballStartY = getDigitProperty("ball.start.y");
        ballRadius = getDigitProperty("ball.radius");
    }

    private static void loadBricksParameters() {
        brickHeight = getDigitProperty("brick.height");
        brickWidth = getDigitProperty("brick.width");
        distanceBetweenBricks = getDigitProperty("distance.between.bricks");

        startOfBricksX = getDigitProperty("start.of.bricks.x");
        startOfBricksY = getDigitProperty("start.of.bricks.y");
    }

    private static void loadPauseButtonParameters() {
        pauseButton.setOnMouseClicked(event -> isPause = !isPause);

        pauseButton.setTranslateX(getDigitProperty("pause.button.x"));
        pauseButton.setTranslateY(getDigitProperty("pause.button.y"));
        pauseButton.setPrefSize(getDigitProperty("pause.button.pref.width"), getDigitProperty("pause.button.pref.height"));
        pauseButton.setStyle("-fx-font-size: " + getStringProperty("pause.button.font.size"));
        pauseButton.setFont(Font.font(getStringProperty("pause.button.font")));
        pauseButton.setTextFill(Color.valueOf(LevelView.fieldParameters.getProperty("pause.button.text.color")));
    }

    private static void loadHighScoreLabelParameters() {
        highScoreLabel.setLayoutX(getDigitProperty("high.score.label.x"));
        highScoreLabel.setLayoutY(getDigitProperty("high.score.label.y"));
        highScoreLabel.setFont(Font.font(getStringProperty("high.score.label.font")));
        highScoreLabel.setStyle("-fx-font-size: " + LevelView.fieldParameters.getProperty("high.score.label.font.size"));

        highScoreCountLabel.setLayoutX(getDigitProperty("high.score.count.label.x"));
        highScoreCountLabel.setLayoutY(getDigitProperty("high.score.count.label.y"));
        highScoreCountLabel.setFont(Font.font(getStringProperty("high.score.count.label.font")));
        highScoreCountLabel.setStyle("-fx-font-size: " + getStringProperty("high.score.count.label.font.size"));
    }

    private static void loadScoreLabelParameters() {
        scoreLabel.setLayoutX(getDigitProperty("score.label.x"));
        scoreLabel.setLayoutY(getDigitProperty("score.label.y"));
        scoreLabel.setFont(Font.font(getStringProperty("score.label.font")));
        scoreLabel.setStyle("-fx-font-size: " + getStringProperty("score.label.font.size"));

        scoreCountLabel.setLayoutX(getDigitProperty("score.count.label.x"));
        scoreCountLabel.setLayoutY(getDigitProperty("score.count.label.y"));
        scoreCountLabel.setFont(Font.font(getStringProperty("score.count.label.font")));
        scoreCountLabel.setStyle("-fx-font-size: " + getStringProperty("score.count.label.font.size"));
    }

    public static void loadField() throws IOException {

        fieldParameters.load(new FileInputStream("src/main/resources/oop/arkanoid/field-view.properties"));

        sceneHeight = getDigitProperty("scene.height");
        sceneWidth = getDigitProperty("scene.width");

        loadPlatformParameters();

        loadBallParameters();

        loadBricksParameters();

        loadPauseButtonParameters();

        //TODO: вынести в отдельный метод и проверить на null
        loadHighScoreLabelParameters();

        loadScoreLabelParameters();

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
        text.setX(getDigitProperty("level" + level + ".score.x"));
        text.setY(getDigitProperty("level" + level + ".score.y"));
        text.setFont(Font.font(getStringProperty("level" + level + ".score.font")));
        text.setFill(Color.valueOf(getStringProperty("level" + level + ".score.color")));
        text.setStyle("-fx-font-size: " + getStringProperty("level" + level + ".score.font.size"));
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
