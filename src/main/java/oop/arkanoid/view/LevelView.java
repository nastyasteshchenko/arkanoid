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
import oop.arkanoid.model.Point;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public sealed class LevelView permits FirstLevelView, SecondLevelView {

    protected int amountOfBricksLines;
    protected int amountOfBricksInLine;
    protected int amountOfBricks;
    protected int amountOfBreakableBricks;
    protected double platformX = platformStartX + platformWidth / 2;
    //TODO посмотреть как сделать без отдельного поля
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
    protected Scene gameScene;

    protected static Pane root;
    protected static final HashMap<String, Rectangle> bricks = new HashMap<>();

    protected static final Properties fieldParameters = new Properties();

    public void render() {
    }

    protected void setParametersForLevelView() {

        amountOfBricksLines = getPropertyInInt("amount.of.bricks.lines");
        amountOfBricksInLine = getPropertyInInt("amount.of.bricks.in.line");

        amountOfBricks = getPropertyInInt("amount.of.bricks");
        amountOfBreakableBricks = getPropertyInInt("amount.of.breakable.bricks");

        pauseButton.setStyle("-fx-background-color: " + getPropertyInString("pause.button.color"));

        platform.setFill(Color.valueOf(getPropertyInString("platform.color")));

        ball.setFill(Color.valueOf(getPropertyInString("ball.color")));
        ball.setStroke(Color.valueOf(getPropertyInString("ball.stroke.color")));
        ball.setStyle("-fx-stroke-width: " + getPropertyInString("ball.stroke.width"));
    }

    protected static Integer getPropertyInInt(String key) {
        return Integer.parseInt(LevelView.fieldParameters.getProperty(key));
    }

    protected static Double getPropertyInDouble(String key) {
        return Double.parseDouble(LevelView.fieldParameters.getProperty(key));
    }

    protected static String getPropertyInString(String key) {
        return LevelView.fieldParameters.getProperty(key);
    }

    private static void loadPlatformParameters() {
        platformWidth = getPropertyInDouble("platform.width");
        platformHeight = getPropertyInDouble("platform.height");
        platformStartX = getPropertyInDouble("platform.start.x");
        platformStartY = getPropertyInDouble("platform.start.y");
    }

    private static void loadBallParameters() {
        ballStartX = getPropertyInDouble("ball.start.x");
        ballStartY = getPropertyInDouble("ball.start.y");
        ballRadius = getPropertyInDouble("ball.radius");
    }

    private static void loadBricksParameters() {
        brickHeight = getPropertyInDouble("brick.height");
        brickWidth = getPropertyInDouble("brick.width");
        distanceBetweenBricks = getPropertyInDouble("distance.between.bricks");

        startOfBricksX = getPropertyInDouble("start.of.bricks.x");
        startOfBricksY = getPropertyInDouble("start.of.bricks.y");
    }

    private static void loadPauseButtonParameters() {
        pauseButton.setOnMouseClicked(event -> isPause = !isPause);

        pauseButton.setTranslateX(getPropertyInDouble("pause.button.x"));
        pauseButton.setTranslateY(getPropertyInDouble("pause.button.y"));
        pauseButton.setPrefSize(getPropertyInDouble("pause.button.pref.width"), getPropertyInDouble("pause.button.pref.height"));
        pauseButton.setStyle("-fx-font-size: " + getPropertyInString("pause.button.font.size"));
        pauseButton.setFont(Font.font(getPropertyInString("pause.button.font")));
        pauseButton.setTextFill(Color.valueOf(LevelView.fieldParameters.getProperty("pause.button.text.color")));
    }

    private static void loadHighScoreLabelParameters() {
        highScoreLabel.setLayoutX(getPropertyInDouble("high.score.label.x"));
        highScoreLabel.setLayoutY(getPropertyInDouble("high.score.label.y"));
        highScoreLabel.setFont(Font.font(getPropertyInString("high.score.label.font")));
        highScoreLabel.setStyle("-fx-font-size: " + LevelView.fieldParameters.getProperty("high.score.label.font.size"));

        highScoreCountLabel.setLayoutX(getPropertyInDouble("high.score.count.label.x"));
        highScoreCountLabel.setLayoutY(getPropertyInDouble("high.score.count.label.y"));
        highScoreCountLabel.setFont(Font.font(getPropertyInString("high.score.count.label.font")));
        highScoreCountLabel.setStyle("-fx-font-size: " + getPropertyInString("high.score.count.label.font.size"));
    }

    private static void loadScoreLabelParameters() {
        scoreLabel.setLayoutX(getPropertyInDouble("score.label.x"));
        scoreLabel.setLayoutY(getPropertyInDouble("score.label.y"));
        scoreLabel.setFont(Font.font(getPropertyInString("score.label.font")));
        scoreLabel.setStyle("-fx-font-size: " + getPropertyInString("score.label.font.size"));

        scoreCountLabel.setLayoutX(getPropertyInDouble("score.count.label.x"));
        scoreCountLabel.setLayoutY(getPropertyInDouble("score.count.label.y"));
        scoreCountLabel.setFont(Font.font(getPropertyInString("score.count.label.font")));
        scoreCountLabel.setStyle("-fx-font-size: " + getPropertyInString("score.count.label.font.size"));
    }

    public static void loadField() {

        try (FileInputStream fieldView = new FileInputStream("src/main/resources/oop/arkanoid/field-view.properties")) {
            fieldParameters.load(fieldView);
        } catch (IOException e) {
//надо что-то написать потом
        }

        sceneHeight = getPropertyInDouble("scene.height");
        sceneWidth = getPropertyInDouble("scene.width");

        loadPlatformParameters();

        loadBallParameters();

        loadBricksParameters();

        loadPauseButtonParameters();

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

    public void setRecordText(Text text, String level) {
        try (FileInputStream fieldView = new FileInputStream("src/main/resources/oop/arkanoid/level" + level + "-view.properties")) {
            fieldParameters.load(fieldView);
        } catch (IOException e) {
//надо что-то написать потом
        }

        text.setX(getPropertyInDouble("score.x"));
        text.setY(getPropertyInDouble("score.y"));
        text.setFont(Font.font(getPropertyInString("score.font")));
        text.setFill(Color.valueOf(getPropertyInString("score.color")));
        text.setStyle("-fx-font-size: " + getPropertyInString("score.font.size"));
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

    public int getAmountOfBricksInLine() {
        return amountOfBricksInLine;
    }

    public int getAmountOfBricksLines() {
        return amountOfBricksLines;
    }

}
