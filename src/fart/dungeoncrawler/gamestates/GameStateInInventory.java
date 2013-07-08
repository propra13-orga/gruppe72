package fart.dungeoncrawler.gamestates;

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

public class GameStateInInventory extends BaseGameState {
	private Inventory inventory;
	private Equipment equip;
	private Stats stats;
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
	
	public void setCurrentActor(Actor actor) {
		this.inventory = actor.getInventory();
		this.equip = actor.getEquipment();
		this.stats = actor.getStats();
	}

	@Override
	public void update(float elapsed) {
		if(controller.justPressed(KeyEvent.VK_I) || controller.justPressed(KeyEvent.VK_ESCAPE))
			game.setGameState(GameState.InGame);
		
		inventory.update(elapsed);
		dManager.update(elapsed);
	}

	@Override
	public void draw(Graphics2D graphics) {
		map.draw(graphics);
		sManager.draw(graphics);
		dManager.draw(graphics);
		inventory.draw(graphics);
		equip.draw(graphics);
		stats.draw(graphics);
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
