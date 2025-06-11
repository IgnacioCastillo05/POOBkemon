package domain;

import java.io.Serializable;
import java.util.*;


public abstract class Entrenador implements Serializable {
    private String nombre;
    private List<Pokemon> pokemones;
    private List<Item> bolsa;

    public Entrenador(String nombre) {
        this.nombre = nombre;
        this.pokemones = new ArrayList<>();
        this.bolsa = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Pokemon> getPokemones() {
        return pokemones;
    }

    public List<Item> getItems() {
        return bolsa;
    }

    public List<Item> getBolsa() {
        return bolsa;
    }

    public void agregarPokemon(Pokemon pokemon) {
        pokemones.add(pokemon);
    }

    public void agregarItem(Item item) {
        bolsa.add(item);
    }

    public void eliminarPokemon(Pokemon pokemon) {
        pokemones.remove(pokemon);
    }

    public void eliminarItem(Item item) {
        bolsa.remove(item);
    }

    public void usarItem(Item item, Pokemon pokemon) throws POOBkemonException {
        if (bolsa.contains(item)) {
            item.usar(pokemon);
            eliminarItem(item);
        } else {
            throw new POOBkemonException("No tienes ese item en tu inventario.");
        }
    }

    public abstract int elegirAccion(Entrenador usuario, Scanner scanner) throws POOBkemonException;

    public abstract Pokemon elegirPokemon() throws POOBkemonException;

    public abstract Movimiento elegirMovimiento(Pokemon pokemon, Scanner scanner) throws POOBkemonException;

    public abstract Item elegirItem() throws POOBkemonException;

    public abstract Pokemon elegirPokemonParaItem() throws POOBkemonException;

    public abstract boolean decidirHuir(Entrenador usuario, Scanner scanner) throws POOBkemonException;

    public boolean puedeCombatir() {
        for (Pokemon pokemon : pokemones) {
            if (!pokemon.estaDebilitado()) {
                return true;
            }
        }
        return false;
    }


    public Pokemon getPrimerPokemonDisponible() {
        for (Pokemon pokemon : pokemones) {
            if (!pokemon.estaDebilitado()) {
                return pokemon;
            }
        }
        return null;
    }

    public String getEstadoEquipo() {
        StringBuilder estado = new StringBuilder();
        estado.append("Equipo de ").append(nombre).append(":\n");
        
        for (Pokemon pokemon : pokemones) {
            estado.append("- ").append(pokemon.getName())
                  .append(": PS ").append(pokemon.getPs()).append("/").append(pokemon.getMaxPs());
            
            if (pokemon.estaDebilitado()) {
                estado.append(" [DEBILITADO]");
            } else if (pokemon.tieneEstado()) {
                estado.append(" [").append(pokemon.getStatus()).append("]");
            }
            
            estado.append("\n");
        }
        
        return estado.toString();
    }

}
