package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

import fart.dungeoncrawler.actor.Actor;

import Utils.Vector2;

/**
 * This message is sent as soon as a players velocity changes (or it is teleported after death). It contains
 * the current position, velocity and dynamicObjectState (for animations). 
 * @author Felix
 *
 */
public class GamePositionMessage extends GameMessage implements Serializable {
	private static final long serialVersionUID = 4753623280356892808L;
	
	public Vector2 position;
	public Vector2 velocity;
	public byte state;

	/**
	 * Creates the message. All informations needed are reached through the given actor-instance.
	 * @param a 
	 */
	public GamePositionMessage(Actor a) {
		super((byte)a.getActorID(), GameMessage.GAME_POSITION_MESSAGE);
		
		this.position = a.getPosition();
		this.velocity = a.getVelocity();
		this.state = (byte)a.getState().ordinal();
	}
}
