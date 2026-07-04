import java.util.Scanner;

import controlador.*;
import vista.VistaConsola;


/**
 * Punto de entrada. Solo ensambla las piezas y delega el control al controlador.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VistaConsola vista = new VistaConsola(scanner);
        FloydControlador controlador = new FloydControlador(vista);

        controlador.iniciar();

        scanner.close();
    }
}
