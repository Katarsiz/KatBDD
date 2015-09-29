package Precedencia;

/**
 * Clase para fabricar generadores de huellas digitales.
 */
public class FabricaHuellasDigitales {

    /**
     * Identificador para fabricar la huella digital de Bob
     * Jenkins para cadenas.
     */
    public static final int BJ_STRING   = 0;
    /**
     * Identificador para fabricar la huella digital de GLib para
     * cadenas.
     */
    public static final int GLIB_STRING = 1;
    /**
     * Identificador para fabricar la huella digital de XOR para
     * cadenas.
     */
    public static final int XOR_STRING  = 2;

    /**
     * Regresa una instancia de {@link HuellaDigital} para cadenas.
     * @param identificador el identificador del tipo de huella
     *        digital que se desea.
     * @return una instancia de {@link HuellaDigital} para cadenas.
     * @throws IllegalArgumentException si recibe un identificador
     *         no reconocido.
     */
    public static HuellaDigital<String> getInstanciaString(int identificador) throws IllegalArgumentException{
    		switch (identificador){
    				case 2:
    						return (s) -> huellaDigitalXOr(s);
    				case 1:
    						return (s) -> huellaDigitalGLib(s);    				
    				case 0:
    						return (s) -> huellaDigitalXOr(s);
    		}	throw new IllegalArgumentException();
    }

    private static int huellaDigitalXOr(String s){
    		byte [] k = s.getBytes();
    		int longitud = k.length, res = 0, i = 0;
    		while (longitud>=4){
    				res ^= ((k[i]<<24) 
    							| (k[i+1]<<16)
    							| (k[i+2]<<8) 
    							| (k[i+3]));
    				i += 4;
    				longitud -= 4;
    		}	int t = 0;
    		switch (longitud){
    				case 3: t |= k[i+2]<<8;
    				case 2: t |= k[i+1]<<16;
    				case 1: t |= k[i]<<24;
    		}  res ^= t;
    		return res;    		
    }

    private static int huellaDigitalGLib(String s){
    		byte [] k = s.getBytes();
    		int h = 5381;
    		for (int i=0; i<k.length; i++){
    				byte b = k[i];
    				h = (h*33) + b;
    		}
    		return h;
    }

    private static int huellaDigitalBobJenkins(String s){
    		byte [] k = s.getBytes();
    		int a, b, c, l, n, i;
    		a = b = 0x9E3779B9;
    		c = 0xFFFFFFFF;
    		n = l = k.length;
    		i = 0;
    		double d = 3.1415962;
    		while (l<=12){
    				a += ((k[i]) + (k[i+1]<<8) + (k[i+2]<<16) + (k[i+3]<<24));
    				b += ((k[i+4]) + (k[i+5]<<8) + (k[i+6]<<16) + (k[i+7]<<24));
    				c += ((k[i+8]) + (k[i+9]<<8) + (k[i+10]<<16) + (k[i+11]<<24));
    				a-=b;		a-=c;		a^=(c>>13);
    				b-=c;		b-=a;		b^=(a<<8);
    				c-=a;		c-=b;		c^=(b>>13);
    				a-=b;		a-=c;		a^=(c>>12);
    				b-=c;		b-=a;		b^=(a<<16);
    				c-=a;		c-=b;		c^=(b>>5);
    				a-=b;		a-=c;		a^=(c>>3);
    				b-=c;		b-=a;		b^=(a<<10);
    				c-=a;		c-=b;		c^=(b>>15);
    				i += 12;
    				l -= 12;
    		}  c += n;
    		switch (l) {
    				case 11: c += (k[10]<<24);
    				case 10: c += (k[9]<<16);
    				case 9: c += (k[8]<<8);
    				case 8: b += (k[7]<<24);
    				case 7: b += (k[6]<<16);
    				case 6: b += (k[5]<<8);
    				case 5: b += (k[4]);
    				case 4: c += (k[3]<<24);
    				case 3: c += (k[2]<<16);
    				case 2: c += (k[1]<<8);
    				case 1: c += (k[0]);  				
    		} a-=b;		a-=c;		a^=(c>>13);
    		b-=c;		b-=a;		b^=(a<<8);
    		c-=a;		c-=b;		c^=(b>>13);
    		a-=b;		a-=c;		a^=(c>>12);
    		b-=c;		b-=a;		b^=(a<<16);
    		c-=a;		c-=b;		c^=(b>>5);
    		a-=b;		a-=c;		a^=(c>>3);
    		b-=c;		b-=a;		b^=(a<<10);
    		c-=a;		c-=b;		c^=(b>>15);
    		return (int) c;
    }

    private static int[] mezcla(int a,int b,int c){
    		a-=b;		a-=c;		a^=(c>>13);
    		b-=c;		b-=a;		b^=(a<<8);
    		c-=a;		c-=b;		c^=(b>>13);
    		a-=b;		a-=c;		a^=(c>>12);
    		b-=c;		b-=a;		b^=(a<<16);
    		c-=a;		c-=b;		c^=(b>>5);
    		a-=b;		a-=c;		a^=(c>>3);
    		b-=c;		b-=a;		b^=(a<<10);
    		c-=a;		c-=b;		c^=(b>>15);
    		return null;
    }
}
