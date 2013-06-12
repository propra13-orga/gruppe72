package fart.dungeoncrawler.npc;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import MathUtils.Vector2;

import fart.dungeoncrawler.Animation;
import fart.dungeoncrawler.Collision;
import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.enums.DynamicObjectState;

public class MeleeEnemy extends BaseEnemy {
	private DynamicObjectState curState;
	
	public MeleeEnemy(EnemyDescription desc, Collision collision) {
		super(desc, collision);
		curState = DynamicObjectState.Idle;
		
		buildAnimations(desc.getSpriteSheet());
	}
	
	private void buildAnimations(BufferedImage spriteSheet) {
		BufferedImage[] bi = new BufferedImage[1];
		bi[0] = spriteSheet;
		curAnim = new Animation(bi, 0);
	}

	@Override
	public Rectangle getCollisionRect() {
		return collisionRect;
	}

	@Override
	public void terminate() {
		machine.setState(DynamicObjectState.Terminated);
	}

	@Override
	public void update(float elapsed) {
		curState = machine.getState();
		if(curState == DynamicObjectState.Terminated) {
			return;
		}
		//if(curState == DynamicObjectState.Walking || curState == DynamicObjectState.Alerted) {
		if(!velocity.equals(new Vector2())) {
			screenPosition = screenPosition.add(velocity);
			collisionRect.x = (int)screenPosition.x;
			collisionRect.y = (int)screenPosition.y;
			
			//if the object is in front of a static object it stops it's movement. If it is hunting the player
			//it tries to avoid obstacles and moves further. offset is used to enlarge the collisionRect.
			int offset = 4;
			if(curState == DynamicObjectState.Alerted || curState == DynamicObjectState.Fleeing)
				offset = 0;
			if(collision.isCollidingStatic(this) || collision.isNearTrigger(this, offset)) {
				if(curState == DynamicObjectState.Alerted) {
					//reset simulated step
					screenPosition = screenPosition.sub(velocity);
					collisionRect.x = (int)screenPosition.x;
					collisionRect.y = (int)screenPosition.y;
					
					//calculate new velocity. direction will be changed. 
					GameObject player = machine.getPlayer();
					velocity = getAvoidanceVelocity(player.getPosition().sub(screenPosition));
					
					//test again if movement is now possible
					screenPosition = screenPosition.add(velocity);
					collisionRect.x = (int)screenPosition.x;
					collisionRect.y = (int)screenPosition.y;
					
					if(collision.isCollidingStatic(this) || collision.isNearTrigger(this, offset)) {
						//it didn't work, so we reset again and wait for the next frame
						screenPosition = screenPosition.sub(velocity);
						collisionRect.x = (int)screenPosition.x;
						collisionRect.y = (int)screenPosition.y;
						
						return;
					}
				} else if(curState == DynamicObjectState.Fleeing) {
					//reset simulated step
					screenPosition = screenPosition.sub(velocity);
					collisionRect.x = (int)screenPosition.x;
					collisionRect.y = (int)screenPosition.y;
					
					//calculate new velocity. direction will be changed. 
					GameObject player = machine.getPlayer();
					velocity = getAvoidanceVelocity(player.getPosition().sub(screenPosition));
					velocity = velocity.mul(-1);
					
					//test again if movement is now possible
					screenPosition = screenPosition.add(velocity);
					collisionRect.x = (int)screenPosition.x;
					collisionRect.y = (int)screenPosition.y;
					
					if(collision.isCollidingStatic(this) || collision.isNearTrigger(this, offset)) {
						//it didn't work, so we reset again and wait for the next frame
						screenPosition = screenPosition.sub(velocity);
						collisionRect.x = (int)screenPosition.x;
						collisionRect.y = (int)screenPosition.y;
						
						return;
					}
				} else {
					screenPosition = screenPosition.sub(velocity);
					collisionRect.x = (int)screenPosition.x;
					collisionRect.y = (int)screenPosition.y;
					machine.setState(DynamicObjectState.Idle);
				}
			}
		}
		
		machine.update(elapsed);
	}

	//calculates the new velocity based on the current one to try to avoid obstacles. 
	private Vector2 getAvoidanceVelocity(Vector2 dir) {
		float speed = velocity.length();
		Vector2 newVelocity;
		if(velocity.x > 0.01f || velocity.x < -0.01f) {
			newVelocity = new Vector2(0.0f, dir.y);
			newVelocity.normalize();
			newVelocity.mul(speed);
		} else {
			newVelocity = new Vector2(dir.x, 0.0f);
			newVelocity.normalize();
			newVelocity.mul(speed);
		}
		
		return newVelocity;
	}
}
