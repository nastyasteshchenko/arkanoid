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

class LevelsManager {
    private static int currentLevel = 1;
    private final static String pathToLevels = "Levels";
    private final Map<String, JsonObject> availableLevels = new HashMap<>();

    LevelsManager() throws IOException {

        File levelsDir = new File("src/main/resources/oop/arkanoid/" + pathToLevels);

        if (!levelsDir.isDirectory()) {
            throw new NotDirectoryException("Expected \"Levels\" directory, but got file");
        }

        Gson gson = new Gson();

        for (File f : Objects.requireNonNull(levelsDir.listFiles())) {
            if (f.isDirectory()) {
                continue;
            }

            try (JsonReader reader = new JsonReader(new FileReader(f))) {
                availableLevels.put(f.getName(), gson.fromJson(reader, JsonObject.class));
            }
        }
    }

    String getCurrentLevel() {
        return "level" + currentLevel;
    }

    void checkGeneratingAllLevels() throws GeneratingGameException {
        for (JsonObject object : availableLevels.values()) {
            GameLevel.initLevel(object);
        }
    }

    void increaseLevel() {
        currentLevel++;
    }

    JsonObject getCurrentLevelJsonObject() {
        return availableLevels.get(getCurrentLevel() + ".json");
    }
}
