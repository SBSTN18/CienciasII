package vista;

import java.util.Scanner;
import modelo.Grafo;
import modelo.ResultadoColoreado;

/**
 * Responsabilidad unica: toda la interaccion con el usuario.
 * No conoce reglas de negocio ni de los algoritmos; solo lee y muestra.
 */
public class VistaConsola {
    private static final String ABECEDARIO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String[] NOMBRES_COLOR = {
        "", "Rojo", "Azul", "Verde", "Amarillo", "Naranja",
        "Morado", "Rosa", "Cyan", "Marron", "Gris"
    };

    private final Scanner entrada;

    public VistaConsola(Scanner entrada) {
        this.entrada = entrada;
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }

    public void mostrarMenu() {
        System.out.println("\n===== COLOREADO DE GRAFOS =====");
        System.out.println("1. Ingresar grafo");
        System.out.println("2. Mostrar lista de adyacencia");
        System.out.println("3. Ejecutar Voraz");
        System.out.println("4. Ejecutar Welsh-Powell");
        System.out.println("5. Ejecutar D-Satur");
        System.out.println("6. Comparar los tres algoritmos");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    public int pedirEntero(String prompt) {
        Integer valor = null;
        while (valor == null) {
            System.out.print(prompt);
            if (entrada.hasNextInt()) {
                valor = entrada.nextInt();
            } else {
                System.out.println("Debe ingresar un numero entero.");
                entrada.next();
            }
        }
        return valor;
    }

    public boolean pedirBooleano(String prompt) {
        while (true) {
            System.out.print(prompt + " (1=Si / 0=No): ");
            if (entrada.hasNextInt()) {
                int v = entrada.nextInt();
                if (v == 1) return true;
                if (v == 0) return false;
            } else {
                entrada.next();
            }
            System.out.println("Ingrese 1 o 0.");
        }
    }

    /**
     * Lee las aristas del grafo interactivamente.
     * Retorna arreglo de pares [origen, destino] (base 0).
     * El usuario ingresa en base 1 (A=1, B=2...).
     */
    public int[][] pedirAristas(int numVertices) {
        System.out.println("\nVertices disponibles:");
        for (int i = 0; i < numVertices; i++) {
            System.out.print(ABECEDARIO.charAt(i) + "=" + (i + 1) + "  ");
        }
        System.out.println();

        int maxAristas = numVertices * (numVertices - 1) / 2;
        int[][] aristasTmp = new int[maxAristas][2];
        int count = 0;

        System.out.println("Ingrese aristas (origen destino). Escriba 0 0 para terminar.");
        while (true) {
            System.out.print("Arista " + (count + 1) + ": ");
            if (!entrada.hasNextInt()) { entrada.next(); continue; }
            int origen = entrada.nextInt();
            if (!entrada.hasNextInt()) { entrada.next(); continue; }
            int destino = entrada.nextInt();

            if (origen == 0 && destino == 0) break;

            if (origen < 1 || origen > numVertices || destino < 1 || destino > numVertices) {
                System.out.println("Vertices fuera de rango. Intente de nuevo.");
                continue;
            }
            if (origen == destino) {
                System.out.println("No se permiten bucles. Intente de nuevo.");
                continue;
            }

            aristasTmp[count][0] = origen - 1; // convertir a base 0
            aristasTmp[count][1] = destino - 1;
            count++;
        }

        int[][] aristas = new int[count][2];
        for (int i = 0; i < count; i++) {
            aristas[i][0] = aristasTmp[i][0];
            aristas[i][1] = aristasTmp[i][1];
        }
        return aristas;
    }

    /**
     * Muestra la lista de adyacencia del grafo.
     */
    public void mostrarGrafo(Grafo grafo) {
        System.out.println("\n--- Lista de adyacencia ---");
        for (int i = 0; i < grafo.getNumVertices(); i++) {
            System.out.print(grafo.getEtiqueta(i) + " -> ");
            int[] vecinos = grafo.getVecinos(i);
            if (vecinos.length == 0) {
                System.out.print("(sin vecinos)");
            } else {
                for (int j = 0; j < vecinos.length; j++) {
                    System.out.print(grafo.getEtiqueta(vecinos[j]));
                    if (j < vecinos.length - 1) System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Muestra el resultado de un algoritmo de coloreado.
     */
    public void mostrarResultado(ResultadoColoreado resultado, Grafo grafo) {
        System.out.println("\n--- Resultado: " + resultado.getNombreAlgoritmo() + " ---");
        System.out.println("Numero cromatico: " + resultado.getNumeroCromatico());
        System.out.printf("Tiempo de ejecucion: %.4f ms%n", resultado.getTiempoMs());
        System.out.println("\nColoreado:");
        for (int i = 0; i < resultado.getNumVertices(); i++) {
            int color = resultado.getColor(i);
            String nombreColor = color <= NOMBRES_COLOR.length - 1
                ? NOMBRES_COLOR[color]
                : "Color" + color;
            System.out.println("  " + grafo.getEtiqueta(i) + " -> Color " + color + " (" + nombreColor + ")");
        }
    }

    /**
     * Muestra una tabla comparando los tres algoritmos.
     */
    public void mostrarComparacion(ResultadoColoreado voraz,
                                   ResultadoColoreado welsh,
                                   ResultadoColoreado dsatur) {
        System.out.println("\n========== COMPARACION ==========");
        System.out.printf("%-20s %-18s %-15s%n", "Algoritmo", "N° Cromatico", "Tiempo (ms)");
        System.out.println("-".repeat(55));
        imprimirFila(voraz);
        imprimirFila(welsh);
        imprimirFila(dsatur);
        System.out.println("-".repeat(55));

        ResultadoColoreado mejorColor = menorNumeroCromatico(voraz, welsh, dsatur);
        ResultadoColoreado masFast  = menorTiempo(voraz, welsh, dsatur);

        System.out.println("Menor numero cromatico: " + mejorColor.getNombreAlgoritmo()
                + " (" + mejorColor.getNumeroCromatico() + " colores)");
        System.out.printf("Mas rapido: %s (%.4f ms)%n",
                masFast.getNombreAlgoritmo(), masFast.getTiempoMs());
    }

    private void imprimirFila(ResultadoColoreado r) {
        System.out.printf("%-20s %-18d %.4f%n",
                r.getNombreAlgoritmo(), r.getNumeroCromatico(), r.getTiempoMs());
    }

    private ResultadoColoreado menorNumeroCromatico(ResultadoColoreado a,
                                                     ResultadoColoreado b,
                                                     ResultadoColoreado c) {
        ResultadoColoreado mejor = a;
        if (b.getNumeroCromatico() < mejor.getNumeroCromatico()) mejor = b;
        if (c.getNumeroCromatico() < mejor.getNumeroCromatico()) mejor = c;
        return mejor;
    }

    private ResultadoColoreado menorTiempo(ResultadoColoreado a,
                                            ResultadoColoreado b,
                                            ResultadoColoreado c) {
        ResultadoColoreado mejor = a;
        if (b.getTiempoNanos() < mejor.getTiempoNanos()) mejor = b;
        if (c.getTiempoNanos() < mejor.getTiempoNanos()) mejor = c;
        return mejor;
    }
}
