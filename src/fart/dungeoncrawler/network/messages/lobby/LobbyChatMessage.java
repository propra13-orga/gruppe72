package fart.dungeoncrawler.network.messages.lobby;

import java.io.Serializable;

public class LobbyChatMessage extends LobbyMessage implements Serializable {

	private static final long serialVersionUID = 6055111871382298997L;
	public String text;
	public LobbyChatMessage(String text) {
		super(LobbyMessage.LOBBY_CHAT_MESSAGE);
		this.text = text;
	}
}

