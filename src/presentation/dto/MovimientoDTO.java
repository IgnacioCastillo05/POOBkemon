package presentation.dto;

public class MovimientoDTO {
    private String nombre;
    private String tipo;  // Nombre del tipo, ej: "Fuego", "Agua", etc.
    private int poder;
    private int precision;
    private int ppActual;
    private int ppMaximo;
    private boolean prioridad;
    private String claseMovimiento;  // "Físico", "Especial", "Estado"
    private String descripcion;
    
    // Constructor
    public MovimientoDTO() {}
    
    // Getters y setters
    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public String getTipo() { 
        return tipo; 
    }
    
    public void setTipo(String tipo) { 
        this.tipo = tipo; 
    }
    
    public int getPoder() { 
        return poder; 
    }
    
    public void setPoder(int poder) { 
        this.poder = poder; 
    }
    
    public int getPrecision() { 
        return precision; 
    }
    
    public void setPrecision(int precision) { 
        this.precision = precision; 
    }
    
    public int getPP() { 
        return ppActual; 
    }
    
    public void setPPActual(int ppActual) { 
        this.ppActual = ppActual; 
    }
    
    public int getMaxPP() { 
        return ppMaximo; 
    }
    
    public void setPPMaximo(int ppMaximo) { 
        this.ppMaximo = ppMaximo; 
    }
    
    public boolean getPrioridad() {
        return prioridad;
    }
    
    public void setPrioridad(boolean prioridad) {
        this.prioridad = prioridad;
    }
    
    public String getClaseMovimiento() {
        return claseMovimiento;
    }
    
    public void setClaseMovimiento(String claseMovimiento) {
        this.claseMovimiento = claseMovimiento;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    // Métodos adicionales
    public boolean tienePPDisponibles() {
        return ppActual > 0;
    }
    
    public void usarMovimiento() {
        if (ppActual > 0) {
            ppActual--;
        }
    }
}