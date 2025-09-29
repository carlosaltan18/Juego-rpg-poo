package model;

import java.util.*;

/**
 * Clase abstracta Combatiente.
 * Representa un combatiente en la batalla, ya sea un jugador o un enemigo.
 * Define atributos y métodos comunes para todos los combatientes.
 * 
 * @author Carlos Altán
 * @since 2025-09-28
 */
public abstract class Combatiente {
    protected String nombre;
    protected int vidaMaxima;
    protected int vidaActual;
    protected int ataqueBase;
    protected boolean estaVivo;
    protected List<String> efectosActivos;

    /**
     * Constructor de Combatiente.
     * Inicializa los atributos básicos del combatiente.
     * 
     * @param nombre Nombre del combatiente.
     * @param vida Vida máxima del combatiente.
     * @param ataque Ataque base del combatiente.
     */
    public Combatiente(String nombre, int vida, int ataque) {
        this.nombre = nombre;
        this.vidaMaxima = vida;
        this.vidaActual = vida;
        this.ataqueBase = ataque;
        this.estaVivo = true;
        this.efectosActivos = new ArrayList<>();
    }

    /**
     * Método abstracto para que el combatiente tome su turno.
     * 
     * @param objetivos Lista de objetivos disponibles.
     * @return Descripción de la acción realizada.
     */
    public abstract String tomarTurno(List<Combatiente> objetivos);

    /**
     * Método abstracto para mostrar el mensaje de inicio del combatiente.
     * 
     * @return Mensaje de inicio.
     */
    public abstract String desplegarMensajeInicio();

    /**
     * Método abstracto para mostrar el mensaje de muerte del combatiente.
     * 
     * @return Mensaje de muerte.
     */
    public abstract String desplegarMensajeMuerte();

    /**
     * Método abstracto para realizar una habilidad especial.
     * 
     * @param objetivos Lista de objetivos disponibles.
     * @return Descripción de la habilidad especial realizada.
     */
    public abstract String habilidadEspecial(List<Combatiente> objetivos);

    /**
     * Realiza un ataque básico contra un objetivo.
     * 
     * @param objetivo Combatiente objetivo del ataque.
     * @return Descripción del ataque realizado.
     */
    public String atacar(Combatiente objetivo) {
        if (!this.estaVivo || !objetivo.estaVivo) {
            return "";
        }

        int dano = calcularDanoAtaque();
        objetivo.recibirDano(dano);

        return String.format("%s ataca a %s causando %d de daño!", 
                             this.nombre, objetivo.nombre, dano);
    }

    /**
     * Recibe daño y actualiza el estado del combatiente.
     * 
     * @param dano Cantidad de daño recibido.
     */
    public void recibirDano(int dano) {
        this.vidaActual = Math.max(0, this.vidaActual - dano);
        if (this.vidaActual <= 0) {
            this.estaVivo = false;
        }
    }

    /**
     * Cura al combatiente, restaurando su vida.
     * 
     * @param cantidad Cantidad de vida restaurada.
     */
    public void curar(int cantidad) {
        if (this.estaVivo) {
            this.vidaActual = Math.min(this.vidaMaxima, this.vidaActual + cantidad);
        }
    }

    /**
     * Modifica el ataque del combatiente.
     * 
     * @param modificador Cantidad de ataque adicional.
     */
    public void modificarAtaque(int modificador) {
        aplicarEfecto("Ataque modificado: +" + modificador);
    }

    /**
     * Limpia todos los efectos activos del combatiente.
     */
    public void limpiarEfectos() {
        this.efectosActivos.clear();
    }

    /**
     * Calcula el daño del ataque con una variación aleatoria.
     * 
     * @return Cantidad de daño calculado.
     */
    protected int calcularDanoAtaque() {
        Random rand = new Random();
        double factor = 0.9 + (rand.nextDouble() * 0.2); // Variación entre 0.9 y 1.1
        return (int) (this.ataqueBase * factor);
    }

    /**
     * Aplica un efecto al combatiente.
     * 
     * @param efecto Descripción del efecto aplicado.
     */
    protected void aplicarEfecto(String efecto) {
        this.efectosActivos.add(efecto);
    }

    // Getters

    /**
     * Obtiene el nombre del combatiente.
     * 
     * @return Nombre del combatiente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la vida actual del combatiente.
     * 
     * @return Vida actual del combatiente.
     */
    public int getVidaActual() {
        return vidaActual;
    }

    /**
     * Obtiene la vida máxima del combatiente.
     * 
     * @return Vida máxima del combatiente.
     */
    public int getVidaMaxima() {
        return vidaMaxima;
    }

    /**
     * Obtiene el ataque base del combatiente.
     * 
     * @return Ataque base del combatiente.
     */
    public int getAtaqueBase() {
        return ataqueBase;
    }

    /**
     * Verifica si el combatiente está vivo.
     * 
     * @return true si el combatiente está vivo, false en caso contrario.
     */
    public boolean isEstaVivo() {
        return estaVivo;
    }

    /**
     * Obtiene una lista de los efectos activos del combatiente.
     * 
     * @return Lista de efectos activos.
     */
    public List<String> getEfectosActivos() {
        return new ArrayList<>(efectosActivos);
    }

    @Override
    public String toString() {
        String estado = estaVivo ? "Vivo" : "Muerto";
        String efectos = efectosActivos.isEmpty() ? "" : " [Efectos: " + String.join(", ", efectosActivos) + "]";
        return String.format("%s - HP: %d/%d - ATK: %d - Estado: %s%s", 
                             nombre, vidaActual, vidaMaxima, ataqueBase, estado, efectos);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Combatiente that = (Combatiente) obj;
        return Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
