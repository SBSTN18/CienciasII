package modelo;

/**
 * Algoritmo Voraz (Greedy) de coloreado.
 * Responsabilidad unica: colorear el grafo en orden natural de vertices (A, B, C...).
 *
 * Para cada vertice asigna el menor color positivo que no use ninguno de sus vecinos.
 * No garantiza el numero cromatico optimo, pero es simple y rapido O(V + E).
 */
public class AlgoritmoVoraz implements IAlgoritmoColoreado {

    @Override
    public ResultadoColoreado ejecutar(Grafo grafo) {
        long inicio = System.nanoTime();
        int n = grafo.getNumVertices();
        int[] colores = colorear(grafo, n);
        long tiempo = System.nanoTime() - inicio;
        return new ResultadoColoreado(colores, tiempo, "Voraz");
    }

    protected int[] colorear(Grafo grafo, int n) {
        int[] colores = new int[n]; // 0 = sin color

        for (int v = 0; v < n; v++) {
            boolean[] colorUsado = coloresUsadosPorVecinos(grafo, v, colores, n);
            colores[v] = menorColorDisponible(colorUsado);
        }
        return colores;
    }

    /**
     * Construye un arreglo booleano donde colorUsado[c] = true
     * si algun vecino de v ya tiene el color c.
     */
    protected boolean[] coloresUsadosPorVecinos(Grafo grafo, int v, int[] colores, int n) {
        boolean[] colorUsado = new boolean[n + 1]; // color 1..n
        int[] vecinos = grafo.getVecinos(v);
        for (int vecino : vecinos) {
            if (colores[vecino] != 0) {
                colorUsado[colores[vecino]] = true;
            }
        }
        return colorUsado;
    }

    /**
     * Retorna el menor color positivo que no este marcado como usado.
     */
    protected int menorColorDisponible(boolean[] colorUsado) {
        for (int c = 1; c < colorUsado.length; c++) {
            if (!colorUsado[c]) {
                return c;
            }
        }
        return colorUsado.length;
    }
}
