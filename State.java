import java.awt.Graphics;


public abstract class State {
	//classe abstraite mère des classes GameState , ImageState et MenuState

	private static State currentState = null;
	
	public static void setState(State state){
		currentState = state;
	}
	
	public static State getState(){
		return currentState;
	}
	
	//CLASS
	public abstract void tick() throws InterruptedException;
	
	public abstract void render(Graphics g);
	
}