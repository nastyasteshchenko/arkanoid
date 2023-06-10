package oop.arkanoid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
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
    private static final Gson GSON_LOADER = new GsonBuilder().setPrettyPrinting().create();
    private static JsonObject records;
    private static int currentLevel = 1;
    private static final int AMOUNT_OF_LEVELS = 3;
    private static Timeline animation;
    private static LevelView gameView;
    private static GameLevel model;
    private static boolean gameIsStarted = false;
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

    static void checkGeneratingAllLevels() throws GeneratingGameException {
        for (; currentLevel < AMOUNT_OF_LEVELS + 1; currentLevel++) {
            model = GameLevel.initLevel(loadFileWithParamsForLevel());
        }
        currentLevel = 1;
    }

    static void loadResourcesBeforeStartApp() {
        try {
            mainScene = loadNewScene("FXML/main-scene.fxml");
            aboutScene = loadNewScene("FXML/about-scene.fxml");
            gameLoseScene = loadNewScene("FXML/game-over-scene.fxml");
            gameWinScene = loadNewScene("FXML/game-win-scene.fxml");
        } catch (IOException e) {
            createAlert(e);
        }
        try (JsonReader reader = new JsonReader(new FileReader("src/main/resources/oop/arkanoid/records.json"))) {
            records = GSON_LOADER.fromJson(reader, JsonObject.class);
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
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(Arkanoid.class.getResource("FXML/records-scene.fxml")));
            for (int level = 1; level < AMOUNT_OF_LEVELS + 1; level++) {
                Text levelScore = new Text(records.getAsJsonObject("records").get("level" + level).getAsString());
                LevelView.setRecordText(levelScore, records, "level" + level);
                root.getChildren().add(levelScore);
            }
            changeScene(new Scene(root));
        } catch (IOException e) {
            createAlert(e);
        }
    }

    private void gameLose() {
        prepareForGameOver(gameLoseScene);
    }

    private void gameWin() {
        prepareForGameOver(gameWinScene);
        currentLevel++;
    }

    private void prepareForGameOver(Scene gameWinScene) {
        animation.stop();
        setRecord();
        gameIsStarted = false;

        try (JsonWriter writer = new JsonWriter(new FileWriter("src/main/resources/oop/arkanoid/records.json"))) {
            GSON_LOADER.toJson(records, writer);
        } catch (IOException e) {
            createAlert(e);
        }
        changeScene(gameWinScene);
    }

    private void setRecord() {
        if (model.getScore() > records.getAsJsonObject("records").get("level" + currentLevel).getAsInt()) {
            records.getAsJsonObject("records").addProperty("level" + currentLevel, model.getScore());
        }
    }

    private void startLevel() {
        JsonObject paramsForLevel = loadFileWithParamsForLevel();

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

    private static JsonObject loadFileWithParamsForLevel() {
        String jsonFileName = "src/main/resources/oop/arkanoid/Levels/level" + currentLevel + ".json";

        JsonObject paramsForLevel = new JsonObject();
        try (JsonReader reader = new JsonReader(new FileReader(jsonFileName))) {
            paramsForLevel = GSON_LOADER.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            createAlert(e);
        }
        return paramsForLevel;
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

        builder.highScore(records.getAsJsonObject("records").get("level" + currentLevel).getAsInt());

        gameView = builder.build();
    }

}