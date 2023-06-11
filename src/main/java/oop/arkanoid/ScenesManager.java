package oop.arkanoid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class ScenesManager {
    private final static String pathToLevels = "FXML/";
    private final Map<String, Scene> scenes = new HashMap<>();

    ScenesManager(ScoresManager scoresManager) throws IOException {

        scenes.put("main", loadNewScene(pathToLevels + "main-scene.fxml"));
        scenes.put("about", loadNewScene(pathToLevels + "about-scene.fxml"));
        scenes.put("game_over", loadNewScene(pathToLevels + "game-over-scene.fxml"));
        scenes.put("game_win", loadNewScene(pathToLevels + "game-win-scene.fxml"));
        scenes.put("game_passed", loadNewScene(pathToLevels + "game-passed-scene.fxml"));

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

            Pane root = FXMLLoader.load(Objects.requireNonNull(Arkanoid.class.getResource(pathToLevels + "records-scene.fxml")));
            Collection<ScoresManager.LevelScore> scores = scoresManager.getScores();

            for (ScoresManager.LevelScore score : scores) {

                Text levelScore = new Text(String.valueOf(score.score()));
                setTextParams(records, score, levelScore);

                root.getChildren().add(levelScore);
            }
            scenes.put("records", new Scene(root));
        }
    }


    private static void setTextParams(JsonObject records, ScoresManager.LevelScore score, Text levelScore) {
        JsonObject scoreText = records.getAsJsonObject("scoreLabel").getAsJsonObject(score.levelName());
        levelScore.setX(scoreText.get("x").getAsDouble());
        levelScore.setY(scoreText.get("y").getAsDouble());
        levelScore.setFont(Font.font(scoreText.get("font").getAsString()));
        levelScore.setFill(Color.valueOf(scoreText.get("color").getAsString()));
        levelScore.setStyle("-fx-font-size: " + scoreText.get("fontSize").getAsString());
    }

    private static Scene loadNewScene(String fileName) throws IOException {
        return new Scene(FXMLLoader.load(Objects.requireNonNull(Presenter.class.getResource(fileName))));
    }
}

