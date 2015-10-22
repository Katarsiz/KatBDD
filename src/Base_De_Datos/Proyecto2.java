package Base_De_Datos;
import java.sql.*;
import java.util.Scanner;


/*
* Proyecto 2 : Base De Datos
* @author: González Rojas Julio Ezequiel
*/

public class Proyecto2{

	public static void main(String [] args) throws Exception{
		Proyecto2 kj = new Proyecto2();
		BaseDeDatos bdd = new BaseDeDatos();
		Scanner et = new Scanner(System.in);
		Scanner xz = new Scanner(System.in);
		Lista<DataSet> pint = bdd.busca("PINTURAS",null,null);
		Lista<DataSet> aut = bdd.busca("AUTORES",null,null);
		Lista<DataSet> gal = bdd.busca("GALERIAS",null,null);
		System.out.println(pint);
		DataSet [] p = kj.listaArreglo(pint);
		DataSet [] a = kj.listaArreglo(aut);
		DataSet [] g = kj.listaArreglo(gal);
		System.out.println(p);
		Interfaz i = new Interfaz(bdd,p,a,g);
		while (true){
			Scanner e = new Scanner(System.in);
			String s = e.nextLine();
			if (s.equals("e")){
				bdd.limpia();
				System.exit(1);
			}
			Lista<DataSet> lk = bdd.busca(s,null,null);
			System.out.println(lk);
		}
	}

	public DataSet [] listaArreglo(Lista<DataSet> l){
		DataSet [] r = new DataSet[l.getElementos()];
		int f = 0;
		for(DataSet d : l){
			r[f++] = d;
		}
		return r;
	}

}