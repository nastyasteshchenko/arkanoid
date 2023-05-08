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
import oop.arkanoid.model.Destroyable;
import oop.arkanoid.model.Game;
import oop.arkanoid.view.FirstLevelView;
import oop.arkanoid.view.LevelView;
import oop.arkanoid.view.SecondLevelView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

//Я когда-нибудь нормально обработаю исключения

public class Presenter {

    private static final Properties records = new Properties();
    private static int numLevel = 1;

    private static Timeline animation;

    private static Timeline pauseTimeline;

    private static LevelView gameView = new FirstLevelView();

    private static Game model;

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

//    private void setRecord() {
//        if (model.getScore() > Integer.parseInt(records.getProperty(String.valueOf(numLevel)))) {
//            records.setProperty(String.valueOf(numLevel), String.valueOf(model.getScore()));
//        }
//    }
    //TODO подумать над названием
    private void moveBall() {

        animation = new Timeline(new KeyFrame(Duration.millis(2.5), ae -> {
            if (gameView.isStartMovingBall()) {
                try {
                    gameView.moveBall(model.nextBallPosition());
//                    gameView.changeScore(model.getScore());
//                    if (model.getAmountOfBreakableBricks() == 0) {
//                        gameWin();
//                    }
                    if (gameView.isPause()) {
                        pause();
                    }
                    if (model.gameOver()) {
                        gameOver();
                    }
                    gameView.movePlatform(model.updatePlatformPosition(gameView.getPlatformX()));
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

        Game.Builder builder = new Game.Builder();

        gameView.setHighScoreCountLabel(records.getProperty(String.valueOf(numLevel)));

        gameView.render();

        builder.platform(gameView.getPlatform().getX(), gameView.getPlatform().getY(), gameView.getPlatform().getWidth(), gameView.getPlatform().getHeight());

        builder.ball(gameView.getBall().getRadius(), gameView.getBall().getCenterX(), gameView.getBall().getCenterY());

        List<Rectangle> bricks = gameView.getBricks();

        for (int i = 0; i < gameView.getAmountOfBricksInLine() * (gameView.getAmountOfBricksLines() - 1); i++) {
            Rectangle block = bricks.get(i);
            builder.addDestroyableBrick(block.getX(), block.getY(), block.getWidth(), block.getHeight(), 1);
        }

        for (int i = gameView.getAmountOfBricksInLine() * (gameView.getAmountOfBricksLines() - 1); i < gameView.getAmountOfBricksInLine() * gameView.getAmountOfBricksLines(); i++) {
            Rectangle block = bricks.get(i);
            builder.addDestroyableBrick(block.getX(), block.getY(), block.getWidth(), block.getHeight(), 2);
        }
        addWalls(builder);
    }

    private void addWalls(Game.Builder builder) {
        builder.addWall(0, 0, 2, gameView.getSceneHeight());
        builder.addWall(gameView.getSceneWidth() - 2, 0, 2, gameView.getSceneHeight());
        builder.addWall(0, 0, gameView.getSceneWidth(), 2);
        Arkanoid.changeScene(gameView.getGameScene());
        model = builder.build();
        moveBall();
    }

    private void startSecondLevel() {
        Game.Builder builder = new Game.Builder();

        gameView = new SecondLevelView();

        gameView.setHighScoreCountLabel(records.getProperty(String.valueOf(numLevel)));
        gameView.render();

        Rectangle platform = gameView.getPlatform();
        builder.platform(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());

        builder.ball(gameView.getBall().getRadius(), gameView.getBall().getCenterX(), gameView.getBall().getCenterY());

        List<Rectangle> bricks = gameView.getBricks();

        for (int i = 0; i < gameView.getAmountOfBreakableBricks(); i++) {
            Rectangle brick = bricks.get(i);
            builder.addDestroyableBrick(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight(), 5);
        }

        for (int i = gameView.getAmountOfBreakableBricks(); i < gameView.getAmountOfBricks(); i++) {
            Rectangle brick = bricks.get(i);
            builder.addImmortalBrick(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
        }

        addWalls(builder);
    }

    @FXML
    protected void endGame() {
        System.exit(0);
    }

    private static Scene loadNewScene(String fileName) throws IOException {
        return new Scene(FXMLLoader.load(Objects.requireNonNull(Presenter.class.getResource(fileName))));
    }

    public static void loadScenes() throws IOException {
        mainScene = loadNewScene("main-scene.fxml");
        aboutScene = loadNewScene("about-scene.fxml");
        gameOverScene = loadNewScene("game-over-scene.fxml");
        gameWinScene = loadNewScene("game-win-scene.fxml");
    }

    @FXML
    protected void startGame() {

        Notifications.getInstance().subscribe(Notifications.EventType.DESTROY, destroyable -> gameView.deleteBrick(destroyable.position()));
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
        //  setRecord();
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
        // setRecord();
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