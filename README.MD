# Juego de Batalla RPG - Consola Java

Juego de rol por turnos desarrollado en Java. Permite a 1 o 2 jugadores luchar contra enemigos generados aleatoriamente o cargados desde un CSV. El juego se ejecuta completamente en consola, mostrando menús, turnos, acciones, inventario y el registro de la batalla.

## Requisitos

- Java 17 o superior.
- Consola / terminal para ejecutar el programa.
- (Opcional) Archivo CSV de enemigos si deseas cargar enemigos personalizados.

## Estructura del Proyecto

src/
├─ controller/ # Controlador del juego y lógica de turnos
├─ model/ # Clases de jugadores, enemigos, ítems y batalla
├─ view/ # Vista por consola
└─ Principal.java # Clase main que ejecuta el juego


## Compilar el Proyecto

1. Abre la terminal en la carpeta raíz del proyecto.
2. Compila todos los archivos `.java` en `src/` hacia la carpeta `bin/`:

```bash
javac -d bin $(find src -name "*.java")

javac -d bin src\**\*.java

Ejecutar el Juego

Ejecuta la clase principal desde la carpeta bin:

java -cp bin Principal