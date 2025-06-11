package domain;

public class EfectoEnvenenadoStrategy implements EfectoEstadoStrategy {
    @Override
    public String aplicar(Pokemon pokemon) {
        int dañoVeneno = Math.max(1, pokemon.getMaxPs() / 8);
        pokemon.recibirDaño(dañoVeneno);
        return pokemon.getName() + " sufre " + dañoVeneno + " PS de daño por veneno";
    }
    
    @Override
    public boolean puedeAtacar(Pokemon pokemon) {
        return true;
    }
    
    @Override
    public String procesarIntentarAtacar(Pokemon pokemon) {
        return null;
    }
    
    @Override
    public void alCurar(Pokemon pokemon) {
    }
}
