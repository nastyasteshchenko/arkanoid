package oop.arkanoid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import oop.arkanoid.model.Ball;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.model.barriers.Barrier;
import oop.arkanoid.model.barriers.Brick;
import oop.arkanoid.model.barriers.Health;
import oop.arkanoid.model.barriers.Platform;
import oop.arkanoid.view.LevelView;

import java.io.*;
import java.util.*;

import static oop.arkanoid.Arkanoid.changeScene;
import static oop.arkanoid.Arkanoid.createAlert;

public class Presenter {
    private static ScoresManager scoresManager;
    private static LevelsManager levelsManager;
    private static Timeline animation;
    private static LevelView gameView;
    private static GameLevel model;
    private static boolean gameIsStarted = false;
    private static boolean isPause = false;
    private static Scene mainScene;
    private static Scene aboutScene;
    private static Scene gameLoseScene;
    private static Scene gameWinScene;
    private static Scene recordsScene;

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

    static void loadResourcesBeforeStartApp() {
        try {
            scoresManager = new ScoresManager();
            levelsManager = new LevelsManager();
        } catch (Exception e) {
            createAlert(e);
        }

        try {
            mainScene = loadNewScene("FXML/main-scene.fxml");
            aboutScene = loadNewScene("FXML/about-scene.fxml");
            gameLoseScene = loadNewScene("FXML/game-over-scene.fxml");
            gameWinScene = loadNewScene("FXML/game-win-scene.fxml");

            try (JsonReader reader = new JsonReader(new InputStreamReader(Objects.requireNonNull(Presenter.class.getResourceAsStream("records_scene.json"))))) {
                Gson GSON_LOADER = new GsonBuilder().setPrettyPrinting().create();
                JsonObject records = GSON_LOADER.fromJson(reader, JsonObject.class);

                Pane root = FXMLLoader.load(Objects.requireNonNull(Arkanoid.class.getResource("FXML/records-scene.fxml")));
                Collection<ScoresManager.LevelScore> scores = scoresManager.getScores();

                for (ScoresManager.LevelScore score : scores) {
                    Text levelScore = new Text(String.valueOf(score.score()));
                    LevelView.setRecordText(levelScore, records, score.levelName());
                    root.getChildren().add(levelScore);
                }

                recordsScene = new Scene(root);
            }
        } catch (IOException e) {
            createAlert(e);
        }
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
            model.increaseScore(destroyable.score());
            gameView.deleteBrick(destroyable.position());
            gameView.drawScore(model.getScore());
        });

        startLevel();
    }

    @FXML
    protected void backToMainScene() {
        changeScene(mainScene);
    }

    @FXML
    protected void watchAboutGame() {
        changeScene(aboutScene);
    }

    @FXML
    protected void watchRecords() {
        changeScene(recordsScene);
    }

    private void gameLose() {
        prepareForGameOver(gameLoseScene);
    }

    private void gameWin() {
        prepareForGameOver(gameWinScene);
        levelsManager.increaseLevel();
    }

    private void prepareForGameOver(Scene gameWinScene) {
        animation.stop();
        setRecord();
        gameIsStarted = false;
        changeScene(gameWinScene);
    }

    private void setRecord() {
        scoresManager.writeScore("level" + levelsManager.getCurrentLevel(), model.getScore());
        scoresManager.storeRecords();
    }

    private void startLevel() {
        JsonObject paramsForLevel = levelsManager.getCurrentLevelJsonObject();

        try {
            model = GameLevel.initLevel(paramsForLevel);
        } catch (GeneratingGameException e) {
            createAlert(e);
        }

        setGameView(paramsForLevel);

        changeScene(gameView.getGameScene());

        startAnimation();

    }

    private void startAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(2.5), ae -> {
            if (gameIsStarted) {
                gameView.drawBall(model.nextBallPosition());
                switch (model.gameState()) {
                    case GAME_WIN -> gameWin();
                    case GAME_LOSE -> gameLose();
                }
            }
        }));
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }

    private static Scene loadNewScene(String fileName) throws IOException {
        return new Scene(FXMLLoader.load(Objects.requireNonNull(Presenter.class.getResource(fileName))));
    }

    private static void setGameView(JsonObject paramsForLevel) {
        LevelView.Builder builder = new LevelView.Builder(paramsForLevel);

        List<Barrier> barriers = model.getBarriers();
        for (Barrier barrier : barriers) {
            if (barrier instanceof Platform platform) {
                builder.platform(platform.position, platform.size);
                continue;
            }
            if (barrier instanceof Brick brick) {
                if (brick.health instanceof Health.Immortal) {
                    builder.addImmortalBrick(brick.position, brick.size);
                    continue;
                }
                if (brick.health.getValue() == 1) {
                    builder.addStandardBrick(brick.position, brick.size);
                    continue;
                }
                if (brick.health.getValue() == 2) {
                    builder.addDoubleHitBrick(brick.position, brick.size);
                }
            }
        }

        Ball ball = model.getBall();
        builder.ball(ball.getPosition(), ball.radius).gameScene(model.getSceneSize());

        builder.highScore(scoresManager.getScore("level" + levelsManager.getCurrentLevel()));

        gameView = builder.build();
    }

}