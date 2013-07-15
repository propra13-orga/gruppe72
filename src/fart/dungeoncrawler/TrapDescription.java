package fart.dungeoncrawler;

import fart.dungeoncrawler.actor.BaseDescription;

/**
 * Describes a trap and is used to create trap-instances.
 * @author Erhan
 *
 */
public class TrapDescription extends BaseDescription {
	private int damage;
	
	/**
	 * Creates the description. 
	 * @param spritePath path to the texture
	 * @param damage damage done by the trap per frame. 
	 */
	public TrapDescription(String spritePath, int damage) {
		super(spritePath);
		this.damage = damage;
	}
	
	/**
	 * Returns the damage done by this trap per frame. 
	 * @return
	 */
	public int getDamage() {
		return damage;
	}
}
