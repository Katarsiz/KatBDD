package Precedencia;

public class Ecuaciones{

	private String ecuacion;
	private ArbolSintactico arbol;

	//Constructor que recibe una ecuación y genera el arbol sintáctico
	public Ecuaciones(String e) throws FichaInvalidaException{
		arbol = new ArbolSintactico(); 
		Lista<String> l = generarFichas(e);
		Lista<Token> fichas = new Lista<Token>();
		for (String s : l){
			fichas.agrega(new Token(s));
		}
		for (Token t : fichas){
			arbol.agrega2(t);
		}
		System.out.println(arbol);
	}

	/*	Genera una lista de fichas a partir de una cadena, asegurándose de que
	*	todas sean válidas.
	* @ throw ExcepcionFichaInvalida si alguna subinstancia de la cadena es inválida.
	* @ return Lista<String> con las fichas válidas, en el orden de aparición.
	*/
	public Lista<String> generarFichas(String data) throws FichaInvalidaException{
		data = data.replaceAll(" ","");								//Reemplazamos todo espacio por un espacio vacío.
		Lista<String> fichaje = new Lista<String>();
		char [] ec = new char [data.length()];
		ec = data.toCharArray();							//Convertimos la instancia String en un arreglo de caracteres.
		String actual = "";
		for (int i = 0; i<ec.length; i++){
			if (esNumero(ec[i])){
				actual += ec[i];								//Si el caracter es un número, lo sumamos a la cadena actual
				if (i<(ec.length-1) && esNumero(ec[i+1])){	//Si el siguiente caracter es un número, sólo continuamos
					continue;
				} else {
					if (fichaValida(actual)!=(-1)){
						fichaje.agrega(actual);				//Si el siguiente caracter no es un número, agregamos el "número"
					}			
					actual = "";								//a la lista, reseteamos la cadena actual y continuamos.
					continue;								//Nos aseguramos así de que los números estén bien delimitados.
				}
			} actual += ec[i];
			if (fichaValida(actual)!=(-1)){
				fichaje.agrega(actual);						//Si es una ficha válida => es un operador, un paréntesis o una función
				actual = "";									//(La función puede ser resultado de sumar caracteres anteriores que no sean
			}												//números o operadores) . Lo reseteamos y lo agregamos a la lista.
		}
		if (actual.length()>4 && fichaValida(actual)==-1){
			throw new FichaInvalidaException();
		}
		return fichaje;										//Cabe la posibilidad de que sea parte de una función, lo sumamos al actual.
	}

	private boolean esNumero(char c){
		switch (c){
			case '0':	case '1':		case '2':		case '3':	case '4':	case '5': 	case '6':	case '7':		case '8':	case '9':	case '.':
				return true;
		} return false;
	}

	private static boolean esNumero(String s){
		try{
			double dato = Double.parseDouble(s);
			return true;
		} catch (Exception exe){
			return false;
		}
	}

	/*	Determina si una instancia string es una ficha válida en nuestra gramática
	* @ return <tt> 1 </tt> si la cadena recibida es un número válido.
	* <tt> 2 </tt> si es un operador , <tt> 3 </tt> si es un paréntesis identado a voluntad.
	* <tt> 4 </tt> si es una función trigonométrica,  <tt> -1 </tt> en cualquier otro caso
	*/
	public static int fichaValida(String data){
		try{			
			double dato = Double.parseDouble(data);
			return 1;			//Identificamos un número válido.
		} catch (Exception exe) {
			switch (data){
				case "x":	
					return 1;	//Identificamos las variables.
				case "^":	case "+":	case "-":	case "*":	case "/":											
					return 3;	// Identificamos los operadores binarios
				case "sin":	case "cos":	case "tan":	case "sec":	case "csc":	case "cot":	case "sqrt":
					return 4;	// Identificamos las funciones trigonométricas y raíz cuadrada.
				case "(":	case ")": 		
					return 6;	// Identificamos el término de paréntesis para determinar las subraíces				
			}
		} return -1;				//Regresamos entero negativo si la ficha no es válida.
	}

