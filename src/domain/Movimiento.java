package domain;

import java.io.Serializable;

public abstract class Movimiento implements Serializable {
    private static final long serialVersionUID = -8555692449260925048L;
    
    private String nombre;
    private Tipo tipo;
    private int poder;
    private int precision;
    private int ppActual;
    private int ppMaximo;
    private boolean prioridad;
    private String claseMovimiento;
    private String descripcion;

    public Movimiento(String nombre, Tipo tipo, int poder, int precision, int ppActual, int ppMaximo, boolean prioridad, String claseMovimiento, String descripcion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.poder = poder;
        this.precision = precision;
        this.ppActual = ppActual;
        this.ppMaximo = ppMaximo;
        this.prioridad = prioridad;
        this.claseMovimiento = claseMovimiento;
        this.descripcion = descripcion;
    }

    /**
     * Constructor de copia para crear un nuevo Movimiento con los mismos atributos.
     * @param original El Movimiento original a copiar
     */
    protected Movimiento(Movimiento original) {
        this.nombre = original.nombre;
        this.tipo = original.tipo;
        this.poder = original.poder;
        this.precision = original.precision;
        this.ppActual = original.ppActual;
        this.ppMaximo = original.ppMaximo;
        this.prioridad = original.prioridad;
        this.claseMovimiento = original.claseMovimiento;
        this.descripcion = original.descripcion;
    }

    public abstract String aplicarEfecto(Pokemon usuario, Pokemon objetivo);

    public String getNombre() {
        return nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getPoder() {
        return poder;
    }

    public int getPrecision() {
        return precision;
    }

    public int getPP() {
        return ppActual;
    }

    public int getMaxPP() {
        return ppMaximo;
    }

    public boolean getPrioridad() {
        return prioridad;
    }

    public String getClaseMovimiento() {
        return claseMovimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setPoder(int poder) {
        this.poder = poder;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * Establece el valor actual de PP.
     * @param PP Nuevo valor de PP (debe ser <= ppMaximo).
     */
    public void setPP(int PP) {
        this.ppActual = Math.min(PP, ppMaximo);
    }
    
    /**
     * Reduce los PP en la cantidad especificada (por defecto 1).
     * @param cantidad Cantidad a reducir.
     * @return true si pudo reducirse, false si no hay suficientes PP.
     */
    public boolean reducirPP(int cantidad) {
        if (ppActual >= cantidad) {
            ppActual -= cantidad;
            return true;
        }
        return false;
    }
    
    /**
     * Reduce los PP en 1 después de usar el movimiento.
     * @return true si pudo reducirse, false si no hay suficientes PP.
     */
    public boolean usarMovimiento() {
        return reducirPP(1);
    }
    
    /**
     * Restaura los PP en la cantidad especificada.
     * @param cantidad Cantidad a restaurar.
     */
    public void restaurarPP(int cantidad) {
        ppActual = Math.min(ppActual + cantidad, ppMaximo);
    }
    
    /**
     * Restaura completamente los PP al valor máximo.
     */
    public void restaurarPPCompleto() {
        ppActual = ppMaximo;
    }
    
    /**
     * Verifica si el movimiento tiene PP disponibles.
     * @return true si tiene PP disponibles, false en caso contrario.
     */
    public boolean tienePPDisponibles() {
        return ppActual > 0;
    }
}