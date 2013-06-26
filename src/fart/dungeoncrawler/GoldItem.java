package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Utils.Vector2;

import fart.dungeoncrawler.actor.Actor;

public class GoldItem extends GameObject implements ITriggerableOnKey {
	private int amount;
	private Rectangle triggerArea;
	private StaticObjectManager sManager;
	private static BufferedImage texture;
	private Game game;
	
	public GoldItem(Game game, Vector2 position, int amount) {
		this.amount = amount;
		this.screenPosition = position;
		triggerArea = new Rectangle((int)position.x, (int)position.y, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
		this.game = game;
		
		if(texture == null) {
			try {
				texture = ImageIO.read(new File("res/icons/gold.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		game.getStaticManager().addObject(this);
		game.getCollision().addTriggerOnKey(this);
	}

	@Override
	public void trigger(Actor actor) {
		actor.getInventory().addGold(amount);
		game.getStaticManager().removeObject(this);
		game.getCollision().removeTriggerOnKey(this);
	}

	@Override
	public Rectangle getTriggerArea() {
		return triggerArea;
	}

	@Override
	protected BufferedImage getTexture() {
		return texture;
	}

	@Override
	public Rectangle getCollisionRect() {
		return new Rectangle(0, 0, 0, 0);
	}

	@Override
	public void terminate() {
		
	}

}
