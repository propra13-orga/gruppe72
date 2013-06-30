package fart.dungeoncrawler.gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fart.dungeoncrawler.Controller;
import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.StaticObjectManager;
import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.actor.DynamicObjectManager;
import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.GameState;
import fart.dungeoncrawler.items.Equipment;
import fart.dungeoncrawler.items.Inventory;
import fart.dungeoncrawler.items.Shop;

public class GameStateInShop extends BaseGameState {
	private Shop shop;
	private Inventory inventory;
	private Equipment equip;
	private Stats stats;
	private StaticObjectManager sManager;
	private DynamicObjectManager dManager;
	private Tilemap map;
	private Controller controller;
	
	
	public static final Font FONT = new Font("Lucida Console", 0x1, 12);
	public static final Color FONT_COLOR = new Color(0.275f, 0.0f, 0.14f);
	
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
	
	public void setCurrentActor(Actor actor) {
		this.inventory = actor.getInventory();
		this.equip = actor.getEquipment();
		this.stats = actor.getStats();
		shop.setInventory(inventory);
	}
	
	@Override
	public void activate() {
		
	}

	@Override
	public void exit() {
		
	}

	@Override
	public void update(float elapsed) {
		if(controller.justPressed(KeyEvent.VK_ESCAPE))
			game.setGameState(GameState.InGame);
		
		shop.update(elapsed);
	}

	@Override
	public void draw(Graphics2D graphics) {
		map.draw(graphics);
		sManager.draw(graphics);
		dManager.draw(graphics);
		shop.draw(graphics);
		inventory.draw(graphics);
		equip.draw(graphics);
		stats.draw(graphics);
	}

}
