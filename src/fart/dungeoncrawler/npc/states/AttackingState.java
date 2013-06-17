package fart.dungeoncrawler.npc.states;

import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.BaseEnemy;
import fart.dungeoncrawler.npc.BaseNPC;

public class AttackingState extends NPCState {

	public AttackingState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);
	}
	
	@Override
	public void activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float elapsed) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}

	@Override
	public DynamicObjectState getDOState() {
		// TODO Auto-generated method stub
		return null;
	}

}
