package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

public class GameMessage implements Serializable {
	private static final long serialVersionUID = 4390082637612867571L;

	public static final byte GAME_POSITION_MESSAGE = 0x1;
	public static final byte GAME_SPELL_MESSAGE = 0x2;
	public static final byte GAME_ATTACK_MESSAGE = 0x3;
	public static final byte GAME_HIT_MESSAGE = 0x4;
	public static final byte GAME_KILLED_MESSAGE = 0x5;
	public static final byte GAME_STATS_UPDATE = 0x6;
	
	public byte ID;
	public byte type;
	//TODO: add timestamp
	
	public GameMessage(byte ID, byte type) {
		this.ID = ID;
		this.type = type;
	}
}
