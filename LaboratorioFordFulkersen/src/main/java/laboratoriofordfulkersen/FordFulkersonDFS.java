package laboratoriofordfulkersen;
/**
 * Implementación del algoritmo de Ford-Fulkerson
 * utilizando DFS (Depth First Search).
 *
 * Este algoritmo encuentra el flujo máximo entre
 * un nodo origen y un nodo destino.
 */
public class FordFulkersonDFS {

    // Vector para reconstruir el camino encontrado
    private int[] padre;

    /**
     * Método recursivo DFS para buscar un camino aumentante.
     **/
    private boolean dfs(int[][] residual,
                        boolean[] visitado,
                        int actual,
                        int destino) {

        // Si llegamos al destino existe un camino
        if (actual == destino) {
            return true;
        }

        // Se marca como visitado
        visitado[actual] = true;

        // Se recorren todos los vecinos
        for (int vecino = 0; vecino < residual.length; vecino++) {

            // Solo se visita si no ha sido recorrido
            // y existe capacidad disponible
            if (!visitado[vecino] && residual[actual][vecino] > 0) {

                padre[vecino] = actual;

                if (dfs(residual, visitado, vecino, destino)) {
                    return true;
                }

            }

        }

        return false;

    }

    /**
     * Calcula el flujo máximo mediante Ford-Fulkerson.
     **/
    public int flujoMaximo(Grafo grafo, int origen, int destino) {

        int vertices = grafo.getVertices();

        padre = new int[vertices];

        // Grafo residual
        int[][] residual = new int[vertices][vertices];

        // Copiar capacidades originales
        for (int i = 0; i < vertices; i++) {

            for (int j = 0; j < vertices; j++) {

                residual[i][j] = grafo.getCapacidad()[i][j];

            }

        }

        int flujoMaximo = 0;

        // Mientras exista un camino aumentante
        while (true) {

            boolean[] visitado = new boolean[vertices];

            if (!dfs(residual, visitado, origen, destino)) {
                break;
            }

            // Buscar el cuello de botella
            int flujoCamino = Integer.MAX_VALUE;

            for (int v = destino; v != origen; v = padre[v]) {

                int u = padre[v];

                flujoCamino = Math.min(flujoCamino,
                        residual[u][v]);

            }

            // Actualizar el grafo residual
            for (int v = destino; v != origen; v = padre[v]) {

                int u = padre[v];

                residual[u][v] -= flujoCamino;

                residual[v][u] += flujoCamino;

            }

            // Acumular el flujo encontrado
            flujoMaximo += flujoCamino;

        }

        return flujoMaximo;

    }

}
