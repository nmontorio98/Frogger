import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Lives{
	private String image_path = "assets/lives.png";
	private Image image;
	private float x,y;
	
	
	Lives(float x, float y) throws SlickException{
		this.x = x;
		this.y = y;
		image = new Image(image_path);
	}
	
	void render(Graphics g){
		image.drawCentered(x, y);
	}
}