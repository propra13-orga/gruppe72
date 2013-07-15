package fart.dungeoncrawler;

import java.awt.Rectangle;

import Utils.Vector2;

import fart.dungeoncrawler.actor.ActorDescription;
import fart.dungeoncrawler.actor.EnemyDescription;
import fart.dungeoncrawler.actor.NPCDescription;
import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.enums.Heading;

/**
 * A CheckPointInfo is created for the player and every enemy. It stores all important data to
 * be able to reload.
 * @author Erhan
 *
 */
public class CheckPointInfo {
	private float curHealth;
	private float maxHealth;
	private float curMana;
	private float maxMana;
	private int heading;
	private int state;
	private Stats stats;
	private Rectangle rectangle;
	private ActorDescription actDesc;
	private NPCDescription npcDesc;
	private EnemyDescription enemyDesc;
	private boolean isBoss;
	
	/**
	 * Creates a CheckPointInfo
	 * @param ch current Health
	 * @param mh max Health
	 * @param cm current Mana
	 * @param mm max Mana
	 * @param heading heading of the actor
	 * @param state state of the actor
	 * @param stats stats of the actor
	 * @param colRect collision rectangle of the actor
	 * @param actDesc actorDescription
	 * @param npcDesc npcDescription - can be null
	 * @param enemyDesc enemyDescription - can be null
	 * @param isBoss if the actor is a boss enemy
	 */
	public CheckPointInfo(
			float ch, 
			float mh, 
			float cm, 
			float mm, 
			int heading, 
			int state, 
			Stats stats, 
			Rectangle colRect, 
			ActorDescription actDesc, 
			NPCDescription npcDesc, 
			EnemyDescription enemyDesc,
			boolean isBoss) {
		this.curHealth = ch;
		this.maxHealth = mh;
		this.curMana = cm;
		this.maxMana = mm;
		this.heading = heading;
		this.state = state;
		this.stats = new Stats(stats);
		this.rectangle = new Rectangle(colRect);
		this.actDesc = actDesc;
		this.npcDesc = npcDesc;
		this.enemyDesc = enemyDesc;
		this.isBoss = isBoss;
	}

	/**
	 * Creates a new instance of the Health-class with the correct values.
	 * @return
	 */
	public Health getHealth() {
		return new Health(maxHealth, curHealth);
	}

	/**
	 * Creates a new instance of the Mana-class with the correct values.
	 * @return
	 */
	public Mana getMana() {
		return new Mana(maxMana, curMana);
	}

	/**
	 * @return the heading
	 */
	public Heading getHeading() {
		return Heading.values()[heading];
	}

	/**
	 * @return the state
	 */
	public DynamicObjectState getState() {
		return DynamicObjectState.values()[state];
	}

	/**
	 * @return the stats
	 */
	public Stats getStats() {
		return stats;
	}

	/**
	 * @return the rectangle
	 */
	public Rectangle getRectangle() {
		return rectangle;
	}

	/**
	 * @return the actDesc
	 */
	public ActorDescription getActDesc() {
		return actDesc;
	}

	/**
	 * @return the npcDesc
	 */
	public NPCDescription getNpcDesc() {
		return npcDesc;
	}

	/**
	 * @return the enemyDesc
	 */
	public EnemyDescription getEnemyDesc() {
		return enemyDesc;
	}
	
	/**
	 * Returns the position of the actor. 
	 * @return
	 */
	public Vector2 getPosition() {
		return new Vector2(rectangle.x, rectangle.y);
	}
	
	/**
	 * Returns if the actor is a boss enemy. 
	 * @return
	 */
	public boolean isBoss() {
		return isBoss;
	}
}
