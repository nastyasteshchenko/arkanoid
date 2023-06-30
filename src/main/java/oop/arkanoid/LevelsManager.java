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
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

class LevelsManager {
    private final static Path PATH_TO_LEVELS_DIR = Path.of(".", "oop", "arkanoid", "Levels");
    private final Map<String, JsonObject> availableLevels = new HashMap<>();
    private LevelInitiator levelInitiator = new LevelInitiator(null, "");

    private LevelsManager() {
    }

    static LevelsManager create() throws IOException, URISyntaxException {
        LevelsManager levelsManager = new LevelsManager();
        levelsManager.scanForLevels();
        return levelsManager;
    }

    GameLevel initLevelModel(String levelName) throws GeneratingGameException {
        if (!levelInitiator.levelName.equals(levelName)) {
            levelInitiator = new LevelInitiator(getLevelJsonObject(levelName), levelName);
        }
        return levelInitiator.initLevelModel();
    }

    LevelView initLevelView(GameLevel model, int highScore) {
        return levelInitiator.initLevelView(model, highScore);
    }

    void checkGeneratingAllLevels() throws GeneratingGameException {
        for (JsonObject jsonObject : availableLevels.values()) {
            LevelInitiator levelsInitiator = new LevelInitiator(jsonObject, "");
            levelsInitiator.initLevelModel();
        }
    }

    private JsonObject getLevelJsonObject(String levelName) {
        return availableLevels.get(levelName + ".json");
    }

    private void scanForLevels() throws IOException, URISyntaxException {

        availableLevels.clear();


        URI uri = ClassLoader.getSystemResource(PATH_TO_LEVELS_DIR.toString()).toURI();

        Path levelsDir = Paths.get(uri);

        if (!Files.isDirectory(levelsDir)) {
            throw new NotDirectoryException("Expected \"Levels\" directory, but got file");
        }

        Gson gson = new Gson();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(levelsDir)) {
            for (Path f : directoryStream) {
                if (Files.isDirectory(f)) {
                    continue;
                }

                try (JsonReader reader = new JsonReader(new BufferedReader(new FileReader(f.toString())))) {
                    availableLevels.put(f.getFileName().toString(), gson.fromJson(reader, JsonObject.class));
                }
            }
        }
    }
}
