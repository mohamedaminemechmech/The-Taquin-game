
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

import javax.swing.JFrame;


public class GameState extends State {

    private Game game;
	public  int side ;
    private  int numTiles ;
 
    private  Random rand = new Random();
    private  int[] tiles ;
    private  int tileSize;
    private int blankPos;
    private  int margin;
    private  int gridSize;
    private boolean gameOver;
	private JFrame frame;
	public GameState(Game g,int s){
		game=g;
		side=s;
		numTiles = side * side - 1; 
		tiles = new int[numTiles + 1];
        final int dim = 600;
        
        
        margin = 0;
        tileSize = (dim - 2 * margin) / side;
        gridSize = tileSize * side;
        frame=game.getDisplay().getFrame();
        frame.setPreferredSize(new Dimension(dim, dim + margin));
        frame.setBackground(Color.BLACK);
        frame.setForeground(new Color(0x6495ED)); // cornflowerblue
        frame.setFont(new Font("SansSerif", Font.BOLD, 60));
        newGame();

		
		
	}
	
	@Override
	public void tick() throws InterruptedException {
		//assure le bon déroulement du jeu 
		if (gameOver) {
			State.setState(game.menuState);
			}
		else if(game.getMouseManager().isRightPressed())
		{
			//appel la méthode solution pour montrer la solution étape par étape
			solution();
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
                    tiles[blankPos] = tiles[newBlankPos];
                    blankPos = newBlankPos;
                } while (blankPos != clickPos);
                tiles[blankPos] = 0;
            }

