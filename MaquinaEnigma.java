import java.util.ArrayList;
import java.util.Map;
import static java.util.Map.entry; 

public class MaquinaEnigma {
    String abecedario = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    ArrayList<String> rotores = new ArrayList<>();

    //Posiciones de los 3 rotores
    int[] posiciones = new int[]{0, 0, 0}; 

    //Inicialización de los rotores
    public MaquinaEnigma() {
        rotores.add("EKMFLGDQVZNTOWYHXUSPAIBRCJ"); //Rotor I
        rotores.add("AJDKSIRUXBLHWTMCQGZNPYFVOE"); //Rotor II
        rotores.add("BDFHJLCPRTXVZNYEIWGAKMUSQO"); //Rotor III
    }

    //Metodo para reiniciar las posiciones de los rotores a su estado inicial
    public void reiniciarRotores() {
        posiciones[0] = 0;
        posiciones[1] = 0;
        posiciones[2] = 0;
    }

    //Clave diaria para el cifrado, que es una permutación simple del alfabeto
    Map<String, String> claveDiaria = Map.ofEntries(
        entry("A", "Z"),
        entry("B", "Y"),
        entry("C", "X"),
        entry("D", "W"),
        entry("E", "V"),
        entry("F", "U"),
        entry("G", "T"),
        entry("H", "S"),
        entry("I", "R"),
        entry("J", "Q"),
        entry("K", "P"),
        entry("L", "O"),
        entry("M", "N"),
        entry("N", "M"),
        entry("O", "L"),
        entry("P", "K"),
        entry("Q", "J"),
        entry("R", "I"),
        entry("S", "H"),
        entry("T", "G"),
        entry("U", "F"),
        entry("V", "E"),
        entry("W", "D"),
        entry("X", "C"),
        entry("Y", "B"),
        entry("Z", "A")
    );

    public String cifrar(String mensaje) {
        StringBuilder mensajeCifrado = new StringBuilder();
            for (char c : mensaje.toCharArray()) {
                if (Character.isLetter(c)) {
                    c = Character.toUpperCase(c);
                    
                    //Rotacion de los rotores, el primero rota por cada letra, el segundo cada 26 letras, y el tercero cada 26*26 letras
                    posiciones[0]++;
                    if (posiciones[0] == 26) {
                        posiciones[0] = 0;
                        posiciones[1]++;
                        if (posiciones[1] == 26) {
                            posiciones[1] = 0;
                            posiciones[2]++;
                            if (posiciones[2] == 26) {
                                posiciones[2] = 0;
                            }
                        }
                    }
                    int pos = abecedario.indexOf(c);

                    //Rotor I -> Rotor II -> Rotor III
                    for (int i = 0; i < rotores.size(); i++) {
                        String rotor = rotores.get(i);
                        pos = (pos + posiciones[i]) % 26;
                        c = rotor.charAt(pos);
                        pos = (abecedario.indexOf(c) - posiciones[i] + 26) % 26;
                        c = abecedario.charAt(pos);
                    }

                    //Clave diaria
                    String letraStr = String.valueOf(c);
                    c = claveDiaria.getOrDefault(letraStr, letraStr).charAt(0);

                    //Rotor III -> Rotor II -> Rotor I
                    for (int i = rotores.size() - 1; i >= 0; i--) {
                        String rotor = rotores.get(i);
                        pos = abecedario.indexOf(c);
                        //Ajusta la señal de entrada sumando el desfase generado por el giro del rotor
                        int entradaRotor = (pos + posiciones[i]) % 26;
                        char letraEntrada = abecedario.charAt(entradaRotor);
                        //Encuentra la conexión física inversa buscando la posición de la letra dentro del cableado del rotor
                        int salidaRotor = rotor.indexOf(letraEntrada);
                        //Resta el desfase del giro al salir del rotor para devolver la señal a la coordenada absoluta del abecedario
                        pos = (salidaRotor - posiciones[i] + 26) % 26;
                        c = abecedario.charAt(pos);
                    }
                }
                mensajeCifrado.append(c);
            }
            return mensajeCifrado.toString();
        }

    public static void main(String[] args) {
        MaquinaEnigma enigma = new MaquinaEnigma();
        String mensajeOriginal = "PRUEBA DE CIFRADO, PRUEBA DE DESCIFRADO, COLOMBIA CAMPEON 2026, VIVA FALCAO";
        String mensajeCifrado = enigma.cifrar(mensajeOriginal);
        enigma.reiniciarRotores();
        String mensajeDescifrado = enigma.cifrar(mensajeCifrado);
        System.out.println("Mensaje Original: " + mensajeOriginal);
        System.out.println("Mensaje Cifrado: " + mensajeCifrado);
        System.out.println("Mensaje Descifrado: " + mensajeDescifrado);
    }
}
