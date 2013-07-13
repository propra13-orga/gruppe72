package fart.dungeoncrawler.actor;

import java.awt.Dimension;
import java.awt.Rectangle;

import Utils.Vector2;
import fart.dungeoncrawler.*;
import fart.dungeoncrawler.enums.*;
import fart.dungeoncrawler.items.Equipment;
import fart.dungeoncrawler.items.Inventory;
import fart.dungeoncrawler.network.NetworkManager;

public abstract class Actor extends GameObject implements IUpdateable {
	private static int serverActorCounter = 0;
	private static int clientActorCounter = 0;
	
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
	protected Level level;
	protected ActorDescription description;
	protected Game game;
	protected int actorID;
	
	protected SpellManager spellManager;
	protected ElementalDamage elementDamage;
	
	protected long lastReg;
	protected static final int REG_MS = 1000;
	protected ElementType elementType;
	
	protected boolean isInNetwork;
	protected NetworkManager netManager;
	
	public Actor(Game game, ActorDescription desc, Vector2 position) {
		this.stats = desc.getStats();
		health = new Health(stats.getStamina() * Stats.HEALTH_PER_STAM);
		mana = new Mana(stats.getWill() * Stats.MANA_PER_WILL);
		this.heading = desc.getHeading();
		this.screenPosition = position;
		this.description = desc;
		this.game = game;
		this.isInNetwork = game.isInNetwork();
		
		if(game.isServer()) {
			actorID = serverActorCounter;
			serverActorCounter += 1;
		} else {
			actorID = clientActorCounter;
			clientActorCounter += 1;
		}
		
		Dimension dim = desc.getCollisionDimension();
		collisionRect = new Rectangle((int)position.x, (int)position.y, dim.width, dim.height);
		
		velocity = new Vector2();
		collision = game.getCollision();
		manager = game.getDynamicManager();
		inventory = new Inventory(game.getController(), this);
		state = DynamicObjectState.Idle;
		equip = new Equipment();
		
		//TEST
		level = new Level(this, desc.getLevel());
		elementType = desc.getElement();
		spellManager = new SpellManager(this);
		elementDamage = new ElementalDamage(0, 0, 0);

		manager.addObject(this);
		collision.addDynamicObject(this);
		
		lastReg = System.currentTimeMillis();
	}
	
	public int getActorID() {
		return actorID;
	}

	protected void regenerate() {
		long cur = System.currentTimeMillis();
		if(cur - lastReg > REG_MS) {
			float hpReg = stats.getHealthRegAmount();
			health.addHealth(hpReg);
			float manaReg = stats.getManaRegAmount();
			mana.addMana(manaReg);
			lastReg = cur;
		}
	}
	
	public ActorDescription getActorDesc() {
		return description;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public ElementType getElementType() {
		return elementType;
	}
	
	public void levelUp() { }

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
	
	public void setElementType(ElementType type) {
		this.elementType = type;
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
	
	public void setStats(Stats newStats) {
		this.stats = newStats;
		
		float cHealth = health.getCurrentHealth();
		float cMana = mana.getCurrentMana();
		health = new Health(stats.getStamina() * Stats.HEALTH_PER_STAM, cHealth);
		mana = new Mana(stats.getWill() * Stats.MANA_PER_WILL, cMana);
	}
	
	public void setVelocity(Vector2 v) {
		this.velocity = new Vector2(v);
		
		if(velocity.x > 0.01f)
			setHeading(Heading.Right);
		else if(velocity.x < -0.01f)
			setHeading(Heading.Left);
		else if(velocity.y > 0.01f)
			setHeading(Heading.Down);
		else if(velocity.y < -0.01f)
			setHeading(Heading.Up);
	}
}
