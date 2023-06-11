package oop.arkanoid;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LevelsManager {
    private static int currentLevel = 1;
    private int amountOfLevels = 0;
    private final static String pathToLevels = "Levels";
    private final Map<String, JsonObject> availableLevels = new HashMap<>();

    public LevelsManager() throws IOException {

        File levelsDir = new File("src/main/resources/oop/arkanoid/" + pathToLevels);

        if (!levelsDir.isDirectory()) {
            throw new NotDirectoryException("Expected \"Levels\" directory, but got file");
        }

        Gson gson = new Gson();

        for (File f : Objects.requireNonNull(levelsDir.listFiles())) {
            if (f.isDirectory()) {
                continue;
            }

            amountOfLevels++;

            try (JsonReader reader = new JsonReader(new FileReader(f))) {
                JsonObject paramsForLevel = gson.fromJson(reader, JsonObject.class);
                availableLevels.put(f.getName(), paramsForLevel);
            }
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    void checkGeneratingAllLevels() throws GeneratingGameException {
        for (JsonObject object : availableLevels.values()) {
            GameLevel.initLevel(object);
        }
    }

    public void increaseLevel() {
        currentLevel++;
    }

    public JsonObject getCurrentLevelJsonObject() {
        return availableLevels.get("level" + currentLevel + ".json");
    }
}
