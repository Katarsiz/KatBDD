package Base_De_Datos;
import java.sql.*;

public class Proyecto2{

	public static void main(String [] args) throws Exception{
		Conector c = new Conector();
		c.conecta();
		Interfaz i = new Interfaz();
	}

}