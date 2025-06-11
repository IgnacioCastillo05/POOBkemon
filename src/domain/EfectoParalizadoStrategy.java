package domain;

public class EfectoParalizadoStrategy implements EfectoEstadoStrategy {
    @Override
    public String aplicar(Pokemon pokemon) {
        return ""; 
    }
    
    @Override
    public boolean puedeAtacar(Pokemon pokemon) {
        return Math.random() >= 0.25;
    }
    
    @Override
    public String procesarIntentarAtacar(Pokemon pokemon) {
        return null;
    }
    
    @Override
    public void alCurar(Pokemon pokemon) {
    }
}