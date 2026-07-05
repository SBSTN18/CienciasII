package model.strategies;

import model.Point2D;
import java.util.*;

public class GrahamScanStrategy implements ConvexHullStrategy {
    @Override
    public String getAlgorithmName() { return "Graham Scan"; }

    @Override
    public List<Point2D> computeHull(List<Point2D> points) {
        if (points.size() < 3) return new ArrayList<>(points);

        // 1. Encontrar el punto con menor 'y' (y menor 'x' en caso de empate)
        Point2D p0 = points.get(0);
        for (Point2D p : points) {
            if (p.y() < p0.y() || (p.y() == p0.y() && p.x() < p0.x())) {
                p0 = p;
            }
        }

        // 2. Ordenar por ángulo polar respecto a p0
        final Point2D pivot = p0;
        List<Point2D> sortedPoints = new ArrayList<>(points);
        sortedPoints.sort((p1, p2) -> {
            if (p1 == pivot) return -1;
            if (p2 == pivot) return 1;
            double orient = Point2D.orientation(pivot, p1, p2);
            if (orient == 0) {
                return Double.compare(pivot.distanceSq(p1), pivot.distanceSq(p2));
            }
            return (orient > 0) ? -1 : 1;
        });

        // 3. Procesar con la pila
        Stack<Point2D> stack = new Stack<>();
        stack.push(sortedPoints.get(0));
        stack.push(sortedPoints.get(1));

        for (int i = 2; i < sortedPoints.size(); i++) {
            Point2D next = sortedPoints.get(i);
            while (stack.size() > 1) {
                Point2D top = stack.pop();
                Point2D prev = stack.peek();
                if (Point2D.orientation(prev, top, next) > 0) {
                    stack.push(top); // Se mantiene el giro a la izquierda
                    break;
                }
            }
            stack.push(next);
        }
        return new ArrayList<>(stack);
    }
}