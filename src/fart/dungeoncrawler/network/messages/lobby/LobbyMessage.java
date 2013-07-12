package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

public class LobbyMessage implements Serializable {
	private static final long serialVersionUID = -8166545479098011185L;
	
	public static final byte LOBBY_JOINED_MESSAGE = 0x1;
	public static final byte LOBBY_CLIENT_LIST = 0x2;
	public static final byte LOBBY_CLIENT_READY = 0x3;
	public static final byte LOBBY_TEXT_MESSAGE = 0x4;
	public static final byte LOBBY_START_GAME_REQUEST = 0x5;
	public static final byte LOBBY_START_GAME_MESSAGE = 0x6;
	public static final byte LOBBY_CHAT_MESSAGE = 0x7;
	
	public byte messageType;
	
	public LobbyMessage(byte messageType) {
		this.messageType = messageType;
	}
}