package domain;

public class Revive extends Item {
    public Revive() {
        super("Revivir", "Revive a un Pokémon debilitado con la mitad de sus PS máximos.");
    }
    
    @Override
    public void usar(Pokemon pokemon) throws POOBkemonException {
        if (pokemon == null) {
            throw new POOBkemonException("No se puede usar Revivir en un Pokémon nulo");
        }
        
        if (!pokemon.estaDebilitado()) {
            throw new POOBkemonException("No se puede usar Revivir en un Pokémon que no está debilitado");
        }
        
        pokemon.revivir(pokemon.getMaxPs() / 2);
    }
}
