package oop.arkanoid;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import oop.arkanoid.model.Game;
import oop.arkanoid.model.Point;
import oop.arkanoid.view.LevelView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

//Я когда-нибудь нормально обработаю исключения

public class Presenter {
    private static final Properties records = new Properties();
    private static int numLevel = 1;
    private static Timeline animation;
    private static LevelView gameView;
    private static Game model;
    private static boolean gameIsStarted;
    private static boolean isPause = false;
    private static Scene mainScene;
    private static Scene aboutScene;
    private static Scene gameLoseScene;
    private static Scene gameWinScene;

    public static void startPlayingGame() {
        gameIsStarted = true;
    }

    public static void setPause() {
        isPause = !isPause;
        if (isPause) {
            animation.pause();
        } else {
            animation.play();
        }
    }

    public static void movePlatform(double x) {
        if (gameIsStarted) {
            gameView.drawPlatform(model.updatePlatformPosition(x));
        }
    }

    static void loadScenes() throws IOException {
        mainScene = loadNewScene("main-scene.fxml");
        aboutScene = loadNewScene("about-scene.fxml");
        gameLoseScene = loadNewScene("game-over-scene.fxml");
        gameWinScene = loadNewScene("game-win-scene.fxml");
    }

    @FXML
    protected void restartGame() {
        startLevel();
    }

    @FXML
    protected void endGame() {
        System.exit(0);
    }

    @FXML
    protected void startGame() {

        Notifications.getInstance().subscribe(Notifications.EventType.DESTROY, destroyable -> {
            gameView.deleteBrick(destroyable.position());
            gameView.drawScore(model.getScore());
        });
        try (FileInputStream recordsInputStream = new FileInputStream("src/main/resources/oop/arkanoid/records.properties")) {
            records.load(recordsInputStream);
        } catch (IOException e) {
//тоже когда-нибудь обработать
        }
        startLevel();
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
        LevelView.setRecordText(level1Score, "1");
        Text level2Score = new Text(records.getProperty("2"));
        LevelView.setRecordText(level2Score, "2");
        root.getChildren().addAll(level1Score, level2Score);
        Arkanoid.changeScene(new Scene(root));

    }

    private void gameLose() throws IOException {
        prepareForGameOver(gameLoseScene);
    }

    private void gameWin() throws IOException {
        prepareForGameOver(gameWinScene);
        numLevel++;
    }

    private void prepareForGameOver(Scene gameWinScene) {
        animation.stop();
        setRecord();
        gameIsStarted = false;
        try (FileOutputStream recordsOutputStream = new FileOutputStream("src/main/resources/oop/arkanoid/records.properties")) {
            records.store(recordsOutputStream, null);
        } catch (IOException e) {
//тоже
        }
        Arkanoid.changeScene(gameWinScene);
    }

    private void setRecord() {
        if (model.getScore() > Integer.parseInt(records.getProperty(String.valueOf(numLevel)))) {
            records.setProperty(String.valueOf(numLevel), String.valueOf(model.getScore()));
        }
    }

    private void startLevel() {
        String propertyFileName = "src/main/resources/oop/arkanoid/level" + numLevel + ".properties";
        Properties properties = new Properties();
        try (FileInputStream recordsInputStream = new FileInputStream(propertyFileName)) {
            properties.load(recordsInputStream);
        } catch (IOException e) {
//тоже когда-нибудь обработать
        }

        model = Game.initLevel(properties);
        LevelView.Builder builder = new LevelView.Builder(properties);

        builder.ball(model.getBallPosition(), model.getBallRadius())
                .platform(model.getPlatformPosition(), model.getPlatformSize())
                .gameScene(model.getSceneSize());

        Point brickSize = model.getBrickSize();
        ArrayList<Point> standardBricks = model.getStandardBricks();

        standardBricks.forEach(b -> builder.addStandardBrick(b, brickSize));

        ArrayList<Point> immortalBricks = model.getImmortalBricks();
        immortalBricks.forEach(b -> builder.addImmortalBrick(b, brickSize));

        ArrayList<Point> doubleHitBricks = model.getDoubleHitBricks();
        doubleHitBricks.forEach(b -> builder.addDoubleHitBrick(b, brickSize));

        builder.highScore(Integer.parseInt(records.getProperty(Integer.toString(numLevel))));

        gameView = builder.build();
        Arkanoid.changeScene(gameView.getGameScene());

        startAnimation();

    }

    //TODO подумать над названием
    private void startAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(2.5), ae -> {
            if (gameIsStarted) {
                try {
                    gameView.drawBall(model.nextBallPosition());
                    if (model.gameWin()) {
                        gameWin();
                    }
                    if (model.gameLose()) {
                        gameLose();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }

    private static Scene loadNewScene(String fileName) throws IOException {
        return new Scene(FXMLLoader.load(Objects.requireNonNull(Presenter.class.getResource(fileName))));
    }
}