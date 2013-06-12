package fart.dungeoncrawler.npc.states;

import java.util.Random;

import MathUtils.Vector2;

import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.BaseNPC;

public class WalkingState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Walking;
	private static final float SPEED = 0.66f;
	private Vector2 direction;
	private int distanceToTravel;
	private long lastMovement;
	
	//In this state, the object walks straight in one direction
	public WalkingState(EnemyStateMachine machine, BaseNPC owner) {
		super(machine, owner);
		
		lastMovement = System.currentTimeMillis();
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
		
		//long timeSinceLastMoved = System.currentTimeMillis() - lastMovement;
		//System.out.println("Time since last movement: " + timeSinceLastMoved);
		//if(timeSinceLastMoved < 1500) {
		//	machine.setState(DynamicObjectState.Idle);
		//	System.out.println("Movement supressed.");
		//	return;
		//}
	}
	
	public void setDirection(Vector2 dir) {
		direction = dir;
		direction = direction.mul(SPEED);
	}

	@Override
	public void update(float elapsed) {
		distanceToTravel--;
		owner.setVelocity(direction);
		
		if(distanceToTravel <= 0) {
			machine.setState(DynamicObjectState.Idle);
			lastMovement = System.currentTimeMillis();
		}
	}

	@Override
	public void exit() {
		owner.setVelocity(new Vector2());
	}

	@Override
	public DynamicObjectState getDOState() {
		return DO_STATE;
	}
}
