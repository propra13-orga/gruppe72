package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fart.dungeoncrawler.actor.DynamicObjectManager;

/**
 * The tilemap is responsible for drawing the map to the screen.
 * @author Timo
 *
 */
public class Tilemap implements IDrawable {
	public static final int TILE_SIZE = 32;
	
	public int rooms[][][];
	private BufferedImage wall, grass;
	
	private int actRoom[][];
	public static final int ROOM_WIDTH = 15;
	public static final int ROOM_HEIGHT = 15;
	private static int width;
	public static int getWidth() { return width; }
	public static void setWidth(int w) { width = w; }
	private static int height;
	public static int getHeight() { return height; }
	public static void setHeight(int h) { height = h; }
	private MapLoader loader;
	private String path;
	public String getPath() { return path; }
	private String name;
	public String getName() { return name; }
	
	/**
	 * Creates an instance of the tilemap. 
	 * @param game instance of the game running.
	 * @param sManager the static object manager. It is passed to the maploader.
	 * @param dManager the dynamic object manager. It is passed to the maploader. 
	 * @param collision the collision detector. It is passed to the maploader. 
	 */
	public Tilemap(Game game, StaticObjectManager sManager, DynamicObjectManager dManager, CollisionDetector collision) {
		loader = new MapLoader(game, sManager, dManager, collision);
		try
		{
			wall = ImageIO.read(new File("res/wall.png"));
			grass = ImageIO.read(new File("res/grass.png"));
		}
		catch (IOException e)
		{
			System.err.println("Couldn't load images");
			System.exit(1);
		}
	}
	
	/**
	 * Returns the maploader. 
	 * @return
	 */
	public MapLoader getLoader() {
		return loader;
	}
	
	/**
	 * Returns the map-array. 
	 * @return
	 */
	public int[][] getActRoom() {
		return actRoom;
	}
	
	/**
	 * Loads a new map. This is done in the maploader. 
	 * @param mapPath path to the mapfile. 
	 */
	public void loadMap(String mapPath) {
		loader.loadMap(this, mapPath);
		actRoom = loader.getMap();
		name = loader.getMapName();
		path = mapPath;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		for(int j=0; j<height; j++)
			for(int i=0; i<width; i++)
			{
				if((actRoom[i][j]&1) != 0)
					graphics.drawImage(grass, null, i*TILE_SIZE, j*TILE_SIZE);
				else if((actRoom[i][j]&2) != 0)
					graphics.drawImage(wall, null, i*TILE_SIZE, j*TILE_SIZE);
			}
	}

}
