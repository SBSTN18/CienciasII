package listaIncidencia;

import java.util.ArrayList;

public class Vertice {

    private String nombre;
    private ArrayList<String> incidencias;

    public Vertice(String nombre) {
        this.nombre = nombre;
        incidencias = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }
// Agrega una arista a la lista de incidencia del vértice
    public void agregarIncidencia(String arista) {
        incidencias.add(arista);
    }

    public ArrayList<String> getIncidencias() {
        return incidencias;
    }

}