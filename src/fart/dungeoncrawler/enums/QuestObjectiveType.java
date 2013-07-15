package fart.dungeoncrawler.enums;

/**
 * Describes the type of a quest-objectives. Every quest contains a list of objectives that have to
 * be fulfilled to finish the quest.
 * @author Felix
 *
 */
public enum QuestObjectiveType {
	/**
	 * Player has to kill a number of enemies.
	 */
	Kill,
	/**
	 * Player has to reach a specific map.
	 */
	Reach,
	/**
	 * Player has to kill a boss on a specific map.
	 */
	KillBoss,
	/**
	 * Player has to collect specific items.
	 */
	Collect
}
