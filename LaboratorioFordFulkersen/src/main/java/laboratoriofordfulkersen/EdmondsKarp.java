package laboratoriofordfulkersen;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Implementación del algoritmo de Edmonds-Karp.
 *
 * Es una variante de Ford-Fulkerson que utiliza
 * BFS (Breadth First Search) para encontrar
 * el camino aumentante más corto.
 */
public class EdmondsKarp {

    // Vector para reconstruir el camino encontrado
    private int[] padre;

    /**
     * Busca un camino aumentante utilizando BFS.
     **/
    private boolean bfs(int[][] residual, int origen, int destino) {

        boolean[] visitado = new boolean[residual.length];

        Queue<Integer> cola = new LinkedList<>();

        cola.offer(origen);

        visitado[origen] = true;

        padre[origen] = -1;

        while (!cola.isEmpty()) {

            int actual = cola.poll();

            // Se recorren todos los vecinos
            for (int vecino = 0; vecino < residual.length; vecino++) {

                // Si existe capacidad y aún no ha sido visitado
                if (!visitado[vecino] && residual[actual][vecino] > 0) {

                    padre[vecino] = actual;

                    visitado[vecino] = true;

                    cola.offer(vecino);

                }

            }

        }

        return visitado[destino];

    }

    /**
     * Calcula el flujo máximo utilizando Edmonds-Karp.
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
        while (bfs(residual, origen, destino)) {

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
