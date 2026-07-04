package modelo;

/**
 * Grafo dirigido con pesos positivos representado mediante matriz de adyacencia.
 * Responsabilidad unica: mantener la estructura del grafo (vertices y aristas).
 */
public class Grafo {
    public static final int INFINITO = Integer.MAX_VALUE / 2;

    private final int[][] matriz;
    private final int numVertices;

    public Grafo(int numVertices) {
        this.numVertices = numVertices;
        this.matriz = new int[numVertices][numVertices];
        inicializarMatriz();
    }

    /**
     * Diagonal en 0 (distancia de un vertice a si mismo),
     * resto en INFINITO (sin arista directa).
     */
    private void inicializarMatriz() {
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                matriz[i][j] = (i == j) ? 0 : INFINITO;
            }
        }
    }

    /**
     * Agrega una arista dirigida de 'origen' a 'destino' con el peso dado.
     * Los vertices se reciben en base 0.
     */
    public void agregarArista(int origen, int destino, int peso) {
        matriz[origen][destino] = peso;
    }

    public int getPeso(int origen, int destino) {
        return matriz[origen][destino];
    }

    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Retorna una copia de la matriz para que el algoritmo trabaje
     * sin modificar la estructura original del grafo.
     */
    public int[][] copiarMatriz() {
        int[][] copia = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                copia[i][j] = matriz[i][j];
            }
        }
        return copia;
    }

    public boolean verticeValido(int v) {
        return v >= 0 && v < numVertices;
    }

    public boolean aristaExiste(int origen, int destino) {
        return matriz[origen][destino] != INFINITO && origen != destino;
    }
}
