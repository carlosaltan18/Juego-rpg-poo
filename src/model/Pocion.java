package model;

import java.util.*;
/**
 * Clase que representa una poción en el juego de rol.
 * Esta poción restaura una cantidad fija de puntos de vida a un solo objetivo.
 * Hereda de la clase Item.
 * @author Carlos Altán
 * @since 2025-09-28
 */
public class Pocion extends Item {
    private int curacion;
    
    public Pocion() {
        this(1);
    }
    /**
     * Constructor de la clase Pocion con cantidad especificada.
     * @param cantidad
     */
    public Pocion(int cantidad) {
        super("Poción de Vida", "Restaura 30 puntos de vida", cantidad);
        this.curacion = 30;
    }
    /**
     * Usa la poción en el objetivo especificado.
     * @param objetivos Lista de combatientes objetivo.
     * @return String Resultado del uso de la poción.
     */
    @Override
    public String usar(List<Combatiente> objetivos) {
        if (objetivos.size() == 1) {
            Combatiente objetivo = objetivos.get(0);
            if (objetivo.isEstaVivo()) {
                int vidaAnterior = objetivo.getVidaActual();
                objetivo.curar(curacion);
                int vidaCurada = objetivo.getVidaActual() - vidaAnterior;
                return String.format("¡%s usa una Poción de Vida y recupera %d puntos de vida!", 
                                   objetivo.getNombre(), vidaCurada);
            }
        }
        return "No se pudo usar la poción.";
    }
}
