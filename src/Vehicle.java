import org.newdawn.slick.SlickException;

public class Vehicle extends Sprite{
	
	private float speed;
	//private int offset;
	private int direction;
	private boolean underWater = false;
	private boolean solid;
	private boolean offScreen;

	
	Vehicle(String imageSrc, float x, float y, float spd, boolean solid, int dir) throws SlickException {
		super(imageSrc, x,y);
		
		/* initialise vehicle details */
		direction = dir;		
		speed = spd * direction;
		this.solid = solid;
	}
	
	
	public void update(int delta) {
		
		/* place vehicle and fix it's position when it goes off screen */
		this.setSpriteX(this.getX() + speed*delta);
		offScreen = false;
		if(this.getX() < 0 - this.getWidth() && direction < 0) {
			this.setSpriteX(App.SCREEN_WIDTH + this.getWidth()/2);
			offScreen = true;
		}
		if(this.getX() > App.SCREEN_WIDTH + this.getWidth() && direction > 0) {
			this.setSpriteX(0 - this.getWidth()/2);
			offScreen = true;
		}
		
		/* update boundbox for vehicle */
		setX(getX());
		setY(getY());
	}
	
	public float getSpeed() {
		return speed;
	}
	public boolean getUnderWater() {
		return underWater;
	}
	public void setSpeed(int dir) {
		speed = dir*speed;
	}
	public int getDirection() {
		return direction;
	}
	public boolean getSolid() {
		return solid;
	}
	public boolean getOffScreen() {
		return offScreen;
	}

	
}