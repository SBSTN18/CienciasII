package modelo;

/**
 * Almacena el resultado de ejecutar Floyd-Warshall:
 *   - distancias[i][j]: peso minimo del camino de i a j
 *   - predecesores[i][j]: vertice previo en el camino minimo de i a j
 *                         (-1 si no hay camino o si i == j)
 *
 * Responsabilidad unica: guardar y exponer el resultado; reconstruir caminos.
 */
public class ResultadoFloyd {
    private final int[][] distancias;
    private final int[][] predecesores;
    private final int numVertices;

    public ResultadoFloyd(int[][] distancias, int[][] predecesores, int numVertices) {
        this.distancias = distancias;
        this.predecesores = predecesores;
        this.numVertices = numVertices;
    }

    public int getDistancia(int origen, int destino) {
        return distancias[origen][destino];
    }

    public int[][] getDistancias() {
        return distancias;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public boolean hayCamino(int origen, int destino) {
        return distancias[origen][destino] != Grafo.INFINITO;
    }

    /**
     * Reconstruye el camino minimo de 'origen' a 'destino' como arreglo de vertices.
     * Retorna un arreglo de longitud 0 si no hay camino.
     */
    public int[] reconstruirCamino(int origen, int destino) {
        if (!hayCamino(origen, destino)) {
            return new int[0];
        }

        // Primera pasada: contar cuantos vertices hay en el camino
        int longitud = contarCamino(origen, destino);
        int[] camino = new int[longitud];

        // Segunda pasada: llenar el arreglo de atras hacia adelante
        int pos = longitud - 1;
        int actual = destino;
        while (actual != origen) {
            camino[pos] = actual;
            actual = predecesores[origen][actual];
            pos--;
        }
        camino[0] = origen;

        return camino;
    }

    private int contarCamino(int origen, int destino) {
        int count = 1; // el origen
        int actual = destino;
        while (actual != origen) {
            count++;
            actual = predecesores[origen][actual];
        }
        return count;
    }
}
