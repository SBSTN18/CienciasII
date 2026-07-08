package listaAdyacencia;

/**
 * Representa una arista del grafo.
 * Almacena el vértice destino y el peso del enlace,
 * lo que permite su uso en algoritmos de camino corto.
 */
public class Arista {

    private String destino;
    private int peso;

    public Arista(String destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public String getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }

}
