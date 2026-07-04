package com.tienda.laboratoriodijkstra;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println(" ALGORITMO DE DIJKSTRA ");
        System.out.println("=================================");

        // Solicita la cantidad de vértices
        System.out.print("Cantidad de vertices: ");
        int vertices = entrada.nextInt();

        Grafo grafo = new Grafo(vertices);

        // Solicita la cantidad de aristas
        System.out.print("Cantidad de aristas: ");
        int aristas = entrada.nextInt();

        // Se ingresan todas las aristas
        for (int i = 0; i < aristas; i++) {

            System.out.println("\nArista " + (i + 1));

            System.out.print("Origen: ");
            int origen = entrada.nextInt();

            System.out.print("Destino: ");
            int destino = entrada.nextInt();

            System.out.print("Peso: ");
            int peso = entrada.nextInt();

            grafo.agregarArista(origen, destino, peso);

        }

        // Vértice desde el cual iniciará Dijkstra
        System.out.print("\nVertice inicial: ");
        int origen = entrada.nextInt();

        Dijkstra algoritmo = new Dijkstra();

        // Inicio de la medición del tiempo
        long inicio = System.nanoTime();

        int[] distancias = algoritmo.calcular(grafo, origen);

        // Fin de la medición
        long fin = System.nanoTime();

        System.out.println("\n========== RESULTADOS ==========");

        // Se muestran las distancias encontradas
        for (int i = 0; i < distancias.length; i++) {

            if (distancias[i] == Integer.MAX_VALUE) {

                System.out.println("No existe camino hasta el vertice " + i);

            } else {

                System.out.println("Distancia hasta " + i + " = " + distancias[i]);

            }

        }

        // Cálculo del tiempo de ejecución
        long tiempoNano = fin - inicio;

        double tiempoMilis = tiempoNano / 1000000.0;

        System.out.println("\nTiempo de ejecución:");
        System.out.println(tiempoNano + " nanosegundos");
        System.out.println(tiempoMilis + " milisegundos");

    }

}
