package model;

import java.io.*;
import java.util.*;
/**
 * Clase EnemyFactory.
 * Proporciona métodos para crear enemigos de diferentes tipos,
 * cargar enemigos desde un archivo CSV y generar enemigos aleatorios.
 * @author Carlos Altán
 * @since 2025-09-28
 */
public class EnemyFactory {
    /**
     * Crea un enemigo del tipo especificado.
     * @param tipo
     * @param esJefe
     * @return Combatiente creado.
     */
    public static Combatiente crearEnemigo(String tipo, boolean esJefe) {
        switch (tipo.toLowerCase()) {
            case "orco":
                return new Orco(esJefe);
            case "goblin":
                return new Goblin(esJefe);
            case "dragon":
                return new Dragon(esJefe);
            default:
                return new Orco(false); 
        }
    }
    /**
     * Carga enemigos desde un archivo CSV.
     * @param nombreArchivo
     * @return Lista de Combatientes cargados.
     */
    public static List<Combatiente> cargarEnemigosDesdeCSV(String nombreArchivo) {
        List<Combatiente> enemigos = new ArrayList<>();
        try {
            File archivo = new File(nombreArchivo);
            if (!archivo.exists()) {
                throw new FileNotFoundException("El archivo especificado no existe: " + nombreArchivo);
            }

            Scanner lector = new Scanner(archivo);
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                Combatiente enemigo = parsearLineaCSV(linea); 
                if (enemigo != null) {
                    enemigos.add(enemigo);
                }
            }
            lector.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al leer archivo CSV: " + e.getMessage());
        }
        return enemigos;
    }
    /**
     * Parsea una línea del archivo CSV y crea el enemigo correspondiente.
     * @param linea Línea del archivo CSV.
     * @return Combatiente creado o null si hay error.
     */
    private static Combatiente parsearLineaCSV(String linea) {
        try {
            String[] datos = linea.split(",");
            if (datos.length >= 3) {
                String tipo = datos[0].trim();
                boolean esJefe = datos[1].trim().equalsIgnoreCase("true") || 
                                datos[1].trim().equalsIgnoreCase("si") ||
                                datos[1].trim().equals("1");
                String nombre = datos.length > 2 ? datos[2].trim() : "";
                
                Combatiente enemigo = crearEnemigo(tipo, esJefe);
                
               
                if (!nombre.isEmpty()) {
                    
                }
                
                return enemigo;
            }
        } catch (Exception e) {
            return null;
        }
        
        return null;
    }
    /**
     * Genera una lista de enemigos aleatorios.
     * @return Lista de Combatientes generados aleatoriamente.
     */
    public static List<Combatiente> crearEnemigosAleatorios() {
        List<Combatiente> enemigos = new ArrayList<>();
        Random rand = new Random();
        
        int cantidadEnemigos = 1 + rand.nextInt(3);
        
        String[] tipos = {"orco", "goblin", "dragon"};
        
        for (int i = 0; i < cantidadEnemigos; i++) {
            String tipo = tipos[rand.nextInt(tipos.length)];
            boolean esJefe = rand.nextInt(100) < 30; 
            
            enemigos.add(crearEnemigo(tipo, esJefe));
        }
     
        if (cantidadEnemigos > 1 && enemigos.stream().noneMatch(e -> ((Enemigo)e).isEsJefe())) {
            int indiceJefe = rand.nextInt(enemigos.size());
            Enemigo enemigoNormal = (Enemigo) enemigos.get(indiceJefe);
            Combatiente jefe = crearEnemigo(enemigoNormal.getTipo(), true);
            enemigos.set(indiceJefe, jefe);
        }
        
        return enemigos;
    }
}
