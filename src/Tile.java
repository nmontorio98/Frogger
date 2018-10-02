import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import utilities.BoundingBox;

import org.newdawn.slick.Image;

public class Tile extends BoundingBox{
	
	private final Image image;
/*	private final Image water;
	private final Image grass;*/
	float x;
	float y;
	int touchable; // 0 for kill on touch, 1 for allowed to touch, 2 for can't touch(cant move over)

	/* Will end up splitting these to allow for different types of tiles. But I currently dont have
	 * the time */
	/*public Tile() throws SlickException{
		water = new Image("assets/water.png");
		grass = new Image("assets/grass.png");
		image = null;
		x = 0;
		y = 0;
	}*/
	
	
	public Tile(String imageSrc, int x, int y) throws SlickException {
		super( new Image(imageSrc), x, y);
		
		/* initialises sprite */
		image = new Image(imageSrc);
		this.x = x;
		this.y = y;
/*		this.grass = null;
		this.water = null;*/
	}

	public Tile(String imageSrc, int x, int y, int touchable) throws SlickException {
		super( new Image(imageSrc), x, y);
		
		/* initialises sprite */
		image = new Image(imageSrc);
		this.x = x;
		this.y = y;
//		this.grass = null;
//		this.water = null;
		this.touchable = touchable;
	}
	
	
	public void update(Input input, int delta) {
		
	}
	
	public void render(Graphics g) {
		image.drawCentered(x, y);	
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	
	/* Draws the rows of tiles as specified by the specification */
	/*public void render(Graphics g) {
		for(int x = 0; x <= App.SCREEN_WIDTH; x += World.TILE_SIZE) {
			for(int y = 0; y<= App.SCREEN_HEIGHT; y += World.TILE_SIZE) {
				if(y > 48 && y <= 336) {
					water.drawCentered(x, y);
				}
				if (y==384 || y == 672) {
					grass.drawCentered(x, y);
				}
			}
		}
	}*/
}