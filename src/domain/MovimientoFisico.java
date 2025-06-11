package domain;

public class MovimientoFisico extends Movimiento {
    private static final long serialVersionUID = 7775130181025684876L;
    public MovimientoFisico(String nombre, Tipo tipo, int poder, int precision, int ppActual, int ppMaximo, boolean prioridad, String claseMovimiento, String descripcion) {
        super(nombre, tipo, poder, precision, ppActual, ppMaximo, prioridad, claseMovimiento, descripcion);
    }

    /**
     * Constructor de copia para crear una nueva instancia independiente.
     * @param original El MovimientoFisico original a copiar
     */
    public MovimientoFisico(MovimientoFisico original) {
        super(original);
    }

    @Override
    public String toString() {
        return "Movimiento Fisico: " + super.toString();
    
    }

    @Override
    public String aplicarEfecto(Pokemon usuario, Pokemon objetivo) {
        int daño = calcularDañoFisico(usuario, objetivo);
        objetivo.recibirDaño(daño);
        String efecto = aplicarEfectoSecundario(usuario, objetivo);
        
        return objetivo.getName() + " recibió " + daño + " de daño." + (efecto.isEmpty() ? "" : " " + efecto);
    }

    private String aplicarEfectoSecundario(Pokemon usuario, Pokemon objetivo) {
        if (objetivo.tieneEstado()) return "";
        
        String nombreMovimiento = this.getNombre();
        
        switch (nombreMovimiento) {
            case "Golpe Cuerpo":
                if (Math.random() < 0.3) {
                    objetivo.setEstado("paralizado");
                    return "¡" + objetivo.getName() + " ha sido paralizado!";
                }
                break;
            case "Triturar":
                objetivo.setDefense((int)(objetivo.getDefense() * 0.9)); 
                break;
            case "Colmillo Trueno":
                if (Math.random() < 0.1) {
                    objetivo.setEstado("paralizado");
                    return "¡" + objetivo.getName() + " ha sido paralizado!";
                }
                break;
            case "Carantoña":
                if (Math.random() < 0.1){
                    objetivo.setAttack((int)(objetivo.getAttack() * 0.9));
                    return "¡" + objetivo.getName() + " ha bajado su ataque!"; 
                }
                break;
            case "Rueda Fuego":
                if (Math.random() < 0.1) {
                    objetivo.setEstado("quemado");
                    return "¡" + objetivo.getName() + " ha sido quemado!";
                }
                break;
            case "Puño Hielo":
                if (Math.random() < 0.1) {
                    objetivo.setEstado("congelado");
                    return "¡" + objetivo.getName() + " ha sido congelado!";
                }
                break;
            case "Puya Nociva":
                if (Math.random() < 0.3) {
                    objetivo.setEstado("envenenado");
                    return "¡" + objetivo.getName() + " ha sido envenenado!";
                }
                break;
            case "Garra Metal":
                if (Math.random() < 0.1){
                    usuario.setAttack((int)(usuario.getAttack() * 1.1)); 
                    return "¡" + usuario.getName() + " ha aumentado su ataque!"; 
                }
                break;    
        }
        return "";
    }


    private int calcularDañoFisico(Pokemon atacante, Pokemon defensor) {
       
        double efectividad = this.getTipo().calcularEfectividadContraPokemon(defensor);
        int ataque = atacante.getAttack();
        int defensa = defensor.getDefense();
        int poder = this.getPoder();
        
        
        double daño = (poder * ataque / defensa) * efectividad * 0.5;
        daño *= (0.85 + Math.random() * 0.15);
        
        return (int)Math.round(daño);
    }
}