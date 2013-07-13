package fart.dungeoncrawler.gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fart.dungeoncrawler.Controller;
import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.StaticObjectManager;
import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.actor.DynamicObjectManager;
import fart.dungeoncrawler.actor.NewPlayer;
import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.actor.StatsMenu;
import fart.dungeoncrawler.enums.GameState;
import fart.dungeoncrawler.items.Equipment;
import fart.dungeoncrawler.items.Inventory;

public class GameStateInStatsMenu extends BaseGameState {
	private StatsMenu stats;
	private StaticObjectManager sManager;
	private DynamicObjectManager dManager;
	private Tilemap map;
	private Controller controller;
	private NewPlayer player;

	public GameStateInStatsMenu(Game game) {
		super(game);

		sManager = game.getStaticManager();
		dManager = game.getDynamicManager();
		map = game.getMap();
		controller = game.getController();
	}
	
	public void setCurrentActor(Actor actor) {
		if(actor instanceof NewPlayer) {
			player = (NewPlayer)actor;
			this.stats = player.getStatsMenu();
		}
	}

	@Override
	public void update(float elapsed) {
		if(controller.justPressed(KeyEvent.VK_O) || controller.justPressed(KeyEvent.VK_ESCAPE)) {
			player.setControllerActive(true);
			game.setGameState(GameState.InGame);
			return;
		}
		
		player.setControllerActive(false);
		dManager.update(elapsed);
		stats.update();
	}

	@Override
	public void draw(Graphics2D graphics) {
		map.draw(graphics);
		sManager.draw(graphics);
		dManager.draw(graphics);
		stats.drawMenu(graphics);
	}

	@Override
	public void activate() {
		
	}

	@Override
	public void exit() {
		player.setControllerActive(true);
	}

	

}
