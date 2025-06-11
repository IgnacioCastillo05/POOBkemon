package presentation.dto;

import java.util.List;

public class EntrenadorDTO {
    private String nombre;
    private List<PokemonDTO> pokemones;
    private List<ItemDTO> items;
    
    // Constructor
    public EntrenadorDTO() {}
    
    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public List<PokemonDTO> getPokemones() { return pokemones; }
    public void setPokemones(List<PokemonDTO> pokemones) { this.pokemones = pokemones; }
    
    public List<ItemDTO> getItems() { return items; }
    public void setItems(List<ItemDTO> items) { this.items = items; }
    
    // Métodos adicionales
    public boolean puedeCombatir() {
        for (PokemonDTO pokemon : pokemones) {
            if (!pokemon.estaDebilitado()) {
                return true;
            }
        }
        return false;
    }
}