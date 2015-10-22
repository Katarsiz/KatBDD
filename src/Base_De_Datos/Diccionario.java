package Base_De_Datos;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, permitiendo (en general, dependiendo de qué tan bueno
 * sea su método para generar huellas digitales) agregar, eliminar, y buscar
 * valores en tiempo <i>O</i>(1) (amortizado) en cada uno de estos casos.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Clase privada para iteradores de diccionarios. */
    private class Iterador implements Iterator<V> {

        /* En qué lista estamos. */
        private int indice;
        /* Diccionario. */
        private Diccionario<K,V> diccionario;
        /* Iterador auxiliar. */
        private Iterator<Diccionario<K,V>.Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador(Diccionario<K,V> diccionario) {
        		this.diccionario = diccionario;
        		this.iterador = null;
        		while(indice < diccionario.entradas.length && diccionario.entradas[indice]==null)
        				indice++;
        		if(indice < diccionario.entradas.length)
        				iterador = diccionario.entradas[indice].iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        public boolean hasNext() {
             return this.iterador!=null;
        }

        /* Regresa el siguiente elemento. */
        public V next() {
            if (this.iterador==null)
            		throw new IllegalStateException();
            V r = iterador.next().valor;
            if (iterador.hasNext())
            		return r;
            iterador = null;
            indice++;
            while(indice < diccionario.entradas.length && diccionario.entradas[indice]==null)
        				indice++;
        		if(indice < diccionario.entradas.length)
        				iterador = diccionario.entradas[indice].iterator();
        		return r;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Tamaño mínimo; decidido arbitrariamente a 2^6. */
    private static final int MIN_N = 64;

    /* Máscara para no usar módulo. */
    private int mascara;
	    /* Huella digital. */
    private HuellaDigital<K> huella;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores*/
    private int elementos;

    /* Clase para las entradas del diccionario. */
    private class Entrada {
        public K llave;
        public V valor;
        public Entrada(K llave, V valor) {
            this.llave = llave;
            this.valor = valor;
        }
    }

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private Lista<Entrada>[] nuevoArreglo(int n) {
        Lista[] arreglo = new Lista[n];
        return (Lista<Entrada>[])arreglo;
    }

    /**
     * Construye un diccionario con un tamaño inicial y huella digital
     * predeterminados.
     */
    public Diccionario() {
    		this.huella = (s) -> s.hashCode();
    		entradas = nuevoArreglo(64);
    		mascara = this.mascara();
    }

    /**
     * Construye un diccionario con un tamaño inicial definido por el usuario, y
     * una huella digital predeterminada.
     * @param tam el tamaño a utilizar.
     */
    public Diccionario(int tam) {
         this.huella = (s) -> s.hashCode();
         tam = this.pot(tam);
         if (tam>=64)
    				entradas = nuevoArreglo(tam);	
    		else 
    				entradas = nuevoArreglo(64);
    		mascara = this.mascara();
    }

    /**
     * Construye un diccionario con un tamaño inicial predeterminado, y una
     * huella digital definida por el usuario.
     * @param huella la huella digital a utilizar.
     */
    public Diccionario(HuellaDigital<K> huella) {
         this.huella = huella;
         entradas = nuevoArreglo(64);
         mascara = this.mascara();
    }

    private int mascara(){
    		return this.entradas.length-1;
    }

    private int pot(int n){
    		int i = 0;
    		while (i<n)
    				i = (i<<1) | 1;
    		return (i<<1) | 1;
    }

    /**
     * Construye un diccionario con un tamaño inicial, y un método de huella
     * digital definidos por el usuario.
     * @param tam el tamaño del diccionario.
     * @param huella la huella digital a utilizar.
     */
    public Diccionario(int tam, HuellaDigital<K> huella) {
        this.huella = huella;
        tam = this.pot(tam);
         if (tam>=64)
    				entradas = nuevoArreglo(tam);	
    		else 
    				entradas = nuevoArreglo(64);
    		mascara = this.mascara();
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     */
    public void agrega(K llave, V valor) {
    		int indice = indice(llave);
    		Lista<Entrada> lista = buscaLista(indice, true);
    		Entrada e = buscaEntrada(llave,lista);
    		if (e!=null)
    				e.valor = valor;
    		else{
    				lista.agrega(new Entrada(llave, valor));
    				this.elementos++;
    		} 
    		if (carga()>MAXIMA_CARGA){
    				Lista<Entrada> [] temporal = this.entradas;
    				this.entradas = nuevoArreglo(this.entradas.length*2);
    				this.mascara = this.entradas.length-1;  
    				this.elementos = 0;
    				for(Lista<Entrada> l : temporal)
    						if (l!=null)
    								for (Entrada ep : l)
    										this.agrega(ep.llave,ep.valor);   				
    		}
    }

    private Entrada buscaEntrada(K llave, Lista<Entrada> lista){
    		if (lista.getElementos()==0) 
    				return null;
    		for(Entrada ep : lista)
    				if(ep.llave.equals(llave))
    						return ep;
    		return null;
    }

    private void creceArreglo(){
    		this.entradas = nuevoArreglo(this.entradas.length*2);
    		this.mascara = this.entradas.length-1;    		
    }

    private Lista<Entrada> buscaLista(int indice, boolean crear){
    		Lista <Entrada> lista = null;
    		if (this.entradas[indice]!=null)
    				return this.entradas[indice];
    		if (crear==true){    				
    				this.entradas[indice] = new Lista<Entrada>();
    				return this.entradas[indice];
    		}
    		return lista;
    }

    private int indice(K llave){
    		int huellaD = huella.huellaDigital(llave);
    		int ind = huellaD & this.mascara;
    		return ind;
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        if (!this.contiene(llave))
        		throw new NoSuchElementException();
        int indice = indice(llave);
    	  Lista<Entrada> lista = buscaLista(indice, false);
    	  for (Entrada ep : lista)
    				if (ep.llave.equals(llave))
    						return ep.valor;
    	  throw new NoSuchElementException();
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <tt>true</tt> si la llave está en el diccionario,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(K llave) {
        	int indice = indice(llave);
    		Lista<Entrada> lista = buscaLista(indice, false);
    		if (lista==null)
    				return false;
    		for (Entrada ep : lista)
    				if (ep.llave.equals(llave))
    						return true;
    		return false;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) throws NoSuchElementException{
         int indice = indice(llave);
    		Lista<Entrada> lista = buscaLista(indice, false);
    		if (lista==null)
    				throw new NoSuchElementException();
    		Entrada e = buscaEntrada(llave,lista);
    		if (e!=null){
    				this.elementos--;
    				lista.elimina(e);
    				if (lista.getElementos()==0)
    						entradas[indice] = null;
    				return;
    		} throw new NoSuchElementException();
    }

    /**
     * Regresa una lista con todas las llaves con valores asociados en el
     * diccionario. La lista no tiene ningún tipo de orden.
     * @return una lista con todas las llaves.
     */
    public Lista<K> llaves() {
        Lista<K> lista = new Lista<K>();
        for(Lista<Entrada> l : this.entradas)
        			if (l!=null)
    						for (Entrada ep : l)
    								lista.agrega(ep.llave);  
    	  return lista;
    }

    /**
     * Regresa una lista con todos los valores en el diccionario. La lista no
     * tiene ningún tipo de orden.
     * @return una lista con todos los valores.
     */
    public Lista<V> valores() {
        Lista<V> lista = new Lista<V>();
        for(Lista<Entrada> l : this.entradas)
        			if (l!=null)
    						for (Entrada ep : l)
    								lista.agrega(ep.valor);  
    	  return lista;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        int colisiones = 0 , colisionMayor = 0;
         for (Lista<Entrada> l : this.entradas){
        		if (l!=null)
        				colisiones += l.getElementos() - 1;
        		if (colisiones>colisionMayor)
        				colisionMayor = colisiones;
        	}
        return colisionMayor;
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        int colisiones = 0;
        for (Lista<Entrada> l : this.entradas)
        		if (l!=null)
        				colisiones += l.getElementos() - 1;
        return colisiones;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
    		double el = this.getElementos();
    		double lon = this.entradas.length;
    		double in = el/lon;
        return in;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        return this.elementos;
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar el diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new Iterador(this);
    }
}
