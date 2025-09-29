package model;

import java.util.*;
/**
 * Clase que representa a un Goblin en el juego de rol.
 * Extiende de la clase Enemigo.
 * @author Carlos Altán
 * @since 2025-09-28
 */
public class Goblin extends Enemigo {
    private int velocidad;
    /**
     *  Constructor de Goblin.
     * @param esJefe Indica si el goblin es un jefe o no.
     */
    public Goblin(boolean esJefe) {
        super(esJefe ? "Goblin Rey Ladrón" : "Goblin Explorador", 
              esJefe ? 50 : 35, 
              esJefe ? 18 : 12, 
              "Goblin", esJefe);
        this.velocidad = esJefe ? 30 : 22;
    }
    /**
     * Habilidad especial del Goblin.
     * @param objetivos Lista de combatientes objetivo (no se usa en esta habilidad).
     * @return Descripción de la acción realizada.
     */
    @Override
    public String habilidadEspecial(List<Combatiente> objetivos) {
        // Esquive Ágil - evita el próximo ataque
        aplicarEfecto("Esquivando (próximo ataque fallará)");
        return String.format("¡%s se mueve ágilmente y entra en posición de ESQUIVE!", nombre);
    }
    /**
     * Habilidad especial del Goblin Jefe.
     * @param objetivos Lista de combatientes objetivo.
     * @return Descripción de la acción realizada.
     * 
     */
    @Override
    public String habilidadJefe(List<Combatiente> objetivos) {
        if (objetivos.isEmpty()) return "";
        
        StringBuilder resultado = new StringBuilder();
        resultado.append(String.format("¡%s realiza un ATAQUE MÚLTIPLE!", nombre));
        
        for (Combatiente c : objetivos) {
            if (c.isEstaVivo()) {
                int dano = (int) (calcularDanoAtaque() * 0.7); 
                c.recibirDano(dano);
                resultado.append(String.format(" Golpea a %s por %d!", c.getNombre(), dano));
            }
        }
        
        return resultado.toString();
    }
}

