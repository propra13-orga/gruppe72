package fart.dungeoncrawler.actor.states;

import Utils.Vector2;
import fart.dungeoncrawler.Health;
import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;

/**
 * This state is activated when the enemy is hit. It stops the movement for a while and switches than
 * to AlertState. 
 * @author Felix
 *
 */
public class HitState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Hit;
	private int frameDuration;

	public HitState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);
	}
	
	@Override
	public void activate() {
		owner.setVelocity(new Vector2());
		frameDuration = 45;
	}

	@Override
	public void update(float elapsed) {
		frameDuration -= 1;
		if(frameDuration == 0) {
			machine.setState(DynamicObjectState.Alerted);
		}
	}

	@Override
	public void exit() {
		Health h = owner.getHealth();
		h.setInvul(false);
	}

	@Override
	public DynamicObjectState getDOState() {
		return DO_STATE;
	}

}
