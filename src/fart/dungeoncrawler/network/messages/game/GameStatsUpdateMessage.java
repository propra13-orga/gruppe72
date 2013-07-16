package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.actor.Stats;

/**
 * This message is sent from the client as soon as the player stats change (due to a levelup for example).
 * @author Felix
 *
 */
public class GameStatsUpdateMessage extends GameMessage implements Serializable {
	private static final long serialVersionUID = -4954263331454468674L;
	
	public Stats newStats;

	/**
	 * Creates the message. The new stats are taken from the given actor. 
	 * @param a the actor that changed his stats
	 */
	public GameStatsUpdateMessage(Actor a) {
		super((byte)a.getActorID(), GameMessage.GAME_STATS_UPDATE);

		newStats = a.getStats();
	}

}
