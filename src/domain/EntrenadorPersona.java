package domain;

import java.util.*;

public class EntrenadorPersona extends Entrenador {
    private transient Scanner scanner;
    private Pokemon pokemonElegido;
    private Movimiento movimientoElegido;
    private Item itemElegido;

    public EntrenadorPersona(String nombre) {
        super(nombre);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public int elegirAccion(Entrenador usuario, Scanner scanner) throws POOBkemonException {
        System.out.println("Elige una acción: 1. Atacar 2. Cambiar Pokémon 3. Usar objeto 4. Huir");
        int accion = scanner.nextInt();
        if (accion < 1 || accion > 4) {
            throw new POOBkemonException("Acción no válida. Elige 1, 2 o 3.");
        }
        return accion;
    }

    @Override
    public Pokemon elegirPokemon() throws POOBkemonException {
        System.out.println("Elige un Pokémon:");
        for (int i = 0; i < getPokemones().size(); i++) {
            System.out.println((i + 1) + ". " + getPokemones().get(i).getName() + 
                            " [PS: " + getPokemones().get(i).getPs() + "/" + getPokemones().get(i).getMaxPs() + "]" +
                            (getPokemones().get(i).estaDebilitado() ? " (Debilitado)" : ""));
        }
        int eleccion = scanner.nextInt() - 1;
        if (eleccion < 0 || eleccion >= getPokemones().size()) {
            throw new POOBkemonException("Elección no válida. Elige un número entre 1 y " + getPokemones().size() + ".");
        }
        pokemonElegido = getPokemones().get(eleccion);
        
        if (pokemonElegido.estaDebilitado()) {
            throw new POOBkemonException("No puedes elegir un Pokémon debilitado.");
        }
        
        return pokemonElegido;
    }

    @Override
    public Movimiento elegirMovimiento(Pokemon pokemon, Scanner scanner) throws POOBkemonException {
        if (pokemon.getMovimientos() == null || pokemon.getMovimientos().isEmpty()) {
            throw new POOBkemonException("¡" + pokemon.getName() + " no tiene movimientos disponibles!");
        }
        
        System.out.println("Elige un movimiento para " + pokemon.getName() + ":");
        for (int i = 0; i < pokemon.getMovimientos().size(); i++) {
            Movimiento movimiento = pokemon.getMovimientos().get(i);
            if (movimiento != null) {
                System.out.println((i + 1) + ". " + movimiento.getNombre() + 
                                " [Tipo: " + movimiento.getTipo().getNombre() + 
                                ", Poder: " + movimiento.getPoder() + 
                                ", Precisión: " + movimiento.getPrecision() + 
                                ", PP: " + movimiento.getPP() + "/" + movimiento.getMaxPP() + "]");
            } else {
                System.out.println((i + 1) + ". <Movimiento no disponible>");
            }
        }
        
        int eleccion = scanner.nextInt() - 1;
        if (eleccion < 0 || eleccion >= pokemon.getMovimientos().size()) {
            throw new POOBkemonException("Elección no válida. Elige un número entre 1 y " + pokemon.getMovimientos().size() + ".");
        }
        
        movimientoElegido = pokemon.getMovimientos().get(eleccion);
        if (movimientoElegido == null) {
            throw new POOBkemonException("El movimiento seleccionado no está disponible.");
        }
        if (!movimientoElegido.tienePPDisponibles()) {
            throw new POOBkemonException("El movimiento " + movimientoElegido.getNombre() + " no tiene PP disponibles.");
        }
        
        return movimientoElegido;
    }


    @Override
    public Item elegirItem() throws POOBkemonException {
        if (getBolsa().isEmpty()) {
            throw new POOBkemonException("No tienes objetos en tu bolsa.");
        }
        
        System.out.println("Elige un objeto:");
        for (int i = 0; i < getBolsa().size(); i++) {
            System.out.println((i + 1) + ". " + getBolsa().get(i).getNombre());
        }
        int eleccion = scanner.nextInt() - 1;
        if (eleccion < 0 || eleccion >= getBolsa().size()) {
            throw new POOBkemonException("Elección no válida. Elige un número entre 1 y " + getBolsa().size() + ".");
        }
        itemElegido = getBolsa().get(eleccion);
        return itemElegido;
    }

    @Override
    public Pokemon elegirPokemonParaItem() throws POOBkemonException {
        System.out.println("Elige un Pokémon para usar el objeto:");
        for (int i = 0; i < getPokemones().size(); i++) {
            System.out.println((i + 1) + ". " + getPokemones().get(i).getName() + 
                            " [PS: " + getPokemones().get(i).getPs() + "/" + getPokemones().get(i).getMaxPs() + "]" +
                            (getPokemones().get(i).estaDebilitado() ? " (Debilitado)" : ""));
        }
        int eleccion = scanner.nextInt() - 1;
        if (eleccion < 0 || eleccion >= getPokemones().size()) {
            throw new POOBkemonException("Elección no válida. Elige un número entre 1 y " + getPokemones().size() + ".");
        }
        return getPokemones().get(eleccion);
    }

    public Pokemon getPokemonElegido() {
        return pokemonElegido;
    }
    public Movimiento getMovimientoElegido() {
        return movimientoElegido;
    }

    public Item getItemElegido() {
        return itemElegido;
    }

    public void setPokemonElegido(Pokemon pokemonElegido) {
        this.pokemonElegido = pokemonElegido;
    }

    public void setMovimientoElegido(Movimiento movimientoElegido) {
        this.movimientoElegido = movimientoElegido;
    }

    public void setItemElegido(Item itemElegido) {
        this.itemElegido = itemElegido;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void cerrarScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }

    @Override
    public boolean decidirHuir(Entrenador usuario, Scanner scanner) throws POOBkemonException {
        System.out.println("¿Quieres huir? (1. Sí / 2. No)");
        int decision = scanner.nextInt();
        if (decision == 1) {
            return true;
        } else if (decision == 2) {
            return false;
        } else {
            throw new POOBkemonException("Decisión no válida. Elige 1 o 2.");
        }
    }

    @Override
    public String toString() {
        return "Entrenador Persona: " + super.toString();
    }
}