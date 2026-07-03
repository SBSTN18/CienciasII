package laboratoriofordfulkersen;

public class Grafo {

    // Número de vértices del grafo
    private int vertices;

    // Matriz de capacidades
    private int[][] capacidad;

    /**
     * Constructor del grafo.
     **/
    public Grafo(int vertices) {

        this.vertices = vertices;

        // Se crea una matriz cuadrada inicializada en cero
        capacidad = new int[vertices][vertices];

    }

    /**
     * Agrega una arista dirigida con su capacidad.
     **/
    public void agregarArista(int origen, int destino, int capacidad) {

        this.capacidad[origen][destino] = capacidad;

    }

    /**
     * Retorna la cantidad de vértices.
    **/
    public int getVertices() {

        return vertices;

    }

    /**
     * Retorna la matriz de capacidades.
    **/
    public int[][] getCapacidad() {

        return capacidad;

    }

    /**
     * Muestra la matriz de capacidades del grafo.
     * Este método sirve únicamente para verificar
     * que el grafo fue ingresado correctamente.
     */
    public void mostrarMatriz() {

        System.out.println("\n===== MATRIZ DE CAPACIDADES =====");

        for (int i = 0; i < vertices; i++) {

            for (int j = 0; j < vertices; j++) {

                System.out.printf("%4d", capacidad[i][j]);

            }

            System.out.println();

        }

    }

}