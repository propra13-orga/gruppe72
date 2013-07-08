package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

import fart.dungeoncrawler.actor.Actor;

import Utils.Vector2;

public class GamePositionMessage extends GameMessage implements Serializable {
	private static final long serialVersionUID = 4753623280356892808L;
	
	public Vector2 position;
	public Vector2 velocity;
	public byte state;

	public GamePositionMessage(Actor a) {
		super((byte)a.getActorID(), GameMessage.GAME_POSITION_MESSAGE);
		
		this.position = a.getPosition();
		this.velocity = a.getVelocity();
		this.state = (byte)a.getState().ordinal();
	}
}
