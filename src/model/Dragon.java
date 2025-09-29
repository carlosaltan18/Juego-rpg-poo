package model;

import java.util.*;

/**
 * Clase Dragon.
 * Representa un enemigo de tipo Dragón en la batalla, con habilidades especiales
 * como Aliento de Fuego y Curación Mística.
 * 
 * @author Carlos Altán
 * @since 2025-09-28
 */
public class Dragon extends Enemigo {
    private int poder;

    /**
     * Constructor de Dragon.
     * Inicializa los atributos del dragón dependiendo de si es un jefe o no.
     * 
     * @param esJefe Indica si el dragón es un jefe.
     */
    public Dragon(boolean esJefe) {
        super(esJefe ? "Dragón Ancestral" : "Dragón Joven", 
              esJefe ? 120 : 90, 
              esJefe ? 30 : 22, 
              "Dragón", esJefe);
        this.poder = esJefe ? 40 : 30;
    }

    /**
     * Habilidad especial del dragón: Aliento de Fuego.
     * Inflige daño en área a todos los objetivos y los deja quemados.
     * 
     * @param objetivos Lista de combatientes afectados por la habilidad.
     * @return Descripción de la habilidad realizada.
     */
    @Override
    public String habilidadEspecial(List<Combatiente> objetivos) {
        if (objetivos.isEmpty()) return "";

        StringBuilder resultado = new StringBuilder();
        resultado.append(String.format("¡%s lanza su ALIENTO DE FUEGO!", nombre));

        int dano = calcularDanoAtaque();
        for (Combatiente c : objetivos) {
            if (c.isEstaVivo()) {
                c.recibirDano(dano);
                c.aplicarEfecto("Quemado (-2 HP por turno)");
                resultado.append(String.format(" %s recibe %d de daño y queda quemado!", 
                               c.getNombre(), dano));
            }
        }

        return resultado.toString();
    }

    /**
     * Habilidad especial del jefe: Curación Mística.
     * El dragón se cura a sí mismo y aplica un efecto de regeneración.
     * 
     * @param objetivos Lista de combatientes (no utilizada en esta habilidad).
     * @return Descripción de la habilidad realizada.
     */
    @Override
    public String habilidadJefe(List<Combatiente> objetivos) {
        int curacion = (int) (vidaMaxima * 0.3);
        curar(curacion);
        aplicarEfecto("Regeneración (+10 HP por turno por 3 turnos)");

        return String.format("¡%s invoca una CURACIÓN MÍSTICA y recupera %d puntos de vida!", 
                           nombre, curacion);
    }
}
