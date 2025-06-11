package presentation.dto;

public class MovimientoEspecialDTO extends MovimientoDTO {
    
    // Constructor
    public MovimientoEspecialDTO() {
        super();
        setClaseMovimiento("Especial");
    }
    
    // Constructor de copia
    public MovimientoEspecialDTO(MovimientoEspecialDTO original) {
        this.setNombre(original.getNombre());
        this.setTipo(original.getTipo());
        this.setPoder(original.getPoder());
        this.setPrecision(original.getPrecision());
        this.setPPActual(original.getPP());
        this.setPPMaximo(original.getMaxPP());
        this.setPrioridad(original.getPrioridad());
        this.setClaseMovimiento(original.getClaseMovimiento());
        this.setDescripcion(original.getDescripcion());
    }
}