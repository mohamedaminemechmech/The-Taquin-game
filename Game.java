 import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;



public class Game implements Runnable {
	
	private Display display;
	public int width , height;
	public String title;
	private boolean running=false;
	
	Thread thread;
	
	private BufferStrategy bs;
	private Graphics g;
	
	private MouseManager mouseManager;
	
	public GameState gameState;
	public State menuState;
	
	public int side=4;
	
	
	public Game(String title, int width, int height) {
		this.width= width;
		this.height= height;
		this.title= title;
		mouseManager = new MouseManager();
	}
	private void init() {
		//construit la page du choix des paramètres
		display= new Display(title, width, height);
		getDisplay().getFrame().addMouseListener(mouseManager);
		getDisplay().getFrame().addMouseMotionListener(mouseManager);
		getDisplay().getCanvas().addMouseListener(mouseManager);
		getDisplay().getCanvas().addMouseMotionListener(mouseManager);
		menuState = new MenuState(this);
		State.setState(menuState);
		
	}
	private void tick() throws InterruptedException {
		//assure le bon déroulement du jeu 
		
		if(State.getState() != null)
			State.getState().tick();
		
	}
	
	 void render( ) {
		 //dessine la page initial de choix des paramètres 
		bs =getDisplay().getCanvas().getBufferStrategy();
		if(bs == null) {
			getDisplay().getCanvas().createBufferStrategy(3);
		    return;
		}
		g= bs.getDrawGraphics();
		g.clearRect(0, 0, width, height);

		if(State.getState() != null)
			State.getState().render(g);
		
		//End Drawing!
		bs.show();
		g.dispose();		
				
		bs.show();
		g.dispose();
		
		
	}
	public void run(){
		//assure le fonctionnement du programme tant qu'il est ouvert
		
		init();
		
		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while(running){
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			
			if(delta >= 1){
				try {
					tick();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				render();
				ticks++;
				delta--;
			}
			
			if(timer >= 1000000000){
				System.out.println("Ticks and Frames: " + ticks);
				ticks = 0;
				timer = 0;
			}
		}
		
		stop();
		
	}
	
	public synchronized void start() {
		//démarre le jeu
		if(running)
			return;
		running = true ;
		thread = new Thread(this);
		thread.start();
		
	}
	
	public synchronized void stop() {
		//fait stoper le programme
		if(!running)
			return;
		running= false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MouseManager getMouseManager(){
		return mouseManager;
	}
	public Display getDisplay() {
		return display;
	}
	 
}
