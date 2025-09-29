package model;

import java.util.*;
/**
 * Clase BombaHumo que extiende Item.
 * Representa un ítem que puede confundir a los enemigos en batalla.
 * @autor Carlos Altán
 * @since 2025-10-05
 */
public class BombaHumo extends Item {
    private int efectividad;
    /**
     * Constructor de BombaHumo con cantidad por defecto de 1.
     * @param cantidad Cantidad de bombas de humo.
     */
    public BombaHumo() {
        this(1);
    }
    
    /**
     * Constructor de BombaHumo con cantidad especificada.
     * @param cantidad
     */
    public BombaHumo(int cantidad) {
        super("Bomba de Humo", "Confunde a todos los enemigos", cantidad);
        this.efectividad = 75; // 75% de probabilidad de confundir
    }
    /**
     * Usa la Bomba de Humo en una lista de combatientes.
     * @param objetivos Lista de combatientes a los que se les aplicará el efecto.
     * @return Mensaje indicando el resultado del uso del ítem.
     */
    @Override
    public String usar(List<Combatiente> objetivos) {
        StringBuilder resultado = new StringBuilder();
        resultado.append("¡Se lanza una Bomba de Humo!");
        
        Random rand = new Random();
        int enemigosAfectados = 0;
        
        for (Combatiente c : objetivos) {
            if (c.isEstaVivo() && c instanceof Enemigo) {
                if (rand.nextInt(100) < efectividad) {
                    c.aplicarEfecto("Confundido (50% de fallar ataques)");
                    resultado.append(String.format(" %s queda confundido!", c.getNombre()));
                    enemigosAfectados++;
                }
            }
        }
        
        if (enemigosAfectados == 0) {
            resultado.append(" Pero no afecta a ningún enemigo...");
        }
        
        return resultado.toString();
    }
}
