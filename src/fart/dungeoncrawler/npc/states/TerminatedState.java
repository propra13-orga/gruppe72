package fart.dungeoncrawler.npc.states;

import MathUtils.Vector2;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.BaseNPC;

public class TerminatedState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Terminated;
	
	public TerminatedState(EnemyStateMachine machine, BaseNPC owner) {
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
