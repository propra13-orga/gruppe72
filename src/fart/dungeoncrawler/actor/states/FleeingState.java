package fart.dungeoncrawler.actor.states;

import Utils.Vector2;
import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;

/**
 * This state is activated when the belonging enemy has low HP and the player is near. It starts
 * fleeing. The direction is calculated in the update()-method. 
 * @author Felix
 *
 */
public class FleeingState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Fleeing;
	private static final float SPEED = 0.75f;
	private GameObject threat;
	
	public FleeingState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);
	}
	
	/**
	 * Sets the threat to flee from.
	 * @param threat
	 */
	public void setThreat(GameObject threat) {
		this.threat = threat;
	}
	
	@Override
	public void activate() {
		owner.setState(DO_STATE);
	}

	@Override
	public void update(float elapsed) {
		// TODO Auto-generated method stub
		Vector2 pos = owner.getPosition();
		Vector2 dir = pos.sub(threat.getPosition());
		dir.normalize();
		dir = dir.mul(SPEED);
		
		owner.setVelocity(dir);
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
