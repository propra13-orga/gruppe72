package fart.dungeoncrawler.gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fart.dungeoncrawler.Controller;
import fart.dungeoncrawler.DynamicObjectManager;
import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.StaticObjectManager;
import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.enums.GameState;
import fart.dungeoncrawler.items.Inventory;

public class GameStateInInventory extends BaseGameState {
	private Inventory inventory;
	private StaticObjectManager sManager;
	private DynamicObjectManager dManager;
	private Tilemap map;
	private Controller controller;

	public GameStateInInventory(Game game) {
		super(game);

		sManager = game.getStaticManager();
		dManager = game.getDynamicManager();
		map = game.getMap();
		controller = game.getController();
	}
	
	public void setCurrentInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public void update(float elapsed) {
		if(controller.justPressed(KeyEvent.VK_I))
			game.setGameState(GameState.InGame);
		
		inventory.update(elapsed);
	}

	@Override
	public void draw(Graphics2D graphics) {
		map.draw(graphics);
		sManager.draw(graphics);
		dManager.draw(graphics);
		inventory.draw(graphics);
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

	

}
