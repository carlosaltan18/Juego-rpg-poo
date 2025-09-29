package model;

import java.util.*;

/**
 * Clase Batalla.
 * Representa una batalla entre jugadores y enemigos, gestionando turnos, acciones,
 * efectos y el estado general de la batalla.
 * 
 * @author Carlos Altán
 * @since 2025-09-28
 */
public class Batalla {
    private List<Jugador> jugadores;
    private List<Combatiente> enemigos;
    private int turnoActual;
    private List<String> registroAcciones;
    private boolean batallaActiva;
    private List<Combatiente> ordenTurnos;
    private int indiceTurnoActual;

    /**
     * Constructor de Batalla.
     * Inicializa la batalla con los jugadores y enemigos proporcionados.
     * 
     * @param jugadores Lista de jugadores que participan en la batalla.
     * @param enemigos Lista de enemigos que participan en la batalla.
     */
    public Batalla(List<Jugador> jugadores, List<Combatiente> enemigos) {
        this.jugadores = new ArrayList<>(jugadores);
        this.enemigos = new ArrayList<>(enemigos);
        this.turnoActual = 0;
        this.registroAcciones = new ArrayList<>();
        this.batallaActiva = true;
        this.ordenTurnos = new ArrayList<>();
        this.indiceTurnoActual = 0;
        determinarOrdenTurnos();
    }

    /**
     * Inicia la batalla, mostrando mensajes de inicio y determinando el orden de turnos.
     */
    public void iniciarBatalla() {
        agregarAccion("=== ¡LA BATALLA COMIENZA! ===");
        jugadores.forEach(j -> agregarAccion(j.desplegarMensajeInicio()));
        enemigos.forEach(e -> agregarAccion(e.desplegarMensajeInicio()));
        agregarAccion("=== ORDEN DE TURNOS DETERMINADO ===");
    }

    /**
     * Obtiene el combatiente que tiene el turno actual.
     * 
     * @return Combatiente actual o null si no hay combatientes en el orden de turnos.
     */
    public Combatiente getCombatienteActual() {
        if (ordenTurnos.isEmpty()) return null;
        return ordenTurnos.get(indiceTurnoActual % ordenTurnos.size());
    }

    /**
     * Avanza al siguiente turno, actualizando el índice y limpiando efectos temporales.
     */
    public void avanzarTurno() {
        indiceTurnoActual++;
        if (indiceTurnoActual >= ordenTurnos.size()) {
            indiceTurnoActual = 0;
            turnoActual++;
            limpiarEfectosTurnos();
        }
        ordenTurnos.removeIf(c -> !c.isEstaVivo());
        if (indiceTurnoActual >= ordenTurnos.size() && !ordenTurnos.isEmpty()) {
            indiceTurnoActual = 0;
        }
    }

    /**
     * Verifica si la batalla ha terminado.
     * 
     * @return true si todos los jugadores o enemigos han caído, false en caso contrario.
     */
    public boolean verificarFinBatalla() {
        boolean jugadoresVivos = jugadores.stream().anyMatch(Combatiente::isEstaVivo);
        boolean enemigosVivos = enemigos.stream().anyMatch(Combatiente::isEstaVivo);

        if (!jugadoresVivos) {
            agregarAccion("=== ¡DERROTA! Todos los jugadores han caído ===");
            batallaActiva = false;
            return true;
        }

        if (!enemigosVivos) {
            agregarAccion("=== ¡VICTORIA! Todos los enemigos han sido derrotados ===");
            batallaActiva = false;
            return true;
        }

        return false;
    }

    /**
     * Agrega una acción al registro de la batalla.
     * 
     * @param accion Descripción de la acción realizada.
     */
    public void agregarAccion(String accion) {
        registroAcciones.add("Turno " + (turnoActual + 1) + ": " + accion);
        if (registroAcciones.size() > 10) {
            registroAcciones.remove(0);
        }
    }

    /**
     * Muestra el estado actual de la batalla, incluyendo jugadores y enemigos.
     * 
     * @return Cadena con el estado de la batalla.
     */
    public String mostrarEstado() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== ESTADO DE LA BATALLA ===\n");
        sb.append("Turno: ").append(turnoActual + 1).append("\n\n");
        sb.append("JUGADORES:\n");
        jugadores.forEach(j -> sb.append("  ").append(j.toString()).append("\n"));
        sb.append("\nENEMIGOS:\n");
        enemigos.forEach(e -> sb.append("  ").append(e.toString()).append("\n"));
        return sb.toString();
    }

    /**
     * Obtiene los últimos registros de acciones realizadas en la batalla.
     * 
     * @return Lista de las últimas acciones registradas.
     */
    public List<String> getUltimosRegistros() {
        return new ArrayList<>(registroAcciones.subList(
            Math.max(0, registroAcciones.size() - 3), registroAcciones.size()));
    }

    /**
     * Obtiene una lista de jugadores vivos.
     * 
     * @return Lista de jugadores que aún están vivos.
     */
    public List<Jugador> getJugadoresVivos() {
        return jugadores.stream()
                       .filter(Combatiente::isEstaVivo)
                       .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Obtiene una lista de enemigos vivos.
     * 
     * @return Lista de enemigos que aún están vivos.
     */
    public List<Combatiente> getEnemigosVivos() {
        return enemigos.stream()
                      .filter(Combatiente::isEstaVivo)
                      .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Determina el orden de turnos de los combatientes.
     * Los turnos se asignan aleatoriamente para mayor variedad.
     */
    private void determinarOrdenTurnos() {
        ordenTurnos.addAll(jugadores);
        ordenTurnos.addAll(enemigos);
        Collections.shuffle(ordenTurnos);
    }

    /**
     * Limpia los efectos temporales de los combatientes al final de un turno.
     */
    private void limpiarEfectosTurnos() {
        jugadores.forEach(Jugador::actualizarEfectosTurnos);
        ordenTurnos.forEach(this::procesarEfectosContinuos);
    }

    /**
     * Procesa efectos continuos como daño o regeneración en un combatiente.
     * 
     * @param c Combatiente al que se aplican los efectos.
     */
    private void procesarEfectosContinuos(Combatiente c) {
        List<String> efectos = new ArrayList<>(c.getEfectosActivos());
        for (String efecto : efectos) {
            if (efecto.contains("Quemado")) {
                c.recibirDano(2);
                agregarAccion(c.getNombre() + " recibe 2 de daño por estar quemado");
            } else if (efecto.contains("Regeneración")) {
                c.curar(10);
                agregarAccion(c.getNombre() + " se regenera 10 HP");
            }
        }
    }

    // Getters
    public boolean isBatallaActiva() { return batallaActiva; }
    public int getTurnoActual() { return turnoActual; }
    public List<Jugador> getJugadores() { return new ArrayList<>(jugadores); }
    public List<Combatiente> getEnemigos() { return new ArrayList<>(enemigos); }
    public List<Combatiente> getOrdenTurnos() { return new ArrayList<>(ordenTurnos); }
}
