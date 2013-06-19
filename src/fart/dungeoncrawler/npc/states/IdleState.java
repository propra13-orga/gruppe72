package fart.dungeoncrawler.npc.states;

import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.BaseEnemy;

public class IdleState extends NPCState {
	private static final DynamicObjectState doState = DynamicObjectState.Idle;
	
	//In this state the object is idle. Nothing happens here. 
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
