package almostshortestpath;

public class Arista {

    private int destino;
    private int peso;
    private boolean eliminada;

    public Arista(int destino, int peso) {
        this.destino = destino;
        this.peso = peso;
        this.eliminada = false;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public boolean isEliminada() {
        return eliminada;
    }

    public void setEliminada(boolean eliminada) {
        this.eliminada = eliminada;
    }

}