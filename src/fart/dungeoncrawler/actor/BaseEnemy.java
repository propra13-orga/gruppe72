package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import Utils.*;

import fart.dungeoncrawler.*;
import fart.dungeoncrawler.enums.*;
import fart.dungeoncrawler.network.Server;
import fart.dungeoncrawler.network.messages.game.GamePositionMessage;

/**
 * BaseEnemy is an abstract base class for different kinds of enemies. It contains methods that all
 * enemytyps share. 
 * @author Felix
 *
 */
public abstract class BaseEnemy extends BaseNPC implements IUpdateable {
	protected Animation curAnim;
	protected int aggroRange;
	protected int attackRange;
	protected int spellRange;
	protected HashMap<DynamicObjectState, HashMap<Heading, Animation>> animations;
	protected Attack simpleAttack;
	
	protected Spell simpleSpell;
	protected AttackType curAttackType = AttackType.melee;
	protected EnemyDescription enemyDesc;
	protected float spSpeed = 4.0f;
	protected BufferedImage spTex;
	
	/**
	 * Creates a BaseEnemy from the EnemyDescription at the given position.
	 * @param game instance of the game running
	 * @param position position in screenspace
	 * @param enemyDesc the enemyDescription
	 */
	public BaseEnemy(Game game, Vector2 position, EnemyDescription enemyDesc) {
		super(game, position, (NPCDescription)enemyDesc);
		this.aggroRange = enemyDesc.getAggroRange();
		this.attackRange = enemyDesc.getAttackRange();
		this.enemyDesc = enemyDesc;
		this.collision = game.getCollision();
		
		animations = new HashMap<DynamicObjectState, HashMap<Heading, Animation>>();
		buildAnimations(enemyDesc.getSpriteSheet());
	}
	
	/**
	 * Creates a BaseEnemy from a CheckPointInfo.
	 * @param game instance of the game running
	 * @param info the checkpointinfo
	 */
	public BaseEnemy(Game game, CheckPointInfo info) {
		super(game, info.getPosition(), info.getNpcDesc());
		enemyDesc = info.getEnemyDesc();
		npcDesc = info.getNpcDesc();
		state = info.getState();
		velocity = Vector2.Zero;
		health = info.getHealth();
		mana = info.getMana();
		heading = info.getHeading();
		stats = info.getStats();
		
		animations = new HashMap<DynamicObjectState, HashMap<Heading, Animation>>();
		buildAnimations(enemyDesc.getSpriteSheet());
	}

