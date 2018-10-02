import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


public class World {
	
	private String[] levels = {"assets/levels/0.lvl", "assets/levels/1.lvl"} ;
	private int curr_level = 1;
	public static final int TILE_SIZE = 48;
	public static final int GOAL_SIZE = TILE_SIZE*2;
	private int num_starting_lives = 3;
	
	static Random rand = new Random();
	private static int NUM;
	
	
	@SuppressWarnings("unused")
	private boolean death_flag;
	
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private ArrayList<Vehicle> rideable = new ArrayList<Vehicle>();
	private ArrayList<Vehicle> kot_vehicles = new ArrayList<Vehicle>();
	private ArrayList<Lives> lives = new ArrayList<Lives>();
	private ArrayList<Gates> goals = new ArrayList<Gates>();
	private ArrayList<Vehicle> logs = new ArrayList<Vehicle>();
	private ArrayList<ExtraLife> e_lifes = new ArrayList<ExtraLife>();
	
	private Player sprite;

	public static long map_time =  0;
	
	public World() throws SlickException {
		createMap();
		createGates();
		NUM = rand.nextInt(logs.size());
		//NUM = 5;
		System.out.println(NUM + "x: " +  logs.get(NUM).getX() + "y: " + logs.get(NUM).getY());
		//extraLife = new ExtraLife("assets/extralife.png", logs.get(NUM).getX(), logs.get(NUM).getY(), logs.get(NUM).getWidth());
		sprite = new Player("assets/frog.png", (float)888, 96); // 512,720 : 512, 384 :93.6111
		
	}
	
	public void kill() {
		lives.remove(lives.size() -1);
		if(lives.isEmpty()) {
			System.exit(0);
		}
		else {
			reset();
		}
	}
	
	public void reset() {
		sprite.setSpriteX(512); 
		sprite.setSpriteY(384);
	}
	
	public void render(Graphics g) {
		
		for(Vehicle k: kot_vehicles) {
			k.render(g);
		}
		for(Tile tile: tiles) {
			tile.render(g);
		}
		for(Vehicle r: rideable) {
			r.render(g);
		}
		for(Vehicle log: logs) {
			log.render(g);
		}
		for(Lives life: lives){
			life.render(g);
		}
/*		for(Sprite goal: goals) {
			goal.render(g);
		}*/
		for(ExtraLife object: e_lifes) {
			object.render(g);
		}
		sprite.render(g);
		g.drawString("TIME: " + map_time/1000.1f, 512, 720);
		for(int i = 0; i< App.SCREEN_WIDTH; i+=48) {
			g.drawString("" + i, i, 48);
		}
	}
	
	public void update(Input input, int delta) throws SlickException {
		boolean death_flag = false;
		boolean riding = true; // change back to false
		map_time += delta;
		
		for(ExtraLife life: e_lifes) {
			life.attach(logs.get(NUM).getSpeed(), delta);
		}
		
		/* Change level */
		if(input.isKeyPressed(Input.KEY_C)) {
			curr_level = (curr_level + 1)%2;
			createMap();
			reset();
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
			if(sprite.intersects(w_object) && w_object.getSolid()) {
				sprite.setSpriteX(sprite.getLastX());
				sprite.setSpriteY(sprite.getLastY()); 
			}
			if(sprite.intersects(w_object) && !w_object.getUnderWater()){
				riding = true;
				sprite.attach(w_object.getSpeed(), delta);
				if(w_object.getSolid() && sprite.getX() >= App.SCREEN_WIDTH - TILE_SIZE) {
					death_flag = true;
				}
			}
		}
		
		for (Vehicle log: logs) {
			log.update(delta);
			if(sprite.intersects(log) && !log.getUnderWater()){
				riding = true;
				//sprite.attach(log.getSpeed(), delta);
			}
		}
		
		/* Extralife follows log off screen and onto the other side */
		ExtraLife extraLife = e_lifes.get(0);
		if(logs.get(NUM).getOffScreen()) {	
			System.out.println("FLAG");
			if (logs.get(NUM).getDirection() == 1) {
				extraLife.setSpriteX(0- logs.get(NUM).getWidth()/2 + extraLife.getX() - logs.get(NUM).getPrevX() ); // extraLife.getX() - App.SCREEN_WIDTH 
			}
			else {
				extraLife.setSpriteX(App.SCREEN_WIDTH + logs.get(NUM).getWidth()/2 + extraLife.getX() - logs.get(NUM).getPrevX() );
			}
		}
		if(map_time % 1000 == 0) {
			extraLife.update();
		}
	
		/* kills player if they "fall" in water/KOT tile and also doesn't allow them to move through
		 * solid tiles */
		for( Tile tile: tiles) {
			if(sprite.intersects(tile) && tile.touchable == 0 && riding == false) {
				death_flag = true;
			}
			else if(sprite.intersects(tile) && tile.touchable == 2) {
				sprite.setSpriteX(sprite.getLastX());
				sprite.setSpriteY(sprite.getLastY()); 
				
			}
		}
		if(death_flag == true) {
			kill();
		}
		
		/* Checks to see if the sprite reached a goal which is located in the last row of tiles */
		if(sprite.getY()<= TILE_SIZE) {
			float goal_x =  (float) sprite.getX()%(GOAL_SIZE); //problem if there is only one tree at the start and not 2
			goal_x = (float) (Math.floorDiv((int)sprite.getX(), TILE_SIZE) * TILE_SIZE + 0.5*TILE_SIZE);
			
			
			//goals.add(new Sprite("assets/frog.png", goal_x , TILE_SIZE));
			if(goals.size() == 5) {
				curr_level = (curr_level + 1)%2;
				createMap();
			}
			reset();
		}
		
		if(sprite.intersects(extraLife)) {
			extraLife.setSpriteX(512);
			extraLife.setSpriteY(510);
			System.out.println("asdhiauhd");
			int remainingLives = lives.size();
			lives.add(new Lives(24 + 32*(remainingLives), 744));
			
		}
		
		/* Move player */
		sprite.update(input, delta);
	}
	
	public void createMap() throws SlickException {
		
		/* sets initial direction to left */
		int dir = 1;
		map_time = 0;
		
		/* Creates the number of starting lives */
		for( int i = 0; i< num_starting_lives; i++) {
			lives.add(new Lives(24 + 32*i, 744));
		}
		
		/* clears all objects and tiles on map */
		rideable.clear();
		kot_vehicles.clear();
		logs.clear();
		tiles.clear();
		goals.clear();
		e_lifes.clear();

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
            e_lifes.add(new ExtraLife("assets/extralife.png", logs.get(NUM).getX(), logs.get(NUM).getY(), logs.get(NUM).getWidth()));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void createGates() {
		float curr_x = 0;
		float prev_x = 0;
		
		for(Tile tile: tiles) {
			if(tile.getY() == TILE_SIZE) {
				curr_x = tile.getX();
				if(curr_x - prev_x > TILE_SIZE) {
					System.out.println("prev_x " + prev_x + "cur " + curr_x);
					goals.add(new Gates(prev_x, curr_x, TILE_SIZE,TILE_SIZE));
				}
				prev_x = curr_x;
			}
		}
		
		
		
		/*for(Tile tile: tiles) {
			if(tile.getY() == TILE_SIZE) {
				start_x = tile.getX();
				System.out.println(start_x);
			}
		}*/
	}
}


	
