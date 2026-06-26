import java.util.Scanner;

public class HashConLimite {

    public static long generarHash(String texto) {
        long hash = 7;
        for (int i = 0; i < texto.length(); i++) {
            hash = hash * 31 + texto.charAt(i);
        }
        return Math.abs(hash);
    }

    public static void main(String[] args) {
        Scanner lector = new Scanner(System.in);
        System.out.print("Ingrese una palabra: ");
        String palabra = lector.nextLine();

        long hash = generarHash(palabra);

        int dimensionExacta = 15; 
        
        long limite = (long) Math.pow(10, dimensionExacta);
        long hashAcotado = hash % limite;

        String hashFormateado = String.format("%0" + dimensionExacta + "d", hashAcotado);

        System.out.println("Valor cifrado: " + hashFormateado);
        lector.close();
    }
}
