package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

public class LobbyStartGameRequest extends LobbyMessage implements Serializable {
	private static final long serialVersionUID = -5740691445886710941L;

	public LobbyStartGameRequest() {
		super(LobbyMessage.LOBBY_START_GAME_REQUEST);
	}

}
