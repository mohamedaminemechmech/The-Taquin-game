import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Launcher  {
	public static void main(String[] args) throws IOException  {
		//c'est la partie main du programe qui lanche le jeu.
		Game game = new Game("Taquin", 600, 600);
		// creation d'une nouvelle partie sous le titre "Tile Game" de longuer 600 et largeur 600
		game.start();        
    }
		
        
            
        
		
	}


