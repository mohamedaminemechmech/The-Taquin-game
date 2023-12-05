

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class MenuState extends State {
	
	private JFrame frame;
	private Game game;
	private boolean s8=false,s15=false,s24=false,numbers=false,image=false;
	private int width=150,height=90,s;
	public MenuState(Game g) {
		game=g;
		frame=game.getDisplay().getFrame();
		frame.setBackground(Color.BLACK);
		frame.setForeground(new Color(0x6495ED));
		
	}

	@Override
	public void tick() {
		//Assure le bon deroulement du jeu et le deplacement des pixels (petits images) avec l'aide de Mouse manager.
		int ex = game.getMouseManager().getMouseX();
        int ey = game.getMouseManager().getMouseY();
        
    	if( ex>20 && ex<180 && ey>236 && ey<326 && game.getMouseManager().isLeftPressed())
    		{s8=true;s15=false;s24=false;s=3;}
	    if( ex>220 && ex<380 && ey>236 && ey<326  && game.getMouseManager().isLeftPressed())
	    	{s8=false;s15=true;s24=false;s=4;}
    	if( ex>420 && ex<570 && ey>236 && ey<326 && game.getMouseManager().isLeftPressed())
	    	{s8=false;s15=false;s24=true;s=5;}
    	if( ex>100 && ex<250 && ey>408 && ey<498 && game.getMouseManager().isLeftPressed())
    		{numbers=true;image=false;}
    	if( ex>345 && ex<495 && ey>408 && ey<498 && game.getMouseManager().isLeftPressed())
    		{image=true;numbers=false;}
    	if((s8||s15||s24)&&numbers) {
    		s8=s15=s24=false;
    		numbers=false;
    		GameState gameState = new GameState(game,s);
    		setState(gameState);
    	}
    	if((s8||s15||s24)&&image) {
    		JFileChooser fileChooser = new JFileChooser();
    		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    		int result = fileChooser.showOpenDialog(game.getDisplay().getFrame());
    		if (result == JFileChooser.APPROVE_OPTION) {
    		    File selectedFile = fileChooser.getSelectedFile();
    	        FileInputStream fis=null;
				try {
					fis = new FileInputStream(selectedFile);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
    	        BufferedImage img=null;
				try {
					img = ImageIO.read(fis);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    java.awt.Image tmp = img.getScaledInstance(600, 600, java.awt.Image.SCALE_SMOOTH);
			    BufferedImage dimg = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);

			    Graphics2D g2d = dimg.createGraphics();
			    g2d.drawImage(tmp, 0, 0, null);
			    g2d.dispose();
				image=false;
				s8=s15=s24=false;
    	        ImageState imagestate= new ImageState(game,dimg,s);	    
    		    setState(imagestate);
    		}
    	}
	}

	@Override
	public void render(Graphics g) {
		//c'est la fonction responsable de la bonne partition des couleur.
		if(s8)
		{
			g.setColor(Color.red.darker());
            g.fillRoundRect(20, 236, width, height, 25, 25);
            g.drawRoundRect(20, 236, width, height, 25, 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 60));
 
            drawCenteredString(g, "8", 20, 236);
		}
		if(!s8) {
			g.setColor(new Color(0x6495ED));
            g.fillRoundRect(20, 236, width, height, 25, 25);            
            g.drawRoundRect(20, 236, width, height, 25, 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 60));
 
            drawCenteredString(g, "8", 20, 236);
		}
		if(s15)
		{
			g.setColor(Color.red.darker());
            g.fillRoundRect(220, 236, width, height, 25, 25);
            g.drawRoundRect(220, 236, width, height, 25, 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 60));
 
            drawCenteredString(g, "15", 220, 236);
		}
		else {
			g.setColor(new Color(0x6495ED));
            g.fillRoundRect(220, 236, width, height, 25, 25);            
            g.drawRoundRect(220, 236, width, height, 25, 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 60));
 
            drawCenteredString(g, "15", 220, 236);
		}
		if(s24)
		{
			g.setColor(Color.red.darker());
            g.fillRoundRect(420, 236, width, height, 25, 25);
            g.drawRoundRect(420, 236, width, height, 25, 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 60));
 
            drawCenteredString(g, "24", 420, 236);
		}
		else {
			g.setColor(new Color(0x6495ED));
            g.fillRoundRect(420, 236, width, height, 25, 25);            
            g.drawRoundRect(420, 236, width, height, 25, 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 60));
 
            drawCenteredString(g, "24", 420, 236);
		}
		if(numbers)
		{
			g.setColor(Color.red.darker());
            g.fillRoundRect(100, 408, width, height, 25, 25);
            g.drawRoundRect(100, 408, width, height, 25, 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
 
            drawCenteredString(g, "numbers", 100, 408);
		}
		else {
			g.setColor(new Color(0x6495ED));
            g.fillRoundRect(100, 408, width, height, 25, 25);            
            g.drawRoundRect(100, 408, width, height, 25, 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
 
            drawCenteredString(g, "numbers", 100, 408);
		}
		if(image)
		{
			g.setColor(Color.red.darker());
            g.fillRoundRect(345, 408, width, height, 25, 25);
            g.drawRoundRect(345, 408, width, height, 25, 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
 
            drawCenteredString(g, "image", 345, 408);
		}
		else {
			g.setColor(new Color(0x6495ED));
            g.fillRoundRect(345, 408, width, height, 25, 25);            
            g.drawRoundRect(345, 408, width, height, 25, 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
 
            drawCenteredString(g, "image", 345, 408);
		}
		
	}
    private void drawCenteredString(Graphics g, String s, int x, int y) {
    	//c'est la fonction qui consiste a desiner les different case .
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int des = fm.getDescent();
 
        x = x + (width - fm.stringWidth(s)) / 2;
        y = y + (asc + (height - (asc + des)) / 2);
 
        g.drawString(s, x, y);
    }

}
