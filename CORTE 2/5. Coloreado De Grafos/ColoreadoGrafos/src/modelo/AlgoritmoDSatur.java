package modelo;

/**
 * Algoritmo D-Satur (Degree of Saturation) de coloreado.
 * Responsabilidad unica: en cada paso elegir el vertice sin colorear con mayor
 * saturacion (cantidad de colores distintos entre sus vecinos ya coloreados).
 * Empate en saturacion → mayor grado. Empate en grado → menor indice.
 *
 * Es el mas preciso de los tres: suele encontrar el numero cromatico optimo
 * en grafos regulares. Complejidad: O(V² + E).
 */
public class AlgoritmoDSatur extends AlgoritmoVoraz {

    @Override
    public ResultadoColoreado ejecutar(Grafo grafo) {
        long inicio = System.nanoTime();
        int n = grafo.getNumVertices();
        int[] colores = colorearDSatur(grafo, n);
        long tiempo = System.nanoTime() - inicio;
        return new ResultadoColoreado(colores, tiempo, "D-Satur");
    }

    private int[] colorearDSatur(Grafo grafo, int n) {
        int[] colores = new int[n];       // 0 = sin color
        int[] saturacion = new int[n];    // cuantos colores distintos tienen sus vecinos

        for (int paso = 0; paso < n; paso++) {
            int v = elegirVertice(grafo, colores, saturacion, n);
            boolean[] colorUsado = coloresUsadosPorVecinos(grafo, v, colores, n);
            colores[v] = menorColorDisponible(colorUsado);
            actualizarSaturacion(grafo, v, colores, saturacion);
        }
        return colores;
    }

    /**
     * Elige el vertice sin colorear con mayor saturacion.
     * Desempate: mayor grado. Segundo desempate: menor indice.
     */
    private int elegirVertice(Grafo grafo, int[] colores, int[] saturacion, int n) {
        int elegido = -1;
        for (int v = 0; v < n; v++) {
            if (colores[v] != 0) continue; // ya coloreado

            if (elegido == -1) {
                elegido = v;
                continue;
            }

            int satV = saturacion[v];
            int satE = saturacion[elegido];

            if (satV > satE) {
                elegido = v;
            } else if (satV == satE && grafo.getGrado(v) > grafo.getGrado(elegido)) {
                elegido = v;
            }
        }
        return elegido;
    }

    /**
     * Despues de colorear v, recalcula la saturacion de sus vecinos sin colorear.
     * Solo necesitamos actualizar si el color de v es nuevo para cada vecino.
     */
    private void actualizarSaturacion(Grafo grafo, int v, int[] colores, int[] saturacion) {
        int colorDeV = colores[v];
        int[] vecinos = grafo.getVecinos(v);

        for (int vecino : vecinos) {
            if (colores[vecino] != 0) continue; // ya coloreado, no importa

            // Verificar si colorDeV ya estaba en la saturacion del vecino
            if (!colorYaContado(grafo, vecino, colores, colorDeV)) {
                saturacion[vecino]++;
            }
        }
    }

    /**
     * Verifica si el color dado ya fue contado en la saturacion del vertice.
     * Recorre los vecinos del vertice buscando si otro ya tiene ese color.
     */
    private boolean colorYaContado(Grafo grafo, int vertice, int[] colores, int color) {
        int[] vecinos = grafo.getVecinos(vertice);
        for (int vecino : vecinos) {
            // El vecino que acabamos de colorear aun no cuenta (lo contamos a el mismo)
            if (colores[vecino] == color && vertice != vecino) {
                return true;
            }
        }
        return false;
    }
}
