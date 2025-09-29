package model;

import java.util.*;
/**
 * Clase que representa un kit de curación grupal en el juego de rol.
 * Este kit cura a todos los aliados vivos una cantidad fija de puntos de vida.
 * Hereda de la clase Item.
 * @author Carlos Altán
 * @since 2025-09-28
 * 
 */
public class KitCuracion extends Item {
    private int curacionGrupo;
    /**
     * Constructor de la clase KitCuracion.
     * @param cantidad Cantidad de kits en el inventario.
     */
    public KitCuracion() {
        this(1);
    }
    /**
     * Constructor de la clase KitCuracion con cantidad especificada.
     * @param cantidad Cantidad de kits en el inventario.
     * 
     */
    public KitCuracion(int cantidad) {
        super("Kit de Curación Grupal", "Cura 20 HP a todos los aliados", cantidad);
        this.curacionGrupo = 20;
    }
    /**
     * Usa el kit de curación en los objetivos especificados.
     * @param objetivos Lista de combatientes objetivo.
     * @return String Resultado del uso del kit.
     */
    @Override
    public String usar(List<Combatiente> objetivos) {
        StringBuilder resultado = new StringBuilder();
        resultado.append("¡Se usa un Kit de Curación Grupal!");
        
        int aliadosCurados = 0;
        for (Combatiente c : objetivos) {
            if (c.isEstaVivo() && c instanceof Jugador) {
                int vidaAnterior = c.getVidaActual();
                c.curar(curacionGrupo);
                int vidaCurada = c.getVidaActual() - vidaAnterior;
                if (vidaCurada > 0) {
                    resultado.append(String.format(" %s recupera %d HP!", c.getNombre(), vidaCurada));
                    aliadosCurados++;
                }
            }
        }
        
        if (aliadosCurados == 0) {
            resultado.append(" Pero no hay aliados que curar...");
        }
        
        return resultado.toString();
    }
}
