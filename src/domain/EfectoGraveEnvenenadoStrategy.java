package domain;

public class EfectoGraveEnvenenadoStrategy implements EfectoEstadoStrategy {
    private int turnosEnvenenado = 0;
    
    @Override
    public String aplicar(Pokemon pokemon) {
        turnosEnvenenado++;
        
        int dañoBase = Math.max(1, pokemon.getMaxPs() / 16);
        int dañoVeneno = dañoBase * turnosEnvenenado;
        
        pokemon.recibirDaño(dañoVeneno);
        
        return pokemon.getName() + " sufre " + dañoVeneno + " PS de daño por envenenamiento grave";
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
        turnosEnvenenado = 0;
    }
}