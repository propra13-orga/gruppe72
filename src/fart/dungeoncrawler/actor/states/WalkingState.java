package fart.dungeoncrawler.actor.states;

import java.util.Random;

import Utils.Vector2;

import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;

public class WalkingState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Walking;
	private static final float SPEED = 0.66f;
	private Vector2 direction;
	private int distanceToTravel;
	
	//In this state, the object walks straight in one direction
	public WalkingState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);
		
		//lastMovement = System.currentTimeMillis();
	}
	
	@Override
	public void activate() {
		owner.setState(DO_STATE);
		Random random = new Random();
		
		double d = random.nextDouble();
		//The distance to walk is limited by TILE_SIZE so that we don't walk too far
		int distance = Tilemap.TILE_SIZE + random.nextInt((int)(Tilemap.TILE_SIZE * 0.75f));
		//System.out.println(distance);
		distanceToTravel = distance;
		Vector2 dir = new Vector2();
		if(d < 0.25) {
			dir = new Vector2(-1, 0);
		} else if (d < 0.5) {
			dir = new Vector2(1, 0);
		} else if (d < 0.75) {
			dir = new Vector2(0, -1);
		} else {
			dir = new Vector2(0, 1);
		}
		
		setDirection(dir);
	}
	
	public void setDirection(Vector2 dir) {
		direction = dir;
		direction = direction.mul(SPEED);
		owner.setVelocity(direction);
	}

	@Override
	public void update(float elapsed) {
		distanceToTravel--;

		if(distanceToTravel <= 0) {
			machine.setState(DynamicObjectState.Idle);
		}
	}

	@Override
	public void exit() {
		owner.setVelocity(new Vector2());
		
		/*if(Server.isOnline()) {
			Server.getInstance().broadcastMessage(new GamePositionMessage(owner));
		}*/
	}

	@Override
	public DynamicObjectState getDOState() {
		return DO_STATE;
	}
}
