package listaAdyacencia;

import java.util.ArrayList;

/**
 * Representa el grafo mediante una lista de adyacencia.
 * Cada vértice guarda directamente sus vecinos y el peso
 * de la arista que los conecta, estructura óptima para
 * algoritmos de camino corto (Dijkstra, Bellman-Ford, etc.).
 */
public class Grafo {

    private ArrayList<Vertice> vertices;

    public Grafo() {
        vertices = new ArrayList<>();
    }

    // Agrega un nuevo vértice al grafo
    public void agregarVertice(String nombre) {
        vertices.add(new Vertice(nombre));
    }

    // Agrega una arista dirigida y actualiza la lista de adyacencia del origen
    public void agregarArista(String origen, String destino, int peso) {

        for (Vertice v : vertices) {

            if (v.getNombre().equals(origen)) {
                v.agregarAdyacente(destino, peso);
            }

        }

    }

    // Retorna la lista de vértices (para uso externo como librería)
    public ArrayList<Vertice> getVertices() {
        return vertices;
    }

    // Retorna un vértice por nombre, o null si no existe
    public Vertice buscarVertice(String nombre) {

        for (Vertice v : vertices) {

            if (v.getNombre().equals(nombre)) {
                return v;
            }

        }

        return null;

    }

    // Muestra la lista de adyacencia del grafo
    public void mostrarListaAdyacencia() {

        System.out.println("\nLISTA DE ADYACENCIA\n");

        for (Vertice v : vertices) {

            System.out.print(v.getNombre() + " -> ");

            for (Arista a : v.getAdyacentes()) {

                System.out.print("[" + a.getDestino() + ", w=" + a.getPeso() + "] ");

            }

            System.out.println();

        }

    }

}
