package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.actor.Player;

import Utils.Vector2;

/**
 * The portal lets the player be teleported onto an other map. 
 * @author Roman
 *
 */
public class Portal extends GameObject implements ITriggerable {

	private String mapTo;
	private Vector2 pointTo;
	private Game game;
	private String spritePath;
	private BufferedImage texture;
	private Rectangle collisionRect;
	
	/**
	 * Creates a portal.
	 * @param game instance of the game
	 * @param spritePath path to the texture
	 * @param mapTo path to the map the player is teleported onto
	 * @param tilePositionFrom position in tiles on the sourcemap
	 * @param tilePositionTo position in tiles on the destinationmap
	 */
	public Portal(Game game, String spritePath, String mapTo, Vector2 tilePositionFrom, Vector2 tilePositionTo) {
		this.game = game;
		this.spritePath = spritePath;
		this.mapTo = mapTo;
		pointTo = tilePositionTo.mul(Tilemap.TILE_SIZE);
		screenPosition = new Vector2(tilePositionFrom.x * Tilemap.TILE_SIZE, tilePositionFrom.y * Tilemap.TILE_SIZE);
		collisionRect = new Rectangle((int)screenPosition.x + Tilemap.TILE_SIZE / 4, (int)screenPosition.y + Tilemap.TILE_SIZE / 4, Tilemap.TILE_SIZE / 2, Tilemap.TILE_SIZE / 2);
	}
	
	@Override
	public void trigger(Actor trigger) {
		if(trigger instanceof Player)
			game.changeMap(mapTo, pointTo);
	}

	@Override
	protected BufferedImage getTexture() {
		if(texture == null) {
			try {
				if(spritePath == null)
					texture = ImageIO.read(new File("res/tp.png"));
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
}
