package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Utils.Vector2;

public class Trap extends GameObject implements ITriggerable
{
	//private Point tilePosition;
	private Rectangle triggerArea;
	private BufferedImage texture;
	
	public Trap(Vector2 tilePosition)
	{
		super();
		
		this.screenPosition = new Vector2(tilePosition.x * Tilemap.TILE_SIZE, tilePosition.y * Tilemap.TILE_SIZE);
		this.triggerArea = new Rectangle((int)screenPosition.x, (int)screenPosition.y, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
	}
	
	@Override
	public void trigger(GameObject trigger)
	{
		trigger.terminate();
		//System.out.println("GAME OVER");
		// To Do: Go into Main Menu
	}

	@Override
	protected BufferedImage getTexture()
	{
		//BufferedImage trapimage = null;
		if(texture == null) {
		
			try
			{
				texture = ImageIO.read(new File("res/trap.png"));
			} catch (IOException e)
			{
				System.err.println("Couldn't load trap image");
				System.exit(1);
			}
		}
		return texture;
	}

	@Override
	public Rectangle getCollisionRect() {
		return triggerArea;
	}

	@Override
	public void terminate() { }

}
