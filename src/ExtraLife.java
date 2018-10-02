
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
		if(position == moves || position == 0) { 
			dir = -1*dir;
		}
		this.setSpriteX(getX()+ dir*48);
		position += dir;
		setX(getX());
		setY(getY());
	}
}