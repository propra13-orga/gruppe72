package fart.dungeoncrawler.actor.states;

import Utils.Vector2;

import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;

/**
 * In this state the player is in the aggroRange of the enemy. It freezes for a small amount of time
 * and changes than to ChasingState.
 * @author Felix
 *
 */
public class AlertState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Alerted;
	private int freezeTime;
	
	public AlertState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);
	}
	
	@Override
	public void activate() {
		owner.setState(DO_STATE);
		freezeTime = 60;
	}

	@Override
	public void update(float elapsed) {
		if(freezeTime > 0) {
			//TODO: Set correct heading.
			owner.setVelocity(Vector2.Zero);
			freezeTime--;
			return;
		}
		
		owner.activateState(DynamicObjectState.Chasing);
	}

	@Override
	public void exit() {
		owner.setVelocity(Vector2.Zero);
	}

	@Override
	public DynamicObjectState getDOState() {
		return DO_STATE;
	}
}
