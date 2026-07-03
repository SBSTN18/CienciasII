import java.util.TreeMap;
import java.util.ArrayList;

public class GeneradorPseudoaleatorio {

    //Valores predeterminados para probar la formula, Java usa valores tipo long, para siplificarlo se usan valores enteros
    //Se usa un rango de 0 a 1000 para que los numeros generados sean mas faciles de leer y analizar
    //Se usan numeros primos para el multiplicador y el incremento, y un número primo para el módulo, para mejorar la distribución de los números generados
    private static final int MULTIPLICADOR = 193;
    private static final int INCREMENTO = 103;
    private static final int MODULO = 997;
    private int semillaActual;

    //Lista de los primeros 100 números primos para usar como semillas
    public static final int[] PRIMOS_SEMILLAS = {
        2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71,
        73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 
        157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 
        239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 
        331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 
        421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 
        509, 521, 523, 541
    };

    //El valor inicial (semilla)
    public GeneradorPseudoaleatorio(int semillaInicial) {
        this.semillaActual = semillaInicial;
    }

    //Aplica la fórmula matemática para usar la semilla y actualizarla para generar el siguiente número
    public int siguienteNumeroEntero() {
        //Fórmula: (a * X + c) % m   ->  Sale de la formula que usa Java para generar numeros aleatorios
        semillaActual = (MULTIPLICADOR * semillaActual + INCREMENTO) % MODULO;
        return semillaActual;
    }
    
    //Main que usa dos ciclos para generar 100 numeros aleatorios por cada una de las 100 semillas que se usan
    //Cada semilla se incrementa en 1, empezando desde 1 hasta 100, y se imprimen los resultados de cada semilla
    public static void main(String[] args) { 
        //Mapa para almacenar la frecuencia de cada número generado
        TreeMap<Integer, Integer> frecuencias = new TreeMap<>();
        //Lista para almacenar los números generados y calcular la desviación
        ArrayList<Integer> todosLosNumeros = new ArrayList<>();

        for (int i = 0; i < 100; i++) { 
            int semillaPrimo = PRIMOS_SEMILLAS[i];
            GeneradorPseudoaleatorio gen = new GeneradorPseudoaleatorio(semillaPrimo); 
            System.out.print("Con la semilla " + semillaPrimo + " los numeros aleatorios generados son: "); 
            
            for (int j = 0; j < 100; j++) {
                int num = gen.siguienteNumeroEntero();
                frecuencias.put(num, frecuencias.getOrDefault(num, 0) + 1);
                todosLosNumeros.add(num);
                
                System.out.print(num);
                if (j < 99) System.out.print(", ");
            }
            System.out.println(); 
        } 
        
        //Calcular desviacion estandar
        int totalDatos = todosLosNumeros.size();
        
        //Calcular la Media
        double suma = 0;
        for (int num : todosLosNumeros) {
            suma += num;
        }
        double media = suma / totalDatos;

        //Calcular la varianza
        double sumaDiferenciasAlCuadrado = 0;
        for (int num : todosLosNumeros) {
            sumaDiferenciasAlCuadrado += Math.pow(num - media, 2);
        }
        double varianza = sumaDiferenciasAlCuadrado / totalDatos;
        double desviacionEstandar = Math.sqrt(varianza);

        //Se imprimen los resultados de la frecuencia de cada número generado y el análisis estadístico
        System.out.println("\nFrecuencia de cada número generado {Número=Veces}:");
        System.out.println(frecuencias);
        
        System.out.println("\nANÁLISIS ESTADÍSTICO");
        System.out.printf("Total de números evaluados: %d\n", totalDatos);
        System.out.printf("Media (Promedio): %.2f\n", media);
        System.out.printf("Desviación Estándar: %.2f\n", desviacionEstandar);
    } 
}
