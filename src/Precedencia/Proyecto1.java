package Precedencia;

public class Proyecto1{

	private static Ecuaciones ec;
	private static Interfaz i;

	public static void main(String [] args) throws ValorInvalidoException, FichaInvalidaException{		
		i = new Interfaz();
		while (true){
			String funcion;
			double intervalo1, intervalo2, infi;
			double [] valoresX = new double[500], valoresY = new double[500];
			esperarEntrada();	
			i.aceptarEntrada();
			funcion = i.getFuncion();
			intervalo1 = i.intervalo1();
			intervalo2 = i.intervalo2();	
			infi = Math.max(Math.abs(intervalo1), Math.abs(intervalo2));
			//Sacamos el mínimo y el máximo
			double minimo, maximo;
			if (intervalo1>intervalo2){
				minimo = intervalo2;
				maximo = intervalo1;
			} else {
				minimo = intervalo1;
				maximo = intervalo2;
			}
			double variacion = Math.abs((maximo - minimo ) / 500);
			System.out.println(variacion);
			//Recorremos el intervalo en pequeñas variaciones para crear el arreglo de números.		
			int l = 0;			
			try{
				Ecuaciones ec = new Ecuaciones(funcion); //Nota : Examinar más tarde
			} catch (FichaInvalidaException fie){
				i.lanzarExcepcion ("Sintax");
				while (true);
			} ec = new Ecuaciones(funcion); // Por alguna razón que no alcanzo a comprender, si no hago esto => NullPointerException
			double max = 0, min = 0;
			System.out.println("lol");
			try{
				max = min = ec.evaluar(minimo);
			} catch (ValorInvalidoException v) {
					i.lanzarExcepcion("Math");
					while (true);
			}
			System.out.println(minimo + "," + maximo);
			for (double k = minimo ; k<maximo; k+= variacion){
				if (l==500)
					break;
				valoresX[l] = k;			
				try {
					valoresY[l] = ec.evaluar(k);						
				} catch (Exception exg){
					i.lanzarExcepcion("Math");
					while (true);
				}
				if (valoresY[l]>max){
					max = valoresY[l];
				} else if (valoresY[l] < min){
					min = valoresY[l];
				} else {		}
				if (l%10==0 || l%499 == 0)
					System.out.println("Valor: " + valoresX[l] + "," + valoresY[l]);
				l++;
			}	
			System.out.println( "Máximo : " + max + " Mínimo : " +  min);			
			double escalaX = 1228/(variacion*500);		
			double escalaY = escalaX * .5 * 1.364444;	
			System.out.println("Escala : " + escalaX + "|" + escalaY);
			for (int h = 0; h<500 ;h++){
				valoresX[h] = 650 + (valoresX[h]*escalaX);
				if (valoresY[h]<0){
					valoresY[h] = 450 + (Math.abs((valoresY[h]*escalaY)));
					continue;
				}
				valoresY[h] = 450 - (valoresY[h]*escalaY);
				if (h%10==0 || h%499 == 0)
					System.out.println("Valor: " + valoresX[h] + "," + valoresY[h]);
			}		
			i.generarGrafica(valoresX, valoresY);
		}
	}

	private static void esperarEntrada(){
		while (!i.hayEntrada()){		}
	}
}
