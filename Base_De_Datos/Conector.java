package Base_De_Datos;
import java.sql.*;

public class Conector{

	Connection c;
	Statement s;

	public void conecta(){
		try{			
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Experimento.bdd");
			System.out.println("Database Opened Succesfully");
		} catch (Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      		System.exit(0);
		}
	}

	public SearchSet busca(SearchSet ss){
		return null;
	}

	public void agrega(){
		return;
	}

	public void elimina(){
		return;
	}

	public void limpia(){
		return;
	}

	public void actualizarBaseDeDatos(){
		return;
	}

	private class SearchSet{

		private Object o = null;

	}

}