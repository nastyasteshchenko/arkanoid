package oop.arkanoid;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.*;

import static oop.arkanoid.AlertCreationUtil.createResourcesAlert;

class ScoresManager {
    private final static String PATH_TO_RECORDS = "records.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, SingleScore> scores = new HashMap<>();

    ScoresManager() {
    }

    void scanForScores() {

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
        } catch (IOException e) {
            createResourcesAlert(e.getMessage());
            System.exit(0);
        }
    }

    public boolean isNewScore(String levelName, int scoreValue, double time) {
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

    double getScoreForLevel(String levelName) {
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
            createResourcesAlert(e.getMessage());
        }
    }
}
