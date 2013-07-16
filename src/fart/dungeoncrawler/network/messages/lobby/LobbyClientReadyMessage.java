package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

/**
 * This message is sent from a client as soon as he changes his ready-flag. 
 * @author Felix
 *
 */
public class LobbyClientReadyMessage extends LobbyMessage implements Serializable {
	private static final long serialVersionUID = 5237384206827434348L;

	public byte clientID;
	public boolean isReady;
	
	/**
	 * Creates the message for the given clientID with the appropriate flag. 
	 * @param clientID
	 * @param isReady
	 */
	public LobbyClientReadyMessage(byte clientID, boolean isReady) {
		super(LobbyMessage.LOBBY_CLIENT_READY);
		
		this.clientID = clientID;
		this.isReady = isReady;
	}
}
