package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

import fart.dungeoncrawler.actor.Actor;

/**
 * This message is sent from the server as soon as a spell or an attack has hit a target. The damage calculation
 * is only done on serverside, so this message contains the new health-value of the player that was hit. 
 * @author Felix
 *
 */
public class GameHitMessage extends GameMessage implements Serializable {
	private static final long serialVersionUID = -3826318015850330500L;
	
	public float health;
	public boolean isSpell;
	
	/**
	 * Creates the hit message.
	 * @param a the actor that was hit
	 * @param health the new health-value
	 * @param isSpell if it was hit by a spell
	 */
	public GameHitMessage(Actor a, float health, boolean isSpell) {
		super((byte)a.getActorID(), GameMessage.GAME_HIT_MESSAGE);
		
		this.health = health;
		this.isSpell = isSpell;
	}
}
