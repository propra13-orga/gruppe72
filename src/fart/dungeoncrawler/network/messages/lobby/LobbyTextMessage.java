package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

/**
 * This message can be sent from the server to all clients to write messages. This is done when a player joins
 * the lobby.
 * @author Felix
 *
 */
public class LobbyTextMessage extends LobbyMessage implements Serializable {
	private static final long serialVersionUID = 6258566025884187141L;
	
	public String text;
	
	/**
	 * Creates the message with the given text.
	 * @param text
	 */
	public LobbyTextMessage(String text) {
		super(LobbyMessage.LOBBY_TEXT_MESSAGE);
		
		this.text = text;
	}
}
