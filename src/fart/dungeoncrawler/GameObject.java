package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Utils.Vector2;

/**
 * GameObject is an abstract base class for almost every object inside the game. It contains
 * an unique ID, a screenposition and some abstract methods. ü+++++++++++++++++++++++++++
 * @author Felix
 *
 */
public abstract class GameObject implements IDrawable {
	protected Vector2 screenPosition;
	protected int ID;
	
	protected abstract BufferedImage getTexture();
	public abstract Rectangle getCollisionRect();
	public abstract void terminate();
	
	private static int numObjects = 0;
	
	/**
	 * Initializes the ID. 
	 */
	public GameObject() {
		ID = numObjects;
		numObjects += 1;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		graphics.drawImage(getTexture(), (int)screenPosition.x, (int)screenPosition.y, null);
	}
	
	/**
	 * Returns the position in screenspace. 
	 * @return
	 */
	public Vector2 getPosition() {
		return screenPosition;
	}
	
	/**
	 * Returns the unique ID.
	 * @return
	 */
	public int getID() {
		return ID;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o.getClass() == getClass())
			return ID == ((GameObject)o).ID;
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return ID;
	}
}