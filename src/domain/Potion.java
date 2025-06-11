package domain;

public abstract class Potion extends Item {
    private int cantidadCuracion;
    
    public Potion(String nombre, String descripcion, int cantidadCuracion) {
        super(nombre, descripcion);
        this.cantidadCuracion = cantidadCuracion;
    }
    
    public int getCantidadCuracion() {
        return cantidadCuracion;
    }
    
    @Override
    public void usar(Pokemon pokemon) throws POOBkemonException {
        if (pokemon == null) {
            throw new POOBkemonException("No se puede usar la poción en un Pokémon nulo");
        }
        
        if (pokemon.estaDebilitado()) {
            throw new POOBkemonException("No se puede usar la poción en un Pokémon debilitado");
        }
        
        pokemon.curar(cantidadCuracion);
        
        pokemon.curarEstados();
    }
}