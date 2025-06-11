package domain;

public class EfectoQuemadoStrategy implements EfectoEstadoStrategy {
    @Override
    public String aplicar(Pokemon pokemon) {
        int dañoQuemadura = Math.max(1, pokemon.getMaxPs() / 8);
        pokemon.recibirDaño(dañoQuemadura);
        return pokemon.getName() + " sufre " + dañoQuemadura + " PS de daño por quemadura";
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