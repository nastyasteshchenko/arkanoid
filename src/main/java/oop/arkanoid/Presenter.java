package oop.arkanoid;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.Point;
import oop.arkanoid.view.LevelView;

import java.io.*;
import java.util.*;

//TODO использовать json
//Я когда-нибудь нормально обработаю исключения

public class Presenter {
    private static final Gson gson = new Gson();
    private static JsonObject records = new JsonObject();
    private static int numLevel = 1;
    private static Timeline animation;
    private static LevelView gameView;
    private static GameLevel model;
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

        try (JsonReader reader = new JsonReader(new FileReader("src/main/resources/oop/arkanoid/records.json"))) {
            records = gson.fromJson(reader, JsonObject.class);
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
        try (JsonReader reader = new JsonReader(new FileReader("src/main/resources/oop/arkanoid/records.json"))) {
            records = gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
//тоже когда-нибудь обработать
        }
        Pane root = FXMLLoader.load(Objects.requireNonNull(Arkanoid.class.getResource("records-scene.fxml")));
        Text level1Score = new Text(records.getAsJsonObject("records").get("level1").getAsString());
        LevelView.setRecordText(level1Score, "1");
        Text level2Score = new Text(records.getAsJsonObject("records").get("level2").getAsString());
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

        try (JsonWriter writer = new JsonWriter(new FileWriter("src/main/resources/oop/arkanoid/records.json"))) {
            gson.toJson(records, writer);
        } catch (IOException e) {
//тоже когда-нибудь обработать
        }
        Arkanoid.changeScene(gameWinScene);
    }

    private void setRecord() {
        if (model.getScore() > records.getAsJsonObject("records").get("level" + numLevel).getAsInt()) {
            records.getAsJsonObject("records").addProperty("level1", model.getScore());
        }
    }

    private void startLevel() {
        String jsonFileName = "src/main/resources/oop/arkanoid/level" + numLevel + ".json";

        JsonObject paramsForLevel = new JsonObject();
        try (JsonReader reader = new JsonReader(new FileReader(jsonFileName))) {
            paramsForLevel = gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
//тоже когда-нибудь обработать
        }
        model = GameLevel.initLevel(paramsForLevel);
        LevelView.Builder builder = new LevelView.Builder(paramsForLevel);

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

        builder.highScore(records.getAsJsonObject("records").get("level" + numLevel).getAsInt());

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