import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
public class ImageState extends State {
	private BufferedImage test;
	private Image[] imgs;
	private Game game;
	public  int side ;
    private  int numTiles,chunkWidth,chunkHeight ;
    private  Random rand = new Random();
    private  int tileSize;
    private int blankPos;
    private  int margin;
    private  int gridSize;
    private boolean gameOver;
	private JFrame frame;
	public ImageState(Game g,BufferedImage image,int s) {
		test=image;
		game=g;
		side=s;
		numTiles = side * side -1 ;
		final int dim = 600;
		margin = 0;
        tileSize = (dim - 2 * margin) / side;
        gridSize = tileSize * side;
        chunkWidth = image.getWidth() / side; // determines the chunk width and height
        chunkHeight = image.getHeight() / side;
        int count = 0;
        imgs = new Image[numTiles+1];//Image array to hold image chunks
        System.out.println("numTiles= "+ numTiles);
        for (int x = 0; x < side; x++) {
            for (int y = 0; y < side; y++) {
            	BufferedImage img= new BufferedImage(chunkWidth, chunkHeight, image.getType());
            	Image pic=new Image(img,count);
                imgs[count]=pic;
                Graphics2D gr = imgs[count++].image.createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                gr.dispose();

            }
        }
        frame=game.getDisplay().getFrame();
        frame.setPreferredSize(new Dimension(dim, dim + margin));
        frame.setBackground(Color.BLACK);
        frame.setForeground(new Color(0x6495ED)); // cornflowerblue
        frame.setFont(new Font("SansSerif", Font.BOLD, 60));
        newGame();


	}
	@Override
	public void tick() {
		//assure le bon déroulement du jeu
		if (gameOver) {
			State.setState(game.menuState);
			} 		
		else if(game.getMouseManager().isLeftPressed())
			{
			int ex = game.getMouseManager().getMouseX() - margin;
            int ey = game.getMouseManager().getMouseY() - margin;

            
            if (ex < 0 || ex > gridSize || ey < 0 || ey > gridSize) {
                return;
            }
            int c1 = ex / tileSize;
            int r1 = ey / tileSize;
            int c2 = blankPos % side;
            int r2 = blankPos / side;

       
            int clickPos = r1 * side + c1;
            int dir = 0;
            if (c1 == c2 && Math.abs(r1 - r2) > 0) {
                dir = (r1 - r2) > 0 ? side : -side;
            } else if (r1 == r2 && Math.abs(c1 - c2) > 0) {
                dir = (c1 - c2) > 0 ? 1 : -1;
            }

            
            if (dir != 0) {
                do {
                    int newBlankPos = blankPos + dir;
                    imgs[blankPos].c=imgs[newBlankPos].c;imgs[blankPos].image=imgs[newBlankPos].image;
                    blankPos = newBlankPos;
                } while (blankPos != clickPos);
                imgs[blankPos].image=null;
                imgs[blankPos].c=0;
            }
            gameOver = isSolved();
        }
        frame.repaint();
    }





		

	
	@Override
	public void render(Graphics g) {
		//dessine de nouveau le damier une fois il est changé
        for (int i = 0; i < imgs.length; i++) {
        	System.out.println(side);
            int r = i / side;
            int c = i % side;
            int x = margin + c * tileSize;
            int y = margin + r * tileSize;
            if (imgs[i].c == 0) {
                if (gameOver) {
                    g.setColor(Color.GREEN);
                    drawCenteredString(g, "\u2713", x, y);
                }
                continue;
            }
            g.drawImage(imgs[i].image, x, y,null);


 

 

            

            
        }
	}

	
    private void newGame() {
    	//lance une nouvelle partie
        do {
            reset();
            shuffle();
        } while (!isSolvable());
        gameOver = false;
    }
    private void reset() {
    	//fait construit le tableau qui contient les images des cases de damier
        for (int i = 0; i < imgs.length; i++) {
        	System.out.println(i);
            imgs[i].c=(i + 1) % imgs.length; 
        }
        blankPos = imgs.length - 1;
    }
    private void shuffle() {
        // don't include the blank space in the shuffle, leave it
        // in the home position
    	//fait mélanger les cases du damier
        int n = numTiles;
        while (n > 1) {
            int r = rand.nextInt(n--);
            Image tmp = imgs[r];
            imgs[r]=imgs[n]; 
            imgs[n]=tmp; 
        }
    }
    private boolean isSolvable() {
    	//vérifie si ce damier a une solution ou non
        int countInversions = 0;
        for (int i = 0; i < numTiles; i++) {
            for (int j = 0; j < i; j++) {
                if (imgs[j].c > imgs[i].c) {
                    countInversions++;
                }
            }
        }
        return countInversions % 2 == 0;
    }
    private boolean isSolved() {
    	//vérifie si le damier est résolu ou non
        if (imgs[imgs.length - 1].c != 0) {
            return false;
        }
        for (int i = numTiles - 1; i >= 0; i--) {
            if (imgs[i].c != i + 1) {
                return false;
            }
        }
        return true;
    }
    private void drawStartMessage(Graphics g) {
    	//affiche un message au début du jeu
        if (gameOver) {
            g.setFont(frame.getFont().deriveFont(Font.BOLD, 18));
            g.setColor(frame.getForeground());
            String s = "click to start a new game";
            int x = (frame.getWidth() - g.getFontMetrics().stringWidth(s)) / 2;
            int y = frame.getHeight() - margin;
            g.drawString(s, x, y);
        }
    }
    private void drawCenteredString(Graphics g, String s, int x, int y) {
    	//dessine les cases du damier
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int des = fm.getDescent();
        x = x + (tileSize - fm.stringWidth(s)) / 2;
        y = y + (asc + (tileSize - (asc + des)) / 2);
        g.drawString(s, x, y);
    }

   

    
}