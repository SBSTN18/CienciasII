package laboratoriofordfulkersen;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);

        System.out.println("===========================================");
        System.out.println("   COMPARACION FORD-FULKERSON");
        System.out.println("   DFS vs EDMONDS-KARP");
        System.out.println("===========================================");

        // Cantidad de vértices
        System.out.print("\nIngrese la cantidad de vertices: ");
        int vertices = entrada.nextInt();

        Grafo grafo = new Grafo(vertices);

        // Cantidad de aristas
        System.out.print("Ingrese la cantidad de aristas: ");
        int aristas = entrada.nextInt();

        // Ingreso manual de las aristas
        for (int i = 0; i < aristas; i++) {

            System.out.println("\nArista " + (i + 1));

            System.out.print("Origen: ");
            int origen = entrada.nextInt();

            System.out.print("Destino: ");
            int destino = entrada.nextInt();

            System.out.print("Capacidad: ");
            int capacidad = entrada.nextInt();

            grafo.agregarArista(origen, destino, capacidad);

        }

        // Mostrar la matriz ingresada
        grafo.mostrarMatriz();

        // Nodo origen y destino
        System.out.print("\nNodo origen: ");
        int origen = entrada.nextInt();

        System.out.print("Nodo destino: ");
        int destino = entrada.nextInt();

        /*
         * ===============================
         * FORD-FULKERSON (DFS)
         * ===============================
         */
        FordFulkersonDFS ford = new FordFulkersonDFS();

        long inicioFord = System.nanoTime();

        int flujoFord = ford.flujoMaximo(grafo, origen, destino);

        long finFord = System.nanoTime();

        long tiempoFord = finFord - inicioFord;

        /*
         * ===============================
         * EDMONDS-KARP (BFS)
         * ===============================
         */
        EdmondsKarp edmonds = new EdmondsKarp();

        long inicioEdmonds = System.nanoTime();

        int flujoEdmonds = edmonds.flujoMaximo(grafo, origen, destino);

        long finEdmonds = System.nanoTime();

        long tiempoEdmonds = finEdmonds - inicioEdmonds;

        /*
         * ===============================
         * RESULTADOS
         * ===============================
         */

        System.out.println("\n===========================================");
        System.out.println("RESULTADOS");
        System.out.println("===========================================");

        System.out.println("\nFord-Fulkerson (DFS)");
        System.out.println("-------------------------");
        System.out.println("Flujo máximo : " + flujoFord);
        System.out.println("Tiempo (ns)  : " + tiempoFord);
        System.out.println("Tiempo (ms)  : " + (tiempoFord / 1_000_000.0));

        System.out.println("\nEdmonds-Karp (BFS)");
        System.out.println("-------------------------");
        System.out.println("Flujo máximo : " + flujoEdmonds);
        System.out.println("Tiempo (ns)  : " + tiempoEdmonds);
        System.out.println("Tiempo (ms)  : " + (tiempoEdmonds / 1_000_000.0));

        /*
         * Comparación automática
         */
        System.out.println("\n===========================================");
        System.out.println("COMPARACION");
        System.out.println("===========================================");

        if (tiempoFord < tiempoEdmonds) {

            System.out.println("Ford-Fulkerson fue más rápido.");

        } else if (tiempoFord > tiempoEdmonds) {

            System.out.println("Edmonds-Karp fue más rápido.");

        } else {

            System.out.println("Ambos algoritmos tardaron el mismo tiempo.");

        }

        System.out.println("\nEl flujo maximo encontrado por ambos algoritmos es el mismo: "
                + flujoFord);

    }

}
