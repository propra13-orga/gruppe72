package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import Utils.Vector2;

public class Tilemap implements IDrawable {
	public static final int TILE_SIZE = 32;
	
	public int rooms[][][];
	private BufferedImage wall, grass;
	
	private int actRoom[][];
	private final int ROOM_SIZE = 15;
	private HashMap<Vector2, Trap> traps;
	private HashMap<Vector2, Portal> actPortals;
	private HashMap<Vector2, Goal> goals;
	private Game game;
	
	public Tilemap(Game game) {
		this.game = game;
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
			//goal = ImageIO.read(new File("res/goal.png"));
			//tp = ImageIO.read(new File("res/tp.png"));
		}
		catch (IOException e)
		{
			System.err.println("Couldn't load images");
			System.exit(1);
		}
		
		traps = new HashMap<Vector2, Trap>();
		actPortals = new HashMap<Vector2, Portal>();
		goals = new HashMap<Vector2, Goal>();
	}
	
	public int[][] getActRoom() {
		return actRoom;
	}
	
	public void changeRoom(int room) {
		if(room > rooms.length)
			throw new IndexOutOfBoundsException();
		
		actRoom = rooms[room];
		traps.clear();
		actPortals.clear();
		goals.clear();
		
		for(int j = 0; j < ROOM_SIZE; j++)
			for(int i = 0; i < ROOM_SIZE; i++)
			{
				if((actRoom[i][j]&16) != 0) {
					Vector2 p = new Vector2(i, j);
					traps.put(p, new Trap(p));
				}
			}
		
		switch(room) {
		case 0:
			actPortals.put(new Vector2(14, 5), new Portal(game, 1, new Vector2(14, 5), new Vector2(1, 5)));
			break;
		case 1:
			actPortals.put(new Vector2(0, 5), new Portal(game, 0, new Vector2(0, 5), new Vector2(13, 5)));
			actPortals.put(new Vector2(3,14), new Portal(game, 2, new Vector2(3,14), new Vector2(3,1)));
			break;
		case 2:
			actPortals.put(new Vector2(3, 0), new Portal(game, 1, new Vector2(3, 0), new Vector2(3, 13)));
			goals.put(new Vector2(13, 13), new Goal(new Vector2(13, 13), game));
			break;
		}
		
		System.out.println("Finished loading map. Added " + traps.size() + " traps.");
	}
	
	public Trap getTrap(Vector2 v) {
		if(traps.containsKey(v))
			return traps.get(v);
		
		return null;
	}
	
	public Portal getPortal(Vector2 position) {
		if(actPortals.containsKey(position))
			return actPortals.get(position);
		
		return null;
	}
	
	public Goal getGoal(Vector2 position) {
		if(goals.containsKey(position))
			return goals.get(position);
		
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
				//else if((actRoom[i][j]&4) != 0)
					//graphics.drawImage(tp, null, i*TILE_SIZE, j*TILE_SIZE);
				//else if((actRoom[i][j]&8) != 0)
					//graphics.drawImage(goal, null, i*TILE_SIZE, j*TILE_SIZE);
			}
		
		for(Map.Entry<Vector2, Trap> entry : traps.entrySet())
			entry.getValue().draw(graphics);
		for(Map.Entry<Vector2, Portal> entry : actPortals.entrySet())
			entry.getValue().draw(graphics);
		for(Map.Entry<Vector2, Goal> entry : goals.entrySet())
			entry.getValue().draw(graphics);
	}

}
