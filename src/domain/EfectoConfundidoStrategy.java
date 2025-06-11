package domain;

public class EfectoConfundidoStrategy implements EfectoEstadoStrategy {
    private int turnosRestantes;
    
    public EfectoConfundidoStrategy() {
        this.turnosRestantes = (int)(Math.random() * 4) + 1;
    }
    
    @Override
    public String aplicar(Pokemon pokemon) {
        turnosRestantes--;
        
        if (turnosRestantes <= 0) {
            pokemon.setEstado(null);
            return pokemon.getName() + " ya no está confundido";
        }
        
        return pokemon.getName() + " sigue confundido";
    }
    
    @Override
    public boolean puedeAtacar(Pokemon pokemon) {
        return true;
    }
    
    @Override
    public String procesarIntentarAtacar(Pokemon pokemon) {
        if (Math.random() < 1.0/3.0) {
            int daño = 40;
            pokemon.recibirDaño(daño);
            return pokemon.getName() + " está tan confundido que se hirió a sí mismo por " + daño + " PS";
        }
        return null;
    }
    
    @Override
    public void alCurar(Pokemon pokemon) {
        turnosRestantes = 0;
    }
}