package Precedencia;

/**
 * Clase para excepciones de fichas inválidas.
 */
public class FichaInvalidaException extends Exception{

    /**
     * Constructor vacío.
     */
    public FichaInvalidaException() {
        super();
    }

    /**
     * Constructor que recibe un mensaje para el usuario.
     * @param mensaje un mensaje que verá el usuario cuando ocurra la excepción.
     */
    public FichaInvalidaException(String mensaje) {
        System.err.println(mensaje);
    }
}
