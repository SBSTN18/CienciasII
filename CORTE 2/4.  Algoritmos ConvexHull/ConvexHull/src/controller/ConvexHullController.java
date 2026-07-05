package controller;

import model.Point2D;
import model.strategies.ConvexHullStrategy;
import view.ConvexHullView;
import util.PointGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConvexHullController {
    private final ConvexHullView view;
    private ConvexHullStrategy strategy;

    // Inyección de dependencias por constructor (DIP)
    public ConvexHullController(ConvexHullView view) {
        this.view = view;
    }

    public void setStrategy(ConvexHullStrategy strategy) {
        this.strategy = strategy;
    }

    public void runLaboratoryExperiment(int numPoints) {
        if (strategy == null) {
            view.showErrorMessage("No se ha seleccionado ninguna estrategia de Convex Hull.");
            return;
        }

        view.showStatus("Generando la nube de " + numPoints + " puntos...");
        List<Point2D> points = PointGenerator.generateUniform(numPoints, 10000.0);

        view.showStatus("Ejecutando " + strategy.getAlgorithmName() + " sobre 1 millón de puntos...");
        
        // Limpieza preventiva de memoria antes de la métrica
        System.gc();

        long startTime = System.nanoTime();
        List<Point2D> hull = strategy.computeHull(points);
        long endTime = System.nanoTime();

        long totalTimeMs = (endTime - startTime) / 1_000_000;

        // 1. Enviar resultados cuantitativos a la vista
        view.showResults(strategy.getAlgorithmName(), numPoints, hull.size(), totalTimeMs);

        // 2. Aplicar patrón Proxy/Muestreo para la representación visual masiva
        view.showStatus("Preparando datos de renderizado óptimo...");
        List<Point2D> samplePoints = getSamplePoints(points, 20); // Muestra del 20 de puntos aleatorios para el fondo
        
        // 3. Enviar representación gráfica a la vista
        view.renderGraph(samplePoints, hull);
    }

    /**
     * Toma una muestra representativa de la nube para no saturar la vista.
     */
    private List<Point2D> getSamplePoints(List<Point2D> points, int sampleSize) {
        if (points.size() <= sampleSize) return points;
        List<Point2D> copy = new ArrayList<>(points);
        Collections.shuffle(copy);
        return copy.subList(0, sampleSize);
    }

    public void runWithExistingPoints(List<Point2D> points) {
        if (strategy == null) {
            view.showErrorMessage("No se ha seleccionado ninguna estrategia.");
            return;
        }

        view.showStatus("Ejecutando " + strategy.getAlgorithmName() + " sobre la nube compartida...");
        
        System.gc(); // Limpieza de memoria para medir limpio

        long startTime = System.nanoTime();
        List<Point2D> hull = strategy.computeHull(points);
        long endTime = System.nanoTime();

        long totalTimeMs = (endTime - startTime) / 1_000_000;

        // Mostrar los resultados
        view.showResults(strategy.getAlgorithmName(), points.size(), hull.size(), totalTimeMs);
        view.renderGraph(null, hull); // Solo imprime el Hull
    }
}