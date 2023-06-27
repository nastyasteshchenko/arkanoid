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

    int value() {
        return value;
    }

    static class Immortal extends Health {
        Immortal() {
            super(1);
        }

        @Override
        void decrease() {
        }
    }
}