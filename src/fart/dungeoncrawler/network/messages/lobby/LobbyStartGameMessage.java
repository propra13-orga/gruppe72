package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

public class LobbyStartGameMessage extends LobbyMessage implements Serializable {
	private static final long serialVersionUID = 8740445066667694787L;

	public LobbyStartGameMessage() {
		super(LobbyMessage.LOBBY_START_GAME_MESSAGE);
		
	}

}
