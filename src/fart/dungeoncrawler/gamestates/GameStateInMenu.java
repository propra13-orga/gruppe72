package fart.dungeoncrawler.gamestates;

import java.awt.Graphics2D;

import fart.dungeoncrawler.*;

/**
 * This state is activated whenever the player is in the menu. 
 */
public class GameStateInMenu extends BaseGameState {
	private Menu menu;
	
	public GameStateInMenu(Game game) {
		super(game);

		menu = game.getMenu();
	}

	@Override
	public void activate() {
		
	}

	@Override
	public void exit() {
		
	}
	
	@Override
	public void update(float elapsed) {
		menu.update(elapsed);
	}

	@Override
	public void draw(Graphics2D graphics) {
		menu.draw(graphics);
	}

}
