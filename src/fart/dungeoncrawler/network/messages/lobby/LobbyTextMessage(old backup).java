package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

public class LobbyTextMessage extends LobbyMessage implements Serializable {
	private static final long serialVersionUID = 6258566025884187141L;
	
	public String text;
	
	public LobbyTextMessage(String text) {
		super(LobbyMessage.LOBBY_TEXT_MESSAGE);
		
		this.text = text;
	}
}
