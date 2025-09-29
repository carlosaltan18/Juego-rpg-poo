import controller.ControladorJuego;
import view.VistaConsola;

/**
 * Clase principal del Juego de Batalla RPG.
 * Esta clase inicializa el juego, configura los jugadores y enemigos,
 * y controla el flujo de la batalla.
 * 
 * @author Carlos Altán
 * @since 2025-09-28
 */
public class Principal {

    /**
     * Método principal que ejecuta el juego.
     * 
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        VistaConsola vista = new VistaConsola();
        ControladorJuego controlador = new ControladorJuego(vista);

        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║              JUEGO DE BATALLA RPG                      ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        System.out.print(vista.obtenerPreguntaJugadores());
        int cantidadJugadores = vista.leerEntero(1, 2);

        String[] nombres = new String[cantidadJugadores];
        int[] roles = new int[cantidadJugadores];

        for (int i = 0; i < cantidadJugadores; i++) {
            System.out.print(vista.obtenerPreguntaNombreJugador(i + 1));
            nombres[i] = vista.leerLinea();

            System.out.print(vista.obtenerRoles());
            roles[i] = vista.leerEntero(1, 2);
        }

        System.out.print(vista.obtenerPreguntaCSV());
        int opcionCSV = vista.leerEntero(1, 2);
        String nombreArchivo = "";
        if (opcionCSV == 1) {
            System.out.print(vista.obtenerPreguntaNombreArchivo());
            nombreArchivo = vista.leerLinea();
        }

        controlador.configurarJuego(cantidadJugadores, nombres, roles, opcionCSV, nombreArchivo);

        controlador.iniciarBatalla();

        boolean batallaActiva = true;
        while (batallaActiva) {
            batallaActiva = controlador.procesarTurnoBatalla();

            for (String msg : controlador.obtenerMensajesPendientes()) {
                System.out.println(msg);
            }

            if (batallaActiva) {
                System.out.print(vista.obtenerMensajeContinuar());
                vista.esperarEnter();
            }
        }

        System.out.println(controlador.obtenerResultadoFinal());

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              ¡GRACIAS POR JUGAR!                       ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}
