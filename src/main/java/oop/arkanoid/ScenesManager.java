package oop.arkanoid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import oop.arkanoid.view.LevelView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScenesManager {
    private final static String pathToLevels = "FXML/";
    private final Map<String, Scene> scenes = new HashMap<>();

    public ScenesManager(ScoresManager scoresManager) throws IOException {

        scenes.put("main", loadNewScene(pathToLevels + "main-scene.fxml"));
        scenes.put("about", loadNewScene(pathToLevels + "about-scene.fxml"));
        scenes.put("game_over", loadNewScene(pathToLevels + "game-over-scene.fxml"));
        scenes.put("game_win", loadNewScene(pathToLevels + "game-win-scene.fxml"));

        try (JsonReader reader = new JsonReader(new InputStreamReader(Objects.requireNonNull(Presenter.class.getResourceAsStream("records_scene.json"))))) {
            Gson GSON_LOADER = new GsonBuilder().setPrettyPrinting().create();
            JsonObject records = GSON_LOADER.fromJson(reader, JsonObject.class);

            Pane root = FXMLLoader.load(Objects.requireNonNull(Arkanoid.class.getResource(pathToLevels + "records-scene.fxml")));
            Collection<ScoresManager.LevelScore> scores = scoresManager.getScores();

            for (ScoresManager.LevelScore score : scores) {
                Text levelScore = new Text(String.valueOf(score.score()));
                LevelView.setRecordText(levelScore, records, score.levelName());
                root.getChildren().add(levelScore);
            }
            scenes.put("records", new Scene(root));
        }

    }

    private static Scene loadNewScene(String fileName) throws IOException {
        return new Scene(FXMLLoader.load(Objects.requireNonNull(Presenter.class.getResource(fileName))));
    }


    public Scene getScene(String name) {
        return scenes.get(name);
    }
}

