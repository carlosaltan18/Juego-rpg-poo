package model;

import java.util.*;
/**
 * Clase que representa un orco en el juego de rol.
 * Puede ser un enemigo normal o un jefe con habilidades especiales.
 * Hereda de la clase Enemigo.
 * @author Carlos Altán
 * @since 2025-09-28
 */
public class Orco extends Enemigo {
    private int fuerza;
    /**
     * Constructor de la clase Orco.
     * @param esJefe
     */
    public Orco(boolean esJefe) {
        super(esJefe ? "Orco Señor de Guerra" : "Orco Guerrero", 
              esJefe ? 80 : 60, 
              esJefe ? 20 : 15, 
              "Orco", esJefe);
        this.fuerza = esJefe ? 25 : 18;
    }
    /**
     * Habilidad especial del orco.
     * Si es un jefe, usa una habilidad diferente.
     * @param objetivos Lista de combatientes objetivo.
     * @return String Resultado de la habilidad especial.
     */
    @Override
    public String habilidadEspecial(List<Combatiente> objetivos) {
        if (objetivos.isEmpty()) return "";
        
        Random rand = new Random();
        Combatiente objetivo = objetivos.get(rand.nextInt(objetivos.size()));
        
        int dano = (int) (calcularDanoAtaque() * 1.5);
        objetivo.recibirDano(dano);
        
        return String.format("¡%s usa GOLPE BRUTAL contra %s causando %d de daño devastador!", 
                           nombre, objetivo.getNombre(), dano);
    }
    /**
     *  Habilidad de jefe del orco.
     *  Debilita a todos los enemigos.
     * @param objetivos Lista de combatientes objetivo.
     * @return String Resultado de la habilidad de jefe.
     */
    @Override
    public String habilidadJefe(List<Combatiente> objetivos) {
        if (objetivos.isEmpty()) return "";
        
        StringBuilder resultado = new StringBuilder();
        resultado.append(String.format("¡%s lanza un RUGIDO INTIMIDANTE!", nombre));
        
        for (Combatiente c : objetivos) {
            if (c.isEstaVivo() && c instanceof Jugador) {
                c.aplicarEfecto("Intimidado (-5 ATK por 2 turnos)");
                resultado.append(String.format(" %s se siente intimidado!", c.getNombre()));
            }
        }
        
        return resultado.toString();
    }
}

