package presentation.dto;

public class ResultadoAtaqueDTO {
    private String mensaje;
    private boolean exito;
    private int daño;
    private boolean pokemonDebilitado;
    private String efectoSecundario;
    
    // Constructor
    public ResultadoAtaqueDTO(String mensaje, boolean exito, int daño, boolean pokemonDebilitado) {
        this.mensaje = mensaje;
        this.exito = exito;
        this.daño = daño;
        this.pokemonDebilitado = pokemonDebilitado;
        this.efectoSecundario = "";
    }
    
    // Constructor con efecto secundario
    public ResultadoAtaqueDTO(String mensaje, boolean exito, int daño, boolean pokemonDebilitado, String efectoSecundario) {
        this(mensaje, exito, daño, pokemonDebilitado);
        this.efectoSecundario = efectoSecundario;
    }
    
    // Getters
    public String getMensaje() { 
        return mensaje; 
    }
    
    public boolean isExito() { 
        return exito; 
    }
    
    public int getDaño() { 
        return daño; 
    }
    
    public boolean isPokemonDebilitado() { 
        return pokemonDebilitado; 
    }
    
    public String getEfectoSecundario() {
        return efectoSecundario;
    }
    
    // Setters
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public void setExito(boolean exito) {
        this.exito = exito;
    }
    
    public void setDaño(int daño) {
        this.daño = daño;
    }
    
    public void setPokemonDebilitado(boolean pokemonDebilitado) {
        this.pokemonDebilitado = pokemonDebilitado;
    }
    
    public void setEfectoSecundario(String efectoSecundario) {
        this.efectoSecundario = efectoSecundario;
    }
}