	/**
	 * Sets up all animations from the spritesheet.
	 * @param spriteSheet
	 */
	protected void buildAnimations(BufferedImage spriteSheet) {
		BufferedImage[] bi = new BufferedImage[1];
		bi[0] = spriteSheet;
		
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
	public void terminate() {
		velocity = Vector2.Zero;
		machine.setState(DynamicObjectState.Terminated);
		collision.removeDynamicObject(this);
		manager.removeObject(this);
		
		Random r = new Random();
		if(r.nextFloat() > 0.5f) {
			int amount = r.nextInt(12);
			new GoldItem(game, screenPosition, amount);
		}
	}
	
	/**
	 * Calculates the new velocity based on the current one to try to avoid obstacles. 
	 * @param dir the direction
	 * @return
	 */
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
	
	/**
	 * Returns the EnemyDescription
	 * @return
	 */
	public EnemyDescription getDescription() {
		return enemyDesc;
	}
	
	/**
	 * Sets the heading based on the current velocity.
	 */
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
			setCurrentAnimation(state);
		}
	}
	
	/**
	 * Sets the corrent animation for a given DynamicObjectState
	 * @param state
	 */
	public void setCurrentAnimation(DynamicObjectState state) {
		HashMap<Heading, Animation> map = animations.get(state);
		if(map == null)
			map = animations.get(DynamicObjectState.Idle);
		
		curAnim = map.get(heading);
	}
	
	/**
	 * Sets a specific animation.
	 * @param anim
	 */
	public void setCurrentAnimation(Animation anim) {
		curAnim = anim;
	}
	
	/**
	 * Returns the simple attack off the enemy.
	 * @return
	 */
	public Attack getSimpleAttack() {
		return simpleAttack;
	}
	
	/**
	 * Returns the spell of this enemy.
	 * @return
	 */
	public Spell getSimpleSpell() {
		return simpleSpell;
	}
	
	@Override
	public BufferedImage getTexture() {
		return curAnim.getTexture();
	}
	
	/**
	 * Returns the aggroRange of this enemy.
	 * @return
	 */
	public int getAggroRange() {
		return aggroRange;
	}
	
	/**
	 * Sets the aggroRange of this enemy.
	 * @param newAggroRange
	 */
	public void setAggroRange(int newAggroRange) {
		this.aggroRange = newAggroRange;
	}
	
	/**
	 * Returns the attackRange of this enemy.
	 * @return
	 */
	public int getAttackRange() {
		return attackRange;
	}
	
	/**
	 * Returns the spellRange of this enemy. 
	 * @return
	 */
	public int getSpellRange() {
		return spellRange;
	}
	
	/**
	 * Sets the current attackType
	 * @param newAttackType
	 */
	public void setAttackType(AttackType newAttackType) {
		this.curAttackType = newAttackType;
	}
	
	/**
	 * Returns the current attackType
	 * @return
	 */
	public AttackType getAttackType() {
		return curAttackType;
	}
	
	/**
	 * Updates the logic (EnemyStateMachine)
	 * @param elapsed
	 */
	public void updateLogic(float elapsed) {
		machine.update(elapsed);
	}
	
	/**
	 * Tries to cast a spell. Checks cooldown and mana.
	 */
	public void spell() {
		if(!simpleSpell.isOnCooldown() && mana.getCurrentMana() >= simpleSpell.getManaCost()) {
			
			Vector2 spVelo = new Vector2();
			Vector2 startPos = new Vector2();
			if(heading == Heading.Right) {
				spVelo.x = spSpeed;
				startPos = new Vector2(collisionRect.x + collisionRect.width, collisionRect.y);
			}
			else if(heading == Heading.Left) {
				spVelo.x = -spSpeed;
				startPos = new Vector2(collisionRect.x - spTex.getWidth(), collisionRect.y);
			}
			else if(heading == Heading.Up) {
				spVelo.y = -spSpeed;
				startPos = new Vector2(collisionRect.x, collisionRect.y - spTex.getHeight());
			} else {
				spVelo.y = spSpeed;
				startPos = new Vector2(collisionRect.x, collisionRect.y + (float)collisionRect.getHeight());
			}
			
			mana.reduceMana(simpleSpell.getManaCost());
			simpleSpell.activate();
			manager.spawnSpell(simpleSpell.getProjectile(startPos, heading));
			manager.addSpellToUpdate(simpleSpell);
			System.out.println("Spell!");
		}
	}
	
	/**
	 * Performs a meleeAttack. 
	 */
	public void attack() {
		setCurrentAnimation(simpleAttack.getAnimation(heading));
		manager.registerAttack(simpleAttack);
		simpleAttack.activate();
	}
	
	@Override
	public void update(float elapsed) {
		state = machine.getState();
		Vector2 sVelo = new Vector2(getVelocity());

		if(state == DynamicObjectState.Terminated) {
			return;
		}
		curAnim.update(elapsed);
		
		if(state == DynamicObjectState.Attacking) {
			if(curAttackType == AttackType.melee) {
				return;
			}
		}
		
		if(!getVelocity().equals(new Vector2())) {
			screenPosition = screenPosition.add(getVelocity());
			collisionRect.x = (int)screenPosition.x;
			collisionRect.y = (int)screenPosition.y;
			
			//if the object is in front of a static object it stops it's movement. If it is hunting the player
			//it tries to avoid obstacles and moves further. offset is used to enlarge the collisionRect.
			int offset = 4;
			if(state == DynamicObjectState.Alerted || state == DynamicObjectState.Fleeing)
				offset = 0;
			if(collision.isCollidingStatic(this) || 
				collision.isNearTrigger(this, offset) || 
				collision.isCollidingDynamic(this)) {
				
				if(state == DynamicObjectState.Alerted) {
					//reset simulated step
					screenPosition = screenPosition.sub(getVelocity());
					collisionRect.x = (int)screenPosition.x;
					collisionRect.y = (int)screenPosition.y;
					
					//calculate new velocity. direction will be changed. 
					GameObject player = machine.getNearestPlayer();
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
				} else if(state == DynamicObjectState.Fleeing) {
					//reset simulated step
					screenPosition = screenPosition.sub(getVelocity());
					collisionRect.x = (int)screenPosition.x;
					collisionRect.y = (int)screenPosition.y;
					
					//calculate new velocity. direction will be changed. 
					GameObject player = machine.getNearestPlayer();
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
			
			if(Server.isOnline() && sVelo != getVelocity()) {
				Server.getInstance().broadcastMessage(new GamePositionMessage(this));
			}
		}
	}
}
