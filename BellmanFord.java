import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class BellmanFord {
    public static class Arista {

        private String destino;
        private int peso;

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }

        public String getDestino() {
            return destino;
        }

        public int getPeso() {
            return peso;
        }
    }
    public static class Vertice {

        private String nombre;
        private ArrayList<Arista> adyacentes;

        public Vertice(String nombre) {
            this.nombre = nombre;
            adyacentes = new ArrayList<>();
        }

        public String getNombre() {
            return nombre;
        }

        public void agregarAdyacente(String destino, int peso) {
            adyacentes.add(new Arista(destino, peso));
        }

        public ArrayList<Arista> getAdyacentes() {
            return adyacentes;
        }

    }

    public static class Grafo {
    
        private ArrayList<Vertice> vertices;
    
        public Grafo() {
            vertices = new ArrayList<>();
        }
    
        //Agrega un nuevo vértice al grafo
        public void agregarVertice(String nombre) {
            vertices.add(new Vertice(nombre));
        }
    
        //Agrega una arista dirigida y actualiza la lista de adyacencia del origen
        public void agregarArista(String origen, String destino, int peso) {
    
            for (Vertice v : vertices) {
    
                if (v.getNombre().equals(origen)) {
                    v.agregarAdyacente(destino, peso);
                }
    
            }
    
        }
    
        //Retorna la lista de vértices (para uso externo como librería)
        public ArrayList<Vertice> getVertices() {
            return vertices;
        }
    
        //Retorna un vértice por nombre, o null si no existe
        public Vertice buscarVertice(String nombre) {
    
            for (Vertice v : vertices) {
    
                if (v.getNombre().equals(nombre)) {
                    return v;
                }
    
            }
    
            return null;
    
        }
    
        //Muestra la lista de adyacencia del grafo
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

    public void ejecutarBellmanFord(String nombreOrigen) {
            //Estructura para guardar las distancias mínimas y los predecesores (para armar el camino)
            Map<String, Integer> distancias = new HashMap<>();
            Map<String, String> predecesores = new HashMap<>();

            //Inicializar todas las distancias en "Infinito"
            for (Vertice v : vertices) {
                distancias.put(v.getNombre(), Integer.MAX_VALUE);
                predecesores.put(v.getNombre(), "Ninguno");
            }
            
            //La distancia del origen a sí mismo es siempre 0
            if (!distancias.containsKey(nombreOrigen)) {
                System.out.println("El nodo origen no existe en el grafo.");
                return;
            }
            distancias.put(nombreOrigen, 0);

            int totalVertices = vertices.size();

            //Relajar todas las aristas (V - 1) veces
            for (int i = 1; i < totalVertices; i++) {
                // Recorremos cada arista existente en el grafo
                for (Vertice u : vertices) {
                    String uNombre = u.getNombre();
                    
                    //Si el nodo actual aún es inaccesible, saltamos sus aristas
                    if (distancias.get(uNombre) == Integer.MAX_VALUE) continue;

                    for (Arista arista : u.getAdyacentes()) {
                        String vNombre = arista.getDestino();
                        int peso = arista.getPeso();

                        //Condición de relajación
                        if (distancias.get(uNombre) + peso < distancias.get(vNombre)) {
                            distancias.put(vNombre, distancias.get(uNombre) + peso);
                            predecesores.put(vNombre, uNombre);
                        }
                    }
                }
            }

            //Detectar ciclos de peso negativo
            for (Vertice u : vertices) {
                String uNombre = u.getNombre();
                if (distancias.get(uNombre) == Integer.MAX_VALUE) continue;

                for (Arista arista : u.getAdyacentes()) {
                    String vNombre = arista.getDestino();
                    int peso = arista.getPeso();

                    if (distancias.get(uNombre) + peso < distancias.get(vNombre)) {
                        System.out.println("\n¡ALERTA!: El grafo contiene un ciclo de peso negativo accesible desde el origen.");
                        return;
                    }
                }
            }

            //Mostrar los resultados en pantalla si todo sale bien
            mostrarResultadosBF(nombreOrigen, distancias, predecesores);
        }

        private void mostrarResultadosBF(String origen, Map<String, Integer> distancias, Map<String, String> predecesores) {
            System.out.println("\n--- RESULTADOS DE BELLMAN-FORD (Origen: " + origen + ") ---");
            System.out.printf("%-15s %-15s %-15s\n", "Vértice Destino", "Distancia Mínima", "Predecesor");
            for (Map.Entry<String, Integer> entrada : distancias.entrySet()) {
                String dest = entrada.getKey();
                int dist = entrada.getValue();
                String pred = predecesores.get(dest);
                
                String distTexto = (dist == Integer.MAX_VALUE) ? "Infinito" : String.valueOf(dist);
                System.out.printf("%-15s %-15s %-15s\n", dest, distTexto, pred);
        }
    }
}
    
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

        //Ejecución de Bellman-Ford
        System.out.print("\nIngrese el nodo origen para Bellman-Ford: ");
        String nodoOrigen = sc.nextLine();
        
        grafo.ejecutarBellmanFord(nodoOrigen);

        sc.close();
    }
}