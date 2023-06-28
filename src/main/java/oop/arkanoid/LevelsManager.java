package oop.arkanoid;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.view.LevelView;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class LevelsManager {
    private final static String PATH_TO_LEVELS_DIR = "./oop/arkanoid/Levels";
    private final Map<String, JsonObject> availableLevels = new HashMap<>();
    private LevelInitiator levelInitiator = new LevelInitiator(0, null);

    LevelsManager() {
    }

    void scanForLevels() throws IOException {

        availableLevels.clear();

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
                availableLevels.put(f.getName(), gson.fromJson(reader, JsonObject.class));
            }
        }
    }

    GameLevel initLevelModel(int level) throws GeneratingGameException {
        if (levelInitiator.numLevel != level) {
            levelInitiator = new LevelInitiator(level, getLevelJsonObject(level));
        }
        return levelInitiator.initLevelModel();
    }

    LevelView initLevelView(GameLevel model, ScoresManager scoresManager) {
        return levelInitiator.initLevelView(model, scoresManager);
    }

    void checkGeneratingAllLevels() throws GeneratingGameException {
        int level = 1;
        for (JsonObject jsonObject : availableLevels.values()) {
            LevelInitiator levelsInitiator = new LevelInitiator(level, jsonObject);
            levelsInitiator.initLevelModel();
            level++;
        }
    }

    JsonObject getLevelJsonObject(int level) {
        return availableLevels.get("level" + level + ".json");
    }

}
