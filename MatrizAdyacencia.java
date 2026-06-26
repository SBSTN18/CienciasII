import java.util.Scanner;

public class MatrizAdyacencia{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el # de vertices");
        int vertices = scanner.nextInt();

        int[][] matrizAdyacencia = new int[vertices][vertices];
        String abecedario = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i=0; i< matrizAdyacencia.length; i++){
            for (int j=0; j<matrizAdyacencia[i].length;j++){
                if (i == j) {
                    continue;
                }
                if (matrizAdyacencia[i][j] != 0){
                    continue;
                }
                char origen = abecedario.charAt(i);
                char destino = abecedario.charAt(j);
                System.out.println("Ingrese el peso del vertice entre: " + origen + " y " + destino + ". Si no hay conexion pulse 0");
                int peso = scanner.nextInt();
                matrizAdyacencia[i][j] = peso;
                matrizAdyacencia[j][i] = peso;
            }
        }
        System.out.print("\t");
        for (int j = 0; j < vertices; j++) {
            System.out.print(abecedario.charAt(j) + "\t");
        }
        System.out.println("\n");

        for (int i = 0; i < vertices; i++) {
            System.out.print(abecedario.charAt(i) + "\t"); 
            
            for (int j = 0; j < vertices; j++) {
                System.out.print(matrizAdyacencia[i][j] + "\t");
            }
            System.out.println();
        }
        scanner.close();
    }
}



