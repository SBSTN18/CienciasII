
package com.tienda.laboratoriodijkstra;

import java.util.ArrayList;

public class Grafo {

    private int vertices;
    private ArrayList<ArrayList<Arista>> listaAdyacencia;

    // Constructor del grafo
    public Grafo(int vertices) {

        this.vertices = vertices;

        listaAdyacencia = new ArrayList<>();

        // Se crea una lista vacía para cada vértice
        for (int i = 0; i < vertices; i++) {
            listaAdyacencia.add(new ArrayList<>());
        }

    }

    // Agrega una arista al grafo
    public void agregarArista(int origen, int destino, int peso) {

        listaAdyacencia.get(origen).add(new Arista(destino, peso));

    }

    // Devuelve la cantidad de vértices
    public int getVertices() {
        return vertices;
    }

    // Devuelve la lista de adyacencia
    public ArrayList<ArrayList<Arista>> getListaAdyacencia() {
        return listaAdyacencia;
    }

}
