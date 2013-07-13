package fart.dungeoncrawler.gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fart.dungeoncrawler.*;
import fart.dungeoncrawler.actor.DynamicObjectManager;
import fart.dungeoncrawler.enums.GameState;

public class GameStateInGame extends BaseGameState {
	private DynamicObjectManager dManager;
	private StaticObjectManager sManager;
	private Tilemap map;
	private Controller controller;
	
	public GameStateInGame(Game game) {
		super(game);
		
		dManager = game.getDynamicManager();
		sManager = game.getStaticManager();
		map = game.getMap();
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
		if(controller != null) {
			if(controller.justPressed(KeyEvent.VK_ESCAPE))
				game.setGameState(GameState.InMenu);
			
			if(controller.justPressed(KeyEvent.VK_I)) {
				game.setGameState(GameState.InInventory);
				((GameStateInInventory)game.getGameState()).setCurrentActor(game.getPlayer());
			}
			
			if(controller.justPressed(KeyEvent.VK_O)) {
				game.setGameState(GameState.InStatsMenu);
				((GameStateInStatsMenu)game.getGameState()).setCurrentActor(game.getPlayer());
			}
			
			if(!game.isInNetwork() && controller.justPressed(KeyEvent.VK_T)) {
				game.setGameState(GameState.InQuestLog);
				((GameStateInQuestLog)game.getGameState()).setCurrentActor(game.getPlayer());
			}
		}
		dManager.update(elapsed);
	}

	@Override
	public void draw(Graphics2D g2d) {
		map.draw(g2d);
		sManager.draw(g2d);
		dManager.draw(g2d);
	}
}
