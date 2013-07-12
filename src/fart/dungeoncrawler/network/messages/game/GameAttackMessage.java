package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

import fart.dungeoncrawler.actor.NewPlayer;

public class GameAttackMessage extends GameMessage implements Serializable {
	private static final long serialVersionUID = 3224959637978371360L;

	public GameAttackMessage(NewPlayer a) {
		super((byte)a.getActorID(), GameMessage.GAME_ATTACK_MESSAGE);

	}

}