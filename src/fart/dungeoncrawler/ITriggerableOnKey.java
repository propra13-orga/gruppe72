package fart.dungeoncrawler;

import java.awt.Rectangle;

import fart.dungeoncrawler.actor.Actor;

/**
 * Everything that can be triggered through a key-press (talking to npcs, getting quests, etc.) implements
 * this interface. 
 * @author Felix
 *
 */
public interface ITriggerableOnKey {
	/**
	 * Triggers the action to be done.
	 * @param actor
	 */
	public void trigger(Actor actor);
	/**
	 * Returns the area in which the action can be triggered. 
	 * @return
	 */
	public Rectangle getTriggerArea();
}