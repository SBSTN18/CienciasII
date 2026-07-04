package controlador;

import modelo.AlgoritmoFloydWarshall;
import modelo.Grafo;
import modelo.IAlgoritmoCaminoMinimo;
import modelo.ResultadoFloyd;
import vista.VistaConsola;

/**
 * Orquesta el flujo del menu, valida reglas de negocio y coordina
 * vista y modelo. No imprime ni lee directamente: delega a la vista.
 *
 * Depende de IAlgoritmoCaminoMinimo (no de la clase concreta), cumpliendo DIP.
 */
public class FloydControlador {
    private final VistaConsola vista;
    private final IAlgoritmoCaminoMinimo algoritmo;
    private Grafo grafo;
    private ResultadoFloyd resultado;

    public FloydControlador(VistaConsola vista) {
        this.vista = vista;
        this.algoritmo = new AlgoritmoFloydWarshall();
    }

    /**
     * Punto de entrada: crea el grafo y ejecuta el ciclo del menu.
     */
    public void iniciar() {
        int numVertices = leerNumVerticesValido();
        this.grafo = new Grafo(numVertices);
        this.resultado = null;
        vista.mostrarMensaje("Grafo dirigido creado con " + numVertices + " vertices.");

        boolean continuar = true;
        while (continuar) {
            vista.mostrarMenu();
            int opcion = vista.pedirEntero("");
            continuar = procesarOpcion(opcion);
        }

        vista.mostrarMensaje("Programa finalizado.");
    }

    private int leerNumVerticesValido() {
        int n = vista.pedirEntero("Ingrese el numero de vertices (minimo 2): ");
        while (n < 2) {
            vista.mostrarError("El grafo debe tener al menos 2 vertices.");
            n = vista.pedirEntero("Ingrese el numero de vertices (minimo 2): ");
        }
        return n;
    }

    private boolean procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                procesarAgregarArista();
                break;
            case 2:
                vista.mostrarMatrizAdyacencia(grafo);
                break;
            case 3:
                procesarEjecutarAlgoritmo();
                break;
            case 4:
                procesarMostrarDistancias();
                break;
            case 5:
                procesarMostrarCamino();
                break;
            case 0:
                return false;
            default:
                vista.mostrarError("Opcion invalida.");
        }
        return true;
    }

    private void procesarAgregarArista() {
        int n = grafo.getNumVertices();

        int origen  = leerVerticeValido("Vertice origen  (1-" + n + "): ") - 1;
        int destino = leerVerticeValido("Vertice destino (1-" + n + "): ") - 1;

        if (origen == destino) {
            vista.mostrarError("El origen y el destino no pueden ser el mismo vertice.");
            return;
        }

        int peso = leerPesoValido();
        grafo.agregarArista(origen, destino, peso);
        resultado = null; // el resultado anterior ya no es valido
        vista.mostrarMensaje("Arista " + (origen + 1) + " -> " + (destino + 1)
                             + " con peso " + peso + " agregada.");
    }

    private void procesarEjecutarAlgoritmo() {
        resultado = algoritmo.ejecutar(grafo);
        vista.mostrarMensaje("Floyd-Warshall ejecutado correctamente.");
    }

    private void procesarMostrarDistancias() {
        if (resultado == null) {
            vista.mostrarError("Primero debe ejecutar Floyd-Warshall (opcion 3).");
            return;
        }
        vista.mostrarMatrizDistancias(resultado);
    }

    private void procesarMostrarCamino() {
        if (resultado == null) {
            vista.mostrarError("Primero debe ejecutar Floyd-Warshall (opcion 3).");
            return;
        }
        int n = grafo.getNumVertices();
        int origen  = leerVerticeValido("Vertice origen  (1-" + n + "): ") - 1;
        int destino = leerVerticeValido("Vertice destino (1-" + n + "): ") - 1;
        vista.mostrarCamino(origen, destino, resultado);
    }

    /**
     * Pide un numero de vertice valido en base 1 y lo retorna en base 1.
     * La conversion a base 0 la hace quien llama.
     */
    private int leerVerticeValido(String prompt) {
        int n = grafo.getNumVertices();
        int v = vista.pedirEntero(prompt);
        while (!grafo.verticeValido(v - 1)) {
            vista.mostrarError("El vertice debe estar entre 1 y " + n + ".");
            v = vista.pedirEntero(prompt);
        }
        return v;
    }

    private int leerPesoValido() {
        int peso = vista.pedirEntero("Peso de la arista (positivo): ");
        while (peso <= 0) {
            vista.mostrarError("El peso debe ser un numero positivo.");
            peso = vista.pedirEntero("Peso de la arista (positivo): ");
        }
        return peso;
    }
}
