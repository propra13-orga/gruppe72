package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tilemap implements IDrawable {
	public static final int TILE_SIZE = 32;
	
	public int rooms[][][];
	private BufferedImage wall, grass;
	
	private int actRoom[][];
	public static final int ROOM_WIDTH = 15;
	public static final int ROOM_HEIGHT = 15;
	private MapLoader loader;
	private String name;
	public String getName() { return name; }
	
	public Tilemap(Game game, StaticObjectManager sManager, DynamicObjectManager dManager, Collision collision) {
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
	
	public MapLoader getLoader() {
		return loader;
	}
	
	public int[][] getActRoom() {
		return actRoom;
	}
	
	public void loadMap(String mapname) {
		loader.loadMap(this, mapname);
		actRoom = loader.getMap();
		name = mapname;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		for(int j=0; j<ROOM_HEIGHT; j++)
			for(int i=0; i<ROOM_WIDTH; i++)
			{
				if((actRoom[i][j]&1) != 0)
					graphics.drawImage(grass, null, i*TILE_SIZE, j*TILE_SIZE);
				else if((actRoom[i][j]&2) != 0)
					graphics.drawImage(wall, null, i*TILE_SIZE, j*TILE_SIZE);
			}
	}

}
