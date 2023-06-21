package oop.arkanoid.model.barrier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthTest {

    @Test
    void isNotDeadTest() {
        Health health = new Health(1);
        assertFalse(health.isDead());
    }

    @Test
    void isDeadTest() {
        Health health = new Health(0);
        assertTrue(health.isDead());
    }

    @Test
    void decreaseHealthTest() {
        Health health = new Health(2);
        health.decrease();
        assertEquals(1, health.value());
    }

    @Test
    void decreaseImmortalTest() {
        Health health = Health.createImmortal();
        assertEquals(1, health.value());
        health.decrease();
        assertEquals(1, health.value());
    }

}