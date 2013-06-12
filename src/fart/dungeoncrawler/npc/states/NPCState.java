package fart.dungeoncrawler.npc.states;

import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.BaseNPC;

public abstract class NPCState {
	protected EnemyStateMachine machine;
	protected BaseNPC owner;
	
	public NPCState(EnemyStateMachine machine, BaseNPC owner) {
		this.machine = machine;
		this.owner = owner;
	}
	
	public abstract void activate();
	public abstract void update(float elapsed);
	public abstract void exit();
	
	public abstract DynamicObjectState getDOState();
}
