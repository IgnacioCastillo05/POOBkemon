package domain;

import java.io.Serializable;

public interface EfectoEstadoStrategy extends Serializable {
    /**
     * Aplica el efecto de estado a un Pokemon al final del turno
     * @param pokemon El Pokemon afectado por el estado
     * @return Mensaje descriptivo del efecto aplicado
     */
    String aplicar(Pokemon pokemon);
    
    /**
     * Determina si el Pokemon puede atacar con su estado actual
     * @param pokemon El Pokemon afectado
     * @return true si puede atacar, false si no puede
     */
    boolean puedeAtacar(Pokemon pokemon);
    
    /**
     * Procesa lo que ocurre cuando el Pokemon intenta atacar (ej: confusión)
     * @param pokemon El Pokemon que intenta atacar
     * @return Mensaje resultante o null si puede atacar normalmente
     */
    String procesarIntentarAtacar(Pokemon pokemon);
    
    /**
     * Se llama cuando el Pokemon es curado de este estado
     * @param pokemon El Pokemon curado
     */
    void alCurar(Pokemon pokemon);
}