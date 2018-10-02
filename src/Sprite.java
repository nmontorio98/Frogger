import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import utilities.BoundingBox;

public class Sprite extends BoundingBox{
	private final Image image;
	private float xpos, ypos, prev_x, prev_y;
	//private float width;
	//private float height;


	public Sprite(String imageSrc, float x, float y) throws SlickException{

		/* Creates a bound box over the sprite */
		super( new Image(imageSrc), x, y);
		/* initialises sprite */
		image = new Image(imageSrc);
		this.xpos = x;
		this.ypos = y; 
	}
	
	public void attach(float speed, int delta) {
		this.xpos+=speed*delta;	
	}
	
	public void moveBack() {
		this.xpos = prev_x;
		this.ypos = prev_y;
	}
	
	/* Getters */
	public float getX() {
		return xpos;
	}
	
	public float getY() {
		return ypos;
	}
	public float getWidth() {	
		return image.getWidth();
	}
	
	public void setSpriteX(float x) {
		this.xpos = x;
	}
	public void setSpriteY(float y) {
		this.ypos = y;
	}
	
	public float getPrevX() {
		return prev_x;
	}
	
	/*public float getHeight() {
		//return height;
	}*/
	
	/* Update bound box with setters */
	public void update(Input input, int delta) {
		setX(xpos);
		setY(ypos);
		
	}
	
	public void render(Graphics g) {
		prev_x = this.xpos;
		prev_y = this.ypos;
		image.drawCentered(xpos, ypos);
	}
	

	
}
