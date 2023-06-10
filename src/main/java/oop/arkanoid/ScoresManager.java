package oop.arkanoid;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScoresManager {
    private record LevelScore(String levelName, String player, int score) {}
    private final static String pathToRecordsFile = "records.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, LevelScore> scores = new HashMap<>();
    private final File scoresJson;
    public ScoresManager() throws FileNotFoundException {
        scoresJson = new File(Objects.requireNonNull(this.getClass().getResource(pathToRecordsFile)).getFile());
        if (scoresJson.isDirectory()) {
            throw new FileNotFoundException("Expected .json file, but got a directory");
        }
        try (FileReader fileReader = new FileReader(scoresJson)) {
            JsonArray mainJa = gson.fromJson(fileReader, JsonArray.class);
            for (JsonElement je : mainJa) {
                LevelScore levelScore = gson.fromJson(je, LevelScore.class);
                scores.put(levelScore.levelName(), levelScore);
            }
        } catch (IOException e) {
            //?
        }
    }

    public void writeScore(String levelName, String player, int scoreValue) {
        scores.put(levelName, new LevelScore(levelName, player, scoreValue));
    }

    public void storeRecords() {
        try (JsonWriter jsonWriter = new JsonWriter(new FileWriter(scoresJson))) {
            jsonWriter.beginArray();
            for (LevelScore levelScore : scores.values()) {
                gson.toJson(levelScore, LevelScore.class, jsonWriter);
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            //?
        }
    }
}
