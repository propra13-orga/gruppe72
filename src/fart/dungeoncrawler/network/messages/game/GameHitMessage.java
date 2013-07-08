package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

import fart.dungeoncrawler.actor.Actor;

public class GameHitMessage extends GameMessage implements Serializable {
	private static final long serialVersionUID = -3826318015850330500L;
	
	public float damage;
	public boolean isSpell;
	
	public GameHitMessage(Actor a, float dmg, boolean isSpell) {
		super((byte)a.getActorID(), GameMessage.GAME_HIT_MESSAGE);
		
		damage = dmg;
		this.isSpell = isSpell;
	}
}
