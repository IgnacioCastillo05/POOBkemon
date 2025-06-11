package domain;

public class MovimientoForcejeo extends MovimientoFisico {
    private static final long serialVersionUID = 7775130181025684877L;

    public MovimientoForcejeo() {
        super("Forcejeo", new Tipo("Normal"), 50, 100, 1, 1, false, "físico", 
             "Causa daño al oponente, pero el usuario recibe la mitad del daño causado. Puede usarse cuando no hay PP disponibles.");
    }

    /**
     * Constructor de copia para crear una nueva instancia independiente.
     * @param original El MovimientoForcejeo original a copiar
     */
    public MovimientoForcejeo(MovimientoForcejeo original) {
        super(original);
    }

    @Override
    public String toString() {
        return "Movimiento Forcejeo: " + super.toString();
    }

    /**
     * Sobrescribimos el método aplicarEfecto para que el usuario reciba daño de retroceso
     */
    @Override
    public String aplicarEfecto(Pokemon usuario, Pokemon objetivo) {
        int daño = calcularDañoFisico(usuario, objetivo);
        objetivo.recibirDaño(daño);
        
        int dañoRetroceso = Math.max(1, daño / 2);
        
        int psAntesDeRetroceso = usuario.getPs();
        usuario.recibirDaño(dañoRetroceso);
        
        StringBuilder mensaje = new StringBuilder();
        mensaje.append(objetivo.getName()).append(" recibió ").append(daño).append(" de daño.");
        
        if (psAntesDeRetroceso > 0 && usuario.getPs() <= 0) {
            mensaje.append(" ").append(usuario.getName())
                  .append(" recibió ").append(dañoRetroceso)
                  .append(" de daño por retroceso y se debilitó.");
        } else {
            mensaje.append(" ").append(usuario.getName())
                  .append(" recibió ").append(dañoRetroceso)
                  .append(" de daño por retroceso.");
        }
        
        return mensaje.toString();
    }

    /**
     * Sobrescribimos el método usarMovimiento para que no reduzca los PP
     */
    @Override
    public boolean usarMovimiento() {
        return true;
    }

    /**
     * Método privado que calcula el daño físico, copiado del MovimientoFisico
     */
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