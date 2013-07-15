package fart.dungeoncrawler;

import fart.dungeoncrawler.actor.Actor;

/**
 * Everything that can be triggered through a collision implements this interface.
 * @author Timo
 *
 */
public interface ITriggerable
{
	/**
	 * Triggers the object and performs the specified action.
	 * @param player
	 */
	public void trigger(Actor player);
}
