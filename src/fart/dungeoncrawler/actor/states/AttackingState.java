package fart.dungeoncrawler.actor.states;

import Utils.Vector2;
import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.AttackType;
import fart.dungeoncrawler.enums.DynamicObjectState;

/**
 * This state is activated when the enemy starts attacking. It looks for the attackType and performs
 * the appropriate attack (melee or spell).
 * @author Felix
 *
 */
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
			owner.attack();
		}
		else {
			owner.setCurrentAnimation(DynamicObjectState.Idle);
			owner.spell();
		}
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
