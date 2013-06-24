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
	private final int ROOM_SIZE = 15;
	public static final int ROOM_WIDTH = 32;
	public static final int ROOM_HEIGHT = 20;
	private MapLoader loader;
	
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
	
	private void init() {
		rooms = new int[3][ROOM_SIZE][ROOM_SIZE];
		
		int tmproom0[][] =
			{	{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
				{ 2,17,17,17, 1, 1, 1, 1, 1, 1,17, 1, 1, 1, 2},
				{ 2,17,17, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
				{ 2,17, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 2, 2, 1, 1, 1, 2, 2, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 1,17, 1, 2, 1, 1, 1, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 2, 1, 1,17, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 2},
				{ 2, 1, 1, 1, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2},
				{ 2, 1, 1, 1,17, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2},
				{ 2, 1, 1, 1, 2, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2},
				{ 2, 2, 2, 2, 2, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2}
			};
		int tmproom1[][] =
			{	{ 2, 2, 2, 2, 2, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2},
				{ 2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 2, 2, 2, 2, 2, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1,17, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 4},
				{ 2, 1, 1, 1, 1, 1,17, 1, 1, 1, 2, 2, 1, 2, 2},
				{ 2, 2, 2, 2, 1, 1,17, 2, 2, 2, 2, 2, 1,17, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1,17, 2, 2, 2, 2, 2, 2, 2, 2, 2,17, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2,17, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,17, 1, 1, 2},
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}
			};
		int tmproom2[][] =
			{	{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
				{ 2, 1, 1, 1,17,17, 2, 2, 1, 1, 1, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 2, 2, 1, 2, 2, 1, 1, 1, 2},
				{ 4, 1, 1, 2, 2, 1, 1, 1, 1, 2, 2, 1, 1, 1, 2},
				{ 2, 2, 1, 2, 2, 1, 1, 2, 2, 1, 1, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 2, 2, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 1, 2, 2, 1,17,17, 1, 2, 2, 2, 2, 2},
				{ 2, 2, 2, 1, 2, 2, 2, 2,17, 1, 1, 1, 2, 2, 2},
				{ 2, 2, 2,17,17, 1, 2, 2, 1, 2, 2, 1, 1, 1, 2},
				{ 2, 1,17,17, 1, 1, 1, 1, 1, 2, 2, 1, 1,17, 2},
				{ 2, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1,17,17, 2},
				{ 2, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 2, 2, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 2, 2, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 8, 2},
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}
			};
		rooms[0] = tmproom0;
		rooms[1] = tmproom1;
		rooms[2] = tmproom2;
		
		// Load images (put into its own class later)
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
	
	public int[][] getActRoom() {
		return actRoom;
	}
	
	public void loadMap(String mapname) {
		loader.loadMap(mapname);
		actRoom = loader.getMap();
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
