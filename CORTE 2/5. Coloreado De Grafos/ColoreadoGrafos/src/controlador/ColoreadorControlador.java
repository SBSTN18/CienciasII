package controlador;

import modelo.AlgoritmoDSatur;
import modelo.AlgoritmoVoraz;
import modelo.AlgoritmoWelshPowell;
import modelo.Grafo;
import modelo.IAlgoritmoColoreado;
import modelo.ResultadoColoreado;
import vista.VistaConsola;

/**
 * Orquesta el flujo del menu, valida reglas de negocio y coordina
 * vista y modelo sin que ninguna de las dos sepa de la otra.
 */
public class ColoreadorControlador {
    private final VistaConsola vista;
    private final IAlgoritmoColoreado voraz;
    private final IAlgoritmoColoreado welshPowell;
    private final IAlgoritmoColoreado dSatur;

    private Grafo grafo;
    private ResultadoColoreado resultadoVoraz;
    private ResultadoColoreado resultadoWelsh;
    private ResultadoColoreado resultadoDSatur;

    public ColoreadorControlador(VistaConsola vista) {
        this.vista = vista;
        this.voraz = new AlgoritmoVoraz();
        this.welshPowell = new AlgoritmoWelshPowell();
        this.dSatur = new AlgoritmoDSatur();
    }

    public void iniciar() {
        boolean continuar = true;
        while (continuar) {
            vista.mostrarMenu();
            int opcion = vista.pedirEntero("");
            continuar = procesarOpcion(opcion);
        }
        vista.mostrarMensaje("Programa finalizado.");
    }

    private boolean procesarOpcion(int opcion) {
        switch (opcion) {
            case 1: procesarIngresarGrafo();    break;
            case 2: procesarMostrarGrafo();     break;
            case 3: procesarEjecutar(voraz, "Voraz");          break;
            case 4: procesarEjecutar(welshPowell, "Welsh-Powell"); break;
            case 5: procesarEjecutar(dSatur, "D-Satur");       break;
            case 6: procesarComparar();         break;
            case 0: return false;
            default: vista.mostrarError("Opcion invalida.");
        }
        return true;
    }

    private void procesarIngresarGrafo() {
        int n = leerNumVerticesValido();
        boolean dirigido = vista.pedirBooleano("Es un grafo dirigido?");
        grafo = new Grafo(n, dirigido);

        int[][] aristas = vista.pedirAristas(n);
        for (int[] arista : aristas) {
            if (grafo.existeArista(arista[0], arista[1])) {
                vista.mostrarError("La arista ya existe, se omite.");
            } else {
                grafo.agregarArista(arista[0], arista[1]);
            }
        }

        // Limpiar resultados anteriores al ingresar un grafo nuevo
        resultadoVoraz = null;
        resultadoWelsh = null;
        resultadoDSatur = null;

        vista.mostrarMensaje("Grafo creado con " + n + " vertices.");
    }

    private void procesarMostrarGrafo() {
        if (!grafoDisponible()) return;
        vista.mostrarGrafo(grafo);
    }

    private void procesarEjecutar(IAlgoritmoColoreado algoritmo, String nombre) {
        if (!grafoDisponible()) return;
        ResultadoColoreado resultado = algoritmo.ejecutar(grafo);

        // Guardar para comparacion posterior
        if (nombre.equals("Voraz"))        resultadoVoraz = resultado;
        if (nombre.equals("Welsh-Powell")) resultadoWelsh = resultado;
        if (nombre.equals("D-Satur"))      resultadoDSatur = resultado;

        vista.mostrarResultado(resultado, grafo);
    }

    private void procesarComparar() {
        if (!grafoDisponible()) return;

        // Ejecutar los que no se han ejecutado aun
        if (resultadoVoraz == null)  resultadoVoraz  = voraz.ejecutar(grafo);
        if (resultadoWelsh == null)  resultadoWelsh  = welshPowell.ejecutar(grafo);
        if (resultadoDSatur == null) resultadoDSatur = dSatur.ejecutar(grafo);

        vista.mostrarComparacion(resultadoVoraz, resultadoWelsh, resultadoDSatur);
    }

    private int leerNumVerticesValido() {
        int n = vista.pedirEntero("Ingrese el numero de vertices (minimo 2, maximo 26): ");
        while (n < 2 || n > 26) {
            vista.mostrarError("El numero de vertices debe estar entre 2 y 26.");
            n = vista.pedirEntero("Ingrese el numero de vertices (minimo 2, maximo 26): ");
        }
        return n;
    }

    private boolean grafoDisponible() {
        if (grafo == null) {
            vista.mostrarError("Primero debe ingresar un grafo (opcion 1).");
            return false;
        }
        return true;
    }
}
