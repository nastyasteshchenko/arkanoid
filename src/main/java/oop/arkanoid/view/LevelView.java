package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import oop.arkanoid.Presenter;
import oop.arkanoid.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LevelView {

    private final List<Rectangle> bricks;
    private final Rectangle platform;
    private final Circle ball;
    private final Scene gameScene;
    Pane gamePane;

    LevelView(List<Rectangle> bricks, Rectangle platform, Circle ball, Scene gameScene, Pane gamePane) {
        this.ball = ball;
        this.bricks = bricks;
        this.platform = platform;
        this.gameScene = gameScene;
        this.gamePane = gamePane;
    }

    public static class Builder {
        private final List<Rectangle> bricks = new ArrayList<>();
        private Rectangle platform;
        private Circle ball;
        private final Properties properties;
        Pane gamePane = new Pane();
        private Scene gameScene;

        public Builder(Properties properties) {
            this.properties = properties;
        }

        public Builder gameScene(Point size) {
            gamePane.setOpacity(0.5);
            gameScene = new Scene(gamePane, size.x(), size.y(), Color.valueOf(properties.getProperty("scene.color")));
            return this;
        }

        public Builder platform(Point position, Point size) {
            platform = new Rectangle(position.x(), position.y(), size.x(), size.y());
            platform.setFill(Color.valueOf(getPropertyInString("platform.color")));
            gamePane.getChildren().add(platform);
            return this;
        }

        public Builder ball(Point position, double radius) {
            ball = new Circle(position.x(), position.y(), radius);
            setBallParams();
            gamePane.getChildren().add(ball);
            return this;
        }

        public Builder addStandardBrick(Point position, Point size) {
            Rectangle brick = new Rectangle(position.x(), position.y(), size.x(), size.y());
            setStandardBrickParams(brick);
            bricks.add(brick);
            gamePane.getChildren().add(brick);
            return this;
        }

        public Builder addDoubleHitBrick(Point position, Point size) {
            Rectangle brick = new Rectangle(position.x(), position.y(), size.x(), size.y());
            setDoubleHitBrickParams(brick);
            bricks.add(brick);
            gamePane.getChildren().add(brick);
            return this;
        }

        public Builder addImmortalBrick(Point position, Point size) {
            Rectangle brick = new Rectangle(position.x(), position.y(), size.x(), size.y());
            setIndestructibleBrickParams(brick);
            bricks.add(brick);
            gamePane.getChildren().add(brick);
            return this;
        }

        public LevelView build() {

            gameScene.setOnMouseClicked(event -> Presenter.startPlayingGame());
            gameScene.setOnMouseMoved(event -> Presenter.movePlatform(event.getX()));

            return new LevelView(bricks, platform, ball, gameScene, gamePane);

        }

        private void setIndestructibleBrickParams(Rectangle brick) {
            brick.setFill(Color.valueOf(getPropertyInString("indestructible.brick.color")));
            brick.setStroke(Color.valueOf(getPropertyInString("indestructible.brick.stroke.color")));
            brick.setStyle("-fx-stroke-width: " + getPropertyInString("indestructible.brick.stroke.width"));
        }

        private void setDoubleHitBrickParams(Rectangle brick) {
            brick.setFill(Color.valueOf(getPropertyInString("double.hit.brick.color")));
            brick.setStroke(Color.valueOf(getPropertyInString("double.hit.brick.stroke.color")));
            brick.setStyle("-fx-stroke-width: " + getPropertyInString("double.hit.brick.stroke.width"));
        }

        private void setStandardBrickParams(Rectangle brick) {
            brick.setFill(Color.valueOf(getPropertyInString("standard.brick.color")));
            brick.setStroke(Color.valueOf(getPropertyInString("standard.brick.stroke.color")));
            brick.setStyle("-fx-stroke-width: " + getPropertyInString("standard.brick.stroke.width"));
        }

        private void setBallParams() {
            ball.setFill(Color.valueOf(getPropertyInString("ball.color")));
            ball.setStroke(Color.valueOf(getPropertyInString("ball.stroke.color")));
            ball.setStyle("-fx-stroke-width: " + getPropertyInString("ball.stroke.width"));
        }

        private Double getPropertyInDouble(String key) {
            return Double.parseDouble(properties.getProperty(key));
        }

        private Integer getPropertyInInt(String key) {
            return Integer.parseInt(properties.getProperty(key));
        }

        private String getPropertyInString(String key) {
            return properties.getProperty(key);
        }

    }

    //protected double platformX = platformStartX + platformWidth / 2;
    //TODO посмотреть как сделать без отдельного поля
    protected static boolean isPause = false;


//    private static void loadPauseButtonParameters() {
//        pauseButton.setOnMouseClicked(event -> isPause = !isPause);
//
//        pauseButton.setTranslateX(getPropertyInDouble("pause.button.x"));
//        pauseButton.setTranslateY(getPropertyInDouble("pause.button.y"));
//        pauseButton.setPrefSize(getPropertyInDouble("pause.button.pref.width"), getPropertyInDouble("pause.button.pref.height"));
//        pauseButton.setStyle("-fx-font-size: " + getPropertyInString("pause.button.font.size"));
//        pauseButton.setFont(Font.font(getPropertyInString("pause.button.font")));
//        pauseButton.setTextFill(Color.valueOf(LevelView.fieldParameters.getProperty("pause.button.text.color")));
//    }
//
//    private static void loadHighScoreLabelParameters() {
//        highScoreLabel.setLayoutX(getPropertyInDouble("high.score.label.x"));
//        highScoreLabel.setLayoutY(getPropertyInDouble("high.score.label.y"));
//        highScoreLabel.setFont(Font.font(getPropertyInString("high.score.label.font")));
//        highScoreLabel.setStyle("-fx-font-size: " + LevelView.fieldParameters.getProperty("high.score.label.font.size"));
//
//        highScoreCountLabel.setLayoutX(getPropertyInDouble("high.score.count.label.x"));
//        highScoreCountLabel.setLayoutY(getPropertyInDouble("high.score.count.label.y"));
//        highScoreCountLabel.setFont(Font.font(getPropertyInString("high.score.count.label.font")));
//        highScoreCountLabel.setStyle("-fx-font-size: " + getPropertyInString("high.score.count.label.font.size"));
//    }
//
//    private static void loadScoreLabelParameters() {
//        scoreLabel.setLayoutX(getPropertyInDouble("score.label.x"));
//        scoreLabel.setLayoutY(getPropertyInDouble("score.label.y"));
//        scoreLabel.setFont(Font.font(getPropertyInString("score.label.font")));
//        scoreLabel.setStyle("-fx-font-size: " + getPropertyInString("score.label.font.size"));
//
//        scoreCountLabel.setLayoutX(getPropertyInDouble("score.count.label.x"));
//        scoreCountLabel.setLayoutY(getPropertyInDouble("score.count.label.y"));
//        scoreCountLabel.setFont(Font.font(getPropertyInString("score.count.label.font")));
//        scoreCountLabel.setStyle("-fx-font-size: " + getPropertyInString("score.count.label.font.size"));
//    }

    public void drawPlatform(double x) {
        platform.setX(x);
    }

    public void drawBall(Point point) {
        ball.setCenterX(point.x());
        ball.setCenterY(point.y());
    }

//    public void changeScore(int points) {
//        scoreCountLabel.setText(String.valueOf(points));
//    }

    private void removeBrick(Rectangle brick) {
        gamePane.getChildren().remove(brick);
        bricks.remove(brick);
    }

    public void deleteBrick(Point point) {
        removeBrick(bricks.stream().filter(i -> point.x() == i.getX() && point.y() == i.getY()).findFirst().get());
    }

//    public void setRecordText(Text text, String level) {
//        try (FileInputStream fieldView = new FileInputStream("src/main/resources/oop/arkanoid/level" + level + "-view.properties")) {
//            fieldParameters.load(fieldView);
//        } catch (IOException e) {
////надо что-то написать потом
//        }
//
//        text.setX(getPropertyInDouble("score.x"));
//        text.setY(getPropertyInDouble("score.y"));
//        text.setFont(Font.font(getPropertyInString("score.font")));
//        text.setFill(Color.valueOf(getPropertyInString("score.color")));
//        text.setStyle("-fx-font-size: " + getPropertyInString("score.font.size"));
//    }
//
//    public void clear() {
//        ball.setCenterX(ballStartX);
//        ball.setCenterY(ballStartY);
//        scoreCountLabel.setText("0");
//        isStartMovingBall = false;
//        platformX = platformStartX + platformWidth / 2;
//        platform.setX(platformStartX);
//        bricks.clear();
//    }

    public Scene getGameScene() {
        return gameScene;
    }

    public boolean isPause() {
        return isPause;
    }

//    public void setHighScoreCountLabel(String num) {
//        highScoreCountLabel.setText(num);
//    }

}
