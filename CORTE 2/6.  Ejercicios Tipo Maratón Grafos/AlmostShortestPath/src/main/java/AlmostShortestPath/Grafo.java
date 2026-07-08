package almostshortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Queue;

public class Grafo {

    private ArrayList<Arista>[] grafo;
    private ArrayList<Integer>[] padres;

    private int[] distancia;

    private int numeroNodos;

    public Grafo(int numeroNodos) {

        this.numeroNodos = numeroNodos;

        grafo = new ArrayList[numeroNodos];
        padres = new ArrayList[numeroNodos];
        distancia = new int[numeroNodos];

        for (int i = 0; i < numeroNodos; i++) {
            grafo[i] = new ArrayList<>();
            padres[i] = new ArrayList<>();
        }

    }

    public void agregarArista(int origen, int destino, int peso) {

        grafo[origen].add(new Arista(destino, peso));

    }

    public int[] getDistancia() {
        return distancia;
    }

    public ArrayList<Integer>[] getPadres() {
        return padres;
    }

    public ArrayList<Arista>[] getGrafo() {
        return grafo;
    }

    /**
     * Primer Dijkstra. Guarda TODOS los padres que pertenecen a un camino
     * mínimo.
     */
    public void dijkstra(int origen) {

        Arrays.fill(distancia, Integer.MAX_VALUE);

        for (int i = 0; i < numeroNodos; i++) {
            padres[i].clear();
        }

        PriorityQueue<NodoDistancia> cola = new PriorityQueue<>();

        distancia[origen] = 0;

        cola.offer(new NodoDistancia(origen, 0));

        while (!cola.isEmpty()) {

            NodoDistancia actual = cola.poll();

            int nodoActual = actual.nodo;

            if (actual.distancia > distancia[nodoActual]) {
                continue;
            }

            for (Arista arista : grafo[nodoActual]) {

                if (arista.isEliminada()) {
                    continue;
                }

                int vecino = arista.getDestino();

                int nuevaDistancia = distancia[nodoActual] + arista.getPeso();

                if (nuevaDistancia < distancia[vecino]) {

                    distancia[vecino] = nuevaDistancia;

                    padres[vecino].clear();
                    padres[vecino].add(nodoActual);

                    cola.offer(new NodoDistancia(vecino, nuevaDistancia));

                } else if (nuevaDistancia == distancia[vecino]) {

                    padres[vecino].add(nodoActual);

                }

            }

        }

    }

    /**
     * Clase auxiliar para la cola de prioridad.
     */
    private class NodoDistancia implements Comparable<NodoDistancia> {

        private int nodo;
        private int distancia;

        public NodoDistancia(int nodo, int distancia) {
            this.nodo = nodo;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(NodoDistancia otro) {
            return Integer.compare(this.distancia, otro.distancia);
        }

    }

    /**
     * Elimina todas las aristas que pertenecen a cualquier camino mínimo.
     */
    public void eliminarCaminosMinimos(int origen, int destino) {

        Queue<Integer> cola = new LinkedList<>();

        boolean[] visitado = new boolean[numeroNodos];

        cola.offer(destino);

        while (!cola.isEmpty()) {

            int actual = cola.poll();

            for (Integer padre : padres[actual]) {

                for (Arista arista : grafo[padre]) {

                    if (arista.getDestino() == actual) {

                        arista.setEliminada(true);

                    }

                }

                if (!visitado[padre]) {

                    visitado[padre] = true;

                    cola.offer(padre);

                }

            }

        }

    }

    /**
     * Ejecuta nuevamente Dijkstra después de eliminar las aristas.
     */
    public int obtenerAlmostShortestPath(int origen, int destino) {

        Arrays.fill(distancia, Integer.MAX_VALUE);

        PriorityQueue<NodoDistancia> cola = new PriorityQueue<>();

        distancia[origen] = 0;

        cola.offer(new NodoDistancia(origen, 0));

        while (!cola.isEmpty()) {

            NodoDistancia actual = cola.poll();

            int nodoActual = actual.nodo;

            if (actual.distancia > distancia[nodoActual]) {
                continue;
            }

            for (Arista arista : grafo[nodoActual]) {

                if (arista.isEliminada()) {
                    continue;
                }

                int vecino = arista.getDestino();

                int nuevaDistancia = distancia[nodoActual] + arista.getPeso();

                if (nuevaDistancia < distancia[vecino]) {

                    distancia[vecino] = nuevaDistancia;

                    cola.offer(new NodoDistancia(vecino, nuevaDistancia));

                }

            }

        }

        if (distancia[destino] == Integer.MAX_VALUE) {
            return -1;
        }

        return distancia[destino];

    }
}
