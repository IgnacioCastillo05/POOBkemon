package domain;

public class EfectoCongeladoStrategy implements EfectoEstadoStrategy {
    @Override
    public String aplicar(Pokemon pokemon) {
        if (Math.random() < 0.2) {
            pokemon.setEstado(null);
            return pokemon.getName() + " se ha descongelado";
        }
        return pokemon.getName() + " sigue congelado";
    }
    
    @Override
    public boolean puedeAtacar(Pokemon pokemon) {
        boolean esDeHielo = false;
        if (pokemon.getTipo1() != null && pokemon.getTipo1().getNombre().equalsIgnoreCase("Hielo")) {
            esDeHielo = true;
        }
        if (pokemon.getTipo2() != null && pokemon.getTipo2().getNombre().equalsIgnoreCase("Hielo")) {
            esDeHielo = true;
        }
        if (esDeHielo) {
            pokemon.setEstado(null);
            return true;
        }
        return pokemon.getStatus() == null;
    }
    
    @Override
    public String procesarIntentarAtacar(Pokemon pokemon) {
        if (pokemon.getStatus() == null) {
            return null;
        }
        return pokemon.getName() + " está congelado y no puede moverse";
    }
    
    @Override
    public void alCurar(Pokemon pokemon) {
    }
}