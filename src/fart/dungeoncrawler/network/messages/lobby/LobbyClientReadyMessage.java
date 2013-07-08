package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

public class LobbyClientReadyMessage extends LobbyMessage implements Serializable {
	private static final long serialVersionUID = 5237384206827434348L;

	public byte clientID;
	public boolean isReady;
	
	public LobbyClientReadyMessage(byte clientID, boolean isReady) {
		super(LobbyMessage.LOBBY_CLIENT_READY);
		
		this.clientID = clientID;
		this.isReady = isReady;
	}
}
