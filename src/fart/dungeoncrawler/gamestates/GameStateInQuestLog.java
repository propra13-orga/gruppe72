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
import fart.dungeoncrawler.actor.QuestLog;
import fart.dungeoncrawler.enums.GameState;

public class GameStateInQuestLog extends BaseGameState {
	private QuestLog log;
	private StaticObjectManager sManager;
	private DynamicObjectManager dManager;
	private Tilemap map;
	private Controller controller;
	private NewPlayer player;

	public GameStateInQuestLog(Game game) {
		super(game);

		sManager = game.getStaticManager();
		dManager = game.getDynamicManager();
		map = game.getMap();
		controller = game.getController();
	}
	
	public void setCurrentActor(Actor actor) {
		if(actor instanceof NewPlayer) {
			player = (NewPlayer)actor;
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
