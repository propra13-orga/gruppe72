package fart.dungeoncrawler.npc.states;

import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.BaseEnemy;

public abstract class NPCState {
	protected EnemyStateMachine machine;
	protected BaseEnemy owner;
	
	public NPCState(EnemyStateMachine machine, BaseEnemy owner) {
		this.machine = machine;
		this.owner = owner;
	}
	
	public abstract void activate();
	public abstract void update(float elapsed);
	public abstract void exit();
	
	public abstract DynamicObjectState getDOState();
}
