package domain;

import java.io.Serializable;

public abstract class Item implements Serializable {
    private String nombre;
    private String descripcion;
    
    public Item(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    public abstract void usar(Pokemon pokemon) throws POOBkemonException;
    
    public String getNombre() {
        return nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
