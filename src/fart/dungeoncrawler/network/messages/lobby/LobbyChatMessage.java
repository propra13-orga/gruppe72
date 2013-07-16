package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

/**
 * This message is sent when a player sends a chatmessage. 
 * @author Roman
 *
 */
public class LobbyChatMessage extends LobbyMessage implements Serializable {

	private static final long serialVersionUID = 6055111871382298997L;
	public String text;
	
	/**
	 * Creates the message with the given text. 
	 * @param text the text to send.
	 */
	public LobbyChatMessage(String text) {
		super(LobbyMessage.LOBBY_CHAT_MESSAGE);
		this.text = text;
	}
}

