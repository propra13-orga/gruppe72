package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import MathUtils.Vector2;

public class Portal extends GameObject implements ITriggerable {

	private int mapNumberTo;
	private Vector2 pointTo;
	private Game game;
	private BufferedImage texture;
	private Rectangle collisionRect;
	
	public Portal(Game game, int mapTo, Vector2 tilePositionFrom, Vector2 tilePositionTo) {
		this.game = game;
		mapNumberTo = mapTo;
		pointTo = tilePositionTo;
		screenPosition = new Vector2(tilePositionFrom.x * Tilemap.TILE_SIZE, tilePositionFrom.y * Tilemap.TILE_SIZE);
		collisionRect = new Rectangle((int)screenPosition.x + Tilemap.TILE_SIZE / 4, (int)screenPosition.y + Tilemap.TILE_SIZE / 4, Tilemap.TILE_SIZE / 2, Tilemap.TILE_SIZE / 2);
	}
	
	@Override
	public void trigger(GameObject trigger) {
		game.changeMap(mapNumberTo, pointTo);
	}

	@Override
	protected BufferedImage getTexture() {
		if(texture == null) {
			try {
				texture = ImageIO.read(new File("res/tp.png"));
			} catch(IOException e) {
				System.err.println("Couldnt load image.");
				e.printStackTrace();
			} catch(IllegalArgumentException e) {
				System.err.println("Couldnt load image.");
				e.printStackTrace();
			}
		}
		
		return texture;
	}

	@Override
	public Rectangle getCollisionRect() {
		return collisionRect;
	}

	@Override
	public void terminate() {
	}

}
