import org.newdawn.slick.SlickException;

public class Gates{
	
	private Sprite sprite;
	
	private float x_start;
	private float x_end;
	private float y_start;
	private float y_end;
	
	private float x_center;
	private float y_center;
	
	private final int HALF_TILE = World.TILE_SIZE;
	
	Gates(float x_start, float x_end, float y_start, float y_end){
		x_center = (float) (x_start+ 0.5*(x_end - x_start));
		System.out.println(x_center);
		y_center = (float) (y_start - HALF_TILE + 0.5*(y_end - y_start));
		this.x_start = x_start - HALF_TILE;
		this.y_start = y_start - HALF_TILE;
		this.x_end = x_end - HALF_TILE;
		this.y_end = y_end - HALF_TILE;
	}
	
	float getLeft(){
		return x_start;
	}
	float getRight(){
		return x_end;
	}
	float getTop(){
		return y_start;
	}
	float getBottom(){
		return y_end;
	}
	
	float getX(){
		return x_center;
	}
	float getY() {
		return y_center;
	}
	
	void showGoal() throws SlickException {
		sprite = new Sprite("assets/frog.png", (float)888, 96);
	}
}