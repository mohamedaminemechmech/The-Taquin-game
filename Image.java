import java.awt.image.BufferedImage;

public class Image {
	public BufferedImage image;//le morceau d'image  
	public int c;//sa rang
	
	//constructeur de la classe image 
	public Image(BufferedImage i,int n) {
		image=i;
		c=n;
		System.out.println("c="+c);
	}

	

}
