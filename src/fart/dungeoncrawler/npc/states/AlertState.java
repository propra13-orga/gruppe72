package fart.dungeoncrawler.npc.states;

import MathUtils.Vector2;

import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.BaseNPC;

public class AlertState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Alerted;
	private static final float SPEED = 0.75f;
	private GameObject goal;
	private int freezeTime;
	
	//In this state the player is in the aggrorange of this object. 
	//Object heads to the player to attack
	public AlertState(EnemyStateMachine machine, BaseNPC owner) {
		super(machine, owner);
	}
	
	public void setGoal(GameObject goal) {
		this.goal = goal;
	}
	
	@Override
	public void activate() {
		owner.setState(DO_STATE);
		freezeTime = 60;
	}

	@Override
	public void update(float elapsed) {
		if(freezeTime > 0) {
			owner.setVelocity(Vector2.Zero);
			freezeTime--;
			return;
		}
		Vector2 pos = owner.getPosition();
		Vector2 dir = goal.getPosition().sub(pos);
		dir.normalize();
		dir = dir.mul(SPEED);
		
		owner.setVelocity(dir);
	}

	@Override
	public void exit() {
		//this.goal = null;
		owner.setVelocity(Vector2.Zero);
	}

	@Override
	public DynamicObjectState getDOState() {
		return DO_STATE;
	}
}
