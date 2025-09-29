package model;

import java.util.*;
/**
 * Clase abstracta que representa un ítem en el juego de rol.
 * Define atributos y métodos comunes para todos los ítems.
 * @author Carlos Altán
 * @since 2025-09-28
 */
public abstract class Item {
    protected String nombre;
    protected String descripcion;
    protected int cantidad;
    /**
     * Constructor de la clase Item.
     * @param nombre 
     * @param descripcion
     * @param cantidad 
     */
    public Item(String nombre, String descripcion, int cantidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }
    /**
     * Usa el ítem en los objetivos especificados.
     * @param objetivos Lista de combatientes objetivo.
     * @return Descripción del efecto del ítem.
     */
    public abstract String usar(List<Combatiente> objetivos);
    /**
     * Reduce la cantidad del ítem en uno.
     * Si la cantidad es cero, no hace nada.
     * @return void 
     */
    public void reducirCantidad() {
        if (cantidad > 0) {
            cantidad--;
        }
    }
    /**
     * Obtiene la cantidad actual del ítem. 
     * @return int Cantidad del ítem.
     */
    public int getCantidad() { return cantidad; }
    /**
     * Obtiene el nombre del ítem.
     * @return String Nombre del ítem.
     */
    public String getNombre() { return nombre; }
    /**
     * Obtiene la descripción del ítem.
     * @return String Descripción del ítem.
     */
    public String getDescripcion() { return descripcion; }
    /**
     * Representación en cadena del ítem.
     * @return String Representación del ítem.
     * 
     */
    @Override
    public String toString() {
        return String.format("%s (x%d) - %s", nombre, cantidad, descripcion);
    }
    /**
     * Compara dos ítems por su nombre.
     * @param obj Objeto a comparar.
     * @return boolean true si son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return Objects.equals(nombre, item.nombre);
    }
    /**
     * Genera un código hash basado en el nombre del ítem.
     * @return int Código hash del ítem.
     * 
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}

