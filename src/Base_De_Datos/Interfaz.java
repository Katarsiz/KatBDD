package Base_De_Datos;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.Applet;
import java.util.Random;

public class Interfaz extends JFrame{
	
	private BaseDeDatos bdd;
	private DataSet[] pinturas, autores, galerias;
	private JPanel actual;
	private final String [] pi = {"Imagen","Titulo","Tecnica","Autor","Estilo","Paradero Actual"};
	private final String [] au = {"Nombre","Fecha de Nacimiento","Fecha de Muerte","Obras Totales","Obra mas famosa","Obras Vendidas"};
	private final String [] ga = {"Nombre de la Galeria","Ubicacion","# de obras en Exposicion","Fecha de Inauguracion",
								"Estilo Arquitectonico","Tematica Principal"};
	private final String [] mod1 = {"TITLE","IMG","TECH","AUTOR","STYLE","UBIC","PINTURAS"};
	private final String [] mod2 = {"NAME","BORN","DEATH","PIECES","FAMOUS","SOLD","AUTORES"};
	private final String [] mod3 = {"DEN","PLACE","NUMBER","DATE","ARCH","FOCUS","GALERIAS"};
	private final String [] imagine = {"Howl.jpg","Blissful Seeker.jpg","Canon.jpg","For a minute there I lost myself.jpg",
										"Winter Stream.jpg","Preservation.jpg","Fire Within Me.jpg",
										"Society.jpg","Smog.jpg","ThunderSnow.jpg","Batu Caves.jpg"};

	public Interfaz(BaseDeDatos bdd, DataSet [] p, DataSet [] a , DataSet [] g){
		this.bdd = bdd;		
		pinturas = p;
		autores = a;
		galerias = g;
		setTitle("The Blissful Seeker");	
		actual = new Inicio();			
		//División de Paneles		
		PanelPrincipal principal= new PanelPrincipal();
		setLayout(new BorderLayout(0,0));			
		//Agregando paneles
		add(principal, BorderLayout.WEST);	
		add(actual, BorderLayout.CENTER);
		//Agregando atributos principales	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1100,650);
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

	private void lanzarExcepcion(){
		JOptionPane.showMessageDialog(null,"Error en los datos.");
	}
	

	private class PanelPrincipal extends JPanel{
		
		private Image i;

		public PanelPrincipal(){
			//Atributos Generales
			setLayout(new BorderLayout(0,0));
			JPanel panelBotones = new JPanel();
			panelBotones.setLayout(new GridLayout(2,10));
			//Imagen a agregar
			i = new ImageIcon("img\\Howl.jpg").getImage();
			//Creación de botones
			BotonVG vg = new BotonVG();
			BotonHome home = new BotonHome();
			BotonAyuda ayuda = new BotonAyuda();
			BotonSalida salir = new BotonSalida();
			//Agregando botones	al panel de botones.
			panelBotones.add(vg);
			panelBotones.add(home);
			panelBotones.add(ayuda);
			panelBotones.add(salir);
			add(panelBotones,BorderLayout.SOUTH);
		}

		private void actualizarImagen(){
			Random rn = new Random();
			int n = rn.nextInt(11);
			i = new ImageIcon("img\\" + imagine[n]).getImage();
			repaint();
		}

		@Override
		public void paint(Graphics g){
			g.drawImage(i,-20,0,getWidth()+25,getHeight(),this);
			setOpaque(true);
		}

		private class BotonVG extends JApplet {
			public BotonVG(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\botones\\BotonVG.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					matarPanel(new GeneralVision(pinturas,"PINTURAS"));
					actualizarImagen();
				}
			}
		}

		private class BotonHome extends JApplet {
			public BotonHome(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\botones\\BotonHome.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					matarPanel(new Inicio());
					actualizarImagen();
				}
			}
		}

