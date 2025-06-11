package presentation.dto;

public class MovimientoFisicoDTO extends MovimientoDTO {
    
    // Constructor
    public MovimientoFisicoDTO() {
        super();
        setClaseMovimiento("Físico");
    }
    
    // Constructor de copia
    public MovimientoFisicoDTO(MovimientoFisicoDTO original) {
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