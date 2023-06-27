package oop.arkanoid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import oop.arkanoid.pane.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class ScenesManager {
    private final Map<String, Scene> scenes = new HashMap<>();

    ScenesManager() {
    }

    void scanForScenes(ScoresManager scoresManager) throws IOException {

        scenes.clear();

        scenes.put("main", new Scene(new MainMenuPane()));
        scenes.put("about", new Scene(new AboutPane()));
        scenes.put("game_over", new Scene(new GameOverPane()));
        scenes.put("game_win", new Scene(new GameWinPane()));
        scenes.put("game_passed", new Scene(new GamePassedPane()));

        addRecordsScene(scoresManager);
    }

    Scene getScene(String name) {
        return scenes.get(name);
    }

    void changeRecordsScene(ScoresManager scoresManager) throws IOException {
        scenes.remove("records");
        addRecordsScene(scoresManager);
    }

    private void addRecordsScene(ScoresManager scoresManager) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(Objects.requireNonNull(Presenter.class.getResourceAsStream("records_scene.json"))))) {
            Gson GSON_LOADER = new GsonBuilder().setPrettyPrinting().create();
            JsonObject records = GSON_LOADER.fromJson(reader, JsonObject.class);

            Pane recordsPane = new RecordsPane();
            Collection<ScoresManager.LevelScore> scores = scoresManager.getScores();

            for (ScoresManager.LevelScore score : scores) {

                Text levelScore = new Text(String.valueOf(score.score()));
                setTextParams(records, score, levelScore);

                recordsPane.getChildren().add(levelScore);
            }
            scenes.put("records", new Scene(recordsPane));
        }
    }


    private static void setTextParams(JsonObject records, ScoresManager.LevelScore score, Text levelScore) {
        JsonObject scoreText = records.getAsJsonObject("scoreLabel").getAsJsonObject(score.levelName());
        levelScore.setTextAlignment(TextAlignment.CENTER);
        levelScore.setWrappingWidth(100);
        levelScore.setX(scoreText.get("x").getAsDouble());
        levelScore.setY(scoreText.get("y").getAsDouble());
        levelScore.setFont(Font.font(scoreText.get("font").getAsString()));
        levelScore.setFill(Color.valueOf(scoreText.get("color").getAsString()));
        levelScore.setStyle("-fx-font-size: " + scoreText.get("fontSize").getAsString());
    }
}

