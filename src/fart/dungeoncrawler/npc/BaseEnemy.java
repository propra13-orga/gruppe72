package fart.dungeoncrawler.npc;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Utils.TextureSplitter;
import Utils.Vector2;

import fart.dungeoncrawler.Animation;
import fart.dungeoncrawler.Attack;
import fart.dungeoncrawler.Collision;
import fart.dungeoncrawler.DynamicObjectManager;
import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.Health;
import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.enums.Heading;

public abstract class BaseEnemy extends BaseNPC implements IUpdateable {
	protected Animation curAnim;
	protected Health health;
	protected int aggroRange;
	protected int attackRange;
	protected Collision collision;
	protected HashMap<DynamicObjectState, HashMap<Heading, Animation>> animations;
	protected Attack simpleAttack;
	
	public BaseEnemy(EnemyDescription desc, Collision collision, DynamicObjectManager manager) {
		super(desc, manager);
		this.collision = collision;
		this.aggroRange = desc.getAggroRange();
		this.attackRange = desc.getAttackRange();
		this.health = desc.getHealth();
		
		animations = new HashMap<DynamicObjectState, HashMap<Heading, Animation>>();
	}

	protected void buildAnimations(BufferedImage spriteSheet) {
		BufferedImage[] bi = new BufferedImage[1];
		bi[0] = spriteSheet;
		//curAnim = new Animation(bi, 0);
		
		try {
			//Load SpriteSheet
			BufferedImage image = ImageIO.read(new File("res/enemy1.png"));
			
			//Split SpriteSheet, so that we can create the animations
			BufferedImage wd = TextureSplitter.splitTexture(image, image.getWidth(), 32, 0)[0];
			BufferedImage wl = TextureSplitter.splitTexture(image, image.getWidth(), 32, 1)[0];
			BufferedImage wr = TextureSplitter.splitTexture(image, image.getWidth(), 32, 2)[0];
			BufferedImage wu = TextureSplitter.splitTexture(image, image.getWidth(), 32, 3)[0];
			
			//Create WalkingAnimation
			int frameDuration = 150;
			Animation aWalkLeft = Animation.createWalkingAnimation(wl, frameDuration);
			Animation aWalkRight = Animation.createWalkingAnimation(wr, frameDuration);
			Animation aWalkDown = Animation.createWalkingAnimation(wd, frameDuration);
			Animation aWalkUp = Animation.createWalkingAnimation(wu, frameDuration);
			
			//Add to the List
			HashMap<Heading, Animation> walkAnim = new HashMap<Heading, Animation>();
			walkAnim.put(Heading.Left, aWalkLeft);
			walkAnim.put(Heading.Right, aWalkRight);
			walkAnim.put(Heading.Up, aWalkUp);
			walkAnim.put(Heading.Down, aWalkDown);
			
			animations.put(DynamicObjectState.Walking, walkAnim);
			animations.put(DynamicObjectState.Chasing, walkAnim);
			animations.put(DynamicObjectState.Fleeing, walkAnim);
			
			//Create Idle"Animation"
			BufferedImage[] iLeft = { aWalkLeft.getTextureByFrame(1) };
			BufferedImage[] iRight = { aWalkRight.getTextureByFrame(1) };
			BufferedImage[] iDown = { aWalkDown.getTextureByFrame(1) };
			BufferedImage[] iUp = { aWalkUp.getTextureByFrame(1) };
			
			HashMap<Heading, Animation> idleAnim = new HashMap<Heading, Animation>();
			idleAnim.put(Heading.Left, new Animation(iLeft, 1));
			idleAnim.put(Heading.Right, new Animation(iRight, 1));
			idleAnim.put(Heading.Down, new Animation(iDown, 1));
			idleAnim.put(Heading.Up, new Animation(iUp, 1));
			
			animations.put(DynamicObjectState.Idle, idleAnim);
		} catch (IOException e) {
			System.err.println("Could not load image.");
			e.printStackTrace();
		}
	}

	@Override
	public Rectangle getCollisionRect() {
		return collisionRect;
	}

	@Override
	public void terminate() {
		machine.setState(DynamicObjectState.Terminated);
		collision.removeDynamicObject(this);
	}
	
