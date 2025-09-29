package controller;

import model.*;
import view.*;
import java.util.*;

/**
 * ControladorJuego.
 * Coordina la lógica del juego, los turnos de jugadores y enemigos,
 * y la interacción con la vista, retornando mensajes en lugar de imprimir directamente.
 * Permite configurar el juego, iniciar la batalla y procesar cada turno.
 * 
 * @author Carlos Altán
 * @since 2025-09-28
 */
public class ControladorJuego {

    /** Vista asociada al controlador */
    protected VistaConsola vista;

    /** Instancia de la batalla actual */
    private Batalla batalla;

    /** Lista de jugadores */
    private List<Jugador> jugadores;

    /** Lista de enemigos */
    private List<Combatiente> enemigos;

    /** Mensajes pendientes para mostrar en la vista */
    private List<String> mensajesPendientes;

    /**
     * Constructor.
     * Inicializa el controlador con la vista y listas vacías de jugadores, enemigos y mensajes.
     * @param vista Vista para interactuar con el usuario
     */
    public ControladorJuego(VistaConsola vista) {
        this.vista = vista;
        this.jugadores = new ArrayList<>();
        this.enemigos = new ArrayList<>();
        this.mensajesPendientes = new ArrayList<>();
    }

    /**
     * Enum para representar el resultado del turno de un jugador.
     */
    public enum TurnoResultado {
        ACCION_COMPLETADA,
        ACCION_CANCELADA,
        HUIR
    }

    /**
     * Devuelve los mensajes pendientes y los limpia.
     * @return Lista de mensajes pendientes
     */
    public List<String> obtenerMensajesPendientes() {
        List<String> mensajes = new ArrayList<>(mensajesPendientes);
        mensajesPendientes.clear();
        return mensajes;
    }

    /**
     * Agrega un mensaje a la lista de mensajes pendientes.
     * @param mensaje Mensaje a agregar
     */
    private void agregarMensaje(String mensaje) {
        mensajesPendientes.add(mensaje);
    }

    /**
     * Configura el juego creando jugadores y enemigos.
     * @param cantidadJugadores Número de jugadores
     * @param nombres Nombres de los jugadores
     * @param roles Roles de los jugadores (1=Guerrero, 2=Explorador)
     * @param opcionCSV 1 si se cargan enemigos desde CSV, 2 si no
     * @param nombreArchivo Nombre del archivo CSV de enemigos (si aplica)
     * @return true si la configuración fue exitosa
     */
    public boolean configurarJuego(int cantidadJugadores, String[] nombres, int[] roles, int opcionCSV, String nombreArchivo) {
        jugadores.clear();
        enemigos.clear();

        // Crear jugadores
        for (int i = 0; i < cantidadJugadores; i++) {
            RolJugador rol = roles[i] == 1 ? RolJugador.GUERRERO : RolJugador.EXPLORADOR;
            String nombre = nombres[i].isEmpty() ? "Héroe " + (i + 1) : nombres[i];
            jugadores.add(new Jugador(nombre, rol));
        }

        // Crear enemigos
        if (opcionCSV == 1) {
            try {
                enemigos = EnemyFactory.cargarEnemigosDesdeCSV(nombreArchivo);
                if (enemigos.isEmpty()) enemigos = EnemyFactory.crearEnemigosAleatorios();
            } catch (Exception e) {
                enemigos = EnemyFactory.crearEnemigosAleatorios();
            }
        } else {
            enemigos = EnemyFactory.crearEnemigosAleatorios();
        }

        batalla = new Batalla(jugadores, enemigos);

        agregarMensaje("\n--- Enemigos para la batalla ---\n");
        for (Combatiente enemigo : enemigos) {
            Enemigo e = (Enemigo) enemigo;
            String tipo = e.isEsJefe() ? "JEFE " + e.getTipo().toUpperCase() : e.getTipo();
            agregarMensaje("• " + enemigo.getNombre() + " (" + tipo + ")\n");
        }

        return true;
    }

    /**
     * Inicia la batalla y agrega el mensaje inicial.
     */
    public void iniciarBatalla() {
        batalla.iniciarBatalla();
        agregarMensaje("\n🗡️  ¡LA BATALLA COMIENZA! ⚔️\n");
    }

    /**
     * Procesa un turno completo de la batalla (jugador o enemigo).
     * @return true si la batalla continúa, false si terminó
     */
    public boolean procesarTurnoBatalla() {
        Combatiente combatienteActual = batalla.getCombatienteActual();

        if (combatienteActual == null || !combatienteActual.isEstaVivo()) {
            batalla.avanzarTurno();
            return true;
        }

        if (combatienteActual instanceof Jugador) {
            TurnoResultado resultado = manejarTurnoJugador((Jugador) combatienteActual);
            if (resultado == TurnoResultado.HUIR) {
                batalla.agregarAccion(combatienteActual.getNombre() + " huye de la batalla!");
                return false;
            }
        } else {
            manejarTurnoEnemigo(combatienteActual);
        }

        batalla.avanzarTurno();
        return !batalla.verificarFinBatalla();
    }

