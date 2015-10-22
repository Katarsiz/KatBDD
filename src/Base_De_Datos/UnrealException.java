package Base_De_Datos;

public class UnrealException extends Exception{

	public UnrealException(){
		super();
	}

	public UnrealException(String mensaje){
		System.err.println(mensaje);
	}
	
}