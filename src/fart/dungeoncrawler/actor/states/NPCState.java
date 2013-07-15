package fart.dungeoncrawler.actor.states;

import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;

/**
 * This is the abstract base class for all enemy-states with abstract activate(), update() and exit() methods
 * in which the implementing class does all the logic. 
 * @author Felix
 *
 */
public abstract class NPCState {
	protected EnemyStateMachine machine;
	protected BaseEnemy owner;
	
	public NPCState(EnemyStateMachine machine, BaseEnemy owner) {
		this.machine = machine;
		this.owner = owner;
	}
	
	/**
	 * Activates and initializes the state.
	 */
	public abstract void activate();
	/**
	 * Updates the state/logic.
	 * @param elapsed
	 */
	public abstract void update(float elapsed);
	/**
	 * Exits the state.
	 */
	public abstract void exit();
	
	/**
	 * Returns the DynamicObjectState corresponding to this NPCState. 
	 * @return
	 */
	public abstract DynamicObjectState getDOState();
}
