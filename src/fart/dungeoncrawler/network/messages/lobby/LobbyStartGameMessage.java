package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

/**
 * This message is sent from the server to all clients when the game is started. 
 * @author Svenja
 *
 */
public class LobbyStartGameMessage extends LobbyMessage implements Serializable {
	private static final long serialVersionUID = 8740445066667694787L;

	/**
	 * Creates the message. It does not hold any extra-information, the type inside the baseclass is enough. 
	 */
	public LobbyStartGameMessage() {
		super(LobbyMessage.LOBBY_START_GAME_MESSAGE);
		
	}

}
