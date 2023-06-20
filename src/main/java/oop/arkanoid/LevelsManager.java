package oop.arkanoid;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import oop.arkanoid.model.GeneratingGameException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class LevelsManager {
    private final static String PATH_TO_LEVELS_DIR = "./oop/arkanoid/Levels";
    private static final Map<String, JsonObject> AVAILABLE_LEVELS = new HashMap<>();

    LevelsManager() {}

    void scanForLevels() throws IOException {
        File levelsDir = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(PATH_TO_LEVELS_DIR)).getFile());

        if (!levelsDir.isDirectory()) {
            throw new NotDirectoryException("Expected \"Levels\" directory, but got file");
        }

        Gson gson = new Gson();

        for (File f : Objects.requireNonNull(levelsDir.listFiles())) {
            if (f.isDirectory()) {
                continue;
            }

            try (JsonReader reader = new JsonReader(new FileReader(f))) {
                AVAILABLE_LEVELS.put(f.getName(), gson.fromJson(reader, JsonObject.class));
            }
        }
    }

    void checkGeneratingAllLevels() throws GeneratingGameException {
        int level = 1;
        for (JsonObject ignored : AVAILABLE_LEVELS.values()) {
            LevelInitiator levelsInitiator = new LevelInitiator(level);
            levelsInitiator.initLevelModel();
            level++;
        }
    }

    static JsonObject getLevelJsonObject(String level) {
        return AVAILABLE_LEVELS.get(level + ".json");
    }

}
