package fart.dungeoncrawler.actor.states;

import Utils.Vector2;
import fart.dungeoncrawler.Health;
import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;

public class HitState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Hit;
	private int frameDuration;

	//If an enemy is hit, it switches to hitState. While hit, enemy is invulnerable
	//Rebound not yet implemented.
	public HitState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);
	}
	
	@Override
	public void activate() {
		//Health h = owner.getHealth();
		//h.setInvul(true);
		//owner.getHealth().setInvul(true);
		owner.setVelocity(new Vector2());
		frameDuration = 45;
	}

	@Override
	public void update(float elapsed) {
		//rebound is not yet implemented
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
