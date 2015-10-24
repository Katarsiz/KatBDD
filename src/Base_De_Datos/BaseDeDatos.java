package Base_De_Datos;
import java.sql.*;
/*
* Clase para manejar Base de Datos, envuelta en una clase.
* @author: González Rojas Julio Ezequiel
* Idea : Al abrir la base, es estructurada en esta clase para manejarla fácilmente,
* al modificar un objeto en la estructura, éste es actualizado en la base de Datos.
* La clase también se encarga de ligar la Base De Datos con el programa
*/

public class BaseDeDatos{

	private Connection c;
	private Statement s;
	private Tabla tablaP, tablaA, tablaG;	

	public BaseDeDatos() throws SQLException, ClassNotFoundException{
		conecta();
		nuevaBaseDeDatos();
		tablaP = new Tabla("TITLE","IMG","TECH","AUTOR","STYLE","UBIC","PINTURAS");
		tablaA = new Tabla("NAME","BORN","DEATH","PIECES","FAMOUS","SOLD","AUTORES");
		tablaG = new Tabla("DEN","PLACE","NUMBER","DATE","ARCH","FOCUS","GALERIAS");
	}

	// Si la Base De Datos no existe, es creada.
	public void nuevaBaseDeDatos() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		String tabla1 = "CREATE TABLE PINTURAS " +
						"(TITLE TEXT PRIMARY KEY	NOT NULL," +
						" IMG 				TEXT	NOT NULL UNIQUE," +
						" TECH				TEXT	NOT NULL," +
						" AUTOR				TEXT	NOT NULL," +
						" STYLE				TEXT 	NOT NULL," +
						" UBIC				TEXT 	NOT NULL DEFAULT \'Desconocida\')";		
		String tabla2 = "CREATE TABLE AUTORES " +
						"(NAME TEXT PRIMARY	KEY		NOT NULL," +
						" BORN				DATE	NOT NULL," +
						" DEATH				DATE	NOT NULL," +
						" PIECES			INT		NOT NULL CHECK (PIECES>0)," +
						" FAMOUS			TEXT 	NOT NULL UNIQUE," +
						" SOLD 				INT 	NOT NULL CHECK (SOLD>=0))";		
		String tabla3 = "CREATE TABLE GALERIAS " +
						"(DEN TEXT PRIMARY	KEY		NOT NULL," +
						" PLACE				TEXT	NOT NULL," +
						" NUMBER			INT 	NOT NULL CHECK (NUMBER>=0)," +
						" DATE				TEXT	NOT NULL," +
						" ARCH				TEXT 	NOT NULL," +
						" FOCUS				TEXT 	NOT NULL)";	
		try{
			s.executeUpdate(tabla1);
			s.executeUpdate(tabla2);
			s.executeUpdate(tabla3);
			DataSet ds1 = new DataSet("El Grito.jpg","El Grito","Oleo","Edvard Munch","Expresionismo","Noruega");
			DataSet ds2 = new DataSet("Pierre Auguste Renoir","1841-02-25","1919-12-03","220","La Posada de la Mere Antony","5");
			DataSet ds3 = new DataSet("OMR","Mexico","15700","1983-01-05","Modernista","Arte Contemporaneo");
		} catch (SQLException exe){
			System.out.println("Las tablas ya existen");
		} catch (Exception xe){
			System.out.println("Error");
		}
		System.out.println("Nueva Base De Datos Creada.");
	}

	// Conecta el programa Java con la base de datos, si la base no existe, la crea automáticamente.
	public void conecta(){
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Experimento.bdd");
			System.out.println("Database Opened Succesfully");
			s = c.createStatement();
			System.out.println("Statement Created Succesfully");
		} catch (Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      		System.exit(0);
		}
	}

	// Método busca para la base de datos.
	// @param conjunto de datos.
	// @throw UnrealException si el conjunto de datos no fue encontrado o está mal escrito.
	// @return conjunto de datos a mostrar al usuario.
	// 
	public Lista<DataSet> busca(String tabla, String [] condiciones, String [] valores) throws UnrealException,SQLException,DatoInvalidoException{
		Tabla table = tablaSeleccionada(tabla);
		Lista<DataSet> busqueda = new Lista<DataSet>();
		String search = table.instB(condiciones,valores);
		if (search.equals(";")){
			search = table.datos[0] + " IS NOT NULL;";
		}
		search = "SELECT * FROM " + table.denominacion + " WHERE " + search; 
		ResultSet rs = s.executeQuery(search);	
		while(rs.next()){
			String t1 = rs.getString(table.datos[0]);
			String t2 = rs.getString(table.datos[1]);
			String t3 = rs.getString(table.datos[2]);
			String t4 = rs.getString(table.datos[3]);
			String t5 = rs.getString(table.datos[4]);
			String t6 = rs.getString(table.datos[5]);
			DataSet dl = new DataSet(t1,t2,t3,t4,t5,t6);
			busqueda.agrega(dl);
		}
		if (busqueda==null){
			throw new UnrealException();
		} return busqueda;
	}

	public Lista<DataSet> busca2(String tabla, String condicion, String valor) throws UnrealException,SQLException,DatoInvalidoException{
		Tabla table = tablaSeleccionada(tabla);
		String search;		
		Lista<DataSet> busqueda = new Lista<DataSet>();
		search = "SELECT * FROM " + table.denominacion + " WHERE " + condicion;
		search += " LIKE '%" + valor + "%'";
		if (valor.equals("")){
			return busca(tabla,null,null);
		}		
		search +=";";
		ResultSet rs = s.executeQuery(search);	
		while(rs.next()){
			String t1 = rs.getString(table.datos[0]);
			String t2 = rs.getString(table.datos[1]);
			String t3 = rs.getString(table.datos[2]);
			String t4 = rs.getString(table.datos[3]);
			String t5 = rs.getString(table.datos[4]);
			String t6 = rs.getString(table.datos[5]);
			DataSet dl = new DataSet(t1,t2,t3,t4,t5,t6);
			busqueda.agrega(dl);
		}
		if (busqueda.getElementos()==0){
			throw new UnrealException();
		} return busqueda;
	}

	// Método contiene para la base de datos.
	// @param conjunto de datos.
	// @return true si el renglón de la tabla fue encontrado, false en otro caso.
	public boolean contiene(DataSet ds, String tabla)throws UnrealException,SQLException,DatoInvalidoException{
		Tabla table = tablaSeleccionada(tabla);
		ResultSet rs = s.executeQuery("SELECT * FROM " + table.denominacion);
		while(rs.next()){
			String t1 = rs.getString(table.datos[0]);
			String t2 = rs.getString(table.datos[1]);
			String t3 = rs.getString(table.datos[2]);
			String t4 = rs.getString(table.datos[3]);
			String t5 = rs.getString(table.datos[4]);
			String t6 = rs.getString(table.datos[5]);
			DataSet dl = new DataSet(t1,t2,t3,t4,t5,t6);
			if (ds.equals(dl)){
				return true;
			}
		}
		return false;
	}

	// Método agrega para la base de datos.
	// @param conjunto de datos y un String que es la tabla a la que será agregado.
	// @throw UnrealException si el conjunto de datos no fue encontrado o está mal escrito.
	// @throw Repeated si el renglón ya estaba en la Base De Datos.
	public void agrega(DataSet ds, String tabla) throws UnrealException,SQLException,DatoInvalidoException{
		Tabla table = tablaSeleccionada(tabla);
		if (this.contiene(ds, tabla))
			throw new UnrealException();		
		String agregado = table.instA(ds);
		s.executeUpdate(agregado);
	}

	// Método elimina para la base de datos.
	// @param conjunto de datos y un String que es la tabla de la que será eliminado.
	// @throw UnrealException si el conjunto de datos no fue encontrado o está mal escrito.
	public void elimina(String z, String condicion, String tabla) throws UnrealException,SQLException,DatoInvalidoException{
		Tabla table = tablaSeleccionada(tabla);
		if (!this.contenido(z, condicion, tabla)){
			throw new UnrealException();		
		} String eliminado = table.instE(z,condicion);
		s.executeUpdate(eliminado);
	}

	// Método limpia para la base de datos.
	// Borra todo lo que esté en la Base De Datos.
	public void limpia() throws Exception{
		String dropping = "DROP TABLE PINTURAS";
		s.executeUpdate(dropping);
		String dropping2 = "DROP TABLE AUTORES";
		s.executeUpdate(dropping2);
		String dropping3 = "DROP TABLE GALERIAS";
		s.executeUpdate(dropping3);
		System.out.println("La Base De Datos fue vaciada exitósamente");
	}

	// Método contiene auxiliar para la base de datos.
	// @param z = elemento a comprobar ; condicion = columna en la que buscar.
	// @return true si el elemento a buscar fue encontrado, false en otro caso.
	public boolean contenido(String z, String condicion, String tabla)throws UnrealException,SQLException,DatoInvalidoException{
		Tabla table = tablaSeleccionada(tabla);
		ResultSet rs = s.executeQuery("SELECT * FROM " + table.denominacion);
		while(rs.next()){
			String t1 = rs.getString(condicion);
			if (t1.equals(z)){
				return true;
			}
		}
		return false;
	}

	// Método actualizar para la base de datos.
	// @param conjunto de datos.
	// @throw UnrealException si el conjunto de datos no fue encontrado o está mal escrito.
	// @return conjunto de datos a mostrar al usuario.
	public void actualizar(String iden, String tabla, String [] mod, Object [] ob) throws UnrealException,SQLException,DatoInvalidoException{
		Tabla table = tablaSeleccionada(tabla);	
		String actualizado = table.instK(iden, mod, ob);
		s.executeUpdate(actualizado);
	}

	private Tabla tablaSeleccionada(String t){
		switch (t){
			case "PINTURAS":
				return tablaP;
			case "AUTORES":
				return tablaA;
			case "GALERIAS":
				return tablaG;
		} return null; //Nunca sucede.
	}

	// Método para cerrar la base de datos.
	public void salir() throws SQLException, ClassNotFoundException{
		s.close();
		c.close();
		System.out.println("Cerrando Base De Datos");
	}

	private class Tabla{

		public String denominacion;
		public String [] datos;
		public Lista<DataSet> renglones;

		public Tabla(String s1, String s2, String s3, String s4, String s5, String s6, String n){
			datos = new String[6];
			denominacion = n;
			datos[0] = s1;
			datos[1] = s2;
			datos[2] = s3;
			datos[3] = s4;
			datos[4] = s5;
			datos[5] = s6;
		}

		public String instA(DataSet ds){
			String rt = "INSERT INTO " + denominacion + "(" + datos[0] 
						+ "," + datos[1] + "," + datos[2] + "," + datos[3]
						+ "," + datos[4] + "," + datos[5] + ") \n" 
						+ "VALUES " + ds.toString() + ";";
			return rt;
		}

		public String instE(String k, String z){
			String rt = "DELETE FROM " + this.denominacion + " WHERE " + z + "=" + DataSet.esString(k) + ";";
			return rt;
		}

		public String instK(String Id, String [] mod, Object [] ob){
			String rt = "UPDATE " + this.denominacion + " set ";
			for (int i=0; i<ob.length; i++){
				rt += mod[i] + "=" + DataSet.esString(ob[i]);
				if (i!=ob.length-1){
					rt += ",";
				}
			} rt += " where " + datos[0] + "=\'" + Id + "\';";
			return rt;
		}

		public String instB(String [] cond, String[] values){
			String rt = "";
			if (values==null || cond==null){
				return ";";
			}
			for(int i=0; i<values.length; i++){
				if(values[i].equals("")){
					continue;
				}
				if (i!=0){
					rt += " AND ";
				}
				if (esNumero(values[i])){
					if (i<values.length-1 && esNumero(values[i+1])){
						rt += cond[i] + " BETWEEN " + Integer.parseInt(values[i]) + " AND " + Integer.parseInt(values[i+1]) + " ";
						i++;						
						continue;
					} rt += cond[i] + "=" + Integer.parseInt(values[i]);
					continue;
				}
				if (esFecha(values[i])){
					if (i<values.length-1 && esFecha(values[i+1])){
						rt += cond[i] + " BETWEEN \'" + values[i] + "\' AND \'" + values[i+1] + "\'";
						i++;
						continue;
					} rt += cond[i] + "=\'" + values[i] + "\'";
					continue;
				}
				rt += cond[i] + "=\'" + values[i] + "\'";				
			}
			return rt +=";";
		}

		//Determina si un dato es fecha
		private boolean esFecha(String s){
			String [] ff = s.split("-");
			try{
				for(String g : ff) {
					int t = Integer.parseInt(g);	
				}
				System.out.println("Este dato es una fecha");
				return true;
			} catch (Exception exe){
				return false;
			} 
		}

		private boolean esNumero(String s){
			try{
				double t = Double.parseDouble(s);
				return true;
			} catch (Exception exe){
				return false;
			} 
		}
	}
}