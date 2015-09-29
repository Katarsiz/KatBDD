package Precedencia.test;

import Precedencia.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public class TestEcuaciones {

	private Random random;
	private Ecuaciones ec;	
	private String ecuacion;

	public TestEcuaciones(){
		ecuacion = ecuacionAleatoria();
		random = new Random();
		ec = new Ecuaciones(ecuacion);
	}

	private String ecuacionAleatoria(){
		String ecuacion = "sqrt(x)";
		return ecuacion;
	}

	/*
     * Prueba unitaria para {@link Ecuaciones#fichaValida}.
     */
	@Test public void testFichaValida() {
		String [] fichas = new String [] {"sqrt","cos","sin","tan","sec","csc","cot","^","+","-","*","/","(",")","x"};
		String ficha = "";
		Assert.assertTrue(ec.fichaValida(ficha)==(-1));
		ficha = random.nextInt(500) + "." + random.nextInt(500);
		Assert.assertFalse(ec.fichaValida(ficha)==(-1));
		ficha += "." + random.nextInt(500);
		Assert.assertTrue(ec.fichaValida(ficha)==(-1));
		for (String s : fichas)
			Assert.assertFalse(ec.fichaValida(s)==(-1));
  	 }
  	 
    /*
     * Prueba unitaria para {@link Ecuaciones#generarFichas}.
     */
  	  @Test public void testGenerarFichas() {  	  	
  	  	 String r = "";
 	       Lista <String> resultado = ec.generarFichas(this.ecuacion);
 	       for (String s : resultado){
 	       	r+=s;
 	       }
 	       ecuacion = ecuacion.replaceAll(" ","");
 	       Assert.assertTrue(r.equals(this.ecuacion)); 	       	
	   }

	 /*
     * Prueba unitaria para {@link Ecuaciones#comprobarGramatica}.
     */
  	  @Test public void testComprobarGramatica() {
 	       String p = ")";
 	       p += ecuacion + "(";
 	       try{ 	       	
 	       	ec.comprobarGramatica(p);
 	       	Assert.assertFalse(true);
 	       } catch (FichaInvalidaException exe){
 	       	Assert.assertTrue(true);
 	       	try{
 	       		ec.comprobarGramatica(ecuacion);
 	       		Assert.assertTrue(true);
 	       	} catch (Exception ex){
 	       		Assert.assertFalse(true);
 	       	}
 	       }
	   }
	   
    /*
     * Prueba unitaria para {@link Ecuaciones#evaluar}.
     */
  	  @Test public void testEvaluar() throws ValorInvalidoException{
       	 int a = random.nextInt(1) + 5;
       	 System.out.println("Evaluando " + a + " en " + ecuacion + "\nResultado : " + ec.evaluar(a));
       	 int b = a*a;
       	 int c = 2*a;
       	 int d = a*a*a;
       	 double k = Math.sin(45);
       	 Assert.assertTrue(ec.evaluar(a) == Math.sqrt(a));
  	  }

  	  @Test public void testIndeterminadas() {
  	  	try{
  	  		double d = ec.evaluar(-5);
  	  		Assert.assertFalse(false);
  	  	} catch (ValorInvalidoException exe) {
  	  		Assert.assertTrue(true);
  	  	}
  	  }
}
