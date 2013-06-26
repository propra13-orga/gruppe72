package fart.dungeoncrawler.actor;

import java.awt.Dimension;
import java.awt.Rectangle;

import Utils.Vector2;
import fart.dungeoncrawler.*;
import fart.dungeoncrawler.enums.*;
import fart.dungeoncrawler.items.Equipment;
import fart.dungeoncrawler.items.Inventory;

public abstract class Actor extends GameObject implements IUpdateable {
	protected Rectangle collisionRect;
	protected DynamicObjectState state;
	protected Vector2 velocity;
	protected Health health;
	protected Mana mana;
	protected Heading heading;
	protected DynamicObjectManager manager;
	protected Collision collision;
	protected Inventory inventory;
	protected Equipment equip;
	protected Stats stats;
	protected ActorDescription description;
	protected Game game;
	
	public Actor(Game game, ActorDescription desc, Vector2 position) {
		//this.health = desc.getHealth();
		//this.mana = desc.getMana();
		this.stats = desc.getStats();
		health = new Health(stats.getStamina() * Stats.HEALTH_PER_STAM);
		mana = new Mana(stats.getWill() * Stats.MANA_PER_WILL);
		this.heading = desc.getHeading();
		this.screenPosition = position;
		this.description = desc;
		this.game = game;
		
		Dimension dim = desc.getCollisionDimension();
		collisionRect = new Rectangle((int)position.x, (int)position.y, dim.width, dim.height);
		
		velocity = new Vector2();
		collision = game.getCollision();
		manager = game.getDynamicManager();
		inventory = new Inventory(game.getController(), this);
		state = DynamicObjectState.Idle;
		equip = new Equipment();
		
		//TEST

		manager.addObject(this);
		collision.addDynamicObject(this);
	}
	
	public ActorDescription getActorDesc() {
		return description;
	}

	/**
	 * @return the collisionRect
	 */
	public Rectangle getCollisionRect() {
		return collisionRect;
	}

	/**
	 * @return the curState
	 */
	public DynamicObjectState getState() {
		return state;
	}

	/**
	 * @return the velocity
	 */
	public Vector2 getVelocity() {
		return velocity;
	}

	/**
	 * @return the health
	 */
	public Health getHealth() {
		return health;
	}

	/**
	 * @return the mana
	 */
	public Mana getMana() {
		return mana;
	}

	/**
	 * @return the heading
	 */
	public Heading getHeading() {
		return heading;
	}

	/**
	 * @return the manager
	 */
	public DynamicObjectManager getManager() {
		return manager;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}
	
	public Collision getCollision() {
		return collision;
	}
	
	public Stats getStats() {
		return stats;
	}
	
	public Equipment getEquipment() {
		return equip;
	}
	
	public void setHealth(Health health) {
		this.health = health;
	}

	public void setHeading(Heading heading) {
		this.heading = heading;
	}
	
	/**
	 * Sets the ScreenPosition. 
	 * @param position New Position
	 */
	public void setScreenPosition(Vector2 position) {
		screenPosition = position;
		collisionRect.x = (int)screenPosition.x;
		collisionRect.y = (int)screenPosition.y;
	}
	
	/**
	 * Sets a new state. 
	 * @param state New state. 
	 */
	public void setState(DynamicObjectState state) {
		this.state = state;
	}
	
	/**
	 * Sets the tilePosition. ScreenPosition will be generated too.
	 * @param position New position
	 */
	public void setTilePosition(Vector2 position) {
		screenPosition = new Vector2(position.x * Tilemap.TILE_SIZE, position.y * Tilemap.TILE_SIZE);
		collisionRect.x = (int)screenPosition.x;
		collisionRect.y = (int)screenPosition.y;
	}
	
	public void setVelocity(Vector2 v) {
		this.velocity = new Vector2(v);
	}
}
