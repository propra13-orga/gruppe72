package fart.dungeoncrawler.gamestates;

import fart.dungeoncrawler.*;

/**
 * This is the abstract base class for all GameStates. It only contains a reference on the game instance
 * and has abstract activate and exit methods that have to be overriden.
 * Further it must implement update() and draw() for updating and drawing all needed elements. 
 * @author Felix
 *
 */
public abstract class BaseGameState implements IUpdateable, IDrawable {
	protected Game game;
	
	public BaseGameState(Game game) {
		this.game = game;
	}
	
	/**
	 * This method is called when the state is activated to initialize itself if needed.
	 */
	public abstract void activate();
	/**
	 * This method is called when the state is left to clean up itself if needed.
	 */
	public abstract void exit();
}
