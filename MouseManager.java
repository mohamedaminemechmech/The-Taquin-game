import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseManager implements MouseListener, MouseMotionListener {

	private boolean leftPressed, rightPressed;
	private int mouseX, mouseY;
	
	public MouseManager(){
		//c'est la fonction qui assure la traduction des donnés entrés par la sourie vers le programme principal (le jeu taquin).
		
	}
	
	// Getters
	
	public boolean isLeftPressed(){
		return leftPressed;
	}
	
	public boolean isRightPressed(){
		return rightPressed;
	}
	
	public int getMouseX(){
		return mouseX;
	}
	
	public int getMouseY(){
		return mouseY;
	}
	
	// Implemented methods
	
	@Override
	public void mousePressed(MouseEvent e) {
		//c'est la fonction qui faire la projection de boutons droite et gauche de la sourie vers le programme. 
		if(e.getButton() == MouseEvent.BUTTON1)
			leftPressed = true;
		else if(e.getButton() == MouseEvent.BUTTON3)
			rightPressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//c'est la fonction qui detecte que les boutons de sourie ne sont pas cliqués et faire traduire dans le programme.
		if(e.getButton() == MouseEvent.BUTTON1)
			leftPressed = false;
		else if(e.getButton() == MouseEvent.BUTTON3)
			rightPressed = false;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//c'est la fonction qui detecte le mouvement de sourie.
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}