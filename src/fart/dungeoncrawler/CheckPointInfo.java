package fart.dungeoncrawler;

import java.awt.Rectangle;

import Utils.Vector2;

import fart.dungeoncrawler.actor.ActorDescription;
import fart.dungeoncrawler.actor.EnemyDescription;
import fart.dungeoncrawler.actor.NPCDescription;
import fart.dungeoncrawler.actor.Stats;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.enums.Heading;

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

	public Health getHealth() {
		return new Health(maxHealth, curHealth);
	}

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
	
	public Vector2 getPosition() {
		return new Vector2(rectangle.x, rectangle.y);
	}
	
	public boolean isBoss() {
		return isBoss;
	}
}
