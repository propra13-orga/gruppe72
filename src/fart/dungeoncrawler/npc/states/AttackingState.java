package fart.dungeoncrawler.npc.states;

import Utils.Vector2;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.BaseEnemy;

public class AttackingState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Attacking;

	public AttackingState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);
	}
	
	@Override
	public void activate() {
		owner.setState(DO_STATE);
		owner.setVelocity(Vector2.Zero);
		owner.setCurrentAnimation(owner.getSimpleAttack().getAnimation(owner.getHeading()));
		//TODO: Evaluate which attack...
	}

	@Override
	public void update(float elapsed) {
		if(owner.getSimpleAttack().Update())
			machine.setState(DynamicObjectState.Idle);
	}

	@Override
	public void exit() {
	}

	@Override
	public DynamicObjectState getDOState() {
		return DO_STATE;
	}

}
