package fart.dungeoncrawler.actor.states;

import java.util.Random;

import Utils.Vector2;

import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;

/**
 * In this state the object is walking straight in one direction. On activation a random direction and
 * distance is chosen in which the actor will travel.
 * Generally this state is used to make enemies more interesting so that they do not stand around all the
 * time until the player is in range.
 * @author Felix
 *
 */
public class WalkingState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Walking;
	private static final float SPEED = 0.66f;
	private Vector2 direction;
	private int distanceToTravel;
	
	public WalkingState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);

	}
	
	@Override
	public void activate() {
		owner.setState(DO_STATE);
		Random random = new Random();
		
		double d = random.nextDouble();
		//The distance to walk is limited by TILE_SIZE so that we don't walk too far
		int distance = Tilemap.TILE_SIZE + random.nextInt((int)(Tilemap.TILE_SIZE * 0.75f));
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
	
	/**
	 * Sets the direction to travel in. 
	 * @param dir
	 */
	private void setDirection(Vector2 dir) {
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
	}

	@Override
	public DynamicObjectState getDOState() {
		return DO_STATE;
	}
}
