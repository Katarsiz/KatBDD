package Base_De_Datos.test;
import Base_De_Datos.*;
import org.junit.Assert;
import org.junit.Test;
import java.sql.*;


public class TestBaseDeDatos{

	BaseDeDatos bdd;
	private DataSet ds1, ds2, ds3, dsE;
	private static final String tabla1 = "PINTURAS", tabla2 = "AUTORES", tabla3 = "GALERIAS";

	public TestBaseDeDatos() throws Exception{
		bdd = new BaseDeDatos();
		ds1 = new DataSet("La Creacion de Adan","Undefined","Fresco","Miguel Angel","Renacentista","Capilla Sixtina");
		ds2 = new DataSet("Salvador Dalí","1904-05-11","1989-01-23","168","La Persistencia de la Memoria","4");
		ds3 = new DataSet("Centre Pompidou","Francia","102500","1977-01-31","Industrialista","Arte Moderno");		
	}

	@Test
	public void testAgrega() throws UnrealException, SQLException, DatoInvalidoException, Exception {			
		bdd.agrega(ds1,"PINTURAS");		
		Assert.assertTrue(bdd.contiene(ds1,"PINTURAS"));			
		try{		
			dsE = new DataSet("1560","4684","ofqbiw","a",".-+´'","7.7");
			bdd.agrega(dsE,"PINTURAS");
		} catch (DatoInvalidoException x) {
			Assert.assertTrue(true);
			System.out.println(x);
		}
		try{	
			bdd.agrega(ds1,"PINTURAS");
		} catch (UnrealException x) {
			Assert.assertTrue(true);
			System.out.println(x);
		} bdd.elimina("Miguel Angel","AUTOR","PINTURAS");
	}

	@Test
	public void testElimina() throws UnrealException, SQLException, DatoInvalidoException, Exception{
		Assert.assertFalse(bdd.contiene(ds2,"AUTORES"));
		bdd.agrega(ds2,"AUTORES");
		bdd.elimina("168","PIECES","AUTORES");
		Assert.assertTrue(!bdd.contiene(ds2,"AUTORES"));
	}

	@Test
	public void testBusca() throws UnrealException, SQLException, DatoInvalidoException, Exception{
		DataSet ds4 = new DataSet("A","B","C","D","E","F");
		DataSet ds5 = new DataSet("G","H","C","J","K","L");
		Lista<DataSet> l= new Lista<DataSet>();
		l.agrega(ds4);
		l.agrega(ds5);
		bdd.agrega(ds4,"PINTURAS");
		bdd.agrega(ds5,"PINTURAS");
		String [] mod =	{"TECH"};
		String [] val = {"C"};
		Lista<DataSet> comparar = bdd.busca("PINTURAS",mod,val);
		System.out.println(comparar);
		System.out.println(l);
		Assert.assertTrue(l.toString().equals(comparar.toString()));
		bdd.elimina("C","TECH","PINTURAS");
	}

	@Test
	public void testContiene() throws UnrealException, SQLException, DatoInvalidoException, Exception{
		Assert.assertFalse(bdd.contiene(ds1,"PINTURAS"));
		Assert.assertFalse(bdd.contiene(ds2,"AUTORES"));
		Assert.assertFalse(bdd.contiene(ds3,"GALERIAS"));
		bdd.agrega(ds1,"PINTURAS");
		bdd.agrega(ds2,"AUTORES");
		bdd.agrega(ds3,"GALERIAS");
		Assert.assertTrue(bdd.contiene(ds1,"PINTURAS"));
		Assert.assertTrue(bdd.contiene(ds2,"AUTORES"));
		Assert.assertTrue(bdd.contiene(ds3,"GALERIAS"));
		bdd.elimina("La Creacion de Adan","TITLE","PINTURAS");
		bdd.elimina("La Persistencia de la Memoria","FAMOUS","AUTORES");
		bdd.elimina("Arte Moderno","FOCUS","GALERIAS");
		Assert.assertTrue(!bdd.contiene(ds1,"PINTURAS"));
		Assert.assertTrue(!bdd.contiene(ds2,"AUTORES"));
		Assert.assertTrue(!bdd.contiene(ds3,"GALERIAS"));

	}

	@Test
	public void testActualizar() throws UnrealException, SQLException, DatoInvalidoException, Exception{
		bdd.agrega(ds1,"PINTURAS");
		String [] mod =	{"TECH","TITLE","UBIC"};
		String [] val = {"C","Prueba","Desconocida"};
		bdd.actualizar("La Creacion de Adan","PINTURAS",mod,val);
		DataSet dv = new DataSet("Prueba","Undefined","C","Miguel Angel","Renacentista","Desconocida");
		Assert.assertTrue(bdd.contiene(dv,"PINTURAS"));
		bdd.elimina("Prueba","TITLE","PINTURAS");
	}
}