import org.newdawn.slick.SlickException;

public class Bike extends Vehicle{


	Bike(String imageSrc, float x, float y, float spd, boolean solid, int dir) throws SlickException {
		super(imageSrc, x,y,spd,solid,dir);
	}
	

	public void update(int delta) {
		
		if(this.getX() >= 1000 || this.getX() <= 24) {
			setSpeed(-1);

		}
	
		this.setSpriteX(this.getX() + getSpeed()*delta);
		

		//x += this.getSpeed();
		
		if(this.getX() < 0 - this.getWidth() && this.getDirection() < 0) {
			this.setSpriteX(App.SCREEN_WIDTH + this.getWidth()/2);
		}
		if(this.getX()> App.SCREEN_WIDTH + this.getWidth() && this.getDirection() > 0) {
			this.setSpriteX(0 - this.getWidth()/2);
		}
		
		/* update boundbox for vehicle */
		setX(this.getX());
		setY(this.getY());
	}
	
}
