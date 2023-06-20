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
import oop.arkanoid.notifications.*;
import oop.arkanoid.view.LevelView;

import java.io.*;

import static oop.arkanoid.AlertCreationUtil.alert;

public class Presenter {
    private static final Presenter INSTANCE = new Presenter();
    private final LevelsManager levelsManager = new LevelsManager();
    private final ScenesManager scenesManager = new ScenesManager();
    private final ScoresManager scoresManager = new ScoresManager();
    private Timeline animation;
    private LevelView gameView;
    private GameLevel model;
    private boolean gameIsStarted = false;
    private boolean isPause = false;
    private int currentLevel;
    private LevelInitiator levelsInitiator;

    static Presenter getInstance() {
        return INSTANCE;
    }

    public void setGameIsStarted() {
        gameIsStarted = true;
    }

    public void setPause() {
        isPause = !isPause;
        if (isPause) {
            animation.pause();
        } else {
            animation.play();
        }
    }

    public void movePlatform(double x) {
        if (gameIsStarted) {
            gameView.drawPlatform(model.updatePlatformPosition(x));
        }
    }

    void checkGeneratingAllLevels() throws GeneratingGameException {
        levelsManager.checkGeneratingAllLevels();
    }

    void loadResourcesBeforeStartApp() throws IOException {
        levelsManager.scanForLevels();
        scoresManager.scanForScores();
        scenesManager.scanForScenes(scoresManager);
    }

    @FXML
    protected void backToMenu() {
        try {
            scoresManager.reloadScores();
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
        Notifications.getInstance().subscribe(EventType.DESTROY, this, b -> {
            Brick brick = (Brick) b;
            gameView.deleteBrick(brick.position());
            gameView.drawScore(model.getScore());
        });

        Notifications.getInstance().subscribe(EventType.START_GAME, this, v -> {
            setGameIsStarted();
        });

        Notifications.getInstance().subscribe(EventType.PAUSE, this, v -> {
            setPause();
        });

        Notifications.getInstance().subscribe(EventType.MOVE_PLATFORM, this, x -> {
            movePlatform((double) x);
        });

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
        scoresManager.writeScore(levelsInitiator.getLevelName(), model.getScore());
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