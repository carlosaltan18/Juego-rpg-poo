package view;

import model.*;
import java.util.*;

/**
 * Clase VistaConsola.
 * Maneja la interacción con el usuario mediante la consola.
 * Proporciona menús, mensajes de estado, lectura de datos y entradas validadas.
 * Evita errores de entrada usando únicamente nextLine() para capturar todo.
 * 
 * @author Carlos Altán
 * @since 2025-09-28
 */
public class VistaConsola {

    /** Scanner para leer la entrada del usuario */
    private Scanner scanner;

    /**
     * Constructor de VistaConsola.
     * Inicializa el Scanner para lectura de datos por consola.
     */
    public VistaConsola() {
        scanner = new Scanner(System.in);
    }

    /** --- MENÚS Y SALIDAS --- **/

    /**
     * Muestra el menú principal del juego.
     * @return String con el menú principal
     */
    public String obtenerMenu() {
        return "\n╔═══════════════════════════════════════╗\n" +
               "║          JUEGO DE BATALLA RPG         ║\n" +
               "╠═══════════════════════════════════════╣\n" +
               "║  1. Iniciar Nueva Batalla             ║\n" +
               "║  2. Instrucciones CSV                 ║\n" +
               "║  3. Salir                             ║\n" +
               "╚═══════════════════════════════════════╝\n" +
               "Selecciona una opción: ";
    }

    /**
     * Muestra el menú de turno de un combatiente.
     * @param combatiente Combatiente cuyo turno es
     * @return String con las opciones disponibles
     */
    public String obtenerMenuTurno(Combatiente combatiente) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Turno de ").append(combatiente.getNombre()).append(" ---\n");
        sb.append("1. Atacar\n2. Usar habilidad especial\n");
        if (combatiente instanceof Jugador) {
            sb.append("3. Usar ítem\n4. Pasar turno\n5. Ver estado detallado\n0. Huir de la batalla\n");
        } else {
            sb.append("3. Pasar turno\n");
        }
        sb.append("¿Qué acción tomar? ");
        return sb.toString();
    }

    /**
     * Muestra la lista de posibles objetivos de ataque o habilidades.
     * @param objetivos Lista de combatientes disponibles
     * @return String con los combatientes numerados y su estado de vida
     */
    public String obtenerListaObjetivos(List<Combatiente> objetivos) {
        if (objetivos.isEmpty()) return "No hay objetivos disponibles.\n";
        StringBuilder sb = new StringBuilder("\n--- Seleccionar Objetivo ---\n");
        for (int i = 0; i < objetivos.size(); i++) {
            Combatiente c = objetivos.get(i);
            String estado = c.isEstaVivo() ? c.getVidaActual() + "/" + c.getVidaMaxima() : "MUERTO";
            sb.append((i + 1) + ". " + c.getNombre() + " - HP: " + estado + "\n");
        }
        sb.append("Selecciona objetivo (1-" + objetivos.size() + "): ");
        return sb.toString();
    }

    /**
     * Muestra el inventario de un jugador.
     * @param items Lista de ítems en el inventario
     * @return String con los ítems numerados y opción de cancelar
     */
    public String obtenerInventario(List<Item> items) {
        if (items.isEmpty()) return "El inventario está vacío.\n";
        StringBuilder sb = new StringBuilder("\n--- Inventario ---\n");
        for (int i = 0; i < items.size(); i++) sb.append((i + 1) + ". " + items.get(i) + "\n");
        sb.append("0. Cancelar\nSelecciona ítem (0-" + items.size() + "): ");
        return sb.toString();
    }

    /**
     * Muestra un registro de acciones recientes de la batalla.
     * @param acciones Lista de cadenas con las acciones realizadas
     * @return String formateado con las acciones
     */
    public String obtenerRegistroAcciones(List<String> acciones) {
        StringBuilder sb = new StringBuilder("\n--- Últimas Acciones ---\n");
        for (String accion : acciones) sb.append("• " + accion + "\n");
        return sb.toString();
    }

    /**
     * Muestra el resultado final de la batalla.
     * @param ganaron true si los jugadores ganaron, false si perdieron
     * @return String con mensaje de victoria o derrota
     */
    public String obtenerResultadoBatalla(boolean ganaron) {
        return ganaron ? "\n🎉 ¡VICTORIA! 🎉\n" :
                         "\n💀 DERROTA 💀\n";
    }

    /**
     * Muestra la selección de rol de jugador.
     * @return String con la opción de roles disponibles
     */
    public String obtenerRoles() {
        return "\n--- Selección de Rol ---\n1. GUERRERO\n2. EXPLORADOR\nSelecciona rol (1-2): ";
    }

    public String obtenerPreguntaJugadores() { return "¿Cuántos jugadores participarán? (1-2): "; }
    public String obtenerPreguntaNombreJugador(int n) { return "Ingresa el nombre del Jugador " + n + ": "; }
    public String obtenerPreguntaCSV() { return "¿Deseas cargar enemigos desde CSV? (1-Sí / 2-No): "; }
    public String obtenerPreguntaNombreArchivo() { return "Ingresa el nombre del archivo CSV: "; }
    public String obtenerMensajeContinuar() { return "\nPresiona Enter para continuar..."; }

    /** --- MÉTODOS DE LECTURA --- **/

    /**
     * Lee una línea de texto desde la consola.
     * @return String ingresado por el usuario
     */
    public String leerLinea() { return scanner.nextLine().trim(); }

    /**
     * Lee un número entero dentro de un rango usando solo nextLine().
     * Valida que el número esté dentro del rango especificado.
     * @param min Valor mínimo permitido
     * @param max Valor máximo permitido
     * @return Número entero válido ingresado por el usuario
     */
    public int leerEntero(int min, int max) {
    int numero = -1;
    while (true) {
        String linea = "";
        try {
            linea = scanner.nextLine();
            if (linea.isBlank()) {
                System.out.println("Entrada vacía. Ingresa un número válido.");
                continue;
            }
            numero = Integer.parseInt(linea.trim());
            if (numero < min || numero > max) {
                System.out.println("Entrada inválida. Ingresa un número entre " + min + " y " + max);
                continue;
            }
            return numero;
        } catch (NoSuchElementException e) {
            System.out.println("No se encontró línea de entrada. Reintentando...");
            scanner = new Scanner(System.in); // re-inicializa Scanner
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Ingresa un número válido.");
        }
    }
}


    /** 
     * Espera a que el usuario presione Enter.
     */
    public void esperarEnter() { scanner.nextLine(); }

    /**
     * Muestra el estado detallado de un jugador, incluyendo vida, rol e inventario.
     * @param jugador Jugador cuyo estado se mostrará
     * @return String con información completa del jugador
     */
    public String obtenerEstadoDetallado(Jugador jugador) {
        StringBuilder sb = new StringBuilder("\n--- Estado de " + jugador.getNombre() + " ---\n");
        sb.append("Vida: ").append(jugador.getVidaActual()).append("/").append(jugador.getVidaMaxima()).append("\n");
        sb.append("Rol: ").append(jugador.getRol()).append("\nInventario: ");
        if (jugador.getInventario().isEmpty()) sb.append("Vacío\n");
        else {
            for (Item item : jugador.getInventario()) sb.append(item + ", ");
            sb.setLength(sb.length()-2);
            sb.append("\n");
        }
        return sb.toString();
    }
}
