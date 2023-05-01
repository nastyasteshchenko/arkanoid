package oop.arkanoid.model;

class Health {
    private int value;

    Health(int value) {
        this.value = value;
    }

//    static Health create(byte value) {
//        assert value > 0 && value < 5;
//        return new Health(value);
//    }

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
            super((byte) 1);
        }

        @Override
        void decrease() {
        }
    }
}