		private class BotonAyuda extends JApplet {
			public BotonAyuda(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\botones\\BotonAyuda.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					Ayuda ay = new Ayuda();
					actualizarImagen();
				}
			}
		}

		private class BotonSalida extends JApplet {
			public BotonSalida(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\botones\\BotonSalida.jpg"));
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

	private class Ayuda extends JPanel{
	}

	private class Inicio extends JPanel{

		private Image im;

		public Inicio(){
			setLayout(new GridLayout());
			im = new ImageIcon("img\\botones\\WroClaw.jpg").getImage();
		}

		@Override
		public void paint(Graphics g){
			g.drawImage(im,0,0,getWidth(),getHeight(),this);
			setOpaque(true);
		}
	}

	private class GeneralVision extends JPanel implements ActionListener{

		private JButton agrega,bus;
		private String tablaActual;
		private JPanel tab;
		private JTextField txt;
		private Choice bu;
		private String [] cp, campo;
		private Muestreo md;

		private GeneralVision(DataSet [] datos, String t){
			try{
				pinturas = listaArreglo(bdd.busca("PINTURAS",null,null));
				autores = listaArreglo(bdd.busca("AUTORES",null,null));
				galerias = listaArreglo(bdd.busca("GALERIAS",null,null));
			} catch (Exception est){
			}
			tablaActual = t;
			cp =  null;
			switch (t){
				case "PINTURAS":
					cp = pi;
					campo = mod1;
				break;
				case "AUTORES":
					cp = au;
					campo = mod2;
				break;
				case "GALERIAS":
					cp = ga;
					campo = mod3;
				break;
			}
			tab = new JPanel();
			int f = 0;
			tab.setLayout(new GridLayout(1,3));
			setLayout(new BorderLayout(0,0));
			agrega = new JButton("Agregar");
			BotonPinturas pint = new BotonPinturas();
			BotonAutores auto = new BotonAutores();
			BotonGalerias gale = new BotonGalerias();
			BotonAgregar agre = new BotonAgregar();
			BotonEliminar elim = new BotonEliminar();
			tab.add(pint);
			tab.add(auto);
			tab.add(gale);
			tab.add(agre);
			tab.add(elim);
			//Agregando la seccion de busqueda
			JPanel busqueda = new JPanel();
			busqueda.setLayout(new GridLayout());
			busqueda.add(new JLabel("	Busqueda por parametro"));
			bu = new Choice();
			bu.add(cp[f++]);
			bu.add(cp[f++]);
			bu.add(cp[f++]);
			bu.add(cp[f++]);
			bu.add(cp[f++]);
			bu.add(cp[f++]);
			busqueda.add(bu);
			bus = new JButton("Buscar");
			txt = new JTextField();
			busqueda.add(txt);			
			busqueda.add(bus);		
			bus.addActionListener(this);
			//Agregando las secciones al pnael principal.	
			add(tab,BorderLayout.NORTH);
			add(busqueda,BorderLayout.SOUTH);
			md = new Muestreo(datos,tablaActual,bdd);
			add(md,BorderLayout.CENTER);
		}

		private void asesinarTabla(DataSet[] data){
			this.remove(md);
			md = new Muestreo(data,tablaActual,bdd);
			add(md, BorderLayout.CENTER);
			this.revalidate();
			this.repaint();
		}

		private class BotonPinturas extends JApplet {
			public BotonPinturas(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\botones\\BotonPinturas.jpg"));
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
				boton.setIcon(new ImageIcon("img\\botones\\BotonAutores.jpg"));
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
				boton.setIcon(new ImageIcon("img\\botones\\BotonGalerias.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					tablaActual = "GALERIAS";
					matarPanel(new GeneralVision(galerias,"GALERIAS"));
				}
			}
		}

		private class BotonAgregar extends JApplet {
			public BotonAgregar(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\botones\\BotonAgrega.jpg"));
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

		private class BotonEliminar extends JApplet {
			public BotonEliminar(){
				JLabel boton = new JLabel();
				boton.setIcon(new ImageIcon("img\\botones\\BotonEliminar.jpg"));
				add(boton);				
				addMouseListener(new MouseProcessing());
			}
			private class MouseProcessing extends PrimaryProcessing{
				@Override
				public void mousePressed(MouseEvent e){
					Eliminacion el = new Eliminacion(tablaActual);
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
			if(e.getSource()==bus){
				String op = bu.getSelectedItem();
				int j = obtenerIndice(op,cp);
				op = campo[j];
				try{
					Lista<DataSet> resultado = bdd.busca2(tablaActual,op,txt.getText());
					DataSet [] rp= listaArreglo(resultado);
					asesinarTabla(rp);
				} catch (UnrealException x){
					lanzarExcepcion();
				} catch(Exception az){
					System.out.println("meh" + az);
				}
			}
		}
	}

	private class Agregacion extends JDialog implements ActionListener{

		private String t;
		private JButton agregando;
		private JTextField campo1,campo2,campo3,campo4,campo5,campo6;
		private String [] campos;

		private Agregacion(String tabla){
			t = tabla;
			switch (tabla){
				case "PINTURAS":
					campos = pi;
				break;
				case "AUTORES":
					campos = au;
				break;
				case "GALERIAS":
					campos = ga;
				break;
			}
			int z = 0;
			setLayout(new GridLayout());
			setSize(425,200);
			setVisible(true);
			setDefaultCloseOperation(HIDE_ON_CLOSE);
			setLayout(new GridLayout(4,1));
			campo1 = new JTextField();
			campo1.setBounds(10,10,100,30);
			add(new JLabel(campos[z++]));
			add(campo1);
			campo2 = new JTextField();
			campo2.setBounds(110,10,210,30);
			add(new JLabel(campos[z++]));
			add(campo2);
			campo3 = new JTextField();
			campo3.setBounds(220,10,320,30);
			add(new JLabel(campos[z++]));
			add(campo3);
			campo4 = new JTextField();
			campo4.setBounds(330,10,430,30);
			add(new JLabel(campos[z++]));
			add(campo4);
			campo5 = new JTextField();
			campo5.setBounds(440,10,540,30);
			add(new JLabel(campos[z++]));
			add(campo5);
			campo6 = new JTextField();
			campo6.setBounds(440,10,540,30);
			add(new JLabel(campos[z++]));
			add(campo6);
			agregando = new JButton("Agregar");
			agregando.setBounds(10,80,100,30);
			add(agregando);
			agregando.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e){
			if (e.getSource()==agregando){
				try{
					String djk = campo1.getText();
					if (t.equals("PINTURAS")){
						djk = "img\\" + djk;
					}
					DataSet dp = new DataSet(djk,campo2.getText(),campo3.getText(),
										campo4.getText(),campo5.getText(),campo6.getText());	
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
					lanzarExcepcion();
				}
			}
		}		
	}

	private class Eliminacion extends JDialog implements ActionListener{

		private String t;
		private JButton eliminar;
		private JTextField campoTxt;
		private Choice opcionEliminar;
		private String [] it, opciones;
		private int k;

		public Eliminacion(String tabla){
			t = tabla;
			opciones = null;
			int i=0;
			switch (tabla){
				case "PINTURAS":
					opciones = pi;
					it = mod1;
				break;				
				case "AUTORES":
					opciones = au;
					it = mod2;
				break;
				case "GALERIAS":
					opciones = ga;
					it = mod3;
				break;

			}
			setVisible(true);
			setDefaultCloseOperation(HIDE_ON_CLOSE);
			setSize(165,175);
			setLayout(new GridLayout(6,3));
			opcionEliminar = new Choice();
			opcionEliminar.add(opciones[i++]);
			opcionEliminar.add(opciones[i++]);
			opcionEliminar.add(opciones[i++]);
			opcionEliminar.add(opciones[i++]);
			opcionEliminar.add(opciones[i++]);
			opcionEliminar.add(opciones[i]);
			JLabel el1 = new JLabel("  Eliminando de \n " + t);
			JLabel el2 = new JLabel("  los valores en los que su  ");
			JLabel el3 = new JLabel("  tenga un valor igual a  ");
			campoTxt = new JTextField();
			campoTxt.setBounds(25,50,75,100);
			JButton botonElimina = new JButton("Elimina");
			botonElimina.addActionListener(this);
			this.add(el1);
			this.add(el2);
			this.add(opcionEliminar);
			this.add(el3);			
			this.add(campoTxt);			
			this.add(botonElimina);		
		}

		public void actionPerformed(ActionEvent e){	
			k = obtenerIndice(opcionEliminar.getSelectedItem(),opciones);
			try{
				bdd.elimina(campoTxt.getText(),it[k],t);
				switch(t){
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
			} catch (Exception lx) {
				lanzarExcepcion();
			}
		}

	}	

	public DataSet [] listaArreglo(Lista<DataSet> l) throws Exception{
		DataSet [] r = new DataSet[l.getElementos()];
		int f = 0;
		for(DataSet d : l){
			r[f++] = d;
		}
		return r;
	}

	public int obtenerIndice(String otro, String [] analisis){		
		for (int i=0; i<analisis.length;i++){
			if (analisis[i].equals(otro))
				return i;
		} return -1;
	}
}