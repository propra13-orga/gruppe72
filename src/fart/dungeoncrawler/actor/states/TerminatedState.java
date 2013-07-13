package fart.dungeoncrawler.actor.states;

import Utils.Vector2;
import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;

public class TerminatedState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Terminated;
	
	public TerminatedState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);
	}

	@Override
	public void activate() {
		owner.setVelocity(Vector2.Zero);
		owner.setState(DO_STATE);
	}

	@Override
	public void update(float elapsed) {	}

	@Override
	public void exit() { }

	@Override
	public DynamicObjectState getDOState() {
		return DO_STATE;
	}

}
