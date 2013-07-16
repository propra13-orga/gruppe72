package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

import fart.dungeoncrawler.actor.Player;

/**
 * This message is sent as soon as a player attacks. Only the actorID is needed to recreate the
 * attack on the serverside.
 * @author Felix
 *
 */
public class GameAttackMessage extends GameMessage implements Serializable {
	private static final long serialVersionUID = 3224959637978371360L;

	/**
	 * Create the corresponding AttackMessage for the given actor. 
	 * @param a
	 */
	public GameAttackMessage(Player a) {
		super((byte)a.getActorID(), GameMessage.GAME_ATTACK_MESSAGE);

	}

}
