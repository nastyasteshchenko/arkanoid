package oop.arkanoid;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

class ScoresManager {
    private final static Path PATH_TO_RECORDS = Path.of("records.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, SingleScore> scores = new HashMap<>();

    private ScoresManager() {
    }

    static ScoresManager create() throws IOException {
        ScoresManager scoresManager = new ScoresManager();
        scoresManager.scanForScores();
        return scoresManager;
    }

    boolean isNewScore(String levelName, int score, double time) {
        SingleScore singleScore = scores.get(levelName);
        if (singleScore != null) {
            return score > singleScore.score() || (score == singleScore.score() && time < singleScore.time());
        }
        return true;
    }

    void writeScore(SingleScore singleScore) {
        if (this.isNewScore(singleScore.levelName(), singleScore.score(), singleScore.time())) {
            scores.put(singleScore.levelName(), singleScore);
        }
    }

    int getScoreForLevel(String levelName) {
        SingleScore singleScore = scores.get(levelName);
        if (singleScore != null) {
            return singleScore.score();
        }
        return 0;
    }

    Collection<SingleScore> getScores() {
        return scores.values();
    }

    void storeRecords() {
        Path pathToSave = Path.of("src", "main", "resources", "oop", "arkanoid", PATH_TO_RECORDS.toString());
        try (FileWriter jsonWriter = new FileWriter(pathToSave.toString())) {
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

        try (JsonReader fileReader = new JsonReader(new BufferedReader(new InputStreamReader(Objects.requireNonNull(Presenter.class.getResourceAsStream(PATH_TO_RECORDS.toString())))))) {
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
