package presentation.dto;

import java.util.List;

public class PokemonDTO {
    private String nombre;
    private int psActual;
    private int psMaximo;
    private String estado;
    private List<MovimientoDTO> movimientos;
    private String tipo1;
    private String tipo2;
    private int ataque;
    private int defensa;
    private int velocidad;
    private int ataqueEspecial;
    private int defensaEspecial;
    private double multiplicadorPrecision;
    private int attack;
    private int defense;
    
    // NUEVO: Variables para manejar estados con efectos a lo largo del tiempo
    private int turnosEstado; // Para estados que duran varios turnos (dormido, confundido)
    private int contadorVenenoGrave; // Contador para veneno grave que aumenta el daño
    
    // Constructor
    public PokemonDTO() {
        this.multiplicadorPrecision = 1.0; // Valor por defecto
        this.turnosEstado = 0;
        this.contadorVenenoGrave = 0;
    }
    
    // Getters y setters existentes...
    
    // NUEVO: Métodos para manejar los estados
    
    /**
     * Establece un nuevo estado para el Pokémon y reinicia contadores relacionados
     */
    public void setEstado(String estado) { 
        // Si cambia el estado o se quita, resetear los contadores
        if (this.estado == null || !this.estado.equals(estado)) {
            this.turnosEstado = 0;
            this.contadorVenenoGrave = 0;
            
            // Si se establece estado dormido, inicializar turnos aleatorios (1-3)
            if ("dormido".equals(estado)) {
                this.turnosEstado = (int)(Math.random() * 3) + 1;
            }
            // Si se establece confundido, inicializar turnos aleatorios (1-4)
            else if ("confundido".equals(estado)) {
                this.turnosEstado = (int)(Math.random() * 4) + 1;
            }
        }
        
        this.estado = estado;
    }
    
    /**
     * Aplica los efectos de estado al final del turno
     * @return Mensaje describiendo el efecto aplicado
     */
    public String aplicarEfectosEstado() {
        if (estado == null) return "";
        
        String mensaje = "";
        
        switch (estado) {
            case "quemado":
                int dañoQuemadura = Math.max(1, getMaxPs() / 8);
                setPsActual(Math.max(0, getPs() - dañoQuemadura));
                mensaje = getName() + " sufre " + dañoQuemadura + " PS de daño por quemadura";
                break;
                
            case "envenenado":
                int dañoVeneno = Math.max(1, getMaxPs() / 8);
                setPsActual(Math.max(0, getPs() - dañoVeneno));
                mensaje = getName() + " sufre " + dañoVeneno + " PS de daño por veneno";
                break;
                
            case "gravemente envenenado":
                contadorVenenoGrave++;
                int dañoBase = Math.max(1, getMaxPs() / 16);
                int dañoTotal = dañoBase * contadorVenenoGrave;
                setPsActual(Math.max(0, getPs() - dañoTotal));
                mensaje = getName() + " sufre " + dañoTotal + " PS de daño por envenenamiento grave";
                break;
                
            case "dormido":
                turnosEstado--;
                if (turnosEstado <= 0) {
                    setEstado(null);
                    mensaje = getName() + " se ha despertado";
                } else {
                    mensaje = getName() + " sigue dormido";
                }
                break;
                
            case "confundido":
                turnosEstado--;
                if (turnosEstado <= 0) {
                    setEstado(null);
                    mensaje = getName() + " ya no está confundido";
                } else {
                    mensaje = getName() + " sigue confundido";
                }
                break;
                
            case "congelado":
                // 20% de probabilidad de descongelarse
                if (Math.random() < 0.2) {
                    setEstado(null);
                    mensaje = getName() + " se ha descongelado";
                } else {
                    mensaje = getName() + " sigue congelado";
                }
                break;
                
            case "paralizado":
                // La parálisis no causa daño por turno
                mensaje = ""; 
                break;
        }
        
        // Si los PS llegan a 0 por el efecto, establecer estado como debilitado
        if (getPs() <= 0) {
            setEstado("debilitado");
        }
        
        return mensaje;
    }
    
    /**
     * Determina si el Pokémon puede atacar dependiendo de su estado
     * @return true si puede atacar, false si no puede
     */
    public boolean puedeAtacar() {
        if (estado == null) return true;
        
        switch (estado) {
            case "dormido":
            case "congelado":
                return false;
            case "paralizado":
                // 25% de probabilidad de no poder atacar
                return Math.random() >= 0.25;
            default:
                return true;
        }
    }
    