	/*	Determina si una instancia string (ecuación) es válida en nuestra gramática
	* @throws ExcepcionFichaInvalida si la "ecuación" no es válida
	*/
	public void comprobarGramatica(String data) throws FichaInvalidaException{
		Lista<String> lista = generarFichas(data);
		String [] fichas = new String[lista.getLongitud()];
		int p = 0;
		for (String s : lista){
			fichas[p++] = s;
		}
		p = 0;
		for (int i = 0; i<fichas.length; i++){
			if (fichaValida(fichas[i])==1){
				try{
					if (i==0 || i==fichas.length){
						continue;
					} 
					if ((fichas[i+1].equals("*") || fichas[i+1].equals("/") || fichas[i+1].equals("-") || fichas[i+1].equals("+") || fichas[i+1].equals("^"))
						&& (!fichas[i-1].equals("*") && !fichas[i-1].equals("/") && !fichas[i-1].equals("^"))){
							;
					} else {
						throw new FichaInvalidaException();
					}
				} catch (Exception exe){	}
			}
			switch (fichas[i]){
				case "+":	case "-":	case "*":	case "/":	case "^":
					try{
						switch (fichas[i+1]){
							case "*":	case "/":
							throw new FichaInvalidaException();
						}
					} catch (Exception exe){
						throw new FichaInvalidaException();
					}
				break;
				case "sin":		case "cos":		case "tan":		case "csc":		case "sec":		case "cot":		case "sqrt":
					try{
						if (!fichas[i+1].equals("("))
							throw new FichaInvalidaException();
					} catch (Exception exe) {
						throw new FichaInvalidaException();
					}
				break;
				case "(":
					p++;
				break;
				case ")":
					if (--p<=0){
						i = fichas.length;
						break;
					}
					try{
						if (!(fichaValida(fichas[i-1])==1) && !(fichas[i-1].equals(")")))
							throw new FichaInvalidaException();
					} catch (Exception exe) {
						throw new FichaInvalidaException();
					} 				
			}
		} if (p!=0) throw new FichaInvalidaException();
	}

	/*	Evalua el entereo propocionado en la función proporcionada
	* @ return resultado de la ecuación en el valor proporcionado
	*/
	public double evaluar(double valor) throws ValorInvalidoException{
		return arbol.resultado(valor);
	}

	private class Token implements Comparable<Token>{

		private String valor;
		private byte tipo;

		@Override
		public int compareTo(Token t){
			if (t==null)
				return this.tipo;
			return this.tipo-t.tipo;
		}

		public Token(String s){
			valor = s;
			if (s==null)
				valor = null;
			try{			
				double dato = Double.parseDouble(s);
				tipo = 1;			//Identificamos un número válido.
			} catch (Exception exe) {
				switch (s){
					case "x":	
						tipo = 1;	//Identificamos las variables.
					break;
					case "*":	case "/":	case "^":									
						tipo = 2;	// Identificamos los operadores binarios
					break;
					case "+":	case "-":	
						tipo = 3;
					break;
					case "sin":	case "cos":	case "tan":	case "sec":	case "csc":	case "cot":	case "sqrt":
						tipo = 4;	// Identificamos las funciones trigonométricas y raíz cuadrada.
					break;
					case "(":	case ")": 		
						tipo = 5;	// Identificamos el término de paréntesis para determinar las subraíces	
					break;			
				}
			}
		}		
	}

	private class ArbolSintactico{

		private VerticeArbolSintactico raiz;
		private VerticeArbolSintactico ultimoAgregado;
		private int elementos;

		public ArbolSintactico(){
		}

		private double resultado(double x) throws ValorInvalidoException{
			VerticeArbolSintactico v = ultimoAgregado, padre = ultimoAgregado;
			while (v!=null){
				v = v.padre;
				if (v!=null){
					padre = v;
				}
			}
			return resultado(x, padre);
		}

		private double resultado(double x, VerticeArbolSintactico v) throws ValorInvalidoException{
			if (v==null)
				return 0;
			if (fichaValida(v.elemento.valor)==1){
				double data = 0;
				if (esNumero(v.elemento.valor)){
					data = Double.parseDouble(v.elemento.valor);
					return data;
				} return x;
			}
			switch (v.elemento.valor){
				case "+":
					return resultado(x,v.izquierdo) + resultado(x,v.derecho);
				case "-":
					return resultado(x,v.izquierdo) - resultado(x,v.derecho);
				case "*":
					return resultado(x,v.izquierdo) * resultado(x,v.derecho);
				case "/":
					return resultado(x,v.izquierdo) / resultado(x,v.derecho);
				case "^":
					return Math.pow(resultado(x,v.izquierdo),resultado(x,v.derecho));	
				case "sin":
					return Math.sin(resultado(x,v.derecho));		
				case "cos":
					return Math.cos(resultado(x,v.derecho));	
				case "tan":
					return Math.tan(resultado(x,v.derecho));	
				case "cot":
					return 1/Math.tan(resultado(x,v.derecho));	
				case "csc":
					return 1/Math.sin(resultado(x,v.derecho));	
				case "sec":
					return 1/Math.cos(resultado(x,v.derecho));	
				case "sqrt":
					double ent = resultado(x,v.derecho);
					if (ent<0) {
						throw new ValorInvalidoException();
					}
					return Math.sqrt(resultado(x,v.derecho));	
			} return 0;
		}

