package fart.dungeoncrawler.enums;

/**
 * This enum describes the state of an actor and what it is currently doing.
 * Some are only used for NPCs or BaseEnemies.
 * @author Felix
 *
 */
public enum DynamicObjectState {
	/**
	 * The actor is idle and just stands around.
	 */
	Idle,
	/**
	 * The actor is walking.
	 */
	Walking,
	/**
	 * The actor is terminated. Terminated NPCs will not be drawn anymore.
	 */
	Terminated, 
	/**
	 * The actor (a BaseEnemy) is alerted because the player came in range.
	 */
	Alerted, 
	/**
	 * The actor (a BaseEnemy) is chasing the player.
	 */
	Chasing,
	/**
	 * The actor performs an attack.
	 */
	Attacking,
	/**
	 * The actor is hit. It can not move for a certain time.
	 */
	Hit,
	/**
	 * The actor (a BaseEnemy) has low HP and flees from the player.
	 */
	Fleeing
}
