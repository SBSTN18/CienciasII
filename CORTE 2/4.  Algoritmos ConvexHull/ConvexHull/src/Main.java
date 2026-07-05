import controller.ConvexHullController;
import model.Point2D;
import model.strategies.DivideAndConquerStrategy;
import model.strategies.GrahamScanStrategy;
import model.strategies.MonotoneChainStrategy;
import view.ConsoleView;
import view.ConvexHullView;
import util.PointGenerator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Crear la vista y el controlador
        ConvexHullView view = new ConsoleView();
        ConvexHullController controller = new ConvexHullController(view);
        
        int UN_MILLON = 1_000_000;

        // 2. GENERAR LA NUBE UNA SOLA VEZ (Garantiza el rigor del laboratorio)
        System.out.println("[PROCESO] Generando la nube única de 1,000,000 puntos para el experimento...");
        List<Point2D> nubeUnica = PointGenerator.generateUniform(UN_MILLON, 10000.0);

        // --- Experimento 1: Monotone Chain ---
        controller.setStrategy(new MonotoneChainStrategy());
        // Pasamos la nube única (crea un método en tu controlador o ajusta el que tienes para recibir la lista)
        controller.runWithExistingPoints(nubeUnica);

        // --- Experimento 2: Graham Scan ---
        controller.setStrategy(new GrahamScanStrategy());
        controller.runWithExistingPoints(nubeUnica);

        // --- Experimento 3: Divide and Conquer ---
        controller.setStrategy(new DivideAndConquerStrategy());
        controller.runWithExistingPoints(nubeUnica);
    }
}