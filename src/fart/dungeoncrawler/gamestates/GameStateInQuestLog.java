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
import fart.dungeoncrawler.actor.QuestLog;
import fart.dungeoncrawler.enums.GameState;

/**
 * This state is activated when the player opens up the quest-log. It updates the game-state and draws
 * the questlog on screen. 
 * @author Felix
 *
 */
public class GameStateInQuestLog extends BaseGameState {
	private QuestLog log;
	private StaticObjectManager sManager;
	private DynamicObjectManager dManager;
	private Tilemap map;
	private Controller controller;
	private Player player;

	public GameStateInQuestLog(Game game) {
		super(game);

		sManager = game.getStaticManager();
		dManager = game.getDynamicManager();
		map = game.getMap();
		controller = game.getController();
	}
	
	/**
	 * Sets the actor that opened the quest-log to collect all needed data. 
	 * @param actor
	 */
	public void setCurrentActor(Actor actor) {
		if(actor instanceof Player) {
			player = (Player)actor;
			this.log = player.getQuestLog();
		}
	}

	@Override
	public void update(float elapsed) {
		if(controller.justPressed(KeyEvent.VK_T) || controller.justPressed(KeyEvent.VK_ESCAPE)) {
			game.setGameState(GameState.InGame);
			return;
		}
		
		dManager.update(elapsed);
	}

	@Override
	public void draw(Graphics2D graphics) {
		map.draw(graphics);
		sManager.draw(graphics);
		dManager.draw(graphics);
		log.draw(graphics);
	}

	@Override
	public void activate() {
		
	}

	@Override
	public void exit() {
	}

	

}
