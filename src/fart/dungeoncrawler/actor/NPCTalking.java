package fart.dungeoncrawler.actor;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import Utils.Vector2;
import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.enums.GameState;
import fart.dungeoncrawler.enums.NPCType;
import fart.dungeoncrawler.gamestates.GameStateInConversation;

/**
 * Represents an NPC that talks to the player. At the moment dialogs are randomly chosen from a list.
 * @author Felix
 *
 */
public class NPCTalking extends NPCTrigger {
	private Game game;
	private Rectangle triggerArea;
	
	private String[] dialogs = { "It's really hot today, isn't it?", 
							"You won't believe: It's so boring to stand here all the day...",
							"Do we know us?",
							"I heard of some guy named Dennis Esken...they say he's awesome"};

	/**
	 * Creates a talking NPC from an NPCDescription.
	 * @param game instance of the game running
	 * @param position position in screenspace
	 * @param npcDesc the NPCDescription
	 * @param triggerRect the area in which the dialog is triggered
	 */
	public NPCTalking(Game game, Vector2 position, NPCDescription npcDesc, Rectangle triggerRect) {
		super(game, position, npcDesc);
		this.game = game;
		this.screenPosition = position;
		this.triggerArea = triggerRect;
		this.type = NPCType.Talking;
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

	@Override
	public BufferedImage getTexture() {
		return this.texture;
	}
}
