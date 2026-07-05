package view;

import model.Point2D;
import java.util.List;

public interface ConvexHullView {
    /**
     * Muestra los resultados cuantitativos del laboratorio.
     */
    void showResults(String algorithmName, int totalPoints, int hullPoints, long timeMs);

    /**
     * Renderiza o dibuja los puntos. Para evitar colgar la UI con 1M de puntos,
     * se pasa una muestra para el fondo y el 100% de los puntos del Hull.
     */
    void renderGraph(List<Point2D> backgroundSample, List<Point2D> hullPoints);

    /**
     * Muestra un mensaje de error o alerta al usuario.
     */
    void showErrorMessage(String message);

    /**
     * Notifica un cambio de estado o progreso en la ejecución.
     */
    void showStatus(String status);
}