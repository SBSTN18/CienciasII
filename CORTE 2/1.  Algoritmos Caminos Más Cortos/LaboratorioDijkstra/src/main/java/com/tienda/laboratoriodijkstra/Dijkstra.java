
package com.tienda.laboratoriodijkstra;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Dijkstra {

    private class Nodo implements Comparable<Nodo> {

        private int vertice;
        private int distancia;

        public Nodo(int vertice, int distancia) {
            this.vertice = vertice;
            this.distancia = distancia;
        }

        // Permite ordenar la cola por la menor distancia
        @Override
        public int compareTo(Nodo otro) {
            return Integer.compare(this.distancia, otro.distancia);
        }

    }

    /**
     * Ejecuta el algoritmo de Dijkstra.
     **/
    public int[] calcular(Grafo grafo, int origen) {

        // Vector donde se almacenan las distancias mínimas
        int[] distancias = new int[grafo.getVertices()];

        // Indica si un vértice ya fue procesado
        boolean[] visitado = new boolean[grafo.getVertices()];

        // Inicializa todas las distancias como infinito
        Arrays.fill(distancias, Integer.MAX_VALUE);

        // La distancia al origen siempre será 0
        distancias[origen] = 0;

        // Cola de prioridad
        PriorityQueue<Nodo> cola = new PriorityQueue<>();

        // Se agrega el vértice inicial
        cola.add(new Nodo(origen, 0));

        // Mientras existan vértices por visitar
        while (!cola.isEmpty()) {

            Nodo actual = cola.poll();

            int verticeActual = actual.vertice;

            // Si ya fue visitado se ignora
            if (visitado[verticeActual]) {
                continue;
            }

            visitado[verticeActual] = true;

            // Se recorren todos los vecinos
            for (Arista arista : grafo.getListaAdyacencia().get(verticeActual)) {

                int vecino = arista.getDestino();
                int peso = arista.getPeso();

                // Relajación de la arista
                if (!visitado[vecino]
                        && distancias[verticeActual] + peso < distancias[vecino]) {

                    // Se actualiza la distancia mínima
                    distancias[vecino] = distancias[verticeActual] + peso;

                    // Se agrega nuevamente a la cola
                    cola.add(new Nodo(vecino, distancias[vecino]));

                }

            }

        }

        return distancias;

    }

}
