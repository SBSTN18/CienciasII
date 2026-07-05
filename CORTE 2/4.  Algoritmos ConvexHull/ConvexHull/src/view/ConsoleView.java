package view;

import model.Point2D;
import java.util.List;

public class ConsoleView implements ConvexHullView {

    @Override
    public void showResults(String algorithmName, int totalPoints, int hullPoints, long timeMs) {
        System.out.println("\n=============================================");
        System.out.println("        RESULTADOS DEL LABORATORIO           ");
        System.out.println("=============================================");
        System.out.printf(" Algoritmo usado : %s\n", algorithmName);
        System.out.printf(" Puntos Totales  : %,d\n", totalPoints);
        System.out.printf(" Puntos en Hull  : %,d\n", hullPoints); // Aquí verás que son poquitos
        System.out.printf(" Tiempo de Ejec. : %d ms\n", timeMs);
        System.out.println("=============================================");
    }

    @Override
    public void renderGraph(List<Point2D> backgroundSample, List<Point2D> hullPoints) {
        System.out.println("[Vista] -> Vértices del Convex Hull encontrados (El Borde Exterior):");
        
        // IMPRESIÓN CONTROLADA: Solo recorremos los puntos que componen el polígono final
        for (int i = 0; i < hullPoints.size(); i++) {
            Point2D p = hullPoints.get(i);
            System.out.printf("   Vértice %d: (%.2f, %.2f)\n", i + 1, p.x(), p.y());
        }
        System.out.println("=============================================\n");
    }

    @Override
    public void showErrorMessage(String message) {
        System.err.println("[ERROR] " + message);
    }

    @Override
    public void showStatus(String status) {
        System.out.println("[PROCESO] " + status);
    }
}