package oop.arkanoid;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import oop.arkanoid.model.Model;
import oop.arkanoid.view.FirstLevelView;
import oop.arkanoid.view.LevelView;
import oop.arkanoid.view.SecondLevelView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

//Я когда-нибудь нормально обработаю исключения

public class Controller {

    private static final Properties records = new Properties();
    private static int numLevel = 1;

    private static Timeline animation;

    private static Timeline pauseTimeline;

    private static LevelView gameView = new FirstLevelView();

    private static Model model;

    private static Scene mainScene;
    private static Scene aboutScene;
    private static Scene gameOverScene;
    private static Scene gameWinScene;

    private void pause() {
        pauseTimeline = new Timeline(new KeyFrame(Duration.millis(2.5), ae -> {
            if (gameView.isPause()) {
                animation.pause();
            } else {
                animation.play();
                pauseTimeline.stop();
            }
        }));
        pauseTimeline.setCycleCount(Animation.INDEFINITE);
        pauseTimeline.play();
    }

    private void setRecord() {
        if (model.getScore() > Integer.parseInt(records.getProperty(String.valueOf(numLevel)))) {
            records.setProperty(String.valueOf(numLevel), String.valueOf(model.getScore()));
        }
    }

    private void moveBall() {
        animation = new Timeline(new KeyFrame(Duration.millis(2.5), ae -> {
            if (gameView.isStartMovingBall()) {
                try {
                    gameView.moveBall(model.recountBallCoordinates());
                    gameView.deleteBrick(model.detectCollisionsWithBricks());
                    gameView.changeScore(model.getScore());
                    if (model.getAmountOfBreakableBricks() == 0) {
                        gameWin();
                    }
                    if (gameView.isPause()) {
                        pause();
                    }
                    if (model.isGameOver()) {
                        gameOver();
                    }
                    gameView.movePlatform(model.recountPlatformX(gameView.getPlatformX()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }

    @FXML
    protected void restartGame() {
        gameView.clear();
        switch (numLevel) {
            case 1 -> startFirstLevel();
            case 2 -> startSecondLevel();
        }
    }

    private void startFirstLevel() {

        model = new Model(gameView.getSceneHeight(), gameView.getSceneWidth());

        gameView.setHighScoreCountLabel(records.getProperty(String.valueOf(numLevel)));

        gameView.render();

        model.restartModel(gameView.getAmountOfBricks(), gameView.getAmountOfBreakableBricks());

        model.setPlatform(gameView.getPlatform().getX(), gameView.getPlatform().getY(), gameView.getPlatform().getWidth(), gameView.getPlatform().getHeight());

        model.setBall(gameView.getBall().getRadius(), gameView.getBall().getCenterX(), gameView.getBall().getCenterY());

        HashMap<String, Rectangle> bricks = gameView.getBricks();

        for (int i = 0; i < gameView.getAmountOfBricksInLine() * (gameView.getAmountOfBricksLines() - 1); i++) {
            Rectangle block = bricks.get(String.valueOf(i));
            model.addStandardBrick(block.getX(), block.getY(), block.getWidth(), block.getHeight(), block.getId());
        }

        for (int i = gameView.getAmountOfBricksInLine() * (gameView.getAmountOfBricksLines() - 1); i < gameView.getAmountOfBricksInLine() * gameView.getAmountOfBricksLines(); i++) {
            Rectangle block = bricks.get(String.valueOf(i));
            model.addDoubleHitBrickBrick(block.getX(), block.getY(), block.getWidth(), block.getHeight(), block.getId());
        }
        Arkanoid.changeScene(gameView.getGameScene());
        moveBall();
    }

    private void startSecondLevel() {
        model = new Model(gameView.getSceneHeight(), gameView.getSceneWidth());

        gameView = new SecondLevelView();

        gameView.setHighScoreCountLabel(records.getProperty(String.valueOf(numLevel)));
        gameView.render();

        model.restartModel(gameView.getAmountOfBricks(), gameView.getAmountOfBreakableBricks());
        Rectangle platform = gameView.getPlatform();
        model.setPlatform(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());

        model.setBall(gameView.getBall().getRadius(), gameView.getBall().getCenterX(), gameView.getBall().getCenterY());

        HashMap<String, Rectangle> bricks = gameView.getBricks();

        for (int i = 0; i < gameView.getAmountOfBreakableBricks(); i++) {
            Rectangle brick = bricks.get(String.valueOf(i));
            model.addStandardBrick(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight(), brick.getId());
        }

        for (int i = gameView.getAmountOfBreakableBricks(); i < gameView.getAmountOfBricks(); i++) {
            Rectangle brick = bricks.get(String.valueOf(i));
            model.addIndestructibleBrick(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight(), brick.getId());
        }

        Arkanoid.changeScene(gameView.getGameScene());
        moveBall();
    }

    @FXML
    protected void endGame() {
        System.exit(0);
    }

    private static Scene loadNewScene(String fileName) throws IOException {
        return new Scene(FXMLLoader.load(Objects.requireNonNull(Controller.class.getResource(fileName))));
    }

    public static void loadScenes() throws IOException {
        mainScene = loadNewScene("main-scene.fxml");
        aboutScene = loadNewScene("about-scene.fxml");
        gameOverScene = loadNewScene("game-over-scene.fxml");
        gameWinScene = loadNewScene("game-win-scene.fxml");
    }

    @FXML
    protected void startGame() {

        try (FileInputStream recordsInputStream = new FileInputStream("src/main/resources/oop/arkanoid/records.properties")) {
            records.load(recordsInputStream);
        } catch (IOException e) {
//тоже когда-нибудь обработать
        }
        LevelView.loadField();
        startFirstLevel();
    }

    @FXML
    protected void backToMainScene() {
        Arkanoid.changeScene(mainScene);
    }

    @FXML
    protected void watchAboutGame() {
        Arkanoid.changeScene(aboutScene);
    }

    @FXML
    protected void watchRecords() throws IOException {
        try (FileInputStream recordsInputStream = new FileInputStream("src/main/resources/oop/arkanoid/records.properties")) {
            records.load(recordsInputStream);
        } catch (IOException e) {
//тоже когда-нибудь обработать
        }
        Pane root = FXMLLoader.load(Objects.requireNonNull(Arkanoid.class.getResource("records-scene.fxml")));
        Text level1Score = new Text(records.getProperty("1"));
        gameView.setRecordText(level1Score, "1");
        Text level2Score = new Text(records.getProperty("2"));
        gameView.setRecordText(level2Score, "2");
        root.getChildren().addAll(level1Score, level2Score);
        Arkanoid.changeScene(new Scene(root));

    }

    private void gameOver() throws IOException {
        setRecord();
        animation.stop();
        if (pauseTimeline != null) {
            pauseTimeline.stop();
        }
        try (FileOutputStream recordsOutputStream = new FileOutputStream("src/main/resources/oop/arkanoid/records.properties")) {
            records.store(recordsOutputStream, null);
        } catch (IOException e) {
//тоже
        }
        Arkanoid.changeScene(gameOverScene);
    }

    private void gameWin() throws IOException {
        setRecord();
        ++numLevel;
        animation.stop();
        if (pauseTimeline != null) {
            pauseTimeline.stop();
        }
        try (FileOutputStream recordsOutputStream = new FileOutputStream("src/main/resources/oop/arkanoid/records.properties")) {
            records.store(recordsOutputStream, null);
        } catch (IOException e) {
//тоже
        }
        Arkanoid.changeScene(gameWinScene);
    }
}