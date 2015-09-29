import java.sql.*;
import javax.swing.*;
import java.sql.Connection;

public class Conector{

	private Connection c = null;

	public static Connection conectar(){
		try{
			Class.forName("org.sqlite.JDBC");
			Connection c = DriverManager.getConnection("jdbc:sqlite:Experimento.bdd");
			JOptionPane.showMessageDialog(null,"Conexion exitosa");
			return c;
		} catch (Exception exe){
			System.out.println("Te odio" + exe);
			System.exit(1);
		}
		return null;
	}

}