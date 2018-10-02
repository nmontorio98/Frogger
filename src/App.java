/**
 * Project 1 for SWEN20003 by Nicolas Montorio 911211,
 * Base foundations for slick frogger game.
 */

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


import org.newdawn.slick.Input;


/**
 * Main class for the game.
 * Handles initialisation, input and rendering.
 */
public class App extends BasicGame {
    /** screen width, in pixels */
    public static final int SCREEN_WIDTH = 1024;
    /** screen height, in pixels */
    public static final int SCREEN_HEIGHT = 768;
    
    //public static long runningtime = 0;
    
   // public World world;
    private World W1;
    


    public App() {
        super("Shadow Leap");
    }

    @Override
    public void init(GameContainer gc)
            throws SlickException {
    	//world = new World();
    	W1 = new World();
        
        
    }
    
    /** Update the game state for a frame.
     * @param gc The Slick game container object.
     * @param delta Time passed since last frame (milliseconds).
     */
    @Override
    public void update(GameContainer gc, int delta)
            throws SlickException {
        // Get data about the current input (keyboard state).
        Input input = gc.getInput();
        W1.update(input, delta);
        //runningtime += delta;
              	        
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param gc The Slick game container object.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(GameContainer gc, Graphics g)
            throws SlickException {
        W1.render(g);

    }

    /** Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored).
     */
    public static void main(String[] args)
            throws SlickException {
        AppGameContainer app = new AppGameContainer(new App());
        //app.setTargetFrameRate(60);
        app.setShowFPS(true);
        app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
        app.start();
    }

}