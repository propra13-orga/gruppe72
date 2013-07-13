package fart.dungeoncrawler.actor.states;

import java.awt.Rectangle;

import Utils.Vector2;
import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.enums.Heading;

public class ChasingState extends NPCState {
	private static final DynamicObjectState DO_STATE = DynamicObjectState.Chasing;
	private GameObject goal;
	private static final float SPEED = 1.3f;

	public ChasingState(EnemyStateMachine machine, BaseEnemy owner) {
		super(machine, owner);
	}
	
	@Override
	public void activate() {

	}
	
	public void setGoal(GameObject goal) {
		this.goal = goal;
	}

	@Override
	public void update(float elapsed) {
		if(touchesThreat()) {
			owner.setVelocity(Vector2.Zero);
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

	}

	@Override
	public DynamicObjectState getDOState() {
		return DO_STATE;
	}
	
	private boolean touchesThreat() {
		Rectangle r1 = owner.getCollisionRect();
		Rectangle r2 = goal.getCollisionRect();
		
		if(r1.y + r1.height == r2.y ) {
			if((r2.x > r1.x && r2.x < r1.x + r1.width) ||
					(r2.x + r2.width > r1.x && r2.x + r2.width < r1.x + r1.width)) {
				owner.setHeading(Heading.Down);
				return true;
			}
		}
		else if(r1.y == r2.y + r2.height) {
			if((r2.x > r1.x && r2.x < r1.x + r1.width) ||
					(r2.x + r2.width > r1.x && r2.x + r2.width < r1.x + r1.width)) {
				owner.setHeading(Heading.Up);
				return true;
			}
		}
		else if(r1.x + r1.width == r2.x) {
			if((r2.y > r1.y && r2.y < r1.y + r1.height) ||
					(r2.y + r2.height > r1.y && r2.y + r2.height < r1.y + r1.height)) {
				owner.setHeading(Heading.Right);
				return true;
			}
		}
		else if(r1.x == r2.x + r2.width) {
			if((r2.y > r1.y && r2.y < r1.y + r1.height) ||
					(r2.y + r2.height > r1.y && r2.y + r2.height < r1.y + r1.height)) {
				owner.setHeading(Heading.Left);
				return true;
			}
		}
		
		return false;
	}

}
