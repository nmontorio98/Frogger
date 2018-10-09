import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


public class World {
	
	private String[] levels = {"assets/levels/0.lvl", "assets/levels/1.lvl"} ;
	private int curr_level = 0;
	private int current_rnum;
	public static final int TILE_SIZE = 48;
	public static final int GOAL_SIZE = TILE_SIZE*2;
	private final int E_WAIT = 14;
	private int num_starting_lives = 3;
	private int targets_filled;	
	private int ex_timer;
	
	
	static Random rand = new Random();
	
	@SuppressWarnings("unused")
	private boolean death_flag;
	
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private ArrayList<Vehicle> rideable = new ArrayList<Vehicle>();
	private ArrayList<Vehicle> kot_vehicles = new ArrayList<Vehicle>();
	private ArrayList<Lives> lives = new ArrayList<Lives>();
	private ArrayList<Vehicle> logs = new ArrayList<Vehicle>();
	private ArrayList<Sprite> targets = new ArrayList<Sprite>();
	
	private ExtraLife ex;
	private Player sprite;
	private int PLAYER_START_X = 512; // 512,720 : 512, 384 :93.6111
	private int PLAYER_START_Y = 720;
	

	public static long map_time =  0;
	public long ex_alive_time = 0;
	
	
	public World() throws SlickException {
		
		/* Performs initialization of all objects and tiles */
		createMap();
		createLives();
		createGates();
		sprite = new Player("assets/frog.png",PLAYER_START_X, PLAYER_START_Y); 
		
	}
	
	
	public void kill() {
		
		/* Remove a single life from the player and ends the game 
		 * when the life count is zero */ 
		lives.remove(lives.size() -1);
		if(lives.isEmpty()) {
			System.exit(0);
		}
		else {
			reset();
		}
	}
	
	
	public void reset() {
		
		/* Resets players position to values in the specification */
		sprite.setSpriteX(PLAYER_START_X); 
		sprite.setSpriteY(PLAYER_START_Y);
	}
	
	public void render(Graphics g) {
		
		/* Renders all objects that kill the player on contact/ or are tiles*/
		for(Vehicle k: kot_vehicles) {
			k.render(g);
		}
		for(Tile tile: tiles) {
			tile.render(g);
		}
		
		/* renders objects that the player can "attach" to (turtle, log, bulldozer") */
		for(Vehicle r: rideable) {
			r.render(g);
		}
		int count = 0;
		for(Vehicle log: logs) {
			log.render(g);
			g.drawString("" + count, log.getX(),log.getY());
			count++;
		}
		
		/* renders all of the frog sprites */
		for(Lives life: lives){
			life.render(g);
		}
		for(Sprite goal: targets) {
			goal.render(g);
		}
		if(ex != null) {
			ex.render(g);
		}
		sprite.render(g);
		
		
		g.drawString("TIME: " + map_time/1000.1f, 512, 720);
		g.drawString("ex_timer" + ex_timer, 0, 384);
	}
	
	public void update(Input input, int delta) throws SlickException {
		boolean death_flag = false;
		boolean riding = false;
		
		map_time += delta;
		
		/* After a random period of time an ExtraLife is made */
//		if(map_time % (ex_timer*1000)  == 0) {
//			createExtraLife();
//			ex.setUnderWater(true);
//		}
		
		if(map_time == rand.nextInt(10)*1000) {
			createExtraLife();
			ex.setUnderWater(true);
		}
		
		/* Goes to the next level if all the holes are filled */
		if(targets_filled == targets.size()) {
			targets_filled = 0;
			ex = null;
			curr_level = (curr_level + 1) % levels.length;
			createMap();
			createGates();
			createExtraLife(); // have a look
			reset();
	
		}
		
		/* allows extra life sprite to ride on the log*/
		if(ex != null) {
			ex.attach(logs.get(current_rnum).getSpeed(), delta);
			ex.update();
			ex_alive_time += delta;
			
		}

		/* Check for collision with kill on touch vehicles and updates these vehicles */
		for( Vehicle object: kot_vehicles) {
			object.update(delta);
    		if(sprite.intersects(object)) {
    			death_flag = true;	
	        }
	    }
		
		/*Update all rideable objects and attach player to above water objects */
		for(Vehicle w_object: rideable) {
			w_object.update(delta);
			
			/* wont let player move through solid vehicles */
			if(sprite.intersects(w_object) && w_object.getSolid()) {
				sprite.moveBack();
			}
			
			/* Causes player to ride with the vehicle */
			if(sprite.intersects(w_object) && !w_object.getUnderWater()){
				riding = true;
				sprite.attach(w_object.getSpeed(), delta);
				
				/* kills player if they are "squished" against the out of bounds area */
				if(w_object.getSolid() && sprite.getX() >= App.SCREEN_WIDTH - TILE_SIZE) {
					death_flag = true;
				}
			}
		}
		
		/* moves the logs and attaches the sprite if on them */
		for (Vehicle log: logs) {
			log.update(delta);
			
			if(sprite.intersects(log) && !log.getUnderWater()){
				riding = true;
				sprite.attach(log.getSpeed(), delta);
			}
		}
		
		/* Extralife follows log off screen and onto the other side */
		if(ex != null && logs.get(current_rnum).getOffScreen()) {	
			
			if (logs.get(current_rnum).getDirection() == 1) {
				ex.setSpriteX(0- logs.get(current_rnum).getWidth()/2 + ex.getX() - logs.get(current_rnum).getPrevX() );
			}
			else {
				System.out.println("TRIPPED");
				ex.setSpriteX(App.SCREEN_WIDTH + logs.get(current_rnum).getWidth()/2 + ex.getX() - logs.get(current_rnum).getPrevX() );
			}
		}

		/* kills player if they "fall" in water/KOT tile and also doesn't allow them to move through
		 * solid tiles */
		for( Tile tile: tiles) {
			if(sprite.intersects(tile) && tile.touchable == 0 && riding == false) {
				death_flag = true;
			}
			else if(sprite.intersects(tile) && tile.touchable == 2) {
				sprite.moveBack();	
			}
		}
		
		/* kills player if they are supposed to die */
		if(death_flag == true) {
			kill();
		}
		
		/* Checks to see if the sprite reached a goal which is located in the last row of tiles */
		for(Sprite target: targets) {
			if(sprite.intersects(target) && target.getUnderWater()) {
				target.setUnderWater(false);
				targets_filled++;
				reset();
			}
			else if(sprite.intersects(target)) {
				kill();
			}
		}
		
		/* increases lives if extraLife is touched */
		if(ex != null && sprite.intersects(ex)) {
			ex = null;
			int remainingLives = lives.size();
			lives.add(new Lives(24 + 32*(remainingLives), 744));
			createExtraLife();
		}
		
		/* Move player */
		sprite.update(input, delta);
	}
	
