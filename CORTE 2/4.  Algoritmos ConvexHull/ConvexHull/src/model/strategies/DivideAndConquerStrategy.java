package model.strategies;

import model.Point2D;
import java.util.*;

public class DivideAndConquerStrategy implements ConvexHullStrategy {
    
    @Override
    public String getAlgorithmName() { 
        return "Divide and Conquer"; 
    }

    @Override
    public List<Point2D> computeHull(List<Point2D> points) {
        if (points.size() < 3) return new ArrayList<>(points);
        
        // 1. Clonar y ordenar inicialmente por X e Y de forma estricta
        List<Point2D> sorted = new ArrayList<>(points);
        Collections.sort(sorted);
        
        return divideAndConquer(sorted);
    }

    private List<Point2D> divideAndConquer(List<Point2D> points) {
        // CASO BASE: Si el grupo es pequeño (menor o igual a 32), calculamos su Hull directo
        if (points.size() <= 32) {
            return new MonotoneChainStrategy().computeHull(points);
        }

        // 2. Dividir el set de puntos exactamente por la mitad (Eje X)
        int mid = points.size() / 2;
        
        List<Point2D> leftPart = new ArrayList<>(points.subList(0, mid));
        List<Point2D> rightPart = new ArrayList<>(points.subList(mid, points.size()));

        // 3. Resolver recursivamente las dos mitades
        List<Point2D> leftHull = divideAndConquer(leftPart);
        List<Point2D> rightHull = divideAndConquer(rightPart);

        // 4. Fusionar ambos sub-hulls de forma segura
        return mergeHulls(leftHull, rightHull);
    }

    private List<Point2D> mergeHulls(List<Point2D> left, List<Point2D> right) {
        // Unimos los puntos encontrados en ambas mitades en una sola lista intermedia
        List<Point2D> combined = new ArrayList<>(left.size() + right.size());
        combined.addAll(left);
        combined.addAll(right);
        
        // Ordenamos los puntos combinados lexicográficamente
        Collections.sort(combined);
        
        // Re-aplicamos el barrido de Monotone Chain sobre los puntos candidatos de los dos sub-hulls.
        // Esto encuentra las tangentes exactas superiores e inferiores de forma implícita y lineal O(H),
        // eliminando cualquier error geométrico por decimales flotantes.
        int n = combined.size();
        if (n < 3) return combined;

        Point2D[] hull = new Point2D[2 * n];
        int k = 0;

        // Cadena inferior
        for (int i = 0; i < n; i++) {
            while (k >= 2 && Point2D.orientation(hull[k - 2], hull[k - 1], combined.get(i)) <= 0) {
                k--;
            }
            hull[k++] = combined.get(i);
        }

        // Cadena superior
        for (int i = n - 2, t = k + 1; i >= 0; i--) {
            while (k >= t && Point2D.orientation(hull[k - 2], hull[k - 1], combined.get(i)) <= 0) {
                k--;
            }
            hull[k++] = combined.get(i);
        }

        List<Point2D> result = new ArrayList<>(k - 1);
        for (int i = 0; i < k - 1; i++) {
            result.add(hull[i]);
        }
        
        return result;
    }
}