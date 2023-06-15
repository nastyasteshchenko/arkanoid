package oop.arkanoid.model.barrier;

public class Health {
    private int value;

    public Health(int value) {
        this.value = value;
    }

    public static Health createImmortal() {
        return new Immortal();
    }

    boolean isDead() {
        return value <= 0;
    }

    void decrease() {
        value--;
    }

    public static class Immortal extends Health {
        Immortal() {
            super(1);
        }

        @Override
        void decrease() {
        }
    }

    public int getValue() {
        return value;
    }
}