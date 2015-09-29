package Precedencia;

/**
 * Clase para excepciones de fichas inválidas.
 */
public class ValorInvalidoException extends Exception{

    /**
     * Constructor vacío.
     */
    public ValorInvalidoException() {
        super();
    }

    /**
     * Constructor que recibe un mensaje para el usuario.
     * @param mensaje un mensaje que verá el usuario cuando ocurra la excepción.
     */
    public ValorInvalidoException(String mensaje) {
        System.err.println(mensaje);
    }
}