		public void agrega2(Token t){
			VerticeArbolSintactico nuevo = new VerticeArbolSintactico (t);
			if (elementos==0){
				raiz = ultimoAgregado = nuevo;
				if (t.valor.equals("(")){
					VerticeArbolSintactico n = new VerticeArbolSintactico(null);
					n.padre = nuevo;
					nuevo.derecho = n;
					ultimoAgregado = n;
				}
				elementos++;
				return;
			} 
			if (ultimoAgregado.elemento!=null){
			}
			elementos++;		
			if (ultimoAgregado.elemento==null ){
				if (t.valor.equals("(")){
					VerticeArbolSintactico n = new VerticeArbolSintactico(null);
					ultimoAgregado.elemento = t;
					n.padre = ultimoAgregado;
					ultimoAgregado.derecho = n;
					ultimoAgregado = n;
					return;
				}
				ultimoAgregado.elemento = t;
				return;
			}
			if (ultimoAgregado.elemento.valor.equals("-") && t.valor.equals("+")
				&& !(ultimoAgregado.hayDerecho())){
				return;
			}else  if (ultimoAgregado.elemento.valor.equals("-") && t.valor.equals("-") 
				&& !(ultimoAgregado.hayDerecho())){
				ultimoAgregado.elemento.valor = "+";
				return;
			}else if (ultimoAgregado.elemento.valor.equals("+") && t.valor.equals("-") 
				&& !(ultimoAgregado.hayDerecho())){
				ultimoAgregado.elemento.valor = "-";
				return;
			}
			if (ultimoAgregado.elemento.valor.equals("+") && t.valor.equals("+") 
				&& !(ultimoAgregado.hayDerecho())){
				return;
			}			
			if (t.valor.equals("(")){
				VerticeArbolSintactico n = new VerticeArbolSintactico(null);
				ultimoAgregado.derecho = nuevo;
				nuevo.padre = ultimoAgregado;
				nuevo.derecho = n;
				n.padre = nuevo;	
				ultimoAgregado = n;
				return;			
			}
			if (ultimoAgregado.elemento.valor.equals("-") && !ultimoAgregado.hayIzquierdo()){
				nuevo.padre = ultimoAgregado;
				ultimoAgregado.derecho = nuevo;
				ultimoAgregado = nuevo;
				return;
			}
			if (t.valor.equals(")")){
				VerticeArbolSintactico k = ultimoAgregado;
				while (!k.elemento.valor.equals("(")){
					k = k.padre;
				} 
				if (k.hayPadre()){
					ultimoAgregado = k.padre;
					k.padre.derecho = k.derecho;
					k.derecho.padre = k.padre;					
					return;
				} ultimoAgregado = k.derecho;
				ultimoAgregado.padre = null;
				return;			
			}			
			if (nuevo.elemento.compareTo(ultimoAgregado.elemento)<0){
				nuevo.padre = ultimoAgregado;
				if (!ultimoAgregado.hayIzquierdo()){					
					ultimoAgregado.izquierdo = nuevo;
				} else {
					ultimoAgregado.derecho = nuevo;
				} ultimoAgregado = nuevo;
				return;
			}
			if (ultimoAgregado.hayPadre()){
				if (nuevo.elemento.compareTo(ultimoAgregado.padre.elemento)>=0){
					VerticeArbolSintactico b = ultimoAgregado;
					while (b.elemento.compareTo(nuevo.elemento)<=0){
						if (b.padre==null)
							break;
						b = b.padre;						
					}
					if (b.elemento.compareTo(nuevo.elemento)>0){
						recorrerIzquierda(b.derecho,nuevo);
						return;
					}
					if (!b.hayDerecho()){
						b.derecho = nuevo;
						nuevo.padre = b;
						return;
					} 
					b.padre = nuevo;
					nuevo.izquierdo = b;
					ultimoAgregado = nuevo;
					return;
				} recorrerIzquierda(ultimoAgregado,nuevo);
				return;
			} else {
				recorrerIzquierda(ultimoAgregado,nuevo);
			}
		}
		
		private void recorrerIzquierda(VerticeArbolSintactico v, VerticeArbolSintactico b){
			b.izquierdo = v;
			b.padre = v.padre;
			v.padre = b;		
			if  (b.padre!=null){
				b.padre.derecho = b;
			}
			ultimoAgregado = b;
		}

		public String toString(){
			VerticeArbolSintactico v = ultimoAgregado, padre = ultimoAgregado;
			while (v!=null){
				v = v.padre;
				if (v!=null){
					padre = v;
				}
			} String r = "";
			r += st(padre);
			return r;
		}

		private String st(VerticeArbolSintactico v){
			if (v==null || v.elemento==null){
				return "";
			} String s = "";
			if (v.hayIzquierdo())
				s+="[I" + st(v.izquierdo) + "]";
			s+=v.elemento.valor;
			if (v.hayDerecho())
				s+="[D"+ st(v.derecho) + "]";
			return s;
		}

		private class VerticeArbolSintactico{

			private VerticeArbolSintactico padre;
			private VerticeArbolSintactico izquierdo;
			private VerticeArbolSintactico derecho;
			private Token elemento;

			private VerticeArbolSintactico(Token t){
				elemento = t;
			}

			private boolean hayIzquierdo(){
				return this.izquierdo!=null;
			}

			private boolean hayDerecho(){
				return this.derecho!=null;
			}

			private boolean hayPadre(){
				return this.padre!=null;
			}
		}
	}
}
