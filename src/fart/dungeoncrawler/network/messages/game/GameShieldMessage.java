package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

import fart.dungeoncrawler.actor.Player;

/**
 * This message is sent as soon as the player activates a shield to notify the server and 
 * all other clients. 
 * @author Felix
 *
 */
public class GameShieldMessage extends GameMessage implements Serializable {
	private static final long serialVersionUID = 8276018548122752704L;
	public byte shieldID;

	/**
	 * Creates the message. All needed data is taken from the player.
	 * @param player
	 */
	public GameShieldMessage(Player player) {
		super((byte)player.getActorID(), GameMessage.GAME_SHIELD_MESSAGE);
		
		shieldID = (byte)player.getElementType().ordinal();
	}

}
