import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Turtle extends Vehicle{
	private Image image;

	
	Turtle(String imageSrc, float x, float y, float spd, boolean solid, int dir) throws SlickException {
		super(imageSrc, x, y, spd, solid, dir);
		image = new Image(imageSrc);
		this.setX(x);
		this.setY(y);
		
	}

	
	public void render(Graphics g) {
		if((World.map_time % 9000.0) <= 7000) {
			image.drawCentered(getX(), getY());
		}
	}
	
	public boolean getUnderWater() {
		if((World.map_time % 9000.0) <= 7000) {
			return false;
		}
		return true;	
		
	}
	
}