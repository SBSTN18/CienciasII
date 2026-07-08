package modelo;

/**
 * Contrato para algoritmos de coloreado de grafos.
 * El controlador depende de esta interfaz, no de implementaciones concretas (DIP).
 * Agregar un nuevo algoritmo no requiere modificar el controlador (OCP).
 */
public interface IAlgoritmoColoreado {
    ResultadoColoreado ejecutar(Grafo grafo);
}
