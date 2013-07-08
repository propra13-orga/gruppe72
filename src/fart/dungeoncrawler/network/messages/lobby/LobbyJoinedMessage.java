package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

import fart.dungeoncrawler.network.ClientInfo;

public class LobbyJoinedMessage extends LobbyMessage implements Serializable {
	private static final long serialVersionUID = -1217629337569542061L;

	public ClientInfo client;
	
	public LobbyJoinedMessage(ClientInfo client) {
		super(LobbyMessage.LOBBY_JOINED_MESSAGE);
		
		this.client = client;
	}
}
