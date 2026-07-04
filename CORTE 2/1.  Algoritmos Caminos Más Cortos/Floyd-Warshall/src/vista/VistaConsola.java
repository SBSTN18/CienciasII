package vista;

import java.util.Scanner;
import modelo.Grafo;
import modelo.ResultadoFloyd;

/**
 * Responsabilidad unica: toda la interaccion con el usuario.
 * No conoce reglas de negocio ni del algoritmo; solo lee y muestra.
 * Los vertices se muestran en base 1 al usuario (internamente son base 0).
 */
public class VistaConsola {
    private final Scanner entrada;

    public VistaConsola(Scanner entrada) {
        this.entrada = entrada;
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }

    public void mostrarMenu() {
        System.out.println("\n===== FLOYD-WARSHALL =====");
        System.out.println("1. Agregar arista");
        System.out.println("2. Mostrar matriz de adyacencia");
        System.out.println("3. Ejecutar Floyd-Warshall");
        System.out.println("4. Mostrar matriz de distancias minimas");
        System.out.println("5. Mostrar camino minimo entre dos vertices");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    public int pedirEntero(String prompt) {
        Integer valor = null;
        while (valor == null) {
            System.out.print(prompt);
            if (entrada.hasNextInt()) {
                valor = entrada.nextInt();
            } else {
                System.out.println("Debe ingresar un numero entero.");
                entrada.next();
            }
        }
        return valor;
    }

    /**
     * Muestra la matriz de adyacencia del grafo.
     * INF se muestra como " INF" para legibilidad.
     */
    public void mostrarMatrizAdyacencia(Grafo grafo) {
        int n = grafo.getNumVertices();
        System.out.println("\n--- Matriz de adyacencia ---");
        System.out.print("     ");
        for (int j = 0; j < n; j++) {
            System.out.printf("%5d", j + 1);
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.printf("%4d ", i + 1);
            for (int j = 0; j < n; j++) {
                int peso = grafo.getPeso(i, j);
                if (peso == Grafo.INFINITO) {
                    System.out.print("  INF");
                } else {
                    System.out.printf("%5d", peso);
                }
            }
            System.out.println();
        }
    }

    /**
     * Muestra la matriz de distancias minimas resultante de Floyd-Warshall.
     */
    public void mostrarMatrizDistancias(ResultadoFloyd resultado) {
        int n = resultado.getNumVertices();
        int[][] dist = resultado.getDistancias();
        System.out.println("\n--- Matriz de distancias minimas ---");
        System.out.print("     ");
        for (int j = 0; j < n; j++) {
            System.out.printf("%5d", j + 1);
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.printf("%4d ", i + 1);
            for (int j = 0; j < n; j++) {
                if (dist[i][j] == Grafo.INFINITO) {
                    System.out.print("  INF");
                } else {
                    System.out.printf("%5d", dist[i][j]);
                }
            }
            System.out.println();
        }
    }

    /**
     * Muestra el camino minimo reconstruido entre dos vertices.
     */
    public void mostrarCamino(int origen, int destino, ResultadoFloyd resultado) {
        // origen y destino llegan en base 0
        if (!resultado.hayCamino(origen, destino)) {
            System.out.println("No existe camino de " + (origen + 1) + " a " + (destino + 1) + ".");
            return;
        }

        int[] camino = resultado.reconstruirCamino(origen, destino);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camino.length; i++) {
            sb.append(camino[i] + 1); // mostrar en base 1
            if (i < camino.length - 1) {
                sb.append(" -> ");
            }
        }
        System.out.println("Camino: " + sb);
        System.out.println("Distancia total: " + resultado.getDistancia(origen, destino));
    }
}
