package Base_De_Datos;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

/**
 * @version 1.0 11/09/98
 */
public class Muestreo extends JPanel implements TableModelListener{

	private final String [] pi = {"Imagen","Titulo","Tecnica","Autor","Estilo","Paradero Actual"};
	private final String [] au = {"Nombre","F. Nacimiento","F. Muerte","Obras Totales","Obra mas famosa","Obras Vendidas"};
	private final String [] ga = {"Nombre de la Galeria","Ubicacion","# de obras en Exposicion","Fecha de Inauguracion",
								"Estilo Arquitectonico","Tematica Principal"};
	private final String [] mod1 = {"TITLE","IMG","TECH","AUTOR","STYLE","UBIC","PINTURAS"};
	private final String [] mod2 = {"NAME","BORN","DEATH","PIECES","FAMOUS","SOLD","AUTORES"};
	private final String [] mod3 = {"DEN","PLACE","NUMBER","DATE","ARCH","FOCUS","GALERIAS"};
	private String [] idActual;
	private String [] actual;
	private String tabla;
	private BaseDeDatos dataBase;

  public Muestreo(DataSet [] datos, String s, BaseDeDatos db) {
  	dataBase = db;
  	actual = null;
  	switch (s){
  		case "PINTURAS":
  			actual=pi;
  			tabla = "PINTURAS";
  			idActual = mod1;
  		break;
  		case "AUTORES":
  			actual=au;
  			tabla = "AUTORES";
  			idActual = mod2;
  		break;
  		case "GALERIAS":
  			actual = ga;
  			tabla = "GALERIAS";
  			idActual = mod3;
  		break;
  	}  	
  	try{
  		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
  	} catch (Exception exe){
  	}
    DefaultTableModel dm = new DefaultTableModel();
    dm.setDataVector(matrizDataSet(datos), actual);
    JTable table = new JTable(dm);    
    if (s.equals("PINTURAS")){
        table.getColumn(actual[0]).setCellRenderer(new ButtonRenderer());
        table.getColumn(actual[0]).setCellEditor(
            new ButtonEditor(new JCheckBox()));
    }
    table.getModel().addTableModelListener(this);
    JScrollPane scroll = new JScrollPane(table);
    add(scroll);
    setSize(400, 100);
    setVisible(true);
  }

  public Object[][] matrizDataSet(DataSet [] d){
  	Object [] [] rv = new Object[d.length][d[0].getAr().length];
  	for (int z=0; z<rv.length;z++){
  		rv[z] = d[z].getAr();
  	}
  	return rv;
  }

  @Override
  public void tableChanged(TableModelEvent e) {
  	int row = e.getFirstRow();
  	int column = e.getColumn();
  	TableModel model = (TableModel)e.getSource();
  	String columnName = model.getColumnName(column);
  	Object data = model.getValueAt(row,column);
  	Object datoIdentificador = model.getValueAt(row,0);
  	String idPrincipal = datoIdentificador.toString().replaceAll("'","");
  	String datoId = data.toString().replaceAll("'",""); 	
  	try{
  		dataBase.actualizar(idPrincipal,tabla,new String[]{idActual[column]}, new String[]{datoId});
  	} catch (Throwable ex){
  		System.out.println("Meh" + ex);
  	}
  }
}

class ButtonRenderer extends JButton implements TableCellRenderer {

  public ButtonRenderer() {
    setOpaque(true);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(UIManager.getColor("Button.background"));
    }
    setText((value == null) ? "Borrar" : value.toString());
    return this;
  }
}


class ButtonEditor extends DefaultCellEditor {

  protected JButton button;

  private String label;

  private boolean isPushed;

  public ButtonEditor(JCheckBox checkBox) {
    super(checkBox);
    button = new JButton();
    button.setOpaque(true);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {      	
        fireEditingStopped();
      }
    });
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {
    if (isSelected) {
      button.setForeground(table.getSelectionForeground());
      button.setBackground(table.getSelectionBackground());
    } else {
      button.setForeground(table.getForeground());
      button.setBackground(table.getBackground());
    }
    label = (value == null) ? "Borrar" : value.toString();
    button.setText(label);
    isPushed = true;
    return button;
  }

  public Object getCellEditorValue() {
    if (isPushed) {
      try{
      		MuestraImagen mi = new MuestraImagen(label.replaceAll("'",""));
      	} catch (Exception exe){
      		System.out.println("Meh");
      	}
    }
    isPushed = false;
    return new String(label);
  }

  public boolean stopCellEditing() {
    isPushed = false;
    return super.stopCellEditing();
  }

  protected void fireEditingStopped() {
    super.fireEditingStopped();
  }

  private class MuestraImagen extends JDialog{

  	private BufferedImage ima;
  	String ruta;
  	int width, height;

  	public MuestraImagen(String s)throws Exception{
  		System.out.println(s);
  		setVisible(true);
  		setDefaultCloseOperation(HIDE_ON_CLOSE);
  		ruta = s;
  		ima = ImageIO.read(new File(s));
		width = ima.getWidth();
		height = ima.getHeight();
  		setSize(ima.getWidth(null),ima.getHeight(null));
  		setTitle("Imagen");
  	}

  	public void paint(Graphics g){
  		g.drawImage(ima,0,0,width,height,this);
  	}
  }
}