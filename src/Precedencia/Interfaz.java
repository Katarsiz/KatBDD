package Precedencia;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.geom.Line2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Interfaz extends JFrame{

	private int x1, y1, x2, y2;	
	private double cX, cY;
	private Graphics g;
	private boolean presionado, seleccionada;
	private Entrada e;	
	private Grafica grafica;
	private String svg;

	public Interfaz(){
		svg = "<?xml version='1.0' encoding='UTF-8' standalone=\"yes\"?>\n<svg xmlns=\"http://www.w3.org/2000/svg\" width='"
			+ 1350 + "'height='" 
			+ 900 + "'>\n	<g>\n";
		e = new Entrada();
	}

	/*"    <line x1='"+ (v1.cordX + (v1.longitud/2))
						      +	"' y1='" + (v1.cordY)
						      +"' x2='" + (v2.cordX + (v2.longitud/2))
						      + "' y2='" + (v2.cordY)
						      + "' stroke='midnightblue' stroke-width='2.5' />\n";*/

	public void lanzarExcepcion(String s){
		Exce xt = new Exce(s);
	}

	public void crearArchivo(String graficacion,String name){
			String ruta = name +".html";
			File archivo = new File(ruta);
			try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
					bw.write(graficacion);
					bw.close();
			} catch (Exception exe){
					System.err.println("Error");
			}
	}

	private void agregarRecta(double a1, double a2, double a3, double a4){
		svg += "<line x1='"+ a1
			      +	"' y1='" + a2
			      +"' x2='" + a3
			      + "' y2='" + a4
			      + "' stroke='midnightblue' stroke-width='2.5' />\n";
	}

	public boolean hayEntrada(){
		return e.eva;
	}

	public void aceptarEntrada(){
		e.eva = false;
	}

	public void generarGrafica(double [] xa , double [] ya){
		if (grafica==null)
			grafica = new Grafica(xa, ya);
		grafica.actualizarGrafica(xa, ya);
	}

	public String getFuncion(){
		return e.fun;
	}

	public double intervalo1(){
		return e.ntx;
	}

	public double intervalo2(){
		return e.nty;
	}

	private class Entrada extends JFrame{

		private JTextField funcion, ix1, ix2;
		private JButton boton1, boton2, boton3;
		private double ntx, nty;
		private String fun;
		private boolean eva;

		public Entrada(){
			super("Graficadora de Funciones");
			boton1 = new JButton("  Graficar Funcion  ");
			boton2 = new JButton("Ayuda");			
			boton3 = new JButton("  SVG  ");
			funcion = new JTextField(17);
			ix1 =  new JTextField (5);
			ix2 = new JTextField (5);
			add(funcion);
			add(boton1);
			add(ix1);
			add(new JLabel("Intervalo X"));
			add(ix2);
			boton2.addActionListener(new ButtonL());
			add(boton2);
			add(boton3);
			boton3.addActionListener(new EBoton());
			setLayout(new FlowLayout());
			boton1.addActionListener(new OyenteBoton());	
			setSize(354,100);
			setVisible(true);
			setDefaultCloseOperation(EXIT_ON_CLOSE);	
			eva = false;
			while (!eva){
			}
		}
		

		private boolean esNumero(String s){
			try{
				double w = Double.parseDouble(s);
				return true;
			} catch (Exception exe){
				return false;
			} 
		}

		private class OyenteBoton implements ActionListener{
			public void actionPerformed(ActionEvent ae){
				ntx = -10;
				nty = 10;
				 fun = funcion.getText();
				 String h = ix1.getText();
				 String p = ix2.getText();
				 if (esNumero(h)){
				 	ntx = Double.parseDouble(h); 
				 }
				 if (esNumero(p)){
				 	nty = Double.parseDouble(p);
				 }				 
				 eva = true;
			}
		}

		private class EBoton implements ActionListener{
			public void actionPerformed(ActionEvent jt){
				svg +="<g>\n </svg>";
				crearArchivo(svg, fun);
			}
		}		

		private class ButtonL implements ActionListener{
			public void actionPerformed(ActionEvent e){
				Ayuda a = new Ayuda();
			}
		}

			private class Ayuda extends JFrame{

				private JButton acept;

				private Ayuda(){
					setTitle("Ayuda");
					setLayout(new FlowLayout());					
					setDefaultCloseOperation(HIDE_ON_CLOSE);
					add(new JLabel("El valor por defecto de esta \"graficadora\" va de -10 a 10\n"));
					add(new JLabel("en el eje X , si hay valores invalidos o la funcion esta mal "));		
					add(new JLabel("escrita, el programa lanzara una excepcion y terminara su\n"));
					add(new JLabel("ejecucion. | Gramatica usada : Forma Normal de Bacus |"));	
					acept = new JButton("Aceptar");
					acept.addActionListener(new EscuchaBoton());
					add(acept);
					setSize(375,150);
					setVisible(true);
				}		

				private class EscuchaBoton implements ActionListener{
					public void actionPerformed(ActionEvent e){
						setVisible(false);
					}
				}		
			}
		}

	private class Exce extends JFrame{
	
		private JButton aceptar;

		public Exce(String s){
			setTitle(s + " Error");
			setLayout(new FlowLayout());
			setVisible(true);
			if (s.equals("Math")){
				add(new JLabel("Error, la funcion proporcionada no esta definida en el intervalo\n"));
				add(new JLabel(" proporcionado, por favor, reintenta con valores validos :) "));				
			} else {
				add(new JLabel("Error, los caracteres proporcionados no son validos,\n"));
				add(new JLabel(" por favor, reintenta con una funcion que sea valida :) \n"));				
			} aceptar = new JButton("Aceptar");
			aceptar.addActionListener(new EscuchaBoton());
			setSize(375,115);
			setVisible(true);
			add(aceptar);
			setDefaultCloseOperation(EXIT_ON_CLOSE);	
		}

		private class EscuchaBoton implements ActionListener{
			public void actionPerformed(ActionEvent e){
				System.exit(1);
			}
		}
	}

	private class Grafica extends JFrame implements MouseMotionListener{

		private double [] xp;
		private double [] yp;
		private boolean ent;

		public Grafica(double [] xh, double [] yh){
			xp = xh;
			yp = yh;
			addMouseMotionListener(this);
			addMouseListener(new ML());
			setTitle("Grafica de la Funcion " + e.fun);
			setSize(1300,900);
			setResizable(false);
			setVisible(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
			setBackground(Color.BLACK);
		}

		public void actualizarGrafica(double [] xq, double [] yq){
			xp = xq;
			yp = yq;
			repaint();
		}

		public void pintar(Graphics h){
			h.setColor(Color.WHITE);
			h.drawLine(0,450,1300,450);
			h.drawLine(650,0,650,900);
			agregarRecta(0,450,1300,450);
			agregarRecta(650,0,650,900);
			for (int q = 1; q<xp.length; q++){
				recta(h, xp[q], yp[q], xp[q+1], yp[q+1]);
				agregarRecta(xp[q], yp[q], xp[q+1], yp[q+1]);
				if (q+1 == xp.length-1){
					break;
				}
			} System.out.println("Grafica acutalizada");
		}

		public void recta(Graphics h, double x01, double y01, double x02, double y02){
			Graphics2D g2 = (Graphics2D) h;
			g2.draw(new Line2D.Double(x01, y01, x02, y02));
		}

		public void paint(Graphics h){
			pintar(h);
		}
	
		@Override
		public void mouseDragged(MouseEvent e){				
		}
			
		@Override
		public void mouseMoved(MouseEvent me){			
		}
		
		private class ML extends MouseAdapter{
	
			@Override
			public void mousePressed(MouseEvent mp){
				x1 = mp.getX();
				y1 = mp.getY();
				System.out.println ("Punto : " + "(" + x1 + "," + y1);
			}
	
			@Override
			public void mouseReleased(MouseEvent mp){
				x2 = mp.getX();
				y2 = mp.getY();
			}
		}				
	}
}
