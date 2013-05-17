package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tilemap implements IDrawable {
	public static final int TILE_SIZE = 32;
	
	public int rooms[][][];
	private BufferedImage wall, grass, goal, tp;
	
	
	public Tilemap() {
		init();
	}
	
	private void init() {
		rooms = new int[3][15][15];
		
		int tmproom0[][] =
			{	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
				{1,0,0,0,1,1,0,0,0,1,1,0,0,0,1},
				{1,0,0,0,1,1,0,1,0,1,1,0,0,0,1},
				{1,0,0,0,0,0,0,1,0,0,0,0,0,0,1},
				{1,0,0,0,0,1,1,1,1,1,0,0,0,0,1},
				{1,0,0,0,0,0,0,1,0,0,0,0,0,0,1},
				{1,0,0,0,1,1,0,1,0,1,1,1,1,1,1},
				{1,0,0,0,1,1,0,0,0,1,1,1,1,1,1},
				{1,0,0,0,0,0,0,0,0,1,1,1,1,1,1},
				{1,0,0,0,0,0,0,0,0,1,1,1,1,1,1},
				{1,0,0,0,0,0,0,0,0,1,1,1,1,1,1},
				{1,1,1,1,1,2,1,1,1,1,1,1,1,1,1}
			};
		int tmproom1[][] =
			{	{1,1,1,1,1,2,1,1,1,1,1,1,1,1,1},
				{1,0,0,0,0,0,0,0,0,0,1,1,0,0,1},
				{1,0,0,1,1,1,1,1,0,0,1,1,0,0,1},
				{1,0,0,0,0,0,0,0,0,0,1,1,0,0,2},
				{1,0,0,0,0,0,0,0,0,0,1,1,0,0,1},
				{1,1,1,1,0,0,0,1,1,1,1,1,0,0,1},
				{1,0,0,0,0,0,0,0,0,0,1,1,0,0,1},
				{1,0,0,0,0,0,0,0,0,0,1,1,0,0,1},
				{1,0,0,1,1,1,1,1,1,1,1,1,0,0,1},
				{1,0,0,0,0,0,0,0,0,0,1,1,0,0,1},
				{1,0,0,0,0,0,0,0,0,0,1,1,0,0,1},
				{1,0,0,0,0,0,0,0,0,0,1,1,0,0,1},
				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
			};
		int tmproom2[][] =
			{	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,0,0,0,0,0,1,1,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,1,1,0,1,1,0,0,0,1},
				{2,0,0,1,1,0,0,0,0,1,1,0,0,0,1},
				{1,0,0,1,1,0,0,1,1,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,1,1,0,1,1,0,0,1},
				{1,0,0,0,1,1,0,0,0,0,1,1,1,1,1},
				{1,1,1,0,1,1,1,1,0,0,0,0,1,1,1},
				{1,1,1,0,0,0,1,1,0,1,1,0,0,0,1},
				{1,0,0,0,0,0,0,0,0,1,1,0,0,0,1},
				{1,0,0,0,1,1,0,0,0,0,0,0,0,0,1},
				{1,0,0,0,1,1,1,1,0,0,0,1,1,0,1},
				{1,0,0,0,0,0,1,1,0,0,0,1,1,0,1},
				{1,0,0,0,0,0,0,0,0,0,0,0,0,3,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
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
	}

	//ToDo:
	//Add functionality here
	public boolean isTileBlocked(Point tilePosition) {
		return false;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		for(int j=0; j<15; j++)
			for(int i=0; i<15; i++)
			{
				if(rooms[0][i][j] == 0)
					graphics.drawImage(grass, null, i*TILE_SIZE, j*TILE_SIZE);
				else if(rooms[0][i][j] == 1)
					graphics.drawImage(wall, null, i*TILE_SIZE, j*TILE_SIZE);
				else if(rooms[0][i][j] == 2)
					graphics.drawImage(tp, null, i*TILE_SIZE, j*TILE_SIZE);
				else if(rooms[0][i][j] == 3)
					graphics.drawImage(goal, null, i*TILE_SIZE, j*TILE_SIZE);
			}
	}

}
