package almostshortestpath;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);

        while (true) {

            int N = entrada.nextInt();
            int M = entrada.nextInt();

            if (N == 0 && M == 0) {
                break;
            }

            int S = entrada.nextInt();
            int D = entrada.nextInt();

            Grafo grafo = new Grafo(N);

            for (int i = 0; i < M; i++) {

                int U = entrada.nextInt();
                int V = entrada.nextInt();
                int P = entrada.nextInt();

                grafo.agregarArista(U, V, P);

            }

            grafo.dijkstra(S);

            grafo.eliminarCaminosMinimos(S, D);

            System.out.println(grafo.obtenerAlmostShortestPath(S, D));

        }

        entrada.close();

    }

}