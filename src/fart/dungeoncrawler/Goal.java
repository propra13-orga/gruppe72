package fart.dungeoncrawler;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Goal extends GameObject implements ITriggerable {
	private BufferedImage texture;
	private Rectangle triggerArea;
	private Game game;
	
	public Goal(Point tilePosition, Game game) {
		super();
		this.game = game;
		
		try {
			texture = ImageIO.read(new File("res/goal.png"));
		} catch (IOException e) {
			System.err.println("Couldn't load images.");
		} catch (IllegalArgumentException e) {
			System.err.println("Couldn't load images.");
		}
		
		this.screenPosition = new Point(tilePosition.x * Tilemap.TILE_SIZE, tilePosition.y * Tilemap.TILE_SIZE);
		this.triggerArea = new Rectangle(screenPosition, new Dimension(Tilemap.TILE_SIZE, Tilemap.TILE_SIZE));
	}

	@Override
	public void trigger(GameObject trigger) {
		System.out.println("Player reached the goal!");
		//Go Back to Menu//
	}

	@Override
	protected BufferedImage getTexture() {
		return texture;
	}

	@Override
	public Rectangle getCollisionRect() {
		return triggerArea;
	}

	@Override
	public void terminate() { }

}
