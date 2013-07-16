package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

/**
 * GameMessage is the baseclass for all kinds of game-messages. Every message that has to be sent while the 
 * game is running (not in the lobby) has to extend this abstract class.
 * @author Felix
 *
 */
public class GameMessage implements Serializable {
	private static final long serialVersionUID = 4390082637612867571L;

	public static final byte GAME_POSITION_MESSAGE = 0x1;
	public static final byte GAME_SPELL_MESSAGE = 0x2;
	public static final byte GAME_ATTACK_MESSAGE = 0x3;
	public static final byte GAME_HIT_MESSAGE = 0x4;
	public static final byte GAME_KILLED_MESSAGE = 0x5;
	public static final byte GAME_STATS_UPDATE = 0x6;
	public static final byte GAME_SHIELD_MESSAGE = 0x7;
	
	public byte ID;
	public byte type;
	
	/**
	 * When creating a GameMessage the actorID should contain the actorID of the player that should be modified
	 * through this message and a type standing for a specific message.
	 * @param ID actorID
	 * @param type messageType
	 */
	public GameMessage(byte ID, byte type) {
		this.ID = ID;
		this.type = type;
	}
}
