package modelo;

/**
 * Implementacion de Floyd-Warshall para grafos dirigidos con pesos positivos.
 * Responsabilidad unica: ejecutar el algoritmo y retornar el resultado.
 *
 * El algoritmo considera cada vertice 'k' como posible intermediario entre
 * cada par (i, j): si ir por k es mas corto que el camino directo, lo actualiza.
 *
 * Complejidad: O(V^3) en tiempo, O(V^2) en espacio.
 */
public class AlgoritmoFloydWarshall implements IAlgoritmoCaminoMinimo {

    @Override
    public ResultadoFloyd ejecutar(Grafo grafo) {
        int n = grafo.getNumVertices();
        int[][] dist = grafo.copiarMatriz();
        int[][] pred = inicializarPredecesores(grafo, n);

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Grafo.INFINITO && dist[k][j] != Grafo.INFINITO) {
                        int nuevaDistancia = dist[i][k] + dist[k][j];
                        if (nuevaDistancia < dist[i][j]) {
                            dist[i][j] = nuevaDistancia;
                            pred[i][j] = pred[k][j];
                        }
                    }
                }
            }
        }

        return new ResultadoFloyd(dist, pred, n);
    }

    /**
     * Inicializa la matriz de predecesores:
     *   - pred[i][j] = i  si existe arista directa de i a j
     *   - pred[i][j] = -1 si no hay arista (o i == j)
     */
    private int[][] inicializarPredecesores(Grafo grafo, int n) {
        int[][] pred = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && grafo.aristaExiste(i, j)) {
                    pred[i][j] = i;
                } else {
                    pred[i][j] = -1;
                }
            }
        }
        return pred;
    }
}
