package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;
import java.util.ArrayList;

import fart.dungeoncrawler.network.ClientInfo;

/**
 * This message is sent to a client immediately after joining a game. It contains a list of all clients
 * currently connected to the server. 
 * @author Felix
 *
 */
public class LobbyClientListMessage extends LobbyMessage implements	Serializable {
	private static final long serialVersionUID = 9020588473383655592L;
	
	public ArrayList<ClientInfo> clients;

	/**
	 * Creates the message with the list of clients.
	 * @param clients list of all clients
	 */
	public LobbyClientListMessage(ArrayList<ClientInfo> clients) {
		super(LobbyMessage.LOBBY_CLIENT_LIST);
		this.clients = clients;
	}
}