    /**
     * Maneja el turno de un jugador, solicitando acción y ejecutándola.
     * @param jugador Jugador que ejecuta su turno
     * @return TurnoResultado indicando si se completó, canceló o huyó
     */
    private TurnoResultado manejarTurnoJugador(Jugador jugador) {
        boolean accionCompletada = false;
        int intentos = 0;
        final int MAX_INTENTOS = 3;

        while (!accionCompletada && intentos < MAX_INTENTOS) {
            agregarMensaje(vista.obtenerMenuTurno(jugador));
            int accion = vista.leerEntero(0, 5);

            switch (accion) {
                case 0:
                    return TurnoResultado.HUIR;
                case 1:
                    accionCompletada = procesarAtaqueJugador(jugador);
                    break;
                case 2:
                    agregarMensaje("Los jugadores usan ítems como habilidad especial.\n");
                    accionCompletada = true;
                    break;
                case 3:
                    TurnoResultado resultadoItem = manejarUsoItem(jugador);
                    if (resultadoItem == TurnoResultado.ACCION_COMPLETADA) accionCompletada = true;
                    break;
                case 4:
                    batalla.agregarAccion(jugador.getNombre() + " pasa su turno.");
                    accionCompletada = true;
                    break;
                case 5:
                    agregarMensaje(vista.obtenerEstadoDetallado(jugador));
                    continue; // No cuenta como intento
            }
            intentos++;
        }

        if (!accionCompletada) {
            batalla.agregarAccion(jugador.getNombre() + " pierde su turno por indecisión.");
        }

        return accionCompletada ? TurnoResultado.ACCION_COMPLETADA : TurnoResultado.ACCION_CANCELADA;
    }

    /**
     * Procesa un ataque de jugador a un enemigo.
     * @param jugador Jugador que ataca
     * @return true si la acción fue completada
     */
    private boolean procesarAtaqueJugador(Jugador jugador) {
        List<Combatiente> enemigosVivos = batalla.getEnemigosVivos();
        if (enemigosVivos.isEmpty()) {
            agregarMensaje("No hay enemigos disponibles.\n");
            return false;
        }

        agregarMensaje(vista.obtenerListaObjetivos(enemigosVivos));
        int indiceObjetivo = vista.leerEntero(1, enemigosVivos.size()) - 1;

        Combatiente objetivo = enemigosVivos.get(indiceObjetivo);
        String resultado = jugador.atacar(objetivo);
        batalla.agregarAccion(resultado);

        if (!objetivo.isEstaVivo()) {
            batalla.agregarAccion(objetivo.desplegarMensajeMuerte());
        }

        return true;
    }

    /**
     * Maneja el uso de un ítem por un jugador.
     * @param jugador Jugador que usa el ítem
     * @return TurnoResultado indicando si la acción se completó o canceló
     */
    private TurnoResultado manejarUsoItem(Jugador jugador) {
        List<Item> inventario = jugador.getInventario();
        if (inventario.isEmpty()) {
            agregarMensaje("El inventario está vacío.\n");
            return TurnoResultado.ACCION_CANCELADA;
        }

        agregarMensaje(vista.obtenerInventario(inventario));
        int indiceItem = vista.leerEntero(0, inventario.size());
        if (indiceItem == 0) return TurnoResultado.ACCION_CANCELADA;
        indiceItem--;

        Item item = inventario.get(indiceItem);
        List<Combatiente> objetivos = determinarObjetivosItem(item);

        if (objetivos.isEmpty()) return TurnoResultado.ACCION_CANCELADA;

        String resultado = jugador.usarItem(item, objetivos);
        batalla.agregarAccion(resultado);

        return TurnoResultado.ACCION_COMPLETADA;
    }

    /**
     * Determina los objetivos de un ítem según su tipo.
     * @param item Ítem usado
     * @return Lista de combatientes afectados
     */
    private List<Combatiente> determinarObjetivosItem(Item item) {
        List<Combatiente> objetivos = new ArrayList<>();
        if (item instanceof KitCuracion) objetivos.addAll(batalla.getJugadoresVivos());
        else if (item instanceof BombaHumo) objetivos.addAll(batalla.getEnemigosVivos());
        else {
            List<Combatiente> posibles = new ArrayList<>(batalla.getJugadoresVivos());
            if (posibles.size() == 1) objetivos.add(posibles.get(0));
            else {
                agregarMensaje(vista.obtenerListaObjetivos(posibles));
                int indiceObjetivo = vista.leerEntero(1, posibles.size()) - 1;
                objetivos.add(posibles.get(indiceObjetivo));
            }
        }
        return objetivos;
    }

    /**
     * Maneja el turno de un enemigo atacando a los jugadores.
     * @param enemigo Enemigo que ejecuta su turno
     */
    private void manejarTurnoEnemigo(Combatiente enemigo) {
        List<Combatiente> objetivos = new ArrayList<>(batalla.getJugadoresVivos());
        String accion = enemigo.tomarTurno(objetivos);
        if (!accion.isEmpty()) batalla.agregarAccion(accion);

        for (Combatiente jugador : objetivos) {
            if (!jugador.isEstaVivo()) batalla.agregarAccion(jugador.desplegarMensajeMuerte());
        }
    }

    /**
     * Obtiene el resultado final de la batalla incluyendo estado y acciones.
     * @return String con el resumen de la batalla y el resultado
     */
    public String obtenerResultadoFinal() {
        StringBuilder sb = new StringBuilder();
        sb.append(batalla.mostrarEstado());
        sb.append(vista.obtenerRegistroAcciones(batalla.getUltimosRegistros()));

        boolean jugadoresGanaron = !batalla.getJugadoresVivos().isEmpty();
        sb.append(vista.obtenerResultadoBatalla(jugadoresGanaron));

        return sb.toString();
    }

    /**
     * Devuelve la instancia actual de la batalla.
     * @return Objeto Batalla actual
     */
    public Batalla getBatalla() {
        return batalla;
    }
}
