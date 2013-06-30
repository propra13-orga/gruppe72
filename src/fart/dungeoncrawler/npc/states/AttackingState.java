package fart.dungeoncrawler.npc.states;

import Utils.Vector2;
import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.AttackType;
import fart.dungeoncrawler.enums.DynamicObjectState;

public class AttackingState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Attacking;

	public AttackingState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);
	}
	
	@Override
	public void activate() {
		owner.setState(DO_STATE);
		owner.setVelocity(Vector2.Zero);
		if(owner.getAttackType() == AttackType.melee) {
			owner.setCurrentAnimation(owner.getSimpleAttack().getAnimation(owner.getHeading()));
			owner.getManager().registerAttack(owner.getSimpleAttack());
			owner.getSimpleAttack().activate();
		}
		else
			owner.setCurrentAnimation(DynamicObjectState.Idle);
	}

	@Override
	public void update(float elapsed) {
		if(owner.getSimpleAttack().update(elapsed))
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
