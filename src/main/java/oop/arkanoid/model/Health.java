package oop.arkanoid.model;

class Health {
    private int value;

    Health(int value) {
        this.value = value;
    }

    static Health createImmortal() {
        return new Immortal();
    }

    boolean isAlive() {
        return value > 0;
    }

    void decrease() {
        value--;
    }

    static class Immortal extends Health {
        Immortal() {
            super(1);
        }

        @Override
        void decrease() {
        }
    }

    int getValue() {
        return value;
    }
}