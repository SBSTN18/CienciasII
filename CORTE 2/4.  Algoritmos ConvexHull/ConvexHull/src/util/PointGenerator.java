package util;

import model.Point2D;
import java.util.*;

public class PointGenerator {
    public static List<Point2D> generateUniform(int size, double maxCoord) {
        List<Point2D> points = new ArrayList<>(size);
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            points.add(new Point2D(rand.nextDouble() * maxCoord, rand.nextDouble() * maxCoord));
        }
        return points;
    }
}