package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;
import java.util.ArrayList;

import fart.dungeoncrawler.network.ClientInfo;

public class LobbyClientListMessage extends LobbyMessage implements	Serializable {
	private static final long serialVersionUID = 9020588473383655592L;
	
	public ArrayList<ClientInfo> clients;

	public LobbyClientListMessage(ArrayList<ClientInfo> clients) {
		super(LobbyMessage.LOBBY_CLIENT_LIST);
		this.clients = clients;
	}
}
