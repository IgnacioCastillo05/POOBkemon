package domain;

/**
 * Excepción personalizada para errores relacionados con la persistencia de datos
 * en el juego POOBkemon (guardado y carga de partidas).
 */
public class POOBkemonPersistenceException extends Exception {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructor con mensaje de error
     * @param message Mensaje descriptivo del error
     */
    public POOBkemonPersistenceException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje de error y causa
     * @param message Mensaje descriptivo del error
     * @param cause Excepción que causó este error
     */
    public POOBkemonPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor solo con causa
     * @param cause Excepción que causó este error
     */
    public POOBkemonPersistenceException(Throwable cause) {
        super(cause);
    }
}