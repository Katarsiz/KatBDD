package Base_De_Datos;

/*
* Clase para manejar conjuntos de datos, envueltos en una clase.
* @author: González Rojas Julio Ezequiel
*/

public class DataSet{

	//Los renglones de las tablas de la Base De Datos son abstraidos como arreglos de Objetos.
	//Deben ser puestos en orden, pero no nos preocupamos por eso, pues el usuario está
	//encadenado a hacer lo que la interfaz le permita hacer.
	private Object [] datos;

	public DataSet(Object o1,Object o2,Object o3,Object o4,Object o5,Object o6) throws UnrealException, DatoInvalidoException{
		datos = new Object[6];
		if (o1.toString().equals("")){
			throw new UnrealException();
		}
		datos[0] = esString(o1);
		if (esNumero(datos[0].toString())){
			throw new DatoInvalidoException();
		}
		datos[1] = esString(o2);
		datos[2] = esString(o3);
		datos[3] = esString(o4);
		datos[4] = esString(o5);
		datos[5] = esString(o6);
	}

	private boolean esNumero(String s){
		try{
			double t = Double.parseDouble(s);
			return true;
		} catch (Exception exe){
			return false;
		} 
	}

	public static Object esString(Object o){
		try{
			int z = Integer.parseInt((String) o);
			return z;
		} catch (Exception exe){
			return "\'" + o.toString() + "\'";
		}
	}

	public String [] getAr(){
		String [] r = {datos[0].toString(),datos[1].toString(),datos[2].toString(),
						datos[3].toString(),datos[4].toString(),datos[5].toString()};
		return r;
	}

	public boolean equals(DataSet ds){
		for(int i=0; i<ds.datos.length;i++){
			if (!(this.datos[i].equals(ds.datos[i]))){
				return false;
			}
		} return true;
	}

	public String toString(){
		String cad = "(" + datos[0].toString() + "," + datos[1].toString()
					+ "," + datos[2].toString() + "," + datos[3].toString()
					+ "," + datos[4].toString() + "," + datos[5].toString() + ")";
		return cad;
	}

	public String getID(){
		return datos[0].toString();
	}	
}