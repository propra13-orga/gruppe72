package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.actor.Stats;

public class GameStatsUpdateMessage extends GameMessage implements Serializable {
	private static final long serialVersionUID = -4954263331454468674L;
	
	public Stats newStats;

	public GameStatsUpdateMessage(Actor a) {
		super((byte)a.getActorID(), GameMessage.GAME_STATS_UPDATE);

		newStats = a.getStats();
	}

}
