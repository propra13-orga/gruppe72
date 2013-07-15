package fart.dungeoncrawler.enums;

/**
 * Describes the type of an NPC.
 * @author Felix
 *
 */
public enum NPCType {
	/**
	 * The player can talk to this NPC.
	 */
	Talking,
	/**
	 * The NPC opens up a shop.
	 */
	Shop,
	/**
	 * The NPC gives quests to the player.
	 */
	Quest,
	/**
	 * The NPC is a MeleeEnemy.
	 */
	MeleeEnemy,
	/**
	 * The NPC is a RangedEnemy.
	 */
	RangedEnemy
}
