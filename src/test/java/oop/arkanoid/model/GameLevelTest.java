package oop.arkanoid.model;

import oop.arkanoid.model.barrier.Barrier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameLevelTest {

    private static GameLevel gameLevel;

    @BeforeAll
    static void generateLevel() throws GeneratingGameException {
        gameLevel = GameLevelCreator.createLevel();
    }

    @Test
    void updatePlatformPositionTest1() {
        gameLevel.updatePlatformPosition(10);
        assertEquals(0, gameLevel.getPlatform().position().x());
    }

    @Test
    void updatePlatformPositionTest2() {
        gameLevel.updatePlatformPosition(200);
        assertEquals(138, gameLevel.getPlatform().position().x());
    }

    @Test
    void updatePlatformPositionTest3() {
        gameLevel.updatePlatformPosition(588);
        assertEquals(476, gameLevel.getPlatform().position().x());
    }

    @Test
    void gameStateTest() {
        assertEquals(GameState.PROCESS, gameLevel.gameState());
        List<Barrier> barriers = gameLevel.getBarriers();
        barriers.clear();
        assertEquals(GameState.WIN, gameLevel.gameState());
    }

}