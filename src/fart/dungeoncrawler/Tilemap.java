package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Tilemap implements IDrawable {
	public static final int TILE_SIZE = 32;
	
	public int rooms[][][];
	private BufferedImage wall, grass, goal, tp;
	
	private int actRoom[][];
	private final int ROOM_SIZE = 15;
	private HashMap<Point, Trap> traps;
	
	public Tilemap() {
		init();
		changeRoom(0);
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
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2},
				{ 2, 2, 2, 2, 2, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2}
			};
		int tmproom1[][] =
			{	{ 2, 2, 2, 2, 2, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 2, 2, 2, 2, 2, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 4},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
				{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}
			};
		int tmproom2[][] =
			{	{ 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
				{ 2, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 2, 2, 1, 2, 2, 1, 1, 1, 2},
				{ 4, 1, 1, 2, 2, 1, 1, 1, 1, 2, 2, 1, 1, 1, 2},
				{ 2, 1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 2, 2, 1, 2, 2, 1, 1, 2},
				{ 2, 1, 1, 1, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2, 2},
				{ 2, 2, 2, 1, 2, 2, 2, 2, 1, 1, 1, 1, 2, 2, 2},
				{ 2, 2, 2, 1, 1, 1, 2, 2, 1, 2, 2, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 2},
				{ 2, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2},
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
			goal = ImageIO.read(new File("res/goal.png"));
			tp = ImageIO.read(new File("res/tp.png"));
		}
		catch (IOException e)
		{
			System.err.println("Couldn't load images");
			System.exit(1);
		}
		
		traps = new HashMap<Point, Trap>();
	}
	
	public int[][] getActRoom() {
		return actRoom;
	}
	
	public void changeRoom(int room) {
		if(room > rooms.length)
			throw new IndexOutOfBoundsException();
		
		actRoom = rooms[room];
		traps.clear();
		
		for(int j = 0; j < ROOM_SIZE; j++)
			for(int i = 0; i < ROOM_SIZE; i++)
			{
				if((actRoom[i][j]&16) != 0) {
					Point p = new Point(i, j);
					traps.put(p, new Trap(p));
				}
			}
		
		System.out.println("Finished loading map. Added " + traps.size() + " traps.");
	}
	
	public Trap getTrap(Point position) {
		if(traps.containsKey(position))
			return traps.get(position);
		
		return null;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		for(int j=0; j<ROOM_SIZE; j++)
			for(int i=0; i<ROOM_SIZE; i++)
			{
				if((actRoom[i][j]&1) != 0)
					graphics.drawImage(grass, null, i*TILE_SIZE, j*TILE_SIZE);
				else if((actRoom[i][j]&2) != 0)
					graphics.drawImage(wall, null, i*TILE_SIZE, j*TILE_SIZE);
				else if((actRoom[i][j]&4) != 0)
					graphics.drawImage(tp, null, i*TILE_SIZE, j*TILE_SIZE);
				else if((actRoom[i][j]&8) != 0)
					graphics.drawImage(goal, null, i*TILE_SIZE, j*TILE_SIZE);
			}
	}

}
