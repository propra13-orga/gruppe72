package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Utils.Vector2;

import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.actor.Player;
import fart.dungeoncrawler.items.BaseItem;
import fart.dungeoncrawler.items.ItemCollection;

/**
 * Represents an item laying on the map.
 * @author Felix
 *
 */
public class MapItem extends GameObject implements ITriggerableOnKey {
	private BaseItem item;
	private Rectangle colRect;
	private StaticObjectManager sManager;
	private CollisionDetector collision;
	private Game game;
	
	/**
	 * Creates the item. 
	 * @param game instance of the game
	 * @param itemID ID of the item. Used to get the item from the ItemCollection.
	 * @param position the position in screenspace. 
	 */
	public MapItem(Game game, int itemID, Vector2 position) {
		screenPosition = position;
		colRect = new Rectangle((int)position.x, (int)position.y, 32, 32);
		item = ItemCollection.getInstance().getByID(itemID);
		sManager = game.getStaticManager();
		sManager.addObject(this);
		collision = game.getCollision();
		collision.addTriggerOnKey(this);
		this.game = game;
	}

	@Override
	public void trigger(Actor actor) {
		if(actor.getInventory().addItem(item)) {
			if(actor instanceof Player && !game.isInNetwork()) {
				Player p = (Player)actor;
				p.getQuestLog().itemCollected(item.getIndex());
			}
			terminate();
		}
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
