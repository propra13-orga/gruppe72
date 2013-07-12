package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

public class GamePlayerKilledMessage extends GameMessage implements
		Serializable {
	private static final long serialVersionUID = 5256855148979174379L;
	
	public int deadID;

	public GamePlayerKilledMessage(byte ID, byte deadID) {
		super(ID, GameMessage.GAME_KILLED_MESSAGE);

		this.deadID = deadID;
	}

}
