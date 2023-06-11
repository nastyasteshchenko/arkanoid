package oop.arkanoid;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.*;

public class ScoresManager {

    public record LevelScore(String levelName, int score){}
    private final static String pathToRecordsFile = "records.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, LevelScore> scores = new HashMap<>();

    public ScoresManager() {
        try (JsonReader fileReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(Presenter.class.getResourceAsStream(pathToRecordsFile))))) {
            JsonArray mainJa = gson.fromJson(fileReader, JsonArray.class);
            for (JsonElement je : mainJa) {
                JsonObject jsonObject = je.getAsJsonObject();
                LevelScore levelScore = new LevelScore(jsonObject.get("levelName").getAsString(), jsonObject.get("score").getAsInt());
                scores.put(levelScore.levelName, levelScore);
            }
        } catch (IOException e) {
            //?
        }
    }


    public void writeScore(String levelName, int scoreValue) {
        if (scoreValue > scores.get(levelName).score) {
            scores.put(levelName, new LevelScore(levelName, scoreValue));
        }
    }

    public int getScore(String levelName){
        return scores.get(levelName).score;
    }

    public Collection<LevelScore> getScores(){
        return scores.values();
    }

    public void storeRecords() {
        try (JsonWriter jsonWriter = new JsonWriter(new FileWriter("src/main/resources/oop/arkanoid/" + pathToRecordsFile))) {
            jsonWriter.beginArray();
            for (LevelScore levelScore : scores.values()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("levelName", levelScore.levelName());
                jsonObject.addProperty("score", levelScore.score());
                gson.toJson(jsonObject, jsonWriter);
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            //?
        }
    }
}
