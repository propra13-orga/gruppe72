package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.util.ArrayList;

import Utils.Vector2;

public class Collision {
	private ArrayList<Rectangle> staticObjects;
	private ArrayList<ITriggerable> triggers;
	private ArrayList<GameObject> dynamicObjects;
	
	/**
	 * CollisionDetector. Has methods to check all collisions (player/map, player/trigger, player/npc, etc.).
	 * 
	 * @param map Current TileMap
	 */
	public Collision(/*Tilemap map*/) {
		staticObjects = new ArrayList<Rectangle>();
		triggers = new ArrayList<ITriggerable>();
		dynamicObjects = new ArrayList<GameObject>();
		
		//changeMap(map);
	}
	
	/**
	 * Sets a new TileMap and gets all needed data from it.
	 * @param tilemap
	 */
	public void changeMap(Tilemap tilemap) {
		staticObjects.clear();
		triggers.clear();
		
		int[][] map = tilemap.getActRoom();
		
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++){
				if((map[i][j]&2) != 0) {
					staticObjects.add(new Rectangle(i * Tilemap.TILE_SIZE, j * Tilemap.TILE_SIZE, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE));
				}/* else if((map[i][j]&16) != 0) {
					triggers.add(tilemap.getTrap(new Vector2(i, j)));
				} else if((map[i][j]&4) != 0) {
					triggers.add(tilemap.getPortal(new Vector2(i, j)));
				} else if((map[i][j]&8) != 0) {
					triggers.add(tilemap.getGoal(new Vector2(i, j)));
				}*/
			}
		}
		
		//map = tilemap.getActRoom();
	}
	
	/**
	 * Sets a new TileMap and gets all needed data from it.
	 * @param tilemap
	 */
	public void changeMap(MapLoader loader) {
		staticObjects.clear();
		triggers.clear();
		
		int[][] map = loader.getMap();
		
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++){
				if((map[i][j]&2) != 0) {
					staticObjects.add(new Rectangle(i * Tilemap.TILE_SIZE, j * Tilemap.TILE_SIZE, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE));
				}/* else if((map[i][j]&16) != 0) {
					triggers.add(tilemap.getTrap(new Vector2(i, j)));
				} else if((map[i][j]&4) != 0) {
					triggers.add(tilemap.getPortal(new Vector2(i, j)));
				} else if((map[i][j]&8) != 0) {
					triggers.add(tilemap.getGoal(new Vector2(i, j)));
				}*/
			}
		}
		
		//map = tilemap.getActRoom();
	}
	
	/**
	 * Adds a dynamic Object (GameObject) to the list. 
	 * @param obj Object to add
	 */
	public void addDynamicObject(GameObject obj) {
		dynamicObjects.add(obj);
	}
	
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
	
	public void addTrigger(ITriggerable trigger) {
		triggers.add(trigger);
	}
	
	public void removeTrigger(ITriggerable trigger) {
		triggers.remove(trigger);
	}
	
	public void clearTriggers() {
		triggers.clear();
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
	public boolean isCollidingDynamic(GameObject collider) {
		Rectangle rect = collider.getCollisionRect();
		for(int i = 0; i < dynamicObjects.size(); i++) {
			GameObject o = dynamicObjects.get(i);
			if(o.equals(collider))
				continue;
			
			if(o.getCollisionRect().intersects(rect))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if an object stands on a trigger. If so, it is triggered. 
	 * @param collider Object to check.
	 */
	public void checkTriggers(Player collider) {
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
	 * checks if a gameObject is near a trigger. this is used for NPCs so that they don't run
	 * on triggers. To do this we take the original rectangle and enlarge it. The collision-test
	 * is done with the enlarged rect. 
	 * @param collider Object to check.
	 * @param pixel How many pixel the collisionRect is enlarged (per side)
	 * @return if it is near a trigger.
	 */
	public boolean isNearTrigger(GameObject collider, int pixel) {
		Rectangle origRect = collider.getCollisionRect();
		Rectangle rect = new Rectangle(origRect);
		rect.x -= pixel;
		rect.y -= pixel;
		rect.width += (pixel * 2);
		rect.height += (pixel * 2);
		for(int i = 0; i < triggers.size(); i++) {
			Rectangle triggerArea = ((GameObject)triggers.get(i)).getCollisionRect();
			if(triggerArea.intersects(rect)) {
				//triggers.get(i).trigger(collider);
				return true;
			}
		}
		
		return false;
	}
}
