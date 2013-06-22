package fart.dungeoncrawler.npc.states;

import Utils.Vector2;
import fart.dungeoncrawler.Health;
import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;

public class HitState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Hit;
	private Vector2 reboundVelocity;
	private int frameDuration;

	//If an enemy is hit, it switches to hitState. While hit, enemy is invulnerable
	//Rebound not yet implemented.
	public HitState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);
		reboundVelocity = new Vector2();
	}
	
	@Override
	public void activate() {
		//Health h = owner.getHealth();
		//h.setInvul(true);
		owner.getHealth().setInvul(true);
		owner.setVelocity(new Vector2());
		frameDuration = 45;
	}
	
	public void setRebound(Vector2 v) {
		reboundVelocity = v;
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
		reboundVelocity = new Vector2();
		Health h = owner.getHealth();
		h.setInvul(false);
	}

	@Override
	public DynamicObjectState getDOState() {
		return DO_STATE;
	}

}
