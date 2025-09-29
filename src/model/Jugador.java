package model;

import java.util.*;
/**
 * Clase que representa a un jugador en el juego de rol.
 * Extiende de la clase Combatiente.
 * @author Carlos Altán
 * @since 2025-09-28
 */
public class Jugador extends Combatiente {
    private RolJugador rol;
    private List<Item> inventario;
    private int ataqueModificado;
    private int turnosAtaqueModificado;
    /**
     * Constructor de la clase Jugador.
     * @param nombre 
     * @param rol
    */
    public Jugador(String nombre, RolJugador rol) {
        super(nombre, rol.getVida(), rol.getAtaque());
        this.rol = rol;
        this.inventario = new ArrayList<>(rol.getItems());
        this.ataqueModificado = 0;
        this.turnosAtaqueModificado = 0;
    }
    /**
     * Toma el turno del jugador.
     * @param objetivos Lista de combatientes objetivo.
     * @return String Descripción del turno.
     */
    @Override
    public String tomarTurno(List<Combatiente> objetivos) {
        // El turno del jugador será manejado por el controlador
        return "Esperando acción del jugador...";
    }
    /**
     * Despliega el mensaje de inicio del jugador.
     * @return String Mensaje de inicio.
     * 
     */
    @Override
    public String desplegarMensajeInicio() {
        String mensaje = rol == RolJugador.GUERRERO ? 
            "¡Por el honor y la gloria! ¡A la batalla!" :
            "¡Con astucia y destreza venceré!";
        return String.format("%s (%s): %s", nombre, rol.name().toLowerCase(), mensaje);
    }
    /**
     * Despliega el mensaje de muerte del jugador.
     * @return String Mensaje de muerte.
     */
    @Override
    public String desplegarMensajeMuerte() {
        return String.format("%s ha caído en batalla... '¡Vengadme!'", nombre);
    }
    /**
     * Habilidad especial del jugador (uso de ítems).
     * @return String Descripción de la habilidad especial.
     */
    @Override
    public String habilidadEspecial(List<Combatiente> objetivos) {
        return "El jugador puede usar ítems como habilidad especial.";
    }
    /**
     * Usa un ítem del inventario en los objetivos especificados.
     * @param item 
     * @param objetivos
     * @return String Resultado del uso del ítem.
     */
    public String usarItem(Item item, List<Combatiente> objetivos) {
        if (!inventario.contains(item) || item.getCantidad() <= 0) {
            return "No tienes ese ítem disponible.";
        }
        
        String resultado = item.usar(objetivos);
        item.reducirCantidad();
        
        if (item.getCantidad() <= 0) {
            inventario.remove(item);
        }
        
        return resultado;
    }
    /**
     * Agrega un ítem al inventario.
     * @param item Ítem a agregar.
     */
    public void agregarItem(Item item) {
        inventario.add(item);
    }
    /**
     * Obtiene una copia del inventario del jugador.
     * @return List<Item> Copia del inventario.
     */
    public List<Item> getInventario() {
        return new ArrayList<>(inventario);
    }
    /**
     * Actualiza los efectos temporales del jugador al inicio de su turno.
     *  @return void
     */
    public void actualizarEfectosTurnos() {
        if (turnosAtaqueModificado > 0) {
            turnosAtaqueModificado--;
            if (turnosAtaqueModificado <= 0) {
                ataqueModificado = 0;
                efectosActivos.removeIf(efecto -> efecto.contains("Ataque potenciado"));
            }
        }
    }
    /**
     * Calcula el daño de ataque del jugador, considerando modificadores temporales.
     * @return int Daño de ataque.
     * 
     */
    @Override
    protected int calcularDanoAtaque() {
        int ataqueTotal = ataqueBase + ataqueModificado;
        Random rand = new Random();
        double factor = 0.9 + (rand.nextDouble() * 0.2);
        return (int) (ataqueTotal * factor);
    }
    /**
     * Modifica el ataque del jugador temporalmente.
     * @param modificador Cantidad a modificar.
     * @return void
     */
    @Override
    public void modificarAtaque(int modificador) {
        this.ataqueModificado += modificador;
        this.turnosAtaqueModificado = 3; // Dura 3 turnos
        aplicarEfecto("Ataque potenciado: +" + modificador + " por " + turnosAtaqueModificado + " turnos");
    }
    /**
     * Obtiene el rol del jugador.
     * 
     * @return RolJugador Rol del jugador.
     */
    public RolJugador getRol() { return rol; }
}
