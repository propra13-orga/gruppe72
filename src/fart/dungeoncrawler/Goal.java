package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fart.dungeoncrawler.actor.Actor;

import Utils.Vector2;

/**
 * This class represents the goal. When the player reaches the goal the game is won. 
 * @author Felix
 *
 */
public class Goal extends GameObject implements ITriggerableOnKey {
	private BufferedImage texture;
	private Rectangle triggerArea;
	private Game game;
	
	/**
	 * Creates the goal. 
	 * @param tilePosition the position in tiles of the goal
	 * @param game instance of the game
	 */
	public Goal(Vector2 tilePosition, Game game) {
		super();
		this.game = game;
		
		try {
			texture = ImageIO.read(new File("res/goal.png"));
		} catch (IOException e) {
			System.err.println("Couldn't load images.");
		} catch (IllegalArgumentException e) {
			System.err.println("Couldn't load images.");
		}
		
		this.screenPosition = new Vector2(tilePosition.x * Tilemap.TILE_SIZE, tilePosition.y * Tilemap.TILE_SIZE);
		this.triggerArea = new Rectangle((int)screenPosition.x, (int)screenPosition.y, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
	}

	@Override
	public void trigger(Actor trigger) {
		System.out.println("Player reached the goal!");
		game.playerWins();
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

	@Override
	public Rectangle getTriggerArea() {
		return triggerArea;
	}

}
