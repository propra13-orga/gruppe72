package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Utils.Vector2;

import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.items.BaseItem;
import fart.dungeoncrawler.items.ItemCollection;

public class MapItem extends GameObject implements ITriggerableOnKey {
	private BaseItem item;
	private Rectangle colRect;
	private StaticObjectManager sManager;
	private Collision collision;
	
	public MapItem(Game game, int itemID, Vector2 position) {
		screenPosition = position;
		colRect = new Rectangle((int)position.x, (int)position.y, 32, 32);
		item = ItemCollection.getInstance().getByID(itemID);
		sManager = game.getStaticManager();
		sManager.addObject(this);
		collision = game.getCollision();
		collision.addTriggerOnKey(this);
	}

	@Override
	public void trigger(Actor actor) {
		actor.getInventory().addItem(item);
		terminate();
	}

	@Override
	protected BufferedImage getTexture() {
		return item.getIcon();
	}

	@Override
	public Rectangle getCollisionRect() {
		return colRect;
	}

	@Override
	public void terminate() {
		sManager.removeObject(this);
		collision.removeTriggerOnKey(this);
	}

	@Override
	public Rectangle getTriggerArea() {
		return colRect;
	}

}
