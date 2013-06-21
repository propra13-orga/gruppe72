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
import fart.dungeoncrawler.items.Shop;

public class GameStateInShop extends BaseGameState {
	private Shop shop;
	private Inventory inventory;
	private StaticObjectManager sManager;
	private DynamicObjectManager dManager;
	private Tilemap map;
	private Controller controller;
	
	public GameStateInShop(Game game) {
		super(game);
		
		sManager = game.getStaticManager();
		dManager = game.getDynamicManager();
		map = game.getMap();
		controller = game.getController();
	}
	
	public void setCurrentShop(Shop shop) {
		this.shop = shop;
	}
	
	public void setCurrentInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	@Override
	public void activate() {
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float elapsed) {
		if(controller.justPressed(KeyEvent.VK_B))
			game.setGameState(GameState.InGame);
		
		shop.update(elapsed);
		//inventory.update(elapsed);
	}

	@Override
	public void draw(Graphics2D graphics) {
		map.draw(graphics);
		sManager.draw(graphics);
		dManager.draw(graphics);
		shop.draw(graphics);
		inventory.draw(graphics);
	}

}
