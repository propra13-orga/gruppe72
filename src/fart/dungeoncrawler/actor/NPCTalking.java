package fart.dungeoncrawler.actor;

import java.awt.Rectangle;
import java.util.Random;

import Utils.Vector2;
import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.enums.GameState;
import fart.dungeoncrawler.gamestates.GameStateInConversation;

public class NPCTalking extends NPCTrigger {
	private Game game;
	private Rectangle triggerArea;
	
	private String[] dialogs = { "It's really hot today, isn't it?", 
							"You won't believe: It's so boring to stand here all the day...",
							"Do we know us?",
							"I heard of some guy named Dennis Esken...they say he's awesome"};

	public NPCTalking(Game game, Vector2 position, NPCDescription npcDesc, Rectangle colRect) {
		super(game, position, npcDesc);
		this.game = game;
		this.screenPosition = position;
		this.triggerArea = colRect;
	}

	@Override
	public void trigger(Actor actor) {
		game.setGameState(GameState.InConversation);
		
		Random r = new Random();
		int msg = r.nextInt(dialogs.length);
		((GameStateInConversation)game.getGameState()).setCurrentDialog(dialogs[msg]);
	}

	@Override
	public Rectangle getTriggerArea() {
		return triggerArea;
	}

}
