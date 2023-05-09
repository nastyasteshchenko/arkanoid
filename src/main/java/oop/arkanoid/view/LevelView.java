package oop.arkanoid.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import oop.arkanoid.Presenter;
import oop.arkanoid.model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LevelView {

    private final List<Rectangle> bricks;
    private final Rectangle platform;
    private final Circle ball;
    private final Scene gameScene;
    private final Pane gamePane;
    private final Label score;

    LevelView(List<Rectangle> bricks, Rectangle platform, Circle ball, Scene gameScene, Pane gamePane, Label score) {
        this.ball = ball;
        this.bricks = bricks;
        this.platform = platform;
        this.gameScene = gameScene;
        this.gamePane = gamePane;
        this.score = score;
    }

    public static class Builder {
        private final List<Rectangle> bricks = new ArrayList<>();
        private Rectangle platform;
        private Circle ball;
        private final Properties properties;
        private final Pane gamePane = new Pane();
        private Scene gameScene;
        private Label score;
        private int highScore;

        public Builder(Properties properties) {
            this.properties = properties;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder highScoreLabel(int score) {
            highScore = score;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder gameScene(Point size) {
            gamePane.setOpacity(0.5);
            gameScene = new Scene(gamePane, size.x(), size.y(), Color.valueOf(properties.getProperty("scene.color")));
            score = new Label("Score: 0");
            score.setTranslateX(200);
            score.setTranslateY(size.y() - 40);
            setScoreParams();
            gamePane.getChildren().add(score);
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

        @SuppressWarnings("UnusedReturnValue")
        public Builder addStandardBrick(Point position, Point size) {
            Rectangle brick = new Rectangle(position.x(), position.y(), size.x(), size.y());
            setStandardBrickParams(brick);
            bricks.add(brick);
            gamePane.getChildren().add(brick);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder addDoubleHitBrick(Point position, Point size) {
            Rectangle brick = new Rectangle(position.x(), position.y(), size.x(), size.y());
            setDoubleHitBrickParams(brick);
            bricks.add(brick);
            gamePane.getChildren().add(brick);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder addImmortalBrick(Point position, Point size) {
            Rectangle brick = new Rectangle(position.x(), position.y(), size.x(), size.y());
            setIndestructibleBrickParams(brick);
            bricks.add(brick);
            gamePane.getChildren().add(brick);
            return this;
        }

        public LevelView build() {
            Label highScoreLabel = new Label("High score: " + highScore);
            setHighScoreLabelParams(highScoreLabel);
            gamePane.getChildren().add(highScoreLabel);

            Labeled pauseButton = new Button();
            setPauseButtonParams(pauseButton);
            gamePane.getChildren().add(pauseButton);

            gameScene.setOnMouseClicked(event -> Presenter.startPlayingGame());
            gameScene.setOnMouseMoved(event -> Presenter.movePlatform(event.getX()));

            return new LevelView(bricks, platform, ball, gameScene, gamePane, score);

        }

        private void setHighScoreLabelParams(Label highScoreLabel) {
            highScoreLabel.setTranslateX(10);
            highScoreLabel.setTranslateY(gameScene.getHeight() - 40);
            highScoreLabel.setFont(Font.font(getPropertyInString("score.font")));
            highScoreLabel.setStyle("-fx-font-size: " + getPropertyInString("score.font.size"));
        }

        private void setPauseButtonParams(Labeled pauseButton) {
            pauseButton.setOnMouseClicked(event -> Presenter.setPause());
            pauseButton.setText("Pause");
            pauseButton.setTranslateX(gameScene.getWidth() - 75);
            pauseButton.setTranslateY(gameScene.getHeight() - 50);
            pauseButton.setPrefSize(getPropertyInDouble("pause.button.pref.width"), getPropertyInDouble("pause.button.pref.height"));
            pauseButton.setStyle("-fx-font-size: " + getPropertyInString("pause.button.font.size"));
            pauseButton.setFont(Font.font(getPropertyInString("pause.button.font")));
            pauseButton.setTextFill(Color.valueOf(properties.getProperty("pause.button.text.color")));
            pauseButton.setStyle("-fx-background-color: " + getPropertyInString("pause.button.color"));
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

        private void setScoreParams() {
            score.setFont(Font.font(getPropertyInString("score.font")));
            score.setStyle("-fx-font-size: " + getPropertyInString("score.font.size"));
        }

        private Double getPropertyInDouble(String key) {
            return Double.parseDouble(properties.getProperty(key));
        }

        private String getPropertyInString(String key) {
            return properties.getProperty(key);
        }

    }

    public void drawScore(int value) {
        score.setText("Score: " + value);
    }

    public void drawPlatform(double x) {
        platform.setX(x);
    }

    public void drawBall(Point point) {
        ball.setCenterX(point.x());
        ball.setCenterY(point.y());
    }

    private void removeBrick(Rectangle brick) {
        gamePane.getChildren().remove(brick);
        bricks.remove(brick);
    }

    public void deleteBrick(Point point) {
        removeBrick(bricks.stream().filter(i -> point.x() == i.getX() && point.y() == i.getY()).findFirst().get());
    }

    public static void setRecordText(Text text, String level) {
        Properties properties = new Properties();
        try (FileInputStream fieldView = new FileInputStream("src/main/resources/oop/arkanoid/level" + level + ".properties")) {
            properties.load(fieldView);
        } catch (IOException e) {
//надо что-то написать потом
        }

        text.setX(Double.parseDouble(properties.getProperty("score.x")));
        text.setY(Double.parseDouble(properties.getProperty("score.y")));
        text.setFont(Font.font(properties.getProperty("score.font")));
        text.setFill(Color.valueOf(properties.getProperty("score.color")));
        text.setStyle("-fx-font-size: " + properties.getProperty("score.font.size"));
    }

    public Scene getGameScene() {
        return gameScene;
    }

}
