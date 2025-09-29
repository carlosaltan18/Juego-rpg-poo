package model;

import java.util.*;
/**
 * Enum que define los roles de los jugadores en el juego de rol.
 * Cada rol tiene atributos y items iniciales específicos.
 * @author Carlos Altán
 * @since 2025-09-28
 */
public enum RolJugador {
    GUERRERO(120, 25, crearItemsGuerrero()),
    EXPLORADOR(80, 20, crearItemsExplorador());
    
    private final int vida;
    private final int ataque;
    private final List<Item> itemsIniciales;
    /**
     * Constructor del enum RolJugador.
     * @param vida 
     * @param ataque
     * @param items
     */
    RolJugador(int vida, int ataque, List<Item> items) {
        this.vida = vida;
        this.ataque = ataque;
        this.itemsIniciales = items;
    }
    /**
     * Getter de vida.
     * @return int Vida del rol.
     */
    public int getVida() { return vida; }
    /**
     * Getter de ataque.
     * @return int Ataque del rol.
     */
    public int getAtaque() { return ataque; }
    /**
     * Getter de items iniciales.
     * @return List<Item> Items iniciales del rol.
     */
    public List<Item> getItems() { return new ArrayList<>(itemsIniciales); }
    /**
     * Crea la lista de items iniciales para el rol de guerrero.
     * @return List<Item> Lista de items iniciales.
     */
    private static List<Item> crearItemsGuerrero() {
        List<Item> items = new ArrayList<>();
        items.add(new Pocion(2));
        items.add(new PotenciadorAtaque(1));
        return items;
    }
    /**
     * Crea la lista de items iniciales para el rol de explorador.
     * @return List<Item> Lista de items iniciales.
     */
    private static List<Item> crearItemsExplorador() {
        List<Item> items = new ArrayList<>();
        items.add(new Pocion(3));
        items.add(new PotenciadorAtaque(2));
        items.add(new BombaHumo(2));
        items.add(new KitCuracion(1));
        return items;
    }
}
