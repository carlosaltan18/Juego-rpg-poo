package model;

import java.util.*;
/**
 * Clase abstracta Enemigo.
 * Representa un enemigo en la batalla, con atributos y comportamientos
 * específicos para enemigos, incluyendo habilidades especiales y
 * diferenciación entre jefes y enemigos comunes.
 * @author Carlos Altán
 * @since 2025-09-28
 */
public abstract class Enemigo extends Combatiente {
    protected String tipo;
    protected boolean esJefe;
    /**
     * Constructor de Enemigo.
     * @param nombre
     * @param vida
     * @param ataque
     * @param tipo
     * @param esJefe
     */
    public Enemigo(String nombre, int vida, int ataque, String tipo, boolean esJefe) {
        super(nombre, vida, ataque);
        this.tipo = tipo;
        this.esJefe = esJefe;
        
        if (esJefe) {
            // Los jefes son más poderosos
            this.vidaMaxima = (int) (vida * 1.5);
            this.vidaActual = this.vidaMaxima;
            this.ataqueBase = (int) (ataque * 1.3);
        }
    }
    /**
     * Habilidad especial del enemigo.
     * @param objetivos
     * @return
     */
    public abstract String habilidadJefe(List<Combatiente> objetivos);
    /**
     * Toma el turno del enemigo.
     * @param objetivos
     * @return Acción realizada.
     */
    @Override
    public String tomarTurno(List<Combatiente> objetivos) {
        if (!estaVivo || objetivos.isEmpty()) {
            return "";
        }
        
        Random rand = new Random();
        List<Combatiente> objetivosVivos = new ArrayList<>();
        for (Combatiente c : objetivos) {
            if (c.isEstaVivo()) {
                objetivosVivos.add(c);
            }
        }
        
        if (objetivosVivos.isEmpty()) {
            return "";
        }
        
        int accion = rand.nextInt(100);
        
        if (accion < 60) {
            
            Combatiente objetivo = objetivosVivos.get(rand.nextInt(objetivosVivos.size()));
            return atacar(objetivo);
        } else if (accion < 85) {
            return habilidadEspecial(objetivosVivos);
        } else if (esJefe && accion < 95) {
            return habilidadJefe(objetivosVivos);
        } else {
            return nombre + " observa el campo de batalla...";
        }
    }
    /**
     * Despliega el mensaje de inicio del enemigo.
     * @return Mensaje de inicio.
     */
    
    @Override
    public String desplegarMensajeInicio() {
        String prefijo = esJefe ? "¡EL PODEROSO " : "¡";
        return prefijo + nombre.toUpperCase() + " APARECE EN BATALLA!";
    }
    /**
     * Despliega el mensaje de muerte del enemigo.
     * @return Mensaje de muerte.
     */
    @Override
    public String desplegarMensajeMuerte() {
        String mensaje = esJefe ? 
            "¡El temible jefe " + nombre + " ha sido derrotado!" :
            nombre + " cae derrotado...";
        return mensaje;
    }
    /**
     * Calcula el daño de ataque del enemigo.
     * @return Daño calculado.
     */
    @Override
    protected int calcularDanoAtaque() {
        int danoBase = super.calcularDanoAtaque();
        return esJefe ? (int) (danoBase * 1.2) : danoBase; // Jefes hacen 20% más daño
    }
    /**
     * Recibe daño, con reducción si es jefe.
     * @param dano Daño recibido.
     */
    @Override
    public void recibirDano(int dano) {
        if (esJefe) {
            dano = (int) (dano * 0.9); // Jefes reciben 10% menos daño
        }
        super.recibirDano(dano);
    }
    
    public String getTipo() { return tipo; }
    public boolean isEsJefe() { return esJefe; }
}

