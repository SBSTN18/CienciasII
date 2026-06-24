public class GeneradorPseudoaleatorio {

    //Valores predeterminados para probar la formula, Java usa valores tipo long, para siplificarlo se usan valores enteros
    private static final int MULTIPLICADOR = 7;
    private static final int INCREMENTO = 3;
    private static final int MODULO = 101;
    
    private int semillaActual;

    //El valor inicial (semilla)
    public MiGeneradorPseudoaleatorio(int semillaInicial) {
        this.semillaActual = semillaInicial;
    }

    //Aplica la fórmula matemática para usar la semilla y actualizarla para generar el siguiente número
    public int siguienteNumeroEntero() {
        //Fórmula: (a * X + c) % m   ->  Sale de la formula que usa Java para generar numeros aleatorios
        semillaActual = (MULTIPLICADOR * semillaActual + INCREMENTO) % MODULO;
        return semillaActual;
    }
    
    //Main que usa un ciclo para generar cinco numeros aleatorios por cada una de las diez semillas que se usan
    public static void main(String[] args) {     
        for (int i = 1; i <= 10; i++) {
            //Creamos el generador una sola vez por cada semilla
            MiGeneradorPseudoaleatorio gen = new MiGeneradorPseudoaleatorio(i);
            System.out.print("Con la semilla " + i + " los numeros aleatorios generados son: ");
            System.out.print(gen.siguienteNumeroEntero() + ", ");
            System.out.print(gen.siguienteNumeroEntero() + ", ");
            System.out.print(gen.siguienteNumeroEntero() + ", ");
            System.out.print(gen.siguienteNumeroEntero() + ", ");
            System.out.print(gen.siguienteNumeroEntero());
            System.out.println(); 
        }
    }
}