            gameOver = isSolved();
        }
        frame.repaint();
    }




		
	

	@Override
	public void render(Graphics g) {
		//dessine le graphique du jeu à chaque fois qu'il y a un changement ou qu'elle est appelé
        for (int i = 0; i < tiles.length; i++) {
        	System.out.println(blankPos);
            int r = i / side;
            int c = i % side;
            int x = margin + c * tileSize;
            int y = margin + r * tileSize;
            if (tiles[i] == 0) {
                if (gameOver) {
                    g.setColor(Color.GREEN);
                    drawCenteredString(g, "\u2713", x, y);
                }
                continue;
            }
 
 
            g.setColor(frame.getForeground());
            g.fillRoundRect(x, y, tileSize, tileSize, 25, 25);
            g.setColor(Color.blue.darker());
            g.drawRoundRect(x, y, tileSize, tileSize, 25, 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 60));
 
            drawCenteredString(g, String.valueOf(tiles[i]), x, y);
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
    	//fait construit le tableau qui contient les indices des cases des damier
        for (int i = 0; i < tiles.length; i++) {
        	tiles[i] = (i + 1) % tiles.length;
        }
        blankPos = tiles.length - 1;
    }
 
    private void shuffle() {
        // don't include the blank space in the shuffle, leave it
        // in the home position
    	//fait mélanger les cases du damier
        int n = numTiles;
        while (n > 1) {
            int r = rand.nextInt(n--);
            int tmp = tiles[r];
            tiles[r] = tiles[n];
            tiles[n] = tmp;
        }
    }
    private boolean isSolvable() {
    	//fait vérifier si ce damier a une solution ou non
        int countInversions = 0;
        for (int i = 0; i < numTiles; i++) {
            for (int j = 0; j < i; j++) {
                if (tiles[j] > tiles[i]) {
                    countInversions++;
                }
            }
        }
        return countInversions % 2 == 0;
    }
 
    private boolean isSolved() {
    	//fait vérifier si le damier est résolu ou non
        if (tiles[tiles.length - 1] != 0) {
            return false;
        }
        for (int i = numTiles - 1; i >= 0; i--) {
            if (tiles[i] != i + 1) {
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
    public void left() throws InterruptedException {
    	//fait déplacer le blankPos de 2 cases à gauche 
    	int c2 = blankPos % side;
        int r2 = blankPos / side;
        c2++;
	    r2++;
        if (r2==side) {
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=tiles[blankPos-2];
        	tiles[blankPos-2]=0;
        	blankPos-=2;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        }
        else {
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=tiles[blankPos-2];
        	tiles[blankPos-2]=0;
        	blankPos-=2;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        }
        game.render();
    	game.thread.sleep(1000);
    }
    public void right() throws InterruptedException {
    	//fait déplacer le blankPos de 2 cases à droite
    	int c2 = blankPos % side;
        int r2 = blankPos / side;
        c2++;
	    r2++;
        if (r2==side) {
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=tiles[blankPos+2];
        	tiles[blankPos+2]=0;
        	blankPos+=2;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        }
        else {
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=tiles[blankPos+2];
        	tiles[blankPos+2]=0;
        	blankPos+=2;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        }
        game.render();
    	game.thread.sleep(1000);
    }

    public void up() throws InterruptedException {
    	//fait déplacer le blankPos de 2 cases en haut
    	int c2 = blankPos % side;
        int r2 = blankPos / side;
        c2++;
	    r2++;
        if (c2==side) {
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=tiles[blankPos-(2*side)];
        	tiles[blankPos-(2*side)]=0;
        	blankPos=blankPos-(2*side);
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        }
        else {
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=tiles[blankPos-(2*side)];
        	tiles[blankPos-(2*side)]=0;
        	blankPos=blankPos-(2*side);
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        }
        game.render();
    	game.thread.sleep(1000);
    }

    public void down() throws InterruptedException {
    	//fait déplacer le blankPos de 2 cases en bas
    	int c2 = blankPos % side;
        int r2 = blankPos / side;
        c2++;
	    r2++;
        if (c2==side) {
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=tiles[blankPos+(2*side)];
        	tiles[blankPos+(2*side)]=0;
        	blankPos=blankPos+(2*side);
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        }
        else {
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=tiles[blankPos+(2*side)];
        	tiles[blankPos+(2*side)]=0;
        	blankPos=blankPos+(2*side);
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        }
        game.render();
    	game.thread.sleep(1000);
    }

    public int findpos(int a) {
    	//détermine la place du carreau donné en paramètre
    	int b=0;
    	for(int i=0;i<(numTiles+1);i++) {
    		if (tiles[i]==a) {
    			b= i;
    		}
    		continue;
    	}
    	return b;
    }
    
    public void sol_ligne(int l) throws InterruptedException {
    	//fait la solution des lignes
    	game.render();
    	game.thread.sleep(1000);
    	for(int i=0;i<(side-(1+l));i++) {
    		int c2 = blankPos % side;
    	    int r2 = blankPos / side;
    	    c2++;
    	    r2++;
    	    while(c2!=(i+l)) {
    	    	tiles[blankPos]=tiles[blankPos+1];
    	    	tiles[blankPos+1]=0;
    	    	blankPos+=1;
    	    	c2++;
    	    }
    	    game.render();
        	game.thread.sleep(1000);
    	    while (r2!=l) {
    	    	tiles[blankPos]=tiles[blankPos-side];
    	    	tiles[blankPos-side]=0;
    	    	blankPos-=side;
    	    	r2--;
    	    }
    	    game.render();
        	game.thread.sleep(1000);
    		int b=findpos((l-1)*side+(i+l));
    		int c3 = b % side;
    	    int r3 = b / side;
    	    c2 = blankPos % side;
    	    r2 = blankPos / side;
    	    c2++;
    	    r2++;
    	    c3++;
    	    r3++;
    	    if (c2==c3) {
    	    	 while (r3!=(r2+1)) {
    	    		 tiles[blankPos]=tiles[blankPos+side];
    		    	 tiles[blankPos+side]=0;
    		    	 blankPos+=side;
    		    	 r2++;
    	    	 }
    	    	 game.render();
    	     	game.thread.sleep(1000);
    	    }
    	    else {
    	    	while (r3!=r2) {
    		    	if(r3<r2) {
    		    		tiles[blankPos]=tiles[blankPos-side];
    		    		tiles[blankPos-side]=0;
    		    		blankPos-=side;
    		    		r2--;
    		    	}
    		    	else {
    		    		tiles[blankPos]=tiles[blankPos+side];
    		    		tiles[blankPos+side]=0;
    		    		blankPos+=side;
    		    		r2++;
    		    	}
    		    }
    	    	game.render();
    	    	game.thread.sleep(1000);
    		    while((c2<c3)&&(c2!=(c3-1))) {
    		    	tiles[blankPos]=tiles[blankPos+1];
    		    	tiles[blankPos+1]=0;
    		    	blankPos++;
    		    	c2++;
    		    	if(c2==(c3-1)) {
    		    		break;
    		    	}
    		    }
    		    game.render();
    	    	game.thread.sleep(1000);
    		    while((c2>c3)&&(c2!=(c3+1))) {
    		    	tiles[blankPos]=tiles[blankPos-1];
    		    	tiles[blankPos-1]=0;
    		    	blankPos--;
    		    	c2--;
    		    	if(c2==(c3+1)) {
    		    		break;
    		    	}
    		    }game.render();
    	    	game.thread.sleep(1000);
    		    while(c3<(i+l)){
    		    	tiles[blankPos]=tiles[blankPos-1];
    		    	tiles[blankPos-1]=0;
    		    	blankPos--;
    		    	c2--;
    		    	c3++;
    		    	game.render();
    		    	game.thread.sleep(1000);
    		    	right();
    		    	c2+=2;
    		    }
    		    while(c3>(i+l)){
    		    	tiles[blankPos]=tiles[blankPos+1];
    		    	tiles[blankPos+1]=0;
    		    	blankPos++;
    		    	c2++;
    		    	c3--;
    		    	game.render();
    		    	game.thread.sleep(1000);
    		    	if (c3!=(i+l)) {
    		    		left();
    		    		c2--;
    		    	}
    		    }
    		    if(r3!=l) {
    		    	tiles[blankPos]=tiles[blankPos-side];
    			    tiles[blankPos-side]=0;
    			    blankPos-=side;
    			    r2--;
    			    game.render();
    		    	game.thread.sleep(1000);
    			    tiles[blankPos]=tiles[blankPos-1];
    			    tiles[blankPos-1]=0;
    			    blankPos--;
    			    c2--;
    			    game.render();
    		    	game.thread.sleep(1000);
    		    }
    	    }
    	    
    	    while(r3!=l) {
    	    	tiles[blankPos]=tiles[blankPos+side];
    	    	tiles[blankPos+side]=0;
    	    	blankPos+=side;
    	    	r2++;
    	    	r3--;
    	    	game.render();
    	    	game.thread.sleep(1000);
    	    	if (r3!=l) {
    	    		up();
    	    		r2-=2;
    	    	}
    	    }
    	}
    	int c2 = blankPos % side;
        int r2 = blankPos / side;
        c2++;
	    r2++;
        while(c2!=(side-1)) {
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos+=1;
        	c2++;
        }
        game.render();
    	game.thread.sleep(1000);
        while (r2!=l) {
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	r2--;
        }
        game.render();
    	game.thread.sleep(1000);
        
    	int b=findpos(l*side);
    	int c3 = b % side;
        int r3 = b / side;
        c2 = blankPos % side;
        r2 = blankPos / side;
        c2++;
	    r2++;
	    c3++;
	    r3++;
        if (c2==c3) {
        	while (r3!=(r2+1)) {
          		 tiles[blankPos]=tiles[blankPos+side];
       	    	 tiles[blankPos+side]=0;
       	    	 blankPos+=side;
       	    	 r2++;
          	  }
        	game.render();
        	game.thread.sleep(1000);
        }
        else {
        	while (r3!=r2) {
            	if(r3<r2) {
            		tiles[blankPos]=tiles[blankPos-side];
            		tiles[blankPos-side]=0;
            		blankPos-=side;
            		r2--;
            	}
            	else {
            		tiles[blankPos]=tiles[blankPos+side];
            		tiles[blankPos+side]=0;
            		blankPos+=side;
            		r2++;
            	}
            }
        	game.render();
        	game.thread.sleep(1000);
            while((c2<c3)&&(c2!=(c3-1))) {
            	tiles[blankPos]=tiles[blankPos+1];
            	tiles[blankPos+1]=0;
            	blankPos++;
            	c2++;
            	if (c2==(c3-1)) {
            		break;
            	}
            }
            game.render();
        	game.thread.sleep(1000);
            while((c2>c3)&&(c2!=(c3+1))){
            	tiles[blankPos]=tiles[blankPos-1];
            	tiles[blankPos-1]=0;
            	blankPos--;
            	c2--;
            	if (c2==(c3+1)) {
            		break;
            	}
            }
            game.render();
        	game.thread.sleep(1000);
            while(c3<(side-1)){
            	tiles[blankPos]=tiles[blankPos-1];
            	tiles[blankPos-1]=0;
            	blankPos--;
            	c2--;
            	c3++;
            	game.render();
            	game.thread.sleep(1000);
            	right();
            	c2+=2;
            }
            game.render();
        	game.thread.sleep(1000);
            while(c3>(side-1)){
            	tiles[blankPos]=tiles[blankPos+1];
            	tiles[blankPos+1]=0;
            	blankPos++;
            	c2++;
            	c3--;
            	game.render();
            	game.thread.sleep(1000);
            	if (c3!=(side-1)) {
            		left();
            		c2-=2;
            	}
            }
            if(r3!=l) {
            	tiles[blankPos]=tiles[blankPos-side];
                tiles[blankPos-side]=0;
                blankPos-=side;
                r2--;
                game.render();
            	game.thread.sleep(1000);
                tiles[blankPos]=tiles[blankPos-1];
                tiles[blankPos-1]=0;
                blankPos--;
                c2--;
                game.render();
            	game.thread.sleep(1000);
            }
        }
        while(r3!=l) {
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	r2++;
        	r3--;
        	
        	game.render();
        	game.thread.sleep(1000);
        	if (r3!=l) {
        		up();
        		r2-=2;
        	}
        }
        while(r2!=(l+1)) {
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	r2++;
        }
        game.render();
    	game.thread.sleep(1000);
        while(c2!=(side-1)) {
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	c2--;
        }
        game.render();
    	game.thread.sleep(1000);
        b=findpos((l*side)-1);
        if (b==(l*side)-1) {
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	game.render();
        	game.thread.sleep(1000);
        	down();
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=tiles[blankPos-(2*side)];
        	tiles[blankPos-(2*side)]=0;
        	blankPos=blankPos-(2*side);
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	game.render();
        	game.thread.sleep(1000);
        }
        else {
        	c3 = b % side;
            r3 = b / side;
            c2 = blankPos % side;
            r2 = blankPos / side;
            c2++;
    	    r2++;
    	    c3++;
    	    r3++;
            if (c2==c3) {
            	while (r3!=(r2+1)) {
              		 tiles[blankPos]=tiles[blankPos+side];
           	    	 tiles[blankPos+side]=0;
           	    	 blankPos+=side;
           	    	 r2++;
              	  }
            	game.render();
            	game.thread.sleep(1000);
            }
            else {
            	while (r3!=r2) {
                	if(r3<r2) {
                		tiles[blankPos]=tiles[blankPos-side];
                		tiles[blankPos-side]=0;
                		blankPos-=side;
                		r2--;
                	}
                	else {
                		tiles[blankPos]=tiles[blankPos+side];
                		tiles[blankPos+side]=0;
                		blankPos+=side;
                		r2++;
                	}
                }
            	game.render();
            	game.thread.sleep(1000);
                while((c2<c3)&&(c2!=(c3-1))) {
                	tiles[blankPos]=tiles[blankPos+1];
                	tiles[blankPos+1]=0;
                	blankPos++;
                	c2++;
                	if(c2==(c3-1)) {
                		break;
                	}
                }
                game.render();
            	game.thread.sleep(1000);
                while((c2>c3)&&(c2!=(c3+1))) {
                	tiles[blankPos]=tiles[blankPos-1];
                	tiles[blankPos-1]=0;
                	blankPos--;
                	c2--;
                	if(c2==(c3+1)) {
                		break;
                	}
                }
                game.render();
            	game.thread.sleep(1000);
                while(c3<(side-1)){
                	tiles[blankPos]=tiles[blankPos-1];
                	tiles[blankPos-1]=0;
                	blankPos--;
                	c2--;
                	c3++;
                	game.render();
                	game.thread.sleep(1000);
                	right();
                	c2+=2;
                }
                while(c3>(side-1)){
                	tiles[blankPos]=tiles[blankPos+1];
                	tiles[blankPos+1]=0;
                	blankPos++;
                	c2++;
                	c3--;
                	game.render();
                	game.thread.sleep(1000);
                	if (c3!=(side-1)) {
                		left();
                		c2-=2;
                	}
                }
                if(r3!=(l+1)) {
                	tiles[blankPos]=tiles[blankPos-side];
                    tiles[blankPos-side]=0;
                    blankPos-=side;
                    r2--;
                    game.render();
                	game.thread.sleep(1000);
                    tiles[blankPos]=tiles[blankPos-1];
                    tiles[blankPos-1]=0;
                    blankPos--;
                    c2--;
                    game.render();
                	game.thread.sleep(1000);
                }
            }
            while(r3!=(l+1)) {
            	tiles[blankPos]=tiles[blankPos+side];
            	tiles[blankPos+side]=0;
            	blankPos+=side;
            	r2++;
            	r3--;
            	game.render();
            	game.thread.sleep(1000);
            	if (r3!=(l+1)) {
            		up();
            		r2-=2;
            	}
            }
            while(c2!=side) {
            	tiles[blankPos]=tiles[blankPos+1];
            	tiles[blankPos+1]=0;
            	blankPos++;
            	c2++;
            }
            game.render();
        	game.thread.sleep(1000);
            while(r2!=l) {
            	tiles[blankPos]=tiles[blankPos-side];
            	tiles[blankPos-side]=0;
            	blankPos-=side;
            	r2--;
            }
            game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	game.render();
        	game.thread.sleep(1000);
        }
        
    	
    }


    public void sol_col(int l) throws InterruptedException {
    	//fait la solution des colonnes
    	for(int i=0;i<(side-(l+2));i++) {
    		int c2 = blankPos % side;
    	    int r2 = blankPos / side;
    	    c2++;
    	    r2++;
    	    while (r2!=(i+1+l)) {
    	    	tiles[blankPos]=tiles[blankPos+side];
    	    	tiles[blankPos+side]=0;
    	    	blankPos+=side;
    	    	r2++;
    	    }
    	    game.render();
        	game.thread.sleep(1000);
    	    while(c2!=l) {
    	    	tiles[blankPos]=tiles[blankPos-1];
    	    	tiles[blankPos-1]=0;
    	    	blankPos--;
    	    	c2--;
    	    }
    	    game.render();
        	game.thread.sleep(1000);
    		int b=findpos((side*(i+l)+l));
    		System.out.println("b=");
    		System.out.println(b);
    		int c3 = b % side;
    	    int r3 = b / side;
    	    c2 = blankPos % side;
    	    r2 = blankPos / side;
    	    c2++;
    	    r2++;
    	    c3++;
    	    r3++;
    	    if (r2==r3) {
    	    	 while (c3!=(c2+1)) {
    	    		 tiles[blankPos]=tiles[blankPos+1];
    		    	 tiles[blankPos+1]=0;
    		    	 blankPos++;
    		    	 c2++;
    	    	 }
    	    	 game.render();
    	     	game.thread.sleep(1000);
    	    }
    	    else {
    	    	while (c3!=c2) {
    		    	if(c3<c2) {
    		    		tiles[blankPos]=tiles[blankPos-1];
    		    		tiles[blankPos-1]=0;
    		    		blankPos--;
    		    		c2--;
    		    	}
    		    	else {
    		    		tiles[blankPos]=tiles[blankPos+1];
    		    		tiles[blankPos+1]=0;
    		    		blankPos++;
    		    		c2++;
    		    	}
    		    }
    	    	game.render();
    	    	game.thread.sleep(1000);
    		    while((r2<r3)&&(r2!=(r3-1))) {
    		    	tiles[blankPos]=tiles[blankPos+side];
    		    	tiles[blankPos+side]=0;
    		    	blankPos+=side;
    		    	r2++;
    		    	if(r2==(r2-1)) {
    		    		break;
    		    	}
    		    }
    		    game.render();
    	    	game.thread.sleep(1000);
    		    while((r2>r3)&&(r2!=(r3+1))) {
    		    	tiles[blankPos]=tiles[blankPos-side];
    		    	tiles[blankPos-side]=0;
    		    	blankPos-=side;
    		    	r2--;
    		    	if(r2==(r3+1)) {
    		    		break;
    		    	}
    		    }
    		    game.render();
    	    	game.thread.sleep(1000);
    		    while(r3<(i+1+l)){
    		    	tiles[blankPos]=tiles[blankPos-side];
    		    	tiles[blankPos-side]=0;
    		    	blankPos-=side;
    		    	r2--;
    		    	r3++;
    		    	game.render();
    	        	game.thread.sleep(1000);
    		    	down();
    		    	r2+=2;
    		    }
    		    game.render();
    	    	game.thread.sleep(1000);
    		    while(r3>(i+1+l)){
    		    	tiles[blankPos]=tiles[blankPos+side];
    		    	tiles[blankPos+side]=0;
    		    	blankPos+=side;
    		    	r2++;
    		    	r3--;
    		    	game.render();
    	        	game.thread.sleep(1000);
    		    	if (r3!=(i+1+l)) {
    		    		up();
    		    		r2-=2;
    		    	}
    		    }
    		    game.render();
    	    	game.thread.sleep(1000);
    		    if(c3!=l) {
    		    	tiles[blankPos]=tiles[blankPos-1];
    			    tiles[blankPos-1]=0;
    			    blankPos--;
    			    c2--;
    			    game.render();
    		    	game.thread.sleep(1000);
    			    tiles[blankPos]=tiles[blankPos-side];
    			    tiles[blankPos-side]=0;
    			    blankPos-=side;
    			    r2--;
    			    game.render();
    		    	game.thread.sleep(1000);
    		    }
    	    }
    	    while(c3!=l) {
    	    	tiles[blankPos]=tiles[blankPos+1];
    	    	tiles[blankPos+1]=0;
    	    	blankPos++;
    	    	c2++;
    	    	c3--;
    	    	game.render();
            	game.thread.sleep(1000);
    	    	if (c3!=l) {
    	    		left();
    	    		c2-=2;
    	    	}
    	    }
    	    game.render();
        	game.thread.sleep(1000);
    	}
    	int c2 = blankPos % side;
        int r2 = blankPos / side;
        c2++;
        r2++;
        while(r2!=(side-1)) {
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	r2++;
        }
        game.render();
    	game.thread.sleep(1000);
        while (c2!=l) {
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	c2--;
        }
        game.render();
    	game.thread.sleep(1000);
    	int b=findpos(((side-1)*side)+l);
    	int c3 = b % side;
        int r3 = b / side;
        c2 = blankPos % side;
        r2 = blankPos / side;
        c2++;
        r2++;
        c3++;
        r3++;
        if (r2==r3) {
        	while (c3!=(c2+1)) {
          		 tiles[blankPos]=tiles[blankPos+1];
       	    	 tiles[blankPos+1]=0;
       	    	 blankPos++;
       	    	 c2++;
          	  }
        	game.render();
        	game.thread.sleep(1000);
        }
        else {
        	while (c3!=c2) {
            	if(c3<c2) {
            		tiles[blankPos]=tiles[blankPos-1];
            		tiles[blankPos-1]=0;
            		blankPos--;
            		c2--;
            	}
            	else {
            		tiles[blankPos]=tiles[blankPos+1];
            		tiles[blankPos+1]=0;
            		blankPos++;
            		c2++;
            	}
            }
        	game.render();
        	game.thread.sleep(1000);
            while((r2<r3)&&(r2!=(r3-1))) {
            	tiles[blankPos]=tiles[blankPos+side];
            	tiles[blankPos+side]=0;
            	blankPos+=side;
            	r2++;
            	if (r2==(r3-1)) {
            		break;
            	}
            }
            game.render();
        	game.thread.sleep(1000);
            while((r2>r3)&&((r2!=(r3+1)))) {
            	tiles[blankPos]=tiles[blankPos-side];
            	tiles[blankPos-side]=0;
            	blankPos-=side;
            	r2--;
            	if (r2==(r3+1)) {
            		break;
            	}
            }
            game.render();
        	game.thread.sleep(1000);
            while(r3<(side-1)){
            	tiles[blankPos]=tiles[blankPos-side];
            	tiles[blankPos-side]=0;
            	blankPos-=side;
            	r2--;
            	r3++;
            	game.render();
            	game.thread.sleep(1000);
            	down();
            	r2+=2;
            }
            game.render();
        	game.thread.sleep(1000);
            while(r3>(side-1)){
            	tiles[blankPos]=tiles[blankPos+side];
            	tiles[blankPos+side]=0;
            	blankPos+=side;
            	r2++;
            	r3--;
            	game.render();
            	game.thread.sleep(1000);
            	if (r3!=(side-1)) {
            		up();
            		r2--;
            	}
            }
            game.render();
        	game.thread.sleep(1000);
            if(c3!=l) {
            	tiles[blankPos]=tiles[blankPos-1];
                tiles[blankPos-1]=0;
                blankPos--;
                c2--;
                game.render();
            	game.thread.sleep(1000);
                tiles[blankPos]=tiles[blankPos-side];
                tiles[blankPos-side]=0;
                blankPos-=side;
                r2--;
                game.render();
            	game.thread.sleep(1000);
            }        
        }
        while(c3!=l) {
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	c2++;
        	c3--;
        	game.render();
        	game.thread.sleep(1000);
        	if (c3!=l) {
        		left();
        		c2-=2;
        	}
        }
        game.render();
    	game.thread.sleep(1000);
        while(c2!=(l+1)) {
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	c2++;
        }
        game.render();
    	game.thread.sleep(1000);
        while(r2!=(side-1)) {
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	r2--;
        }
        game.render();
    	game.thread.sleep(1000);
        b=findpos(((side-2)*side)+l);
        if (b==((side-1)*side)+l-1) {
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	game.render();
        	game.thread.sleep(1000);
        	right();
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos--;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=tiles[blankPos-2];
        	tiles[blankPos-2]=0;
        	blankPos=blankPos-2;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+side];
        	tiles[blankPos+side]=0;
        	blankPos+=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	game.render();
        	game.thread.sleep(1000);
        }
        else {
        	c3 = b % side;
            r3 = b / side;
            c2 = blankPos % side;
            r2 = blankPos / side;
            c2++;
            r2++;
            c3++;
            r3++;
            if (r2==r3) {
            	while (c3!=(c2+1)) {
              		 tiles[blankPos]=tiles[blankPos+1];
           	    	 tiles[blankPos+1]=0;
           	    	 blankPos++;
           	    	 c2++;
              	  }
            	game.render();
            	game.thread.sleep(1000);
            }
            else {
            	while (c3!=c2) {
                	if(c3<c2) {
                		tiles[blankPos]=tiles[blankPos-1];
                		tiles[blankPos-1]=0;
                		blankPos--;
                		c2--;
                	}
                	else {
                		tiles[blankPos]=tiles[blankPos+1];
                		tiles[blankPos+1]=0;
                		blankPos++;
                		c2++;
                	}
                }
            	game.render();
            	game.thread.sleep(1000);
                while((r2<r3)&&(r2!=(r3-1))) {
                	tiles[blankPos]=tiles[blankPos+side];
                	tiles[blankPos+side]=0;
                	blankPos+=side;
                	r2++;
                	if(r2==(r3-1)) {
                		break;
                	}
                }
                game.render();
            	game.thread.sleep(1000);
                while((r2>r3)&&(r2!=(r3+1))) {
                	tiles[blankPos]=tiles[blankPos-side];
                	tiles[blankPos-side]=0;
                	blankPos-=side;
                	r2--;
                	if(r2==(r3+1)) {
                		break;
                	}
                }
                game.render();
            	game.thread.sleep(1000);
                while(r3<(side-1)){
                	tiles[blankPos]=tiles[blankPos-side];
                	tiles[blankPos-side]=0;
                	blankPos-=side;
                	r2--;
                	r3++;
                	game.render();
                	game.thread.sleep(1000);
                	down();
                	r2+=2;
                }
                game.render();
            	game.thread.sleep(1000);
                while(r3>(side-1)){
                	tiles[blankPos]=tiles[blankPos+side];
                	tiles[blankPos+side]=0;
                	blankPos+=side;
                	r2++;
                	r3--;
                	game.render();
                	game.thread.sleep(1000);
                	if (r3!=(side-1)) {
                		up();
                		r2-=2;
                	}
                }
                game.render();
            	game.thread.sleep(1000);
                if(c3!=l) {
                	tiles[blankPos]=tiles[blankPos-1];
                    tiles[blankPos-1]=0;
                    blankPos--;
                    c2--;
                    game.render();
                	game.thread.sleep(1000);
                    tiles[blankPos]=tiles[blankPos-side];
                    tiles[blankPos-side]=0;
                    blankPos-=side;
                    r2--;
                    game.render();
                	game.thread.sleep(1000);
                }
            }
            while(c3!=(l+1)) {
            	tiles[blankPos]=tiles[blankPos+1];
            	tiles[blankPos+1]=0;
            	blankPos++;
            	c2++;
            	c3--;
            	game.render();
            	game.thread.sleep(1000);
            	if (c3!=(l+1)) {
            		left();
            		c2-=2;
            	}
            }
            while(r2!=side) {
            	tiles[blankPos]=tiles[blankPos+side];
            	tiles[blankPos+side]=0;
            	blankPos+=side;
            	r2++;
            }
            game.render();
        	game.thread.sleep(1000);
            while(c2!=l) {
            	tiles[blankPos]=tiles[blankPos-1];
            	tiles[blankPos-1]=0;
            	blankPos--;
            	c2--;
            }
            game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	game.render();
        	game.thread.sleep(1000);
        	tiles[blankPos]=tiles[blankPos+1];
        	tiles[blankPos+1]=0;
        	blankPos++;
        	game.render();
        	game.thread.sleep(1000);
        }
        
    	
    }


    public void solution() throws InterruptedException {
    	//fait résoudre le damier si elle est appelé
    	startsol();
    	for(int i=0;i<(side-2);i++) {
    		sol_ligne(i+1);
    		sol_col(i+1);
    	}
    	tiles[blankPos]=tiles[blankPos+1];
    	tiles[blankPos+1]=0;
    	blankPos++;
    	if(blankPos!=((side*side)-1)) {
    		tiles[blankPos]=tiles[blankPos+side];
    		tiles[blankPos+side]=0;
    		blankPos+=side;
    	}
    	while(!isSolved()) {
    		tiles[blankPos]=tiles[blankPos-side];
    		tiles[blankPos-side]=0;
    		blankPos-=side;
    		game.render();
        	game.thread.sleep(1000);
    		tiles[blankPos]=tiles[blankPos-1];
    		tiles[blankPos-1]=0;
    		blankPos--;
    		game.render();
        	game.thread.sleep(1000);
    		tiles[blankPos]=tiles[blankPos+side];
    		tiles[blankPos+side]=0;
    		blankPos+=side;
    		game.render();
        	game.thread.sleep(1000);
    		tiles[blankPos]=tiles[blankPos+1];
    		tiles[blankPos+1]=0;
    		blankPos++;
    		game.render();
        	game.thread.sleep(1000);
    	}
    	
    	
    }
    public void startsol() throws InterruptedException {
    	//fait déplacer le blankPos en haut à gauche pour démarrer la solution
    	int c2 = blankPos % side;
        int r2 = blankPos / side;
        c2++;
	    r2++;
        while (r2!=1) {
        	tiles[blankPos]=tiles[blankPos-side];
        	tiles[blankPos-side]=0;
        	blankPos-=side;
        	r2--;
        }
        game.render();
    	game.thread.sleep(1000);
        while(c2!=1) {
        	tiles[blankPos]=tiles[blankPos-1];
        	tiles[blankPos-1]=0;
        	blankPos-=1;
        	c2--;
        }  
        game.render();
    	game.thread.sleep(1000);
    }
    

}
