package fart.dungeoncrawler.npc.states;

import java.util.HashMap;
import java.util.Random;

import Utils.Vector2;

import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.Player;
import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.BaseEnemy;
import fart.dungeoncrawler.npc.MeleeEnemy;

public class EnemyStateMachine implements IUpdateable {
	private HashMap<DynamicObjectState, NPCState> states;
	private NPCState curState;
	private BaseEnemy owner;
	private Player player;
	private Random random;
	
	public EnemyStateMachine(BaseEnemy owner, Player player) {
		this.owner = owner;
		this.player = player;
		
		random = new Random();
		
		initStates();
	}
	
	public GameObject getPlayer() {
		return player;
	}
	
	private void initStates() {
		states = new HashMap<DynamicObjectState, NPCState>();
		
		states.put(DynamicObjectState.Idle, new IdleState(this, owner));
		states.put(DynamicObjectState.Walking, new WalkingState(this, owner));
		states.put(DynamicObjectState.Alerted, new AlertState(this, owner));
		states.put(DynamicObjectState.Terminated, new TerminatedState(this, owner));
		states.put(DynamicObjectState.Fleeing, new FleeingState(this, owner));
		
		curState = states.get(DynamicObjectState.Idle);
	}
	
	@Override
	public void update(float elapsed) {
		DynamicObjectState doState = owner.getState();
		
		//Check if the current state needs to be switched
		//Idle - we check if player is in range or randomly start walking
		if(doState == DynamicObjectState.Idle) {
			if(checkPlayerInAggroRange())
				return;
			
			double d = random.nextDouble();
			if(d < 0.0075)
				setState(DynamicObjectState.Walking);
		}
		//Walking - we check if player is in range
		if(doState == DynamicObjectState.Walking) {
			if(checkPlayerInAggroRange())
				return;
		}
		//Alerted - player is in range, so we start heading towards him. If our 
		//health is below a certain threshold we start fleeing
		if(doState == DynamicObjectState.Alerted) {
			Vector2 dirToPlayer = player.getPosition().sub(owner.getPosition());
			float distanceToPlayer = dirToPlayer.length();
			
			if(owner.getHealth().lowerThan(0.15f)) {
				//HP is lower than 15%, we start to flee
				setState(DynamicObjectState.Fleeing);
				((FleeingState)curState).setThreat(player);
			} else if(owner.getAttackRange() <= distanceToPlayer) {
				//Change to Attacking. Careful, add delays, otherwise enemies are too powerful...
			}
			
		}
		//Attacking
		if(doState == DynamicObjectState.Attacking) {
			
		}
		//Is Hit
		if(doState == DynamicObjectState.Hit) {
			
		}
		//Flees
		if(doState == DynamicObjectState.Fleeing) {
			
		}
		if(doState == DynamicObjectState.Terminated) {
			return;
		}
		
		curState.update(elapsed);
	}
	
	public void setState(DynamicObjectState state) {
		if(!states.containsKey(state))
			return;
		
		curState.exit();
		curState = states.get(state);
		curState.activate();
		
		owner.setState(state);
		owner.setCurrentAnimation(state);
	}
	
	public DynamicObjectState getState() {
		return curState.getDOState();
	}
	
	public boolean isPlayerInAggroRange() {
		Vector2 dirToPlayer = player.getPosition().sub(owner.getPosition());
		float distanceToPlayer = dirToPlayer.length();
		
		return distanceToPlayer < owner.getAggroRange();
	}
	
	public boolean checkPlayerInAggroRange() {
		Vector2 dirToPlayer = player.getPosition().sub(owner.getPosition());
		float distanceToPlayer = dirToPlayer.length();
		
		if(distanceToPlayer < owner.getAggroRange()) {
			setState(DynamicObjectState.Alerted);
			((AlertState)curState).setGoal(player);
			
			return true;
		}
		
		return false;
	}
}
