package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

import fart.dungeoncrawler.network.ClientInfo;

/**
 * This message is sent from the server to all clients after a new client joined the game. It contains all
 * information needed in form of a ClientInfo.
 * @author Felix
 *
 */
public class LobbyJoinedMessage extends LobbyMessage implements Serializable {
	private static final long serialVersionUID = -1217629337569542061L;

	public ClientInfo client;
	
	/**
	 * Creates the message with the given ClientInfo.
	 * @param client
	 */
	public LobbyJoinedMessage(ClientInfo client) {
		super(LobbyMessage.LOBBY_JOINED_MESSAGE);
		
		this.client = client;
	}
}
