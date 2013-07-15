package fart.dungeoncrawler.actor;

import fart.dungeoncrawler.enums.Heading;

/**
 * The EnemyDescription holds some metadata from an enemytype. It is used to instanciate a number
 * of similar enemies. 
 * @author Felix
 *
 */
public class EnemyDescription extends NPCDescription{
	
	private boolean isRanged;
	private int aggroRange;
	private int attackRange;
	
	public EnemyDescription(boolean isRanged,
							String spritePath,
							int aggroRange,
							int attackRange,
							int type,
							int level, 
							int element, 
							Stats stats, 
							Heading heading) {
		super(spritePath, type, level, element, stats, heading);
		this.isRanged = isRanged;
		this.aggroRange = aggroRange;
		this.attackRange = attackRange;
	}
	
	/**
	 * Returns if the enemy is ranged.
	 * @return
	 */
	public boolean getIsRanged() { return isRanged; }
	/**
	 * Returns the aggroRange.
	 * @return
	 */
	public int getAggroRange() { return aggroRange; }
	/**
	 * Returns the attackRange.
	 * @return
	 */
	public int getAttackRange() { return attackRange; }
}
