package fart.dungeoncrawler.gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fart.dungeoncrawler.*;
import fart.dungeoncrawler.enums.GameState;

public class GameStateInGame extends BaseGameState {
	private DynamicObjectManager dManager;
	private StaticObjectManager sManager;
	private Collision collision;
	private Tilemap map;
	private Player player;
	private Controller controller;
	
	public GameStateInGame(Game game) {
		super(game);
		
		dManager = game.getDynamicManager();
		sManager = game.getStaticManager();
		collision = game.getCollision();
		map = game.getMap();
		player = game.getPlayer();
		controller = game.getController();
	}
	
	@Override
	public void activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float elapsed) {
		if(controller.justPressed(KeyEvent.VK_ESCAPE))
			game.setGameState(GameState.InMenu);
		
		dManager.update(elapsed);
	}

	@Override
	public void draw(Graphics2D g2d) {
		map.draw(g2d);
		sManager.draw(g2d);
		dManager.draw(g2d);
	}
}