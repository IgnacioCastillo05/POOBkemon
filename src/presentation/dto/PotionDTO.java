package presentation.dto;

public class PotionDTO extends ItemDTO {
    private int cantidadCuracion;
    
    // Constructor
    public PotionDTO() {
        super();
    }
    
    // Getters y setters
    public int getCantidadCuracion() {
        return cantidadCuracion;
    }
    
    public void setCantidadCuracion(int cantidadCuracion) {
        this.cantidadCuracion = cantidadCuracion;
    }
}