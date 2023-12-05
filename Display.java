import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display {
	private JFrame frame;
	private Canvas canvas;
	private String title;
	private int width ,height;
	
	//constructeur du classe
	public Display(String title, int width, int height) {
		this.title= title;//le titre du fen�tre
		this.height= height;// La longueur du fen�tre
		this.width= width;//La largeur du fen�tre
		createDisplay();
		
	}
	// methode qui difinit les parametre de fen�tre 
	private void createDisplay() {
		frame= new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);// La taille de l'ecran ne peut pas modifi�
		frame.setLocationRelativeTo(null);// la postion du fen�tre sur l'ecran
		frame.setVisible(true);//visibilit� de l'ecran 
		
		canvas= new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));		
		frame.add(canvas);
		frame.pack();
		
	}
	public Canvas getCanvas() {
		return canvas;
	}
	public JFrame getFrame(){
		return frame;
	}

}
