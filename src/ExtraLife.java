
import org.newdawn.slick.SlickException;

public class ExtraLife extends Sprite{
	private int moves;
	private int dir = 1;
	private int position;
	
	public ExtraLife(String imageSrc, float x, float y, float width) throws SlickException{
		super(imageSrc, x, y);

		moves = (int) Math.ceil(width/48) - 1;
		position = Math.floorDiv(moves, 2);
	}
	
	public void update() {
		if(World.map_time % 1000 == 0) {
			if(position == moves || position == 0) { 
				dir = -1*dir;
			}
			this.setSpriteX(getX()+ dir*48);
			position += dir;
			setX(getX());
			setY(getY());
		}
		if((World.map_time % 4000.0) <= 5000 ) {
			this.setUnderWater(false);
		}
		else {
			this.setUnderWater(true);
		}
	}
}