    /**
     * Procesa lo que ocurre cuando el Pokémon intenta atacar
     * @return Mensaje resultante o null si puede atacar normalmente
     */
    public String procesarIntentarAtacar() {
        if ("confundido".equals(estado)) {
            // 33% de probabilidad de golpearse a sí mismo
            if (Math.random() < 1.0/3.0) {
                int dañoConfusion = Math.max(1, getMaxPs() / 10);
                setPsActual(Math.max(0, getPs() - dañoConfusion));
                return getName() + " está tan confundido que se hirió a sí mismo por " + dañoConfusion + " PS";
            }
        } else if ("paralizado".equals(estado)) {
            if (Math.random() < 0.25) {
                return getName() + " está paralizado y no puede moverse";
            }
        } else if ("dormido".equals(estado)) {
            return getName() + " está dormido";
        } else if ("congelado".equals(estado)) {
            return getName() + " está congelado y no puede moverse";
        }
        
        return null; // Puede atacar normalmente
    }
    
    /**
     * Retorna un factor de modificación de ataque basado en estado
     * @return factor multiplicador (1.0 = normal, <1.0 = reducido)
     */
    public double getModificadorAtaque() {
        if ("quemado".equals(estado)) {
            return 0.5; // Ataque reducido a la mitad cuando está quemado
        }
        return 1.0;
    }
    
    /**
     * Obtiene el contador de turnos para estados con duración variable
     */
    public int getTurnosEstado() {
        return turnosEstado;
    }
    
    /**
     * Establece el contador de turnos para estados con duración variable
     */
    public void setTurnosEstado(int turnos) {
        this.turnosEstado = turnos;
    }
    
    /**
     * Obtiene el contador de veneno grave
     */
    public int getContadorVenenoGrave() {
        return contadorVenenoGrave;
    }
    
    /**
     * Establece el contador de veneno grave
     */
    public void setContadorVenenoGrave(int contador) {
        this.contadorVenenoGrave = contador;
    }
    
    /**
     * Cura al Pokémon de todos los estados alterados
     */
    public void curarEstados() {
        this.estado = null;
        this.turnosEstado = 0;
        this.contadorVenenoGrave = 0;
    }
    
    // Getters y setters
    public String getName() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public int getPs() { 
        return psActual; 
    }
    
    public void setPsActual(int psActual) { 
        this.psActual = psActual; 
    }
    
    public int getMaxPs() { 
        return psMaximo; 
    }
    
    public void setPsMaximo(int psMaximo) { 
        this.psMaximo = psMaximo; 
    }
    
    public String getStatus() { 
        return estado; 
    }
    
    public List<MovimientoDTO> getMovimientos() { 
        return movimientos; 
    }
    
    public void setMovimientos(List<MovimientoDTO> movimientos) { 
        this.movimientos = movimientos; 
    }
    
    public String getTipo1() {
        return tipo1;
    }
    
    public void setTipo1(String tipo1) {
        this.tipo1 = tipo1;
    }
    
    public String getTipo2() {
        return tipo2;
    }
    
    public void setTipo2(String tipo2) {
        this.tipo2 = tipo2;
    }
    
    public int getAtaque() {
        return ataque;
    }
    
    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }
    
    public int getDefensa() {
        return defensa;
    }
    
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }
    
    public int getVelocidad() {
        return velocidad;
    }
    
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
    
    public int getAtaqueEspecial() {
        return ataqueEspecial;
    }
    
    public void setAtaqueEspecial(int ataqueEspecial) {
        this.ataqueEspecial = ataqueEspecial;
    }
    
    public int getDefensaEspecial() {
        return defensaEspecial;
    }
    
    public void setDefensaEspecial(int defensaEspecial) {
        this.defensaEspecial = defensaEspecial;
    }
    
    public double getMultiplicadorPrecision() {
        return multiplicadorPrecision;
    }
    
    public void setMultiplicadorPrecision(double multiplicadorPrecision) {
        this.multiplicadorPrecision = multiplicadorPrecision;
    }
    
    // Métodos adicionales
    public boolean estaDebilitado() { 
        return psActual <= 0; 
    }
    
    public boolean tieneEstado() { 
        return estado != null && !estado.isEmpty(); 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PokemonDTO other = (PokemonDTO) obj;
        return this.nombre != null && this.nombre.equals(other.nombre);
    }
    
    @Override
    public int hashCode() {
        return nombre != null ? nombre.hashCode() : 0;
    }
    
    public int getAttack() { 
    	return attack; 
    }
    public void setAttack(int attack) { 
    	this.attack = attack; 
    }

    public int getDefense() { 
    	return defense; 
    }
    
    public void setDefense(int defense) { 
    	this.defense = defense; 
    }
}