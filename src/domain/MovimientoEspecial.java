package domain;

public class MovimientoEspecial extends Movimiento {
    private static final long serialVersionUID = 7775130181025684876L;
    public MovimientoEspecial(String nombre, Tipo tipo, int poder, int precision, int ppActual, int ppMaximo, boolean prioridad, String claseMovimiento, String descripcion) {
        super(nombre, tipo, poder, precision, ppActual, ppMaximo, prioridad, claseMovimiento, descripcion);
    }

    /**
     * Constructor de copia para crear una nueva instancia independiente.
     * @param original El MovimientoEspecial original a copiar
     */
    public MovimientoEspecial(MovimientoEspecial original) {
        super(original);
    }

    @Override
    public String toString() {
        return "Movimiento Fisico: " + super.toString();
    
    }

    @Override
    public String aplicarEfecto(Pokemon usuario, Pokemon objetivo) {
        int daño = calcularDañoEspecial(usuario, objetivo);
        objetivo.recibirDaño(daño);
        
        String efecto = aplicarEfectoSecundario(usuario,objetivo);
        
        return objetivo.getName() + " recibió " + daño + " de daño." + (efecto.isEmpty() ? "" : " " + efecto);
    }

    private String aplicarEfectoSecundario(Pokemon usuario,Pokemon objetivo) {
        if (objetivo.tieneEstado()) return "";
        
        String nombreMovimiento = this.getNombre();
        
        switch (nombreMovimiento) {
            case "Estoicismo":
                objetivo.setSpecialAttack((int)(objetivo.getSpecialAttack() * 0.9)); 
                break;
            
            case "Rayo":
                if (Math.random() < 0.1) {
                    objetivo.setEstado("paralizado");
                    return "¡" + objetivo.getName() + " ha sido paralizado!";
                }
                break;

            case "Fuerza Lunar":
                if (Math.random() < 0.3){
                    objetivo.setSpecialAttack((int)(objetivo.getSpecialAttack() * 0.9));  
                    return "¡" + objetivo.getName() + " ha bajado su ataque especial!"; 
                }
                break;

            case "Lanzallamas":
                if (Math.random() < 0.1) {
                    objetivo.setEstado("quemado");
                    return "¡" + objetivo.getName() + " ha sido quemado!";
                }
                break;

            case "Huracán":
                if (Math.random() < 0.3) {
                    objetivo.setEstado("confundido");
                    return "¡" + objetivo.getName() + " ha sido confundido!";
                }
                break;

            case "Bola Sombra":
                if (Math.random() < 0.2){
                    objetivo.setSpecialDefense((int)(objetivo.getSpecialDefense() * 0.9));  
                    return "¡" + objetivo.getName() + " ha bajado su defensa especial!"; 
                }
                break;

            case "Disparo Lodo":
                objetivo.setSpeed((int)(objetivo.getSpeed() * 0.85)); 
                break;

            case "Rayo Hielo":
                if (Math.random() < 0.1) {
                    objetivo.setEstado("congelado");
                    return "¡" + objetivo.getName() + " ha sido congelado!";
                }
                break;

            case "Bomba Lodo":
                if (Math.random() < 0.3) {
                    objetivo.setEstado("envenenado");
                    return "¡" + objetivo.getName() + " ha sido envenenado!";
                }
                break;

            case "Psíquico":
                if (Math.random() < 0.1){
                    objetivo.setSpecialDefense((int)(objetivo.getSpecialDefense() * 0.9));  
                    return "¡" + objetivo.getName() + " ha bajado su defensa especial!"; 
                }
                break;
            
            case "Poder Pasado":
                if (Math.random() < 0.1){
                    usuario.setSpecialAttack((int)(usuario.getSpecialAttack() * 1.1)); 
                    usuario.setSpecialDefense((int)(usuario.getSpecialDefense() * 1.1));
                    usuario.setSpeed((int)(usuario.getSpeed() * 1.1));
                    usuario.setAttack((int)(usuario.getAttack() * 1.1));
                    usuario.setDefense((int)(usuario.getDefense() * 1.1));
                    return "¡" + usuario.getName() + " ha aumentado su ataque, defensa, ataque especial, defensa especial y velocidad!";
                    
                }
                break;

            case "Foco Resplandor":
                if (Math.random() < 0.1) {
                    objetivo.setSpecialDefense((int)(objetivo.getSpecialDefense() * 0.9));  
                    return "¡" + objetivo.getName() + " ha bajado su defensa especial!"; 
                }
                break;
            
        }
        
        return "";
    }


    private int calcularDañoEspecial(Pokemon atacante, Pokemon defensor) {
        double efectividad = this.getTipo().calcularEfectividadContraPokemon(defensor);
        int ataqueEspecial = atacante.getSpecialAttack();
        int defensaEspecial = defensor.getSpecialDefense();
        int poder = this.getPoder();
        
        double daño = (poder * ataqueEspecial / defensaEspecial) * efectividad * 0.5;
        daño *= (0.85 + Math.random() * 0.15);
        
        return (int)Math.round(daño);
    }
}