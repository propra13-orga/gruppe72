package fart.dungeoncrawler.gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fart.dungeoncrawler.Controller;
import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.StaticObjectManager;
import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.actor.DynamicObjectManager;
import fart.dungeoncrawler.actor.Player;
import fart.dungeoncrawler.actor.StatsMenu;
import fart.dungeoncrawler.enums.GameState;

/**
 * This state is activated when the player opens up the StatsMenu. After a levelup stats can be added. 
 * @author Felix
 *
 */
public class GameStateInStatsMenu extends BaseGameState {
	private StatsMenu stats;
	private StaticObjectManager sManager;
	private DynamicObjectManager dManager;
	private Tilemap map;
	private Controller controller;
	private Player player;

	public GameStateInStatsMenu(Game game) {
		super(game);

		sManager = game.getStaticManager();
		dManager = game.getDynamicManager();
		map = game.getMap();
		controller = game.getController();
	}
	
	public void setCurrentActor(Actor actor) {
		if(actor instanceof Player) {
			player = (Player)actor;
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
