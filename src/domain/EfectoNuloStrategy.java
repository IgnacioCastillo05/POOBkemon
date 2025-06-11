package domain;

public class EfectoNuloStrategy implements EfectoEstadoStrategy {
    @Override
    public String aplicar(Pokemon pokemon) {
        return "";
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