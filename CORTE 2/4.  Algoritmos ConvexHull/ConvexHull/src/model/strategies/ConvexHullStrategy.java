package model.strategies;

import model.Point2D;
import java.util.List;

public interface ConvexHullStrategy {
    List<Point2D> computeHull(List<Point2D> points);
    String getAlgorithmName();
}