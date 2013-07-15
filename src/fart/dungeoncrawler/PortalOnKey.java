package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fart.dungeoncrawler.actor.Actor;

import Utils.Vector2;

/**
 * This class represents a portal that is only triggered on key-press
 * @author Felix
 *
 */
public class PortalOnKey extends GameObject implements ITriggerableOnKey {
	private String mapTo;
	private Vector2 pointTo;
	private Game game;
	private String spritePath;
	private BufferedImage texture;
	private Rectangle collisionRect;
	
	/**
	 * Creates the portal. 
	 * @param game instance of the game
	 * @param mapTo path to the destinationmapfile
	 * @param tilePositionFrom position in tiles on the sourcemap
	 * @param tilePositionTo position in tiles on the destinationmap
	 */
	public PortalOnKey(Game game, String mapTo, Vector2 tilePositionFrom, Vector2 tilePositionTo) {
		this.game = game;
		this.mapTo = mapTo;
		pointTo = tilePositionTo;
		screenPosition = new Vector2(tilePositionFrom.x * Tilemap.TILE_SIZE, tilePositionFrom.y * Tilemap.TILE_SIZE);
		collisionRect = new Rectangle((int)screenPosition.x + Tilemap.TILE_SIZE / 4, (int)screenPosition.y + Tilemap.TILE_SIZE / 4, Tilemap.TILE_SIZE / 2, Tilemap.TILE_SIZE / 2);
	}
	
	@Override
	public void trigger(Actor trigger) {
		game.changeMap(mapTo, pointTo);
	}

	@Override
	protected BufferedImage getTexture() {
		if(texture == null) {
			try {
				if(spritePath == null)
					texture = ImageIO.read(new File("res/tp2.png"));
				else
					texture = ImageIO.read(new File(spritePath));
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

	@Override
	public Rectangle getTriggerArea() {
		return collisionRect;
	}
}
