package oop.arkanoid;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GameState;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.notifications.*;
import oop.arkanoid.pane.*;
import oop.arkanoid.view.LevelView;

import java.io.*;

import static oop.arkanoid.AlertCreationUtil.createResourcesAlert;

class Presenter {

    private final LevelsManager levelsManager = new LevelsManager();
    private final ScoresManager scoresManager = new ScoresManager();
    private final AboutPane aboutPane = new AboutPane();
    private final GameOverPane gameOverPane = new GameOverPane();
    private final GamePassedPane gamePassedPane = new GamePassedPane();
    private final GameWinPane gameWinPane = new GameWinPane();
    private final MainMenuPane mainMenuPane = new MainMenuPane();
    private final SaveScorePane saveScorePane = new SaveScorePane();
    private final ScoresPane scoresPane = new ScoresPane();

    private double secondsPassed = 0.;

    private Timeline animation;
    private LevelView gameView;
    private GameLevel model;
    private boolean isPause = false;
    private int currentLevel;
    private final StackPane rootStackPane;

    Presenter(StackPane rootStackPane) {
        this.rootStackPane = rootStackPane;
    }


    void loadResourcesBeforeStartApp() throws IOException, GeneratingGameException {
        levelsManager.scanForLevels();
        levelsManager.checkGeneratingAllLevels();

        scoresManager.scanForScores();

        Notifications.getInstance().subscribe(EventType.START_GAME, this, v -> startGame());

        Notifications.getInstance().subscribe(EventType.EXIT, this, v -> exitGame());

        Notifications.getInstance().subscribe(EventType.RECORDS, this, v -> watchRecords());

        Notifications.getInstance().subscribe(EventType.BACK, this, v -> backToMainScene());

        Notifications.getInstance().subscribe(EventType.ABOUT, this, v -> watchAboutGame());

        updateMainPane(mainMenuPane);
    }

    private void setGameIsStarted() {
        animation.play();
    }

    private void setPause() {
        isPause = !isPause;
        if (isPause) {
            animation.pause();
        } else {
            animation.play();
        }
    }

    private void movePlatform(double x) {
        if (animation.getCurrentRate() != 0.) {
            gameView.drawPlatform(model.updatePlatformPosition(x));
        }
    }

    private void restartAllGame() {
        scoresManager.scanForScores();
        updateMainPane(mainMenuPane);
    }

    private void exitGame() {
        System.exit(0);
    }

    private void endGame() {
        animation.stop();
//        setScore();

        if (scoresManager.isNewScore("level" + currentLevel, model.getScore(), secondsPassed)) {
            saveScorePane.setShowingScore(model.getScore(), secondsPassed);
            updateMainPane(saveScorePane);
        } else {
            switch (model.gameState()) {
                case WIN -> gameWin();
                case LOSE -> gameLose();
            }
        }
    }

    private void startGame() {
        Notifications.getInstance().subscribe(EventType.DESTROY, this, b -> {
            if (b instanceof Brick brick) {
                gameView.deleteBrick(brick.position());
                gameView.drawScore(model.getScore());
            }
        });

        Notifications.getInstance().subscribe(EventType.START_PLAYING_GAME, this, v -> setGameIsStarted());

        Notifications.getInstance().subscribe(EventType.PAUSE, this, v -> setPause());

        Notifications.getInstance().subscribe(EventType.MOVE_PLATFORM, this, v -> {
            if (v instanceof Double x) {
                movePlatform(x);
            }
        });

        Notifications.getInstance().subscribe(EventType.RESTART_LEVEL, this, v -> startLevel());

        Notifications.getInstance().subscribe(EventType.RESTART_GAME, this, v -> restartAllGame());

        currentLevel = 1;
        startLevel();
    }

    private void backToMainScene() {
        updateMainPane(mainMenuPane);
    }

    private void watchAboutGame() {
        updateMainPane(aboutPane);
    }

    private void watchRecords() {
        scoresPane.updateScores(scoresManager.getScores());
        updateMainPane(scoresPane);
    }

    private void gameLose() {
        prepareForGameOver(gameOverPane);
    }

    private void gameWin() {
        prepareForGameOver(gameWinPane);
        currentLevel++;
    }

    private void prepareForGameOver(Pane pane) {
        animation.stop();
        setRecord();
        updateMainPane(pane);
    }

    private void setRecord() {
        scoresManager.writeScore("level" + currentLevel, "CHANGE!!!", model.getScore(), 0.);
        scoresManager.storeRecords();
    }

    private void startLevel() {
        try {
            model = levelsManager.initLevelModel(currentLevel);
            if (model == null) {
                updateMainPane(gamePassedPane);
                return;
            }
        } catch (GeneratingGameException e) {
            createResourcesAlert(e.getMessage());
        }

        gameView = levelsManager.initLevelView(model, scoresManager);
        updateMainPane(gameView.getGamePane());
        startAnimation();

    }

    private void startAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(2.5), ae -> {
            secondsPassed += animation.getKeyFrames().get(0).getTime().toSeconds();
            gameView.drawBall(model.nextBallPosition());
            if (model.gameState() != GameState.PROCESS) {
                endGame();
            }
        }));
        animation.setCycleCount(Animation.INDEFINITE);
    }

    private void updateMainPane(Pane pane) {
        rootStackPane.getChildren().clear();
        rootStackPane.getChildren().add(pane);
    }

}