package fart.dungeoncrawler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import fart.dungeoncrawler.actor.Actor;

/**
 * The collision detector is responsible for finding and handling all collisions in
 * the game. 
 * @author Felix
 *
 */
public class CollisionDetector {
	private ArrayList<Rectangle> staticObjects;
	private ArrayList<ITriggerable> triggers;
	private ArrayList<ITriggerableOnKey> onKeyTriggers;
	private ArrayList<Actor> dynamicObjects;
	
	/**
	 * Creates a new instance. 
	 */
	public CollisionDetector() {
		staticObjects = new ArrayList<Rectangle>();
		triggers = new ArrayList<ITriggerable>();
		dynamicObjects = new ArrayList<Actor>();
		onKeyTriggers = new ArrayList<ITriggerableOnKey>();
	}
	
	/**
	 * Sets a new TileMap and gets all needed data from it.
	 * @param tilemap
	 */
	public void changeMap(MapLoader loader) {
		staticObjects.clear();
		triggers.clear();
		onKeyTriggers.clear();
		dynamicObjects.clear();
		
		int[][] map = loader.getMap();
		
		for(int i=0; i<Tilemap.getWidth(); i++) {
			for(int j=0; j<Tilemap.getHeight(); j++){
				if((map[i][j]&2) != 0) {
					staticObjects.add(new Rectangle(i * Tilemap.TILE_SIZE, j * Tilemap.TILE_SIZE, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE));
				}
			}
		}
	}
	
	/**
	 * Adds a static object to the list of all static objects.
	 * @param rect
	 */
	public void addStaticObject(Rectangle rect) {
		staticObjects.add(rect);
	}
	
	/**
	 * Adds a dynamic Object (GameObject) to the list. 
	 * @param obj Object to add
	 */
	public void addDynamicObject(Actor obj) {
		dynamicObjects.add(obj);
	}
	
	/**
	 * Removes an object from the list of all dynamic objects.
	 * @param obj
	 */
	public void removeDynamicObject(GameObject obj) {
		int id = obj.getID();
		for(int i = 0; i < dynamicObjects.size(); i++) {
			if(dynamicObjects.get(i).getID() == id) {
				dynamicObjects.remove(i);
				return;
			}
		}
	}
	
	/**
	 * Clears the list of all dynamic objects.
	 */
	public void clearDynamicObjects() {
		dynamicObjects.clear();
	}
	
	/**
	 * Adds a trigger to the trigger list.
	 * @param trigger
	 */
	public void addTrigger(ITriggerable trigger) {
		triggers.add(trigger);
	}
	
	/**
	 * Removes a trigger from the trigger list.
	 * @param trigger
	 */
	public void removeTrigger(ITriggerable trigger) {
		triggers.remove(trigger);
	}
	
	/**
	 * Clears the trigger list.
	 */
	public void clearTriggers() {
		triggers.clear();
	}
	
	/**
	 * Adds a triggerOnKey.
	 * @param trigger
	 */
	public void addTriggerOnKey(ITriggerableOnKey trigger) {
		onKeyTriggers.add(trigger);
	}
	
	/**
	 * Removes a triggerOnKey.
	 * @param trigger
	 */
	public void removeTriggerOnKey(ITriggerableOnKey trigger) {
		onKeyTriggers.remove(trigger);
	}
	
	/**
	 * Clears all triggersOnKey. 
	 */
	public void clearTriggersOnKey() {
		onKeyTriggers.clear();
	}
	
	/**
	 * Checks wether an object is colliding with static geometry.
	 * @param collider Object to check.
	 * @return if a collision happens. 
	 */
	public boolean isCollidingStatic(GameObject collider) {
		Rectangle rect = collider.getCollisionRect();
		for(int i = 0; i < staticObjects.size(); i++) {
			if(staticObjects.get(i).intersects(rect))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Checks wether an object is colliding with any of the dynamic objects.
	 * @param collider Object to check.
	 * @return if a collision happens.
	 */
	public boolean isCollidingDynamic(Actor collider) {
		Rectangle rect = collider.getCollisionRect();
		for(int i = 0; i < dynamicObjects.size(); i++) {
			Actor o = dynamicObjects.get(i);
			if(o.equals(collider))
				continue;
			
			Rectangle rect2 = new Rectangle(o.getCollisionRect());
			rect2.x -= 1;
			rect2.y -= 1;
			rect2.width += 1;
			rect2.height += 1;
			if(rect2.intersects(rect))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if an object stands on a trigger. If so, it is triggered. 
	 * @param collider Object to check.
	 */
	public void checkTriggers(Actor collider) {
		Rectangle rect = collider.getCollisionRect();
		for(int i = 0; i < triggers.size(); i++) {
			Rectangle triggerArea = ((GameObject)triggers.get(i)).getCollisionRect();
			if(triggerArea.intersects(rect)) {
				triggers.get(i).trigger(collider);
				return;
			}
		}
	}
	
	/**
	 * Checks if a gameObject is near a trigger. This is used for NPCs so that they don't run
	 * on triggers. To do this we take the original rectangle and enlarge it. The collision-test
	 * is done with the enlarged rect. 
	 * @param collider Object to check.
	 * @param pixel How many pixel the collisionRect is enlarged (per side)
	 * @return if it is near a trigger.
	 */
	public boolean isNearTrigger(Actor collider, int pixel) {
		Rectangle origRect = collider.getCollisionRect();
		Rectangle rect = new Rectangle(origRect);
		rect.x -= pixel;
		rect.y -= pixel;
		rect.width += (pixel * 2);
		rect.height += (pixel * 2);
		for(int i = 0; i < triggers.size(); i++) {
			Rectangle triggerArea = ((GameObject)triggers.get(i)).getCollisionRect();
			if(triggerArea.intersects(rect)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks all triggersOnKey if they should be triggered. Only called after a key-press.
	 * @param actor
	 */
	public void checkOnKeyTriggers(Actor actor) {
		Rectangle colRect = actor.getCollisionRect();
		
		for(int i = 0; i < onKeyTriggers.size(); i++) {
			Rectangle triggerRect = onKeyTriggers.get(i).getTriggerArea();
			if(colRect.intersects(triggerRect))
				onKeyTriggers.get(i).trigger(actor);
		}
	}
	
	/**
	 * A debug-method to draw all static collision rectangles to the screen. 
	 * @param graphics
	 */
	public void drawCollisionRects(Graphics2D graphics) {
		graphics.setColor(Color.red);
		for(int i = 0; i < dynamicObjects.size(); i++) {
			Rectangle r = dynamicObjects.get(i).getCollisionRect();
			graphics.drawRect(r.x, r.y, (int)r.getWidth(), (int)r.getHeight());
		}
		graphics.setColor(Color.orange);
		for(int i = 0; i < staticObjects.size(); i++) {
			Rectangle r = staticObjects.get(i);
			graphics.drawRect(r.x, r.y, (int)r.getWidth(), (int)r.getHeight());
		}
	}
}
