package fart.dungeoncrawler;

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
				for(int j=0; j<15; j++)
					if(map[i][j] > 0)
						staticObjects.add(new Rectangle(i * Tilemap.TILE_SIZE, j * Tilemap.TILE_SIZE, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE));
		}
		
		for(int i = 0; i < staticObjects.size(); i++) {
			if(staticObjects.get(i).intersects(rect))
				return true;
		}
		
		return false;
	}
}