	//calculates the new velocity based on the current one to try to avoid obstacles. 
	protected Vector2 getAvoidanceVelocity(Vector2 dir) {
		Vector2 velocity = getVelocity();
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
	
	public EnemyDescription getDescription() {
		return null;
	}
	
	public void setHeading() {
		Vector2 velocity = getVelocity();
		Heading newHeading = heading;
		if(velocity.x > 0.01f)
			newHeading = Heading.Right;
		else if(velocity.x < -0.01f)
			newHeading = Heading.Left;
		else if(velocity.y > 0.01f)
			newHeading = Heading.Down;
		else if(velocity.y < -0.01f)
			newHeading = Heading.Up;
		
		if(heading != newHeading) {
			heading = newHeading;
			setCurrentAnimation(curState);
		}
	}
	
	public void setHeading(Heading heading) {
		this.heading = heading;
	}
	
	public Collision getCollisionDetector() {
		return collision;
	}
	
	public void setCurrentAnimation(DynamicObjectState state) {
		HashMap<Heading, Animation> map = animations.get(state);
		if(map == null)
			map = animations.get(DynamicObjectState.Idle);
		
		curAnim = map.get(heading);
	}
	
	public void setCurrentAnimation(Animation anim) {
		curAnim = anim;
	}
	
	public Attack getSimpleAttack() {
		return simpleAttack;
	}
	
	@Override
	public BufferedImage getTexture() {
		return curAnim.getTexture();
	}
	
	public int getAggroRange() {
		return aggroRange;
	}
	
	public int getAttackRange() {
		return attackRange;
	}
	
	@Override
	public void update(float elapsed) {
		curState = machine.getState();
		if(curState == DynamicObjectState.Terminated) {
			return;
		}
		curAnim.update(elapsed);
		
		if(curState == DynamicObjectState.Attacking) {
			manager.handleAttack(simpleAttack, ID);
		}
		
		if(!getVelocity().equals(new Vector2())) {
			screenPosition = screenPosition.add(getVelocity());
			collisionRect.x = (int)screenPosition.x;
			collisionRect.y = (int)screenPosition.y;
			
			//if the object is in front of a static object it stops it's movement. If it is hunting the player
			//it tries to avoid obstacles and moves further. offset is used to enlarge the collisionRect.
			int offset = 4;
			if(curState == DynamicObjectState.Alerted || curState == DynamicObjectState.Fleeing)
				offset = 0;
			if(collision.isCollidingStatic(this) || 
				collision.isNearTrigger(this, offset) || 
				collision.isCollidingDynamic(this)) {
				
				if(curState == DynamicObjectState.Alerted) {
					//reset simulated step
					screenPosition = screenPosition.sub(getVelocity());
					collisionRect.x = (int)screenPosition.x;
					collisionRect.y = (int)screenPosition.y;
					
					//calculate new velocity. direction will be changed. 
					GameObject player = machine.getPlayer();
					setVelocity(getAvoidanceVelocity(player.getPosition().sub(screenPosition)));
					
					//test again if movement is now possible
					screenPosition = screenPosition.add(getVelocity());
					collisionRect.x = (int)screenPosition.x;
					collisionRect.y = (int)screenPosition.y;
					
					if(collision.isCollidingStatic(this) || collision.isNearTrigger(this, offset)) {
						//it didn't work, so we reset again and wait for the next frame
						screenPosition = screenPosition.sub(getVelocity());
						collisionRect.x = (int)screenPosition.x;
						collisionRect.y = (int)screenPosition.y;
						
						return;
					}
				} else if(curState == DynamicObjectState.Fleeing) {
					//reset simulated step
					screenPosition = screenPosition.sub(getVelocity());
					collisionRect.x = (int)screenPosition.x;
					collisionRect.y = (int)screenPosition.y;
					
					//calculate new velocity. direction will be changed. 
					GameObject player = machine.getPlayer();
					setVelocity(getAvoidanceVelocity(player.getPosition().sub(screenPosition)));
					setVelocity(getVelocity().mul(-1));
					
					//test again if movement is now possible
					screenPosition = screenPosition.add(getVelocity());
					collisionRect.x = (int)screenPosition.x;
					collisionRect.y = (int)screenPosition.y;
					
					if(collision.isCollidingStatic(this) || collision.isNearTrigger(this, offset)) {
						//it didn't work, so we reset again and wait for the next frame
						screenPosition = screenPosition.sub(getVelocity());
						collisionRect.x = (int)screenPosition.x;
						collisionRect.y = (int)screenPosition.y;
						
						return;
					}
				} else {
					screenPosition = screenPosition.sub(getVelocity());
					collisionRect.x = (int)screenPosition.x;
					collisionRect.y = (int)screenPosition.y;
					machine.setState(DynamicObjectState.Idle);
				}
			}
			
			setHeading();
		}
		
		machine.update(elapsed);
	}

}
