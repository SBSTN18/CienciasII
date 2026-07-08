package listaAdyacencia;

import java.util.Scanner;

/**
 * Clase principal del programa.
 * Permite ingresar un grafo y generar su lista de adyacencia.
 * Pensado como librería base para algoritmos de camino corto.
 */
public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Grafo grafo = new Grafo();

        System.out.print("Cantidad de vertices: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {

            System.out.print("Nombre del vertice: ");
            String nombre = sc.nextLine();

            grafo.agregarVertice(nombre);

        }

        System.out.print("\nCantidad de aristas: ");
        int m = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < m; i++) {

            System.out.println("\nArista " + (i + 1));

            System.out.print("Origen: ");
            String origen = sc.nextLine();

            System.out.print("Destino: ");
            String destino = sc.nextLine();

            System.out.print("Peso: ");
            int peso = sc.nextInt();
            sc.nextLine();

            grafo.agregarArista(origen, destino, peso);

        }

        grafo.mostrarListaAdyacencia();

    }

}
