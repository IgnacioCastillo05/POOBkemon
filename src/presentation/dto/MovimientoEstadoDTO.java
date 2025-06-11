package presentation.dto;

public class MovimientoEstadoDTO extends MovimientoDTO {
    
    // Constructor
    public MovimientoEstadoDTO() {
        super();
        setClaseMovimiento("Estado");
    }
    
    // Constructor de copia
    public MovimientoEstadoDTO(MovimientoEstadoDTO original) {
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