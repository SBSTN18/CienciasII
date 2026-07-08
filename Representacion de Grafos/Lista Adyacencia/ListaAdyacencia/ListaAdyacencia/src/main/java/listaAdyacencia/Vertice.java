package listaAdyacencia;

import java.util.ArrayList;

/**
 * Representa un vértice del grafo.
 * Mantiene una lista de adyacencia con los vecinos directos
 * y el peso de cada arista hacia ellos.
 */
public class Vertice {

    private String nombre;
    private ArrayList<Arista> adyacentes;

    public Vertice(String nombre) {
        this.nombre = nombre;
        adyacentes = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    // Agrega un vecino a la lista de adyacencia del vértice
    public void agregarAdyacente(String destino, int peso) {
        adyacentes.add(new Arista(destino, peso));
    }

    public ArrayList<Arista> getAdyacentes() {
        return adyacentes;
    }

}
