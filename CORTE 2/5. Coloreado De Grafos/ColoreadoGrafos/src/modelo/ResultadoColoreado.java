package modelo;

/**
 * Almacena el resultado de ejecutar un algoritmo de coloreado.
 * Responsabilidad unica: guardar y exponer el resultado.
 *
 * colores[i] → color asignado al vertice i (los colores son numeros desde 1)
 */
public class ResultadoColoreado {
    private final int[] colores;
    private final int numeroCromatico;
    private final long tiempoNanos;
    private final String nombreAlgoritmo;

    public ResultadoColoreado(int[] colores, long tiempoNanos, String nombreAlgoritmo) {
        this.colores = colores;
        this.tiempoNanos = tiempoNanos;
        this.nombreAlgoritmo = nombreAlgoritmo;
        this.numeroCromatico = calcularNumeroCromatico();
    }

    private int calcularNumeroCromatico() {
        int max = 0;
        for (int c : colores) {
            if (c > max) max = c;
        }
        return max;
    }

    public int getColor(int vertice) {
        return colores[vertice];
    }

    public int getNumeroCromatico() {
        return numeroCromatico;
    }

    public long getTiempoNanos() {
        return tiempoNanos;
    }

    public double getTiempoMs() {
        return tiempoNanos / 1_000_000.0;
    }

    public String getNombreAlgoritmo() {
        return nombreAlgoritmo;
    }

    public int getNumVertices() {
        return colores.length;
    }
}
