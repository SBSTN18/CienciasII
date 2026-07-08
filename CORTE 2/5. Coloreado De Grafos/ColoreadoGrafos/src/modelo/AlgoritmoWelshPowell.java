package modelo;

/**
 * Algoritmo Welsh-Powell de coloreado.
 * Responsabilidad unica: ordenar vertices por grado descendente y luego aplicar
 * el coloreado voraz en ese orden.
 *
 * Idea clave: colorear primero los vertices mas conectados reduce conflictos
 * y tiende a producir un numero cromatico menor que el voraz simple.
 * Complejidad: O(V log V + V + E).
 */
public class AlgoritmoWelshPowell extends AlgoritmoVoraz {

    @Override
    public ResultadoColoreado ejecutar(Grafo grafo) {
        long inicio = System.nanoTime();
        int n = grafo.getNumVertices();

        // Ordenar vertices por grado descendente (insertion sort propio)
        int[] orden = ordenarPorGradoDescendente(grafo, n);
        int[] colores = colorearEnOrden(grafo, n, orden);

        long tiempo = System.nanoTime() - inicio;
        return new ResultadoColoreado(colores, tiempo, "Welsh-Powell");
    }

    /**
     * Retorna un arreglo de indices de vertices ordenados por grado descendente.
     * Usa insertion sort: eficiente para n pequeño (grafos tipicos de coloreado).
     */
    private int[] ordenarPorGradoDescendente(Grafo grafo, int n) {
        int[] orden = new int[n];
        for (int i = 0; i < n; i++) orden[i] = i;

        for (int i = 1; i < n; i++) {
            int clave = orden[i];
            int j = i - 1;
            while (j >= 0 && grafo.getGrado(orden[j]) < grafo.getGrado(clave)) {
                orden[j + 1] = orden[j];
                j--;
            }
            orden[j + 1] = clave;
        }
        return orden;
    }

    /**
     * Aplica el coloreado voraz siguiendo el orden dado.
     */
    private int[] colorearEnOrden(Grafo grafo, int n, int[] orden) {
        int[] colores = new int[n];

        for (int i = 0; i < n; i++) {
            int v = orden[i];
            boolean[] colorUsado = coloresUsadosPorVecinos(grafo, v, colores, n);
            colores[v] = menorColorDisponible(colorUsado);
        }
        return colores;
    }
}
