package model;

import java.util.*;
/**
 * Clase que representa un potenciador de ataque en el juego de rol.
 * Este item incrementa el ataque de un jugador por una cantidad fija durante varios turnos.
 * Hereda de la clase Item.
 * @author Carlos Altán
 * @since 2025-09-28
 */
public class PotenciadorAtaque extends Item {
    private int incremento;
    private int duracion;
    
    public PotenciadorAtaque() {
        this(1);
    }
    /**
     * Constructor de la clase PotenciadorAtaque con cantidad especificada.
     * @param cantidad Cantidad de potenciadores en el inventario.
     * 
     */
    public PotenciadorAtaque(int cantidad) {
        super("Elixir de Fuerza", "Incrementa el ataque +15 por 3 turnos", cantidad);
        this.incremento = 15;
        this.duracion = 3;
    }
    /**
     * Usa el potenciador de ataque en el objetivo especificado.
     * @param objetivos Lista de combatientes objetivo.
     * @return String Resultado del uso del potenciador.
     */
    @Override
    public String usar(List<Combatiente> objetivos) {
        if (objetivos.size() == 1 && objetivos.get(0) instanceof Jugador) {
            Jugador jugador = (Jugador) objetivos.get(0);
            if (jugador.isEstaVivo()) {
                jugador.modificarAtaque(incremento);
                return String.format("¡%s bebe un Elixir de Fuerza! Su ataque aumenta en %d por %d turnos!", 
                                   jugador.getNombre(), incremento, duracion);
            }
        }
        return "No se pudo usar el elixir.";
    }
}
