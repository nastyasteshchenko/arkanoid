package oop.arkanoid;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import oop.arkanoid.model.GameLevel;
import oop.arkanoid.model.GeneratingGameException;
import oop.arkanoid.view.LevelView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class LevelsManager {
    private final static String PATH_TO_LEVELS_DIR = "./oop/arkanoid/Levels";
    private final Map<String, JsonObject> availableLevels = new HashMap<>();
    private LevelInitiator levelInitiator = new LevelInitiator(0, null);

    void scanForLevels() throws IOException, URISyntaxException {

        availableLevels.clear();

        URI uri = ClassLoader.getSystemResource(PATH_TO_LEVELS_DIR).toURI();

        Path levelsDir = Paths.get(uri);

        if (!Files.isDirectory(levelsDir)) {
            throw new NotDirectoryException("Expected \"Levels\" directory, but got file");
        }

        Gson gson = new Gson();

        for (Path f : Objects.requireNonNull(Files.newDirectoryStream(levelsDir))) {
            if (Files.isDirectory(f)) {
                continue;
            }

            try (JsonReader reader = new JsonReader(new BufferedReader(new FileReader(f.toString())))) {
                availableLevels.put(f.getFileName().toString(), gson.fromJson(reader, JsonObject.class));
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

    private JsonObject getLevelJsonObject(int level) {
        return availableLevels.get("level" + level + ".json");
    }

}
