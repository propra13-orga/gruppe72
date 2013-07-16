package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

/**
 * This message is sent from the hosting player to the server. It is a request to start the game.
 * The server checks if all players are ready and if so answers with a LobbyStartGameMessage. 
 * @author Felix
 *
 */
public class LobbyStartGameRequest extends LobbyMessage implements Serializable {
	private static final long serialVersionUID = -5740691445886710941L;

	/**
	 * Creates the message. No extra-information is needed. 
	 */
	public LobbyStartGameRequest() {
		super(LobbyMessage.LOBBY_START_GAME_REQUEST);
	}

}
