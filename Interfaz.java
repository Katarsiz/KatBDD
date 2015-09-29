import java.awt.EventQueue;
import java.sql.*;
import javax.swing.*;
import java.sql.Connection;

public class Interfaz{

	private JFrame frame;

	public static void main(String [] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					Interfaz i = new Interfaz();
					i.frame.setVisible(true);
				} catch (Exception exe){
					System.out.println("Te odio" + exe);
				}
			}
		});
	}

	Connection conex;

	public Interfaz(){
		frame = new JFrame();
		frame.setBounds(100,100,450,450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		conex = Conector.conectar();
	}

}