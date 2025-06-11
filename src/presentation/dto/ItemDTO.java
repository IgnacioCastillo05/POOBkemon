package presentation.dto;

public class ItemDTO {
    private String nombre;
    private String descripcion;
    private String tipo;  // "Pocion", "SuperPocion", "HiperPocion", "Revive"
    
    // Constructor
    public ItemDTO() {}
    
    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}