package fart.dungeoncrawler;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Collision {
	private ArrayList<Rectangle> staticObjects;
	private ArrayList<ITriggerable> triggers;
	
	public Collision(Tilemap map) {
		staticObjects = new ArrayList<Rectangle>();
		triggers = new ArrayList<ITriggerable>();
		
		changeMap(map);
	}
	
	public void changeMap(Tilemap tilemap) {
		staticObjects.clear();
		triggers.clear();
		
		int[][] map = tilemap.getActRoom();
		
		for(int i=0; i<15; i++) {
			for(int j=0; j<15; j++){
				if((map[i][j]&2) != 0) {
					staticObjects.add(new Rectangle(i * Tilemap.TILE_SIZE, j * Tilemap.TILE_SIZE, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE));
				} else if((map[i][j]&16) != 0) {
					triggers.add(tilemap.getTrap(new Point(i, j)));
				} else if((map[i][j]&4) != 0) {
					triggers.add(tilemap.getPortal(new Point(i, j)));
				}
			}
		}
		
		map = tilemap.getActRoom();
	}
	
	public boolean isColliding(GameObject collider) {
		Rectangle rect = collider.getCollisionRect();
		for(int i = 0; i < staticObjects.size(); i++) {
			if(staticObjects.get(i).intersects(rect))
				return true;
		}
		
		for(int i = 0; i < triggers.size(); i++) {
			Rectangle triggerArea = ((GameObject)triggers.get(i)).getCollisionRect();
			if(triggerArea.intersects(rect)) {
				triggers.get(i).trigger(collider);
				return false;
			}
		}
		
		return false;
	}
}
