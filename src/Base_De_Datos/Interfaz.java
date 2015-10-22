package Base_De_Datos;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Interfaz extends JFrame{
	
	private BaseDeDatos bdd;
	private DataSet[] pinturas, autores, galerias;
	private JPanel actual;
	private final String [] pi = {"Imagen","Titulo","Tecnica","Autor","Estilo","Paradero Actual"};
	private final String [] au = {"Nombre","Nacimiento","Muerte","Obras Totales","Obra mas famosa","Obras Vendidas"};
	private final String [] ga = {"Nombre de la Galeria","Ubicacion","# de obras en Exposicion","Fecha de Inauguracion",
								"Estilo Arquitectonico","Tematica Principal"};

	public Interfaz(BaseDeDatos bdd, DataSet [] p, DataSet [] a , DataSet [] g){
		this.bdd = bdd;		
		pinturas = p;
		autores = a;
		galerias = g;
		setTitle("The Blissful Seeker");	
		actual = new GeneralVision(pinturas,"PINTURAS");			
		//División de Paneles		
		PanelPrincipal principal= new PanelPrincipal();
		setLayout(new BorderLayout(0,0));			
		//Agregando paneles
		add(principal, BorderLayout.WEST);	
		add(actual, BorderLayout.CENTER);
		//Agregando atributos principales	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1000,550);
		setVisible(true);
		setResizable(false);
	}

	private void matarPanel(JPanel pan){
		this.remove(actual);
		actual = pan;
		add(actual, BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}

	private class PanelPrincipal extends JPanel{
		
		private Image i;

		public PanelPrincipal(){
			//Atributos Generales
			setLayout(new BorderLayout(0,0));
			JPanel panelBotones = new JPanel();
			panelBotones.setLayout(new GridLayout(2,10));
			//Imagen a agregar
			i = new ImageIcon("img\\Blissful Seeker.jpg").getImage();
			//Creación de botones
			BotonHome vg = new BotonHome();
			BotonBusqueda busqueda = new BotonBusqueda();
			BotonAyuda ayuda = new BotonAyuda();
			BotonSalida salir = new BotonSalida();
			//Agregando botones	al panel de botones.
			panelBotones.add(vg);
			panelBotones.add(busqueda);
			panelBotones.add(ayuda);
			panelBotones.add(salir);
			add(panelBotones,BorderLayout.SOUTH);
		}

		@Override
		public void paint(Graphics g){
			g.drawImage(i,-20,0,getWidth()+25,getHeight(),this);
			setOpaque(true);
		}

		private class BotonVG extends JApplet {
			public BotonVG(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\BotonVG.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
				}
			}
		}

		private class BotonHome extends JApplet {
			public BotonHome(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\BotonHome.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					matarPanel(new Inicio());
				}
			}
		}

		private class BotonBusqueda extends JApplet {
			public BotonBusqueda(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\BotonBusqueda.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					System.exit(1);
				}
			}
		}

		private class BotonAyuda extends JApplet {
			public BotonAyuda(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\BotonAyuda.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					System.exit(1);
				}
			}
		}

		private class BotonSalida extends JApplet {
			public BotonSalida(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\BotonSalida.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					System.exit(1);
				}
			}
		}

		private class PrimaryProcessing implements MouseListener{
			public void mousePressed(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
		}
	}

	private class Inicio extends JPanel{

		private Image im;

		public Inicio(){
			setLayout(new GridLayout());
			im = new ImageIcon("img\\The Blissful Dreamer.jpg").getImage();
		}

		@Override
		public void paint(Graphics g){
			g.drawImage(im,0,0,getWidth(),getHeight(),this);
			setOpaque(true);
		}

	}

	private class GeneralVision extends JPanel implements ActionListener{

		private JButton agrega;
		private String tablaActual;
		private JPanel tab;

		private GeneralVision(DataSet [] datos, String t){
			tablaActual = t;
			tab = new JPanel();
			tab.setLayout(new GridLayout(1,3));
			setLayout(new BorderLayout(0,0));
			agrega = new JButton("Agregar");
			BotonPinturas pint = new BotonPinturas();
			BotonAutores auto = new BotonAutores();
			BotonGalerias gale = new BotonGalerias();
			BotonAgregar agre = new BotonAgregar();
			tab.add(pint);
			tab.add(auto);
			tab.add(gale);
			tab.add(agre);
			add(tab,BorderLayout.NORTH);
			add(new Muestreo(datos,tablaActual),BorderLayout.CENTER);
		}

		private class BotonPinturas extends JApplet {
			public BotonPinturas(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\BotonPinturas.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					tablaActual = "PINTURAS";
					matarPanel(new GeneralVision(pinturas,"PINTURAS"));
				}
			}
		}

		private class BotonAutores extends JApplet {
			public BotonAutores(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\BotonAutores.jpg"));
				add(boton);
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					tablaActual = "AUTORES";
					matarPanel(new GeneralVision(autores,"AUTORES"));
				}
			}
		}

		private class BotonGalerias extends JApplet {
			public BotonGalerias(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\BotonGalerias.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					tablaActual = "GALERIAS";
					matarPanel(new GeneralVision(galerias,"AUTORES"));
				}
			}
		}

		private class BotonAgregar extends JApplet {
			public BotonAgregar(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\BotonAgrega.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					Agregacion ag = new Agregacion(tablaActual);
				}
			}
		}

		private class PrimaryProcessing implements MouseListener{
			public void mousePressed(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
		}

		public void actionPerformed(ActionEvent e){
			if (e.getSource()==agrega){
				Agregacion a = new Agregacion(tablaActual);
			}
		}
	}

	private class Agregacion extends JFrame implements ActionListener{

		private String t;
		private JButton agregando;
		private JTextField campo1,campo2,campo3,campo4,campo5,campo6;

		private Agregacion(String tabla){
			t = tabla;
			setSize(500,75);
			setVisible(true);
			setDefaultCloseOperation(HIDE_ON_CLOSE);
			setLayout(new GridLayout());
			campo1 = new JTextField();
			campo1.setBounds(10,10,100,30);
			add(campo1);
			campo2 = new JTextField();
			campo2.setBounds(110,10,210,30);
			add(campo2);
			campo3 = new JTextField();
			campo3.setBounds(220,10,320,30);
			add(campo3);
			campo4 = new JTextField();
			campo4.setBounds(330,10,430,30);
			add(campo4);
			campo5 = new JTextField();
			campo5.setBounds(440,10,540,30);
			add(campo5);
			campo6 = new JTextField();
			campo6.setBounds(440,10,540,30);
			add(campo6);
			agregando = new JButton("Agregar");
			agregando.setBounds(10,80,100,30);
			add(agregando);
			agregando.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e){
			if (e.getSource()==agregando){
				try{
					DataSet dp = new DataSet(campo1.getText(),campo2.getText(),campo3.getText(),
										campo4.getText(),campo5.getText(),campo6.getText());	
					System.out.println(dp);
					bdd.agrega(dp,t);
					switch (t){
						case "PINTURAS":
							pinturas = listaArreglo(bdd.busca("PINTURAS",null,null));
							matarPanel(new GeneralVision(pinturas,"PINTURAS"));
						break;
						case "AUTORES":
							autores = listaArreglo(bdd.busca("AUTORES",null,null));
							matarPanel(new GeneralVision(autores,"AUTORES"));
						break;
						case "GALERIAS":
							galerias = listaArreglo(bdd.busca("GALERIAS",null,null));
							matarPanel(new GeneralVision(galerias,"GALERIAS"));
						break;
					}
				} catch (Throwable t){
					System.out.println("Meh" + t);
				}
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
}