package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

import fart.dungeoncrawler.actor.Player;

/**
 * This message is sent as soon as a player casts a spell. The server recreates the spell and does all
 * the calculations like collision-detection. It sends back a GameHitMessage when the spell hits a target.
 * @author Felix
 *
 */
public class GameSpellMessage extends GameMessage implements Serializable {
	private static final long serialVersionUID = -7226453598184590828L;
	
	public byte heading;
	public byte spellIndex;

	/**
	 * Creates the message for a given player and a spellIndex that is used on serverside to find the correct
	 * spell
	 * @param p the casting player
	 * @param spellIndex index of the spell
	 */
	public GameSpellMessage(Player p, int spellIndex) {
		super((byte)p.getActorID(), GameMessage.GAME_SPELL_MESSAGE);
		
		heading = (byte)p.getHeading().ordinal();
		this.spellIndex = (byte)spellIndex;
	}
}
