package fart.dungeoncrawler.npc.states;

import Utils.Vector2;

import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;

public class AlertState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Alerted;
	private GameObject goal;
	private int freezeTime;
	
	//In this state the player is in the aggrorange of this object. 
	//Object heads to the player to attack
	public AlertState(EnemyStateMachine machine, BaseEnemy owner) {
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
			//TODO: Set correct heading.
			owner.setVelocity(Vector2.Zero);
			freezeTime--;
			return;
		}
		
		owner.activateState(DynamicObjectState.Chasing);
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
