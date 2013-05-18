package fart.dungeoncrawler;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Trap extends GameObject implements ITriggerable
{
	private Point tilePosition;
	
	public Trap(Point tilePosition)
	{
		super();
		
		this.tilePosition = tilePosition;
		this.screenPosition = new Point(tilePosition.x * Tilemap.TILE_SIZE, tilePosition.y * Tilemap.TILE_SIZE);
	}
	
	@Override
	public void trigger()
	{
		System.out.println("GAME OVER");
		// To Do: Go into Main Menu
	}

	@Override
	protected BufferedImage getTexture()
	{
		BufferedImage trapimage = null;
		
		try
		{
			trapimage = ImageIO.read(new File("res/trap.png"));
		} catch (IOException e)
		{
			System.err.println("Couldn't load trap image");
			System.exit(1);
		}
		return trapimage;
	}

}
