package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tilemap implements IDrawable {
	public static final int TILE_SIZE = 32;
	
	public int room1[][];
	private BufferedImage wall, grass;
	
	
	public Tilemap() {
		init();
	}
	
	private void init() {
		room1 = new int[15][15];
		
		// 0 = space; 1 = wall
		// create a room with outside walls only
		for(int i=0; i<15; i++)
		{
			for(int j=0; j<15; j++)
			{
				if((i==0)||(i==14)||(j==0)||(j==14))
					room1[i][j] = 1;
				else
					room1[i][j] = 0;
			}
		}
		
		// output room1 in console (for checking)
		System.out.println("room1:");
		for(int j=0; j<15; j++)
		{
			for(int i=0; i<15; i++)
				System.out.print(room1[i][j]);
			System.out.println();
		}
		
		// Load images (put into its own class later)
		try
		{
			wall = ImageIO.read(new File("res/wall.png"));
			grass = ImageIO.read(new File("res/grass.png"));
		}
		catch (IOException e)
		{
			System.out.println("Couldn't load images");
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
				if(room1[i][j] == 1)
					graphics.drawImage(wall, null, i*32, j*32);
				else
					graphics.drawImage(grass, null, i*32, j*32);
			}
	}

}
