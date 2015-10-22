package Base_De_Datos;

import java.util.Iterator;
import java.util.NoSuchElementException;
import Base_De_Datos.IteradorLista;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las instancias de la clase Lista implementan la interfaz {@link
 * Coleccion}, y por lo tanto también la interfaz {@link Iterator}, por lo que
 * el recorrerlas es muy sencillo:</p>
 *
<pre>
    for (String s : l)
        System.out.println(s);
</pre>
 *
 * <p>Además, se le puede pedir a una lista una instancia de {@link
 * IteradorLista} para recorrerla en ambas direcciones.</p>
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo {

        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con el elemento especificado. */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
        
        private T getElemento(){
        			return this.elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador<T> implements IteradorLista<T> {

        /* La lista a iterar. */
        Lista<T> lista;
        /* Elemento anterior. */
        private Lista<T>.Nodo anterior;
        /* Elemento siguiente. */
        private Lista<T>.Nodo siguiente;

        /* El constructor recibe una lista para inicializar su siguiente con la
         * cabeza. */
        public Iterador(Lista<T> lista) {
        			this.lista=lista;
               this.anterior=null;
               this.siguiente=lista.cabeza;
        }

        /* Existe un siguiente elemento, si siguiente no es nulo. */
        @Override public boolean hasNext() {
        			if(this.siguiente!=null) return true; 
        			else return false; 
        }

        /* Regresa el elemento del siguiente, a menos que sea nulo, en cuyo caso
         * lanza la excepción NoSuchElementException. */
        @Override public T next() {
        			if(this.hasNext()) {   
        						T elemental = this.siguiente.elemento;
        						this.anterior=this.siguiente;		
        						this.siguiente=this.siguiente.siguiente; 	
        						return elemental;	
        			}
        			else throw new NoSuchElementException("Ya no hay más elementos que mostrar");
        }

        /* Existe un elemento anterior, si anterior no es nulo. */
        @Override public boolean hasPrevious() {
        			if(this.anterior!=null) return true; 
        			else return false; 
        }

        /* Regresa el elemento del anterior, a menos que sea nulo, en cuyo caso
         * lanza la excepción NoSuchElementException. */
        @Override public T previous() {
               if(this.hasPrevious()) {   
               			T elemental = this.anterior.elemento;
        						this.siguiente=this.anterior;		
        						this.anterior=this.anterior.anterior; 	
        						return elemental;	
               } else throw new NoSuchElementException("Ya no hay más elementos que mostrar"); 
        }

        /* No implementamos el método remove(); sencillamente lanzamos la
         * excepción UnsupportedOperationException. */
        @Override public void remove() {
              throw new UnsupportedOperationException();
        }

        /* Mueve el iterador al inicio de la lista; después de llamar este
         * método, y si la lista no es vacía, hasNext() regresa verdadero y
         * next() regresa el primer elemento. */
        @Override public void start() {
        			this.anterior=null;
        			this.siguiente=this.lista.cabeza;
        }

        /* Mueve el iterador al final de la lista; después de llamar este
         * método, y si la lista no es vacía, hasPrevious() regresa verdadero y
         * previous() regresa el último elemento. */
        @Override public void end() {
               this.anterior=this.lista.rabo;
        			this.siguiente=null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
			return this.longitud;
    }
    /**
     * Regresa el número de elementos en la lista. El método es idéntico a
     * {@link getLongitud}.
     * @return el número de elementos en la lista.
     */
    @Override public int getElementos() {
			return this.longitud;
    }

    /**
     * Agrega un elemento al final de la lista. El método es idéntico a {@link
     * #agregaFinal}.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
         	this.agregaFinal(elemento);
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y el último a la vez.
     * @param elemento el elemento a agregar.
     */
    public void agregaFinal(T elemento) {
    			if (this.longitud==0) this.cabeza = this.rabo = new Nodo(elemento);	
    			else {
    					this.rabo.siguiente = new Nodo(elemento);
    					this.rabo.siguiente.anterior = this.rabo;
    					this.rabo = this.rabo.siguiente;
    			}	
    			this.longitud++;
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y el último a la vez.
     * @param elemento el elemento a agregar.
     */
    public void agregaInicio(T elemento) {    
    			if (this.longitud==0) this.cabeza = this.rabo = new Nodo(elemento);	
    			else {
    					this.cabeza.anterior = new Nodo(elemento);
    					this.cabeza.anterior.siguiente = this.cabeza;
    					this.cabeza = this.cabeza.anterior;
    			}	
    			this.longitud++;
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no hace nada. Si el elemento aparece varias veces en la
     * lista, el método elimina el primero.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) { 
    			if (!this.contiene(elemento)) return;
    			Nodo nikai = this.cabeza;
    			while (nikai!=null){
    						if (nikai.elemento.equals(elemento))
    								break;
    						nikai = nikai.siguiente;
    			}	
    		  	if (nikai==this.rabo){
    		  			this.eliminaUltimo();
    		  			return;
    		  	}
    		  	if (nikai==this.cabeza){
    		  			this.eliminaPrimero();
    		  			return;
    		   }
    		   nikai.siguiente.anterior = nikai.anterior;
    		   nikai.anterior.siguiente = nikai.siguiente;
    		   nikai = null;
    		   this.longitud--;
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() throws NoSuchElementException {
         	if (this.longitud==0) 
         			throw new NoSuchElementException();
    			T elemental = this.cabeza.elemento;
    			this.cabeza = this.cabeza.siguiente;
    			if (this.cabeza != null) 
    					this.cabeza.anterior = null;
    			this.longitud--;
    			return elemental;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() throws NoSuchElementException {
        		if (this.longitud==0) 
        				throw new NoSuchElementException();
    			T elemental = this.rabo.elemento;
    			this.rabo = this.rabo.anterior;
    			if (this.rabo != null) 
    					this.rabo.siguiente = null;
    			this.longitud--;
    			return elemental;
    }

    /*
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
    			for(T elemental : this)
    					if (elemental.equals(elemento))
    							return true;
    			return false;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa de la que manda llamar el
     *         método.
     */
    public Lista<T> reversa() {
    			Lista<T> lp = new Lista<T>();
    			Lista<T> li = this.copia();
    			while (li.longitud>0) lp.agregaFinal(li.eliminaUltimo());
    			return lp;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
    			Lista<T> lp = new Lista<T>();
    			Nodo nikai = this.cabeza;
    			while (nikai!=null){
    					lp.agrega(nikai.elemento);
    					nikai = nikai.siguiente;
    			} return lp;
    }

    /**
     * Limpia la lista de elementos. El llamar este método es equivalente a
     * eliminar todos los elementos de la lista.
     */
    public void limpia() {
           Lista<T> ki=new Lista<T>();
           this.cabeza=ki.cabeza;
           this.rabo=ki.rabo;
           this.longitud=ki.longitud;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() throws NoSuchElementException{
         	if (this.cabeza==null || this.longitud==0) 
         			throw new NoSuchElementException();
        		else return this.cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el último elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() throws NoSuchElementException{
    			if (this.rabo==null || this.longitud==0)	throw new NoSuchElementException();
        		else return this.rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista, si <em>i</em> es mayor
     *         o igual que cero y menor que el número de elementos en la lista.
     * @throws ExcepcionIndiceInvalido si el índice recibido es menor que cero,
     *         o mayor que el número de elementos en la lista menos uno.
     */
    public T get(int i) throws ExcepcionIndiceInvalido {
         	if (i<0 || i>((this.longitud)-1)) throw new ExcepcionIndiceInvalido();
         	int k = 0;
         	Nodo nikai = this.cabeza;
    			while (k!=i){
    					nikai = nikai.siguiente;
    					k++;
    			} return nikai.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
    			int i = 0;
    			for (T elemental : this)
    					if (elemental.equals(elemento))
    							return i;
    					else
    							i++;
    			return -1;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o==null) return false;
        else if (!(this.getClass().equals(o.getClass()))) return false;
        @SuppressWarnings("unchecked") Lista<T> l1 = (Lista<T>)o;
        Nodo nikai = this.cabeza;
        Nodo neph = l1.cabeza;
        while (nikai!=null && neph!=null){
        			if (!nikai.elemento.equals(neph.elemento)) return false;
        			nikai = nikai.siguiente;
        			neph = neph.siguiente;
        }
        if (nikai==null && neph!=null) return false;
        if (neph==null && nikai!=null) return false;
        return true;        
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {    			
        		String cad = "[";
        		int i = 0;
        		for(T elemental : this){
        				cad+=elemental.toString(); 
        				if((i++)<(this.longitud)-1) 
        						cad+=", ";   
        		}
        		cad+="]";
        		return cad;
    }

    /**
     * Regresa un iterador para recorrer la lista.
     * @return un iterador para recorrer la lista.
     */
    @Override public Iterator<T> iterator() {
        return iteradorLista();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador<T>(this);
    }
    
    private static <T extends Comparable<T>> Lista<T> mezcla(Lista<T> la,Lista<T> lb){ 
				Lista<T> mezclaV = new Lista<T>();
				int l= la.longitud + lb.longitud;
				Lista<T>.Nodo nei=la.cabeza;
				Lista<T>.Nodo lip=lb.cabeza;
				for(int i=0;i<l;i++){
	 						if(nei == null){
										mezclaV.agrega(lip.elemento);
										lip=lip.siguiente;
	  			  			} else if(lip==null){
										mezclaV.agrega(nei.elemento);
										nei=nei.siguiente;
				 		   }else if(nei.elemento.compareTo(lip.elemento)<0){ 
 										mezclaV.agrega(nei.elemento);
										nei = nei.siguiente;
	 					   }else{
										mezclaV.agrega(lip.elemento);
										lip=lip.siguiente;
				   	  }
			 	}
				return mezclaV;
   }
     /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param l la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */   /*<T extends Comparable<T>>*/ 
  		public static <T extends Comparable<T>> Lista<T> mergeSort(Lista<T> l) {
			if(l.longitud<2)return l.copia();
			else{	Lista<T> l1 = new Lista<T>();
	    				Lista<T> l2 = new Lista<T>();
	    				Lista<T>.Nodo no = l.cabeza;
	    				for(int i=0;i<l.longitud/2;i++){
									l1.agrega(no.elemento);
									no=no.siguiente;
	    				} for(int j=(l.longitud/2);j<l.longitud;j++){
									l2.agrega(no.elemento);
									no=no.siguiente;
	  				  	} l1=mergeSort(l1);
	  				 	l2=mergeSort(l2);
	  			  		return mezcla(l1,l2);
	  			 	 	}
   		}
    
    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param l la lista donde se buscará.
     * @param e el elemento a buscar.
     * @return <tt>true</tt> si e está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public static <T extends Comparable<T>> boolean busquedaLineal(Lista<T> l, T e) {
			 if(l.longitud==0)return false;
	 		else{Lista<T>.Nodo n = l.cabeza;
			 for(int i=0;i<l.longitud;i++){
					 if(n.elemento.equals(e))return true;
					 else n=n.siguiente;
			 }
			 return false;
			 }
   	 }
}
