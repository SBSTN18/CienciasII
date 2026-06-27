package listaIncidencia;

public class Arista {

    private String nombre;
    private String origen;
    private String destino;

    public Arista(String nombre, String origen, String destino) {
        this.nombre = nombre;
        this.origen = origen;
        this.destino = destino;
    }

    public String getNombre() {
        return nombre;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

}
