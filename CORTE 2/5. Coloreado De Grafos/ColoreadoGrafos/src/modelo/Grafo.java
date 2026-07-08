package modelo;

/**
 * Grafo no dirigido representado con lista de adyacencia propia.
 * Responsabilidad unica: mantener la estructura del grafo.
 *
 * ady[i]   → arreglo de indices de vecinos del vertice i
 * grado[i] → cuantos vecinos tiene el vertice i
 *
 * Vertices etiquetados como A, B, C... internamente son 0, 1, 2...
 */
public class Grafo {
    private static final String ABECEDARIO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final int numVertices;
    private final int[][] ady;
    private final int[] grado;
    private final boolean dirigido;

    public Grafo(int numVertices, boolean dirigido) {
        this.numVertices = numVertices;
        this.dirigido = dirigido;
        this.grado = new int[numVertices];
        this.ady = new int[numVertices][numVertices - 1];
    }

    /**
     * Agrega una arista entre origen y destino (indices base 0).
     * Si el grafo no es dirigido, agrega en ambas direcciones.
     */
    public void agregarArista(int origen, int destino) {
        if (!existeArista(origen, destino)) {
            ady[origen][grado[origen]] = destino;
            grado[origen]++;
        }
        if (!dirigido && !existeArista(destino, origen)) {
            ady[destino][grado[destino]] = origen;
            grado[destino]++;
        }
    }

    /**
     * Verifica si ya existe una arista entre origen y destino.
     */
    public boolean existeArista(int origen, int destino) {
        for (int i = 0; i < grado[origen]; i++) {
            if (ady[origen][i] == destino) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna los vecinos del vertice v como arreglo de exactamente grado[v] elementos.
     */
    public int[] getVecinos(int v) {
        int[] vecinos = new int[grado[v]];
        for (int i = 0; i < grado[v]; i++) {
            vecinos[i] = ady[v][i];
        }
        return vecinos;
    }

    public int getGrado(int v) {
        return grado[v];
    }

    public int getNumVertices() {
        return numVertices;
    }

    public boolean esDirigido() {
        return dirigido;
    }

    public char getEtiqueta(int v) {
        return ABECEDARIO.charAt(v);
    }

    public boolean verticeValido(int v) {
        return v >= 0 && v < numVertices;
    }
}
