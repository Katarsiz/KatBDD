package Base_De_Datos;

public class DatoInvalidoException extends Exception{

	public DatoInvalidoException(){
		super();
	}

	public DatoInvalidoException(String mensaje){
		System.err.println(mensaje);
	}
	
}