package model.strategies;

import model.Point2D;
import java.util.*;

public class MonotoneChainStrategy implements ConvexHullStrategy {
    @Override
    public String getAlgorithmName() { return "Monotone Chain"; }

    @Override
    public List<Point2D> computeHull(List<Point2D> points) {
        int n = points.size();
        if (n < 3) return new ArrayList<>(points);

        // 1. Clonar y ordenar puntos lexicográficamente
        Point2D[] sorted = points.toArray(new Point2D[0]);
        Arrays.sort(sorted);

        Point2D[] hull = new Point2D[2 * n];
        int k = 0;

        // 2. Construir la cadena inferior (Lower Hull)
        for (int i = 0; i < n; i++) {
            while (k >= 2 && Point2D.orientation(hull[k - 2], hull[k - 1], sorted[i]) <= 0) {
                k--;
            }
            hull[k++] = sorted[i];
        }

        // 3. Construir la cadena superior (Upper Hull)
        for (int i = n - 2, t = k + 1; i >= 0; i--) {
            while (k >= t && Point2D.orientation(hull[k - 2], hull[k - 1], sorted[i]) <= 0) {
                k--;
            }
            hull[k++] = sorted[i];
        }

        // Redimensionar el arreglo eliminando duplicados extremos
        List<Point2D> result = new ArrayList<>();
        for (int i = 0; i < k - 1; i++) {
            result.add(hull[i]);
        }
        return result;
    }
}