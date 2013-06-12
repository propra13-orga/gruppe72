package fart.dungeoncrawler.npc.states;

import MathUtils.Vector2;
import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.BaseNPC;

public class FleeingState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Fleeing;
	private static final float SPEED = 0.75f;
	private GameObject threat;
	
	public FleeingState(EnemyStateMachine machine, BaseNPC owner) {
		super(machine, owner);
	}
	
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
