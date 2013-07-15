package fart.dungeoncrawler.actor.states;

import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;

/**
 * An actor is in IdleState when nothing is happening. It just stands around. 
 * @author Felix
 *
 */
public class IdleState extends NPCState {
	private static final DynamicObjectState doState = DynamicObjectState.Idle;
	
	public IdleState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);
	}

	@Override
	public void activate() {
		owner.setState(doState);
	}

	@Override
	public void update(float elapsed) {}

	@Override
	public void exit() {}
	
	@Override
	public DynamicObjectState getDOState() {
		return doState;
	}
}
