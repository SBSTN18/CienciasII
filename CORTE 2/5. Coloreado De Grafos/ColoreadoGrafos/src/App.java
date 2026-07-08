import java.util.Scanner;
import controlador.ColoreadorControlador;
import vista.VistaConsola;

/**
 * Punto de entrada. Solo ensambla las piezas y delega el control al controlador.
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VistaConsola vista = new VistaConsola(scanner);
        ColoreadorControlador controlador = new ColoreadorControlador(vista);

        controlador.iniciar();

        scanner.close();
    }
}
