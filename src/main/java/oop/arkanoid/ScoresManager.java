package oop.arkanoid;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.*;

class ScoresManager {
    private final static String PATH_TO_RECORDS = "records.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, SingleScore> scores = new HashMap<>();

    private ScoresManager() {
    }

    static ScoresManager create() throws IOException {
        ScoresManager scoresManager = new ScoresManager();
        scoresManager.scanForScores();
        return scoresManager;
    }

    boolean isNewScore(String levelName, int scoreValue, double time) {
        if (scores.containsKey(levelName)) {
            return scoreValue > scores.get(levelName).score() || (scoreValue == scores.get(levelName).score() && time < scores.get(levelName).time());
        }
        return true;
    }

    void writeScore(String levelName, String author, int scoreValue, double time) {
        if (this.isNewScore(levelName, scoreValue, time)) {
            scores.put(levelName, new SingleScore(levelName, author, scoreValue, time));
        }
    }

    int getScoreForLevel(String levelName) {
        if (scores.containsKey(levelName)) {
            return scores.get(levelName).score();
        }
        return 0;
    }

    Collection<SingleScore> getScores() {
        return scores.values();
    }

    void storeRecords() {
        try (FileWriter jsonWriter = new FileWriter("src/main/resources/oop/arkanoid/" + PATH_TO_RECORDS)) {
            ArrayList<SingleScore> singleScores = new ArrayList<>(scores.values());
            gson.toJson(singleScores, jsonWriter);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error saving scores");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void scanForScores() throws IOException {

        scores.clear();

        try (JsonReader fileReader = new JsonReader(new BufferedReader(new InputStreamReader(Objects.requireNonNull(Presenter.class.getResourceAsStream(PATH_TO_RECORDS)))))) {
            JsonArray ja = gson.fromJson(fileReader, JsonArray.class);
            if (ja == null) {
                return;
            }
            for (JsonElement je : ja) {
                SingleScore levelScore = gson.fromJson(je, SingleScore.class);
                scores.put(levelScore.levelName(), levelScore);
            }
        }
    }
}
