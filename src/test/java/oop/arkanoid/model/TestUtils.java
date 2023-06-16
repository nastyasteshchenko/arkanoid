package oop.arkanoid.model;

import oop.arkanoid.model.barrier.Barrier;
import oop.arkanoid.model.barrier.Brick;
import oop.arkanoid.model.barrier.Health;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    static double roundToThousandths(double value){
        return (double) Math.round(value * 1000) / 1000;
    }

    public static List<Barrier> createBarriers() {
        List<Barrier> barriers = new ArrayList<>();
        barriers.add(new Brick(new Point(477, 81), new Point(114, 30), new Health(1)));
        barriers.add(new Brick(new Point(126, 81), new Point(114, 30), new Health(1)));
        barriers.add(new Brick(new Point(243, 81), new Point(114, 30), new Health(1)));
        barriers.add(new Brick(new Point(360, 81), new Point(114, 30), new Health(1)));
        return barriers;
    }
}
