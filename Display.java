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
		this.title= title;//le titre du fenêtre
		this.height= height;// La longueur du fenêtre
		this.width= width;//La largeur du fenêtre
		createDisplay();
		
	}
	// methode qui difinit les parametre de fenêtre 
	private void createDisplay() {
		frame= new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);// La taille de l'ecran ne peut pas modifié
		frame.setLocationRelativeTo(null);// la postion du fenêtre sur l'ecran
		frame.setVisible(true);//visibilité de l'ecran 
		
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
