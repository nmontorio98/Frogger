
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player extends Sprite {
	
	public static float SPEED = (float)World.TILE_SIZE;
	private float last_x;
	private float last_y;
	
	public Player(String imageSrc, float x, float y) throws SlickException {
		super(imageSrc, x, y);
	}
	
	/* Moves Player given direction of input and speed*/
	public void update(Input input, int delta) {
		last_x = this.getX();
		last_y = this.getY();

		if(input.isKeyPressed(Input.KEY_LEFT)) {
			this.setSpriteX(this.getX() - SPEED);
			
		}
		else if(input.isKeyPressed(Input.KEY_RIGHT)) {

			this.setSpriteX(this.getX()+ SPEED);
		}
		else if(input.isKeyPressed(Input.KEY_UP)) {

			this.setSpriteY(this.getY() - SPEED);
		}
		else if(input.isKeyPressed(Input.KEY_DOWN)) {

			this.setSpriteY(this.getY()+ SPEED);
		}

		
		/* keeps player from going off (and partially off) screen*/
		if (this.getX() < World.TILE_SIZE) {
			this.setSpriteX(World.TILE_SIZE);
		}
		if(this.getX() > App.SCREEN_WIDTH - World.TILE_SIZE) {
			this.setSpriteX(App.SCREEN_WIDTH - World.TILE_SIZE); 
		}
		if (this.getY() < World.TILE_SIZE) {
			this.setSpriteY(World.TILE_SIZE);
		}
		if(this.getY() > App.SCREEN_HEIGHT-World.TILE_SIZE) {
			this.setSpriteY(App.SCREEN_HEIGHT - World.TILE_SIZE);
		}
		
		/* Updates players boundbox */	
		setX(this.getX());
		setY(this.getY());

	} 
	
	public float getLastX() {
		return last_x;
	}
	public float getLastY() {
		return last_y;
	}

}