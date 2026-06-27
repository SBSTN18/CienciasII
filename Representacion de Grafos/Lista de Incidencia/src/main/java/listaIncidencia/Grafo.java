package listaIncidencia;

import java.util.ArrayList;

public class Grafo {

    private ArrayList<Vertice> vertices;
    private ArrayList<Arista> aristas;

    public Grafo() {
        vertices = new ArrayList<>();
        aristas = new ArrayList<>();
    }

    // Agrega un nuevo vértice al grafo
    public void agregarVertice(String nombre) {
        vertices.add(new Vertice(nombre));
    }

    // Agrega una arista y actualiza la lista de incidencia
    public void agregarArista(String origen, String destino) {

        String nombre = "e" + (aristas.size() + 1);

        Arista nueva = new Arista(nombre, origen, destino);

        aristas.add(nueva);

        for (Vertice v : vertices) {

            if (v.getNombre().equals(origen) ||
                v.getNombre().equals(destino)) {

                v.agregarIncidencia(nombre);

            }

        }

    }

    // Muestra la lista de incidencia del grafo
    public void mostrarListaIncidencia() {

        System.out.println("\nLISTA DE INCIDENCIA\n");

        for (Vertice v : vertices) {

            System.out.print(v.getNombre() + " -> ");

            for (String e : v.getIncidencias()) {

                System.out.print(e + " ");

            }

            System.out.println();

        }

    }

}