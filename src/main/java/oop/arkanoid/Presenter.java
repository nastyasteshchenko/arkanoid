package oop.arkanoid;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.util.Duration;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.notifications.NotificationsAboutDestroy;
import oop.arkanoid.notifications.Subscriber;
import oop.arkanoid.view.LevelView;

import java.io.*;

import static oop.arkanoid.AlertCreationUtil.alert;

//TODO подумать над inner class, либо подписка на события
public class Presenter implements Subscriber {
    private static int currentLevel;
    private static LevelInitiator levelsInitiator;
    private static ScoresManager scoresManager;
    private static LevelsManager levelsManager;
    private static ScenesManager scenesManager;
    private static Timeline animation;
    private static LevelView gameView;
    private static GameLevel model;
    private static boolean gameIsStarted = false;
    private static boolean isPause = false;

    @Override
    public void update(Brick brick) {
        gameView.deleteBrick(brick.position());
        gameView.drawScore(model.getScore());
    }

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

    static void checkGeneratingAllLevels() throws GeneratingGameException {
        levelsManager.checkGeneratingAllLevels();
    }

    static void loadResourcesBeforeStartApp() throws IOException {
        levelsManager = new LevelsManager();
        levelsManager.scanForLevels();
        scoresManager = new ScoresManager();
        scoresManager.scanForScores();
        scenesManager = new ScenesManager();
        scenesManager.scanForScenes(scoresManager);
    }

    @FXML
    protected void backToMenu() {
        try {
            scoresManager = new ScoresManager();
            scenesManager.changeRecordsScene(scoresManager);
        } catch (Exception e) {
            alert(e.getMessage());
        }
        changeScene(scenesManager.getScene("main"));
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
        NotificationsAboutDestroy.getInstance().subscribe(this);
        currentLevel = 1;
        levelsInitiator = new LevelInitiator(currentLevel);
        startLevel();
    }

    @FXML
    protected void backToMainScene() {
        changeScene(scenesManager.getScene("main"));
    }

    @FXML
    protected void watchAboutGame() {
        changeScene(scenesManager.getScene("about"));
    }

    @FXML
    protected void watchRecords() {
        changeScene(scenesManager.getScene("records"));
    }

    private void gameLose() {
        prepareForGameOver(scenesManager.getScene("game_over"));
    }

    private void gameWin() {
        prepareForGameOver(scenesManager.getScene("game_win"));
        currentLevel++;
        levelsInitiator = new LevelInitiator(currentLevel);
    }

    private void prepareForGameOver(Scene gameWinScene) {
        animation.stop();
        setRecord();
        gameIsStarted = false;
        changeScene(gameWinScene);
    }

    private void setRecord() {
        scoresManager.writeScore(levelsInitiator.getCurrentLevel(), model.getScore());
        scoresManager.storeRecords();
    }

    private void startLevel() {

        try {
            model = levelsInitiator.initLevelModel();
            if (model == null) {
                changeScene(scenesManager.getScene("game_passed"));
                return;
            }
        } catch (GeneratingGameException e) {
            alert(e.getMessage());
        }

        gameView = levelsInitiator.initLevelView(model);
        changeScene(gameView.getGameScene());
        startAnimation();

    }

    private void startAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(2.5), ae -> {
            if (gameIsStarted) {
                gameView.drawBall(model.nextBallPosition());
                switch (model.gameState()) {
                    case WIN -> gameWin();
                    case LOSE -> gameLose();
                }
            }
        }));
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }

    private void changeScene(Scene scene) {
        Arkanoid.getStage().setScene(scene);
    }
}