import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class ComparacionPrimKrusal {

    //Comparable para Kruskal y estructura global
    public static class Arista implements Comparable<Arista> {
        private String origen; // Atributo extra útil para Kruskal global
        private String destino;
        private int peso;

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }

        // Sobrecarga de constructor para rastrear el origen en Kruskal
        public Arista(String origen, String destino, int peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }

        public String getOrigen() { return origen; }
        public String getDestino() { return destino; }
        public int getPeso() { return peso; }

        @Override
        public int compareTo(Arista otra) {
            return Integer.compare(this.peso, otra.peso);
        }
    }

    public static class Vertice {
        private String nombre;
        private ArrayList<Arista> adyacentes;

        public Vertice(String nombre) {
            this.nombre = nombre;
            adyacentes = new ArrayList<>();
        }

        public String getNombre() { return nombre; }

        public void agregarAdyacente(String destino, int peso) {
            adyacentes.add(new Arista(destino, peso));
        }

        public ArrayList<Arista> getAdyacentes() { return adyacentes; }
    }

    public static class Grafo {
        private ArrayList<Vertice> vertices;

        public Grafo() { vertices = new ArrayList<>(); }

        public void agregarVertice(String nombre) {
            vertices.add(new Vertice(nombre));
        }

        public void agregarArista(String origen, String destino, int peso) {
            for (Vertice v : vertices) {
                if (v.getNombre().equals(origen)) {
                    v.agregarAdyacente(destino, peso);
                }
                if (v.getNombre().equals(destino)) {
                    v.agregarAdyacente(origen, peso);
                }
            }
        }

        public ArrayList<Vertice> getVertices() { return vertices; }

        public Vertice buscarVertice(String nombre) {
            for (Vertice v : vertices) {
                if (v.getNombre().equals(nombre)) return v;
            }
            return null;
        }

        public ArrayList<Arista> obtenerTodasLasAristas() {
            ArrayList<Arista> todas = new ArrayList<>();
            for (Vertice v : vertices) {
                for (Arista a : v.getAdyacentes()) {
                    // Para evitar duplicados en grafos no dirigidos (A-B y B-A)
                    if (v.getNombre().compareTo(a.getDestino()) < 0) {
                        todas.add(new Arista(v.getNombre(), a.getDestino(), a.getPeso()));
                    }
                }
            }
            return todas;
        }

        public void mostrarListaAdyacencia() {
            System.out.println("\nLISTA DE ADYACENCIA\n");
            for (Vertice v : vertices) {
                System.out.print(v.getNombre() + " -> ");
                for (Arista a : v.getAdyacentes()) {
                    System.out.print("[" + a.getDestino() + ", w=" + a.getPeso() + "] ");
                }
                System.out.println();
            }
        }
    }

    public static class AlgoritmoPrim {
        public static void ejecutar(Grafo grafo) {
            if (grafo.getVertices().isEmpty()) return;

            long startTime = System.nanoTime();

            Set<String> visitados = new HashSet<>();
            PriorityQueue<Arista> colaPrioridad = new PriorityQueue<>();
            ArrayList<Arista> mst = new ArrayList<>();
            int costoTotal = 0;

            //Tomar el primer vértice disponible como punto de partida
            Vertice inicial = grafo.getVertices().get(0);
            visitados.add(inicial.getNombre());

            //Agregar sus aristas iniciales a la cola
            for (Arista a : inicial.getAdyacentes()) {
                colaPrioridad.add(new Arista(inicial.getNombre(), a.getDestino(), a.getPeso()));
            }

            while (!colaPrioridad.isEmpty() && visitados.size() < grafo.getVertices().size()) {
                Arista aristaActual = colaPrioridad.poll();
                String destino = aristaActual.getDestino();

                if (visitados.contains(destino)) continue;

                //Registrar en el MST
                visitados.add(destino);
                mst.add(aristaActual);
                costoTotal += aristaActual.getPeso();

                // Agregar aristas del nuevo nodo visitado
                Vertice vDestino = grafo.buscarVertice(destino);
                if (vDestino != null) {
                    for (Arista a : vDestino.getAdyacentes()) {
                        if (!visitados.contains(a.getDestino())) {
                            colaPrioridad.add(new Arista(vDestino.getNombre(), a.getDestino(), a.getPeso()));
                        }
                    }
                }
            }

            long endTime = System.nanoTime();
            System.out.println("\n Algoritmo PRIM");
            System.out.println("Tiempo de ejecución: " + (endTime - startTime) + " ns");
            System.out.println("Costo total del MST: " + costoTotal);
        }
    }

    public static class AlgoritmoKruskal {
        //Estructura interna de apoyo Disjoint-Set usando Strings
        static class UnionFind {
            private ArrayList<String> nodos;
            private int[] padre;

            public UnionFind(ArrayList<Vertice> vertices) {
                nodos = new ArrayList<>();
                for (Vertice v : vertices) nodos.add(v.getNombre());
                padre = new int[nodos.size()];
                for (int i = 0; i < padre.length; i++) padre[i] = i;
            }

            private int find(int i) {
                if (padre[i] == i) return i;
                return padre[i] = find(padre[i]);
            }

            public int buscar(String nombre) {
                int index = nodos.indexOf(nombre);
                return find(index);
            }

            public void unir(String nodo1, String nodo2) {
                int raiz1 = buscar(nodo1);
                int raiz2 = buscar(nodo2);
                if (raiz1 != raiz2) {
                    padre[raiz1] = raiz2;
                }
            }
        }

        public static void ejecutar(Grafo grafo) {
            long startTime = System.nanoTime();

            ArrayList<Arista> aristas = grafo.obtenerTodasLasAristas();
            Collections.sort(aristas); //Ordenamiento base O(E log E)

            UnionFind uf = new UnionFind(grafo.getVertices());
            ArrayList<Arista> mst = new ArrayList<>();
            int costoTotal = 0;

            for (Arista arista : aristas) {
                int conjuntoOrigen = uf.buscar(arista.getOrigen());
                int conjuntoDestino = uf.buscar(arista.getDestino());

                // Si no pertenecen al mismo conjunto, no forman ciclos
                if (conjuntoOrigen != conjuntoDestino) {
                    mst.add(arista);
                    costoTotal += arista.getPeso();
                    uf.unir(arista.getOrigen(), arista.getDestino());
                }
                
                if (mst.size() == grafo.getVertices().size() - 1) break;
            }

            long endTime = System.nanoTime();
            System.out.println("\n Algoritmo KRUSKAL");
            System.out.println("Tiempo de ejecución: " + (endTime - startTime) + " ns");
            System.out.println("Costo total del MST: " + costoTotal);
        }
    }

    public static class GeneradorGrafos {

        public static Grafo crearGrafoAleatorio(int numVertices, int numAristasMax) {
            Grafo g = new Grafo();
            
            //1. Crear los vértices textualmente ("0", "1", "2"...)
            for (int i = 0; i < numVertices; i++) {
                g.agregarVertice(String.valueOf(i));
            }

            //2. Para asegurar la exactitud sin duplicar aristas no dirigidas, se mapean todas las conexiones teóricas posibles.
            ArrayList<String[]> conexionesPosibles = new ArrayList<>();
            for (int i = 0; i < numVertices; i++) {
                for (int j = i + 1; j < numVertices; j++) {
                    conexionesPosibles.add(new String[]{String.valueOf(i), String.valueOf(j)});
                }
            }

            //Mezclar aleatoriamente el universo de conexiones posibles
            Collections.shuffle(conexionesPosibles);

            //Tomar exactamente los casos de prueba, con pesos aleatorios entre 1 y 100
            int limiteAristas = Math.min(numAristasMax, conexionesPosibles.size());
            for (int k = 0; k < limiteAristas; k++) {
                String[] par = conexionesPosibles.get(k);
                int pesoAleatorio = (int) (Math.random() * 100) + 1;
                g.agregarArista(par[0], par[1], pesoAleatorio);
            }

            return g;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Grafo grafo = new Grafo();

        /* MANERA PARA INGRESAR DATOS MANUALES
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

        AlgoritmoPrim.ejecutar(grafo);
        AlgoritmoKruskal.ejecutar(grafo);

        */
        sc.close();

        //MANERA DE PROBAR ARBOLES GRANDES ALEATORIOS Y PODER COMPARARLOS REALMENTE
        //CASO DE PRUEBA 1: GRAFO PEQUEÑO
        System.out.println("\nCASO DE PRUEBA 1: Grafo Pequeño (5 Vértices, 7 Aristas)");
        Grafo g1 = GeneradorGrafos.crearGrafoAleatorio(5, 7);
        g1.mostrarListaAdyacencia();
        AlgoritmoPrim.ejecutar(g1);
        AlgoritmoKruskal.ejecutar(g1);

        //CASO DE PRUEBA 2: GRAFO MEDIANO
        System.out.println("\nCASO DE PRUEBA 2: Grafo Mediano (20 Vértices, 35 Aristas)");
        Grafo g2 = GeneradorGrafos.crearGrafoAleatorio(20, 35);
        AlgoritmoPrim.ejecutar(g2);
        AlgoritmoKruskal.ejecutar(g2);

        //CASO DE PRUEBA 3: GRAFO DENSO
        System.out.println("\nCASO DE PRUEBA 3: Grafo Denso (100 Vértices, 450 Aristas)");
        Grafo g3 = GeneradorGrafos.crearGrafoAleatorio(100, 450);
        AlgoritmoPrim.ejecutar(g3);
        AlgoritmoKruskal.ejecutar(g3);

        //CASO DE PRUEBA 4: GRAFO DISPERSO
        System.out.println("\nCASO DE PRUEBA 4: Grafo Disperso (100 Vértices, 120 Aristas)");
        Grafo g4 = GeneradorGrafos.crearGrafoAleatorio(100, 120);
        AlgoritmoPrim.ejecutar(g4);
        AlgoritmoKruskal.ejecutar(g4);
    }
}