package oop.arkanoid;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class LevelsManager {
    private final static String pathToLevels = "Levels";
    private final Map<String, File> availableLevels = new HashMap<>();

    public LevelsManager() throws NotDirectoryException {
        File levelsDir = new File(Objects.requireNonNull(this.getClass().getResource(pathToLevels)).getFile());
        if (!levelsDir.isDirectory()) {
            throw new NotDirectoryException("Expected \"Levels\" directory, but got file");
        }
        for (File f : Objects.requireNonNull(levelsDir.listFiles())) {
            if (f.isDirectory()) {
                continue;
            }
            if (f.canRead()) {
                availableLevels.put(f.getName(), f);
            }
        }
    }

    public Set<String> getLevelsNameAsSet() {
        return availableLevels.keySet();
    }

    public File getLevelFileByName(String name) {
        return availableLevels.get(name);
    }
}
