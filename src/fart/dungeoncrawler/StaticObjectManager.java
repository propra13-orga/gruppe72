package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * A Manager that handles (primarily draws) all static objects in game. 
 * @author Felix
 *
 */
public class StaticObjectManager {
	private ArrayList<GameObject> objects;
	
	/**
	 * Handles all static objects like traps, portals, etc. Only for drawing. 
	 */
	public StaticObjectManager() {
		objects = new ArrayList<GameObject>();
	}
	
	/**
	 * Adds an object to the static-object-list
	 * @param obj object to add.
	 */
	public void addObject(GameObject obj) {
		objects.add(obj);
	}
	
	/**
	 * Removes an object from the static-object-list
	 * @param obj object to remove.
	 */
	public void removeObject(GameObject obj) {
		objects.remove(obj);
	}
	
	/**
	 * Clears the list of static objects.
	 */
	public void clearObjects() {
		objects.clear();
	}

	/**
	 * Draws all static objects to the screen. 
	 * @param g2d Graphics to draw in. 
	 */
	public void draw(Graphics2D g2d) {
		for(GameObject obj : objects)
			obj.draw(g2d);
	}
}
