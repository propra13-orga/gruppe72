package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

/**
 * This message is sent from serverside when a player was killed. The actorID stands for the killer,
 * deadID for the killed. 
 * @author Felix
 *
 */
public class GamePlayerKilledMessage extends GameMessage implements	Serializable {
	private static final long serialVersionUID = 5256855148979174379L;
	
	public int deadID;

	/**
	 * Creates the message.
	 * @param ID ActorID of the killer
	 * @param deadID ActorID of the killed
	 */
	public GamePlayerKilledMessage(byte ID, byte deadID) {
		super(ID, GameMessage.GAME_KILLED_MESSAGE);

		this.deadID = deadID;
	}

}
