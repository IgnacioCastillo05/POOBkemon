package domain;

public class EfectoDormidoStrategy implements EfectoEstadoStrategy {
    private int turnosRestantes;
    
    public EfectoDormidoStrategy() {
        this.turnosRestantes = (int)(Math.random() * 4) + 1;
    }
    
    @Override
    public String aplicar(Pokemon pokemon) {
        turnosRestantes--;
        
        if (turnosRestantes <= 0) {
            pokemon.setEstado(null);
            return pokemon.getName() + " se ha despertado";
        }
        
        return pokemon.getName() + " sigue dormido";
    }
    
    @Override
    public boolean puedeAtacar(Pokemon pokemon) {
        return false; 
    }
    
    @Override
    public String procesarIntentarAtacar(Pokemon pokemon) {
        return pokemon.getName() + " está dormido";
    }
    
    @Override
    public void alCurar(Pokemon pokemon) {
        turnosRestantes = 0;
    }
}