	public void createMap() throws SlickException {
		
		/* sets initial direction to left */
		int dir = 1;
		map_time = 0;
		ex_timer = rand.nextInt(10) + 1;// + 25;
		targets_filled = 0;
		
		/* clears all objects and tiles on map */
		rideable.clear();
		kot_vehicles.clear();
		logs.clear();
		tiles.clear();
		targets.clear();
		ex = null;

		/* reads the level file and adds each object to their appropriate ArrayList */
		try (BufferedReader br = new BufferedReader(new FileReader(levels[curr_level]))) {
            String text;
            
            while ((text = br.readLine()) != null) {
                String[] words = text.split(",");
                int x = Integer.parseInt(words[1]);
                int y = Integer.parseInt(words[2]);
                
                /* if not a tile then check if the direction needs to be switch to right */
                if(words.length > 3) {
                	if (words[3].equals("false")) {
                		dir = -1;
                	}
                }
                
                /* Initialize tiles */
                if(words[0].equals("water")) {
                	tiles.add(new Tile("assets/water.png", x, y, 0));
                	
                }
                else if(words[0].equals("grass")) {
                	tiles.add(new Tile("assets/grass.png", x, y, 1));
                }
                else if(words[0].equals("tree")) {
                	tiles.add(new Tile("assets/tree.png", x, y, 2));
                }
        
                /* Initialize every obstacle/ vehicle */
                else if(words[0].equals("bus")) {
                	kot_vehicles.add(new Vehicle("assets/bus.png", x, y, 0.15f, false, dir));
                }
                else if(words[0].equals("bulldozer")) {
                	rideable.add(new Vehicle("assets/bulldozer.png", x, y, 0.05f, true, dir));
                }
                else if(words[0].equals("bike")) {
                	kot_vehicles.add(new Bike("assets/bike.png", x, y, 0.2f, false, dir));
                }
                else if(words[0].equals("log")) {
                	logs.add(new Vehicle("assets/log.png", x, y, 0.1f, false, dir));
                }
                else if(words[0].equals("longLog")) {
                	logs.add(new Vehicle("assets/longlog.png", x, y, 0.07f, false, dir));
                }
                
                else if(words[0].equals("racecar")) {
                	kot_vehicles.add(new Vehicle("assets/racecar.png", x, y, 0.5f, false, dir));
                }
                else if(words[0].equals("turtle")) {
                	rideable.add(new Turtle("assets/turtles.png", x, y, 0.1f, false, dir));
                }
                dir = 1;
            }         
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/* Finds the holes in the top 2nd row of tiles and puts sprites in them */
	public void createGates() throws SlickException {
		float curr_x = 0;
		float prev_x = 0;
		
		for(Tile tile: tiles) {
			if(tile.getY() == TILE_SIZE) {
				curr_x = tile.getX();
				
				/* Finds the gap and puts a hidden sprite in the middle */
				if(curr_x - prev_x > TILE_SIZE) {
					targets.add(new Sprite("assets/frog.png", (float) (prev_x+ 0.5*(curr_x - prev_x)), TILE_SIZE, true));
				}
				prev_x = curr_x;
			}
		}

	}
	
	public void createLives() throws SlickException {
		/* Creates the number of starting lives */
		for( int i = 0; i< num_starting_lives; i++) {
			lives.add(new Lives(24 + 32*i, 744));
		}
	}
	
	
	public void createExtraLife() throws SlickException {
		current_rnum = createRnd(logs.size());
		System.out.println(ex_alive_time);
		ex = new ExtraLife("assets/extralife.png", logs.get(current_rnum).getX(), logs.get(current_rnum).getY(), logs.get(current_rnum).getWidth());
		ex_alive_time = 0;
		
	}
	
	
	public int createRnd(int max) {
		return rand.nextInt(max);
	}
}

	
	
