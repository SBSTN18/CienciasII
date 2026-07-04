package com.tienda.laboratoriodijkstra;

public class Arista {

    private int destino;
    private int peso;

    // Constructor de la arista
    public Arista(int destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    // Retorna el vértice destino
    public int getDestino() {
        return destino;
    }

    // Retorna el peso de la arista
    public int getPeso() {
        return peso;
    }

}
