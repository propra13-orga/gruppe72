package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.util.ArrayList;

public class CollisionDetector {
	public static int[][] map;
	public static ArrayList<Rectangle> staticObjects;
	public static ArrayList<Rectangle> harmfulObjects;
	
	// returns 0 if not colliding, 1 if colliding, 2 if harmfully colliding
	public static int isColliding(Rectangle rect) {
		if((staticObjects == null) && (harmfulObjects == null)) {
			if(map == null)
				return 0;
			
			staticObjects = new ArrayList<Rectangle>();
			harmfulObjects = new ArrayList<Rectangle>();
			
			for(int i=0; i<15; i++)
				for(int j=0; j<15; j++){
					if((map[i][j]&2) != 0)
						staticObjects.add(new Rectangle(i * Tilemap.TILE_SIZE, j * Tilemap.TILE_SIZE, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE));
					else if((map[i][j]&16) != 0)
						harmfulObjects.add(new Rectangle(i * Tilemap.TILE_SIZE, j * Tilemap.TILE_SIZE, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE));
				}
		}
		
		for(int i = 0; i < staticObjects.size(); i++) {
			if(staticObjects.get(i).intersects(rect))
				return 1;
		}
		for(int i = 0; i < harmfulObjects.size(); i++) {
			if(harmfulObjects.get(i).intersects(rect))
				return 2;
		}
		
		return 0;
	}
}

// OLD VERSION (does not differentiate between walls and traps)
/*package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.util.ArrayList;

public class CollisionDetector {
	public static int[][] map;
	public static ArrayList<Rectangle> staticObjects;
	
	public static boolean isColliding(Rectangle rect) {
		if(staticObjects == null) {
			if(map == null)
				return false;
			
			staticObjects = new ArrayList<Rectangle>();
			
			for(int i=0; i<15; i++)
				for(int j=0; j<15; j++){
					if((map[i][j]&18) != 0)
						staticObjects.add(new Rectangle(i * Tilemap.TILE_SIZE, j * Tilemap.TILE_SIZE, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE));
				}
		}
		
		for(int i = 0; i < staticObjects.size(); i++) {
			if(staticObjects.get(i).intersects(rect))
				return true;
		}
		
		return false;
	}
}*/
