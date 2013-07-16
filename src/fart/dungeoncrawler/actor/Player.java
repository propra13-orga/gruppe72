package fart.dungeoncrawler.actor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Utils.Vector2;

import fart.dungeoncrawler.*;
import fart.dungeoncrawler.enums.*;
import fart.dungeoncrawler.network.DeathMatchStatistics;
import fart.dungeoncrawler.network.NetworkManager;

/**
 * This class represents an actos that can be controlled by a player through the keyboard.
 * @author Felix
 *
 */
public class Player extends Actor implements IUpdateable {
	private HashMap<Heading, Animation> walkAnim;
	private HashMap<Heading, Animation> idleAnim;
	private HashMap<Heading, Animation> simpleAttackAnim;
	private Attack simpleAttack;
	private Animation curAnim;
	
	private Controller controller;
	private Game game;
	
	private boolean supressEnemyCollision = false;
	public StatusBar statusbar;
	
	private SpellManager spellManager;
	private SkillTree skillTree;
	
	//DEBUG
	private int maxHitDuration = 15;
	private int curHitDuration;
	private float spSpeed = 4.0f;
	private boolean controllerActive;
	private boolean controlled;
	private PositionState posState;
	private QuestLog questLog;
	private StatsMenu statsMenu;
	
	/**
	 * Creates an instace of the player from an actorDescription.
	 * @param game instance of the game running
	 * @param desc the ActorDescription
	 * @param position the position in screenspace
	 * @param controllerActive indicates if the player should be controlled by the keyboard. 
	 */
	public Player(Game game, ActorDescription desc, Vector2 position, boolean controllerActive) {
		super(game, desc, position);
		
		this.state = DynamicObjectState.Idle;
		this.velocity = new Vector2(0, 0);
		this.game = game;
		
		controller = game.getController();
		statsMenu = new StatsMenu(stats, controller, this);
		questLog = new QuestLog();
		statusbar = new StatusBar(this, game);
		inventory.setGold(25);
		
		//TEST
		spellManager = new SpellManager(this);
		spellManager.addShields();

		initAnimations();
		
		//DEBUG
		skillTree = new SkillTree(this, controller, collision);
		buildSpell();
		this.controllerActive = controllerActive;
		controlled = controllerActive;
		isInNetwork = game.isInNetwork();
		posState = new PositionState(this);
	}
	
	/**
	 * Creates an instace of the player from an actorDescription.
	 * @param game instance of the game running
	 * @param desc the ActorDescription
	 * @param position the position in screenspace
	 * @param controllerActive indicates if the player should be controlled by the keyboard
	 * @param actID the actorID to be given. This is only done in network-games
	 */
	public Player(Game game, ActorDescription desc, Vector2 position, boolean controllerActive, int actID) {
		super(game, desc, position);
		
		this.actorID = actID;
		
		this.state = DynamicObjectState.Idle;
		this.velocity = new Vector2(0, 0);
		this.game = game;
		
		controller = game.getController();
		statsMenu = new StatsMenu(stats, controller, this);
		statusbar = new StatusBar(this, game);
		inventory.setGold(25);
		
		//TEST
		spellManager = new SpellManager(this);
		spellManager.addShields();

		initAnimations();
		
		//DEBUG
		skillTree = new SkillTree(this, controller, collision);
		buildSpell();
		this.controllerActive = controllerActive;
		controlled = controllerActive;
		isInNetwork = game.isInNetwork();
		posState = new PositionState(this);
	}
	
	/**
	 * Sets a flag of the game is running in the network.
	 * @param isInNetwork
	 */
	public void setInNetwork(boolean isInNetwork) {
		this.isInNetwork = isInNetwork;
	}
	
	/**
	 * Returns the spellmanager.
	 * @return
	 */
	public SpellManager getSpellManager() {
		return spellManager;
	}
	
	@Override
	public void levelUp() {
		health.fillHealth();
		mana.fillMana();
		statsMenu.leveledUp();
		spellManager.addShields();
	}
	
	/**
	 * Updates MaxHealth and MaxMana after a change in stats. 
	 */
	public void renewHealthMana() {
		health.setMaxHealh(stats.getStamina() * Stats.HEALTH_PER_STAM);
		mana.setMaxMana(stats.getWill() * Stats.MANA_PER_WILL);
		spellManager.addShields();
	}
	
	/**
	 * Returns the QuestLog of the player. 
	 * @return
	 */
	public QuestLog getQuestLog() {
		return questLog;
	}
	
	/**
	 * Resets the player to a checkpoint with the given CheckPointInfo
	 * @param info
	 */
	public void resetCheckpoint(CheckPointInfo info) {
		state = info.getState();
		velocity = Vector2.Zero;
		health = info.getHealth();
		mana = info.getMana();
		heading = info.getHeading();
		stats = info.getStats();
		screenPosition = info.getPosition();
		collisionRect = info.getRectangle();
		statusbar.setHealth(health);
		statusbar.setMana(mana);
	}
	
	/**
	 * Sets the currentAnimation to the idleAnimation. 
	 */
	public void setIdleAnim() {
		curAnim = idleAnim.get(heading);
	}

	/**
	 * Creates all spells.
	 */
	private void buildSpell() {
		spellManager.addSpell(skillTree.getFireSkills().get(0).getSpell());
		spellManager.addSpell(skillTree.getWaterSkills().get(0).getSpell());
		spellManager.addSpell(skillTree.getEarthSkills().get(0).getSpell());
	}
	
	/**
	 * Initializes all animations for the player. 
	 */
	private void initAnimations() {
		//Setup Animations
				try {
					BufferedImage wl, wr, wu, wd;
					wl = ImageIO.read(new File("res/plWleft.png"));
					wr = ImageIO.read(new File("res/plWright.png"));
					wd = ImageIO.read(new File("res/plWdown.png"));
					wu = ImageIO.read(new File("res/plWup.png"));
					
					int frameDuration = 150;
					Animation aWalkLeft = Animation.createWalkingAnimation(wl, frameDuration);
					Animation aWalkRight = Animation.createWalkingAnimation(wr, frameDuration);
					Animation aWalkDown = Animation.createWalkingAnimation(wd, frameDuration);
					Animation aWalkUp = Animation.createWalkingAnimation(wu, frameDuration);
					
					walkAnim = new HashMap<Heading, Animation>();
					walkAnim.put(Heading.Left, aWalkLeft);
					walkAnim.put(Heading.Right, aWalkRight);
					walkAnim.put(Heading.Down, aWalkDown);
					walkAnim.put(Heading.Up, aWalkUp);
					
					BufferedImage[] iLeft = { aWalkLeft.getTextureByFrame(1) };
					BufferedImage[] iRight = { aWalkRight.getTextureByFrame(1) };
					BufferedImage[] iDown = { aWalkDown.getTextureByFrame(1) };
					BufferedImage[] iUp = { aWalkUp.getTextureByFrame(1) };
					
					idleAnim = new HashMap<Heading, Animation>();
					idleAnim.put(Heading.Left, new Animation(iLeft, 1));
					idleAnim.put(Heading.Right, new Animation(iRight, 1));
					idleAnim.put(Heading.Down, new Animation(iDown, 1));
					idleAnim.put(Heading.Up, new Animation(iUp, 1));
					
					//Only for debugging.
					simpleAttackAnim = new HashMap<Heading, Animation>();
					BufferedImage[] iSALeft = new BufferedImage[1];
					iSALeft[0] = new BufferedImage(32, 32, iLeft[0].getType());
					Graphics2D g2 = (Graphics2D)iSALeft[0].getGraphics();
					g2.setColor(new Color(0.0f, 0.0f, 1.0f));
					g2.fillRect(0, 0, 32, 32);
					simpleAttackAnim.put(Heading.Left, new Animation(iSALeft, 1));
					
					BufferedImage[] iSARight = new BufferedImage[1];
					iSARight[0] = new BufferedImage(32, 32, iLeft[0].getType());
					g2 = (Graphics2D)iSARight[0].getGraphics();
					g2.setColor(new Color(1.0f, 0.0f, 0.0f));
					g2.fillRect(0, 0, 32, 32);
					simpleAttackAnim.put(Heading.Right, new Animation(iSARight, 1));
					
					BufferedImage[] iSAUp = new BufferedImage[1];
					iSAUp[0] = new BufferedImage(32, 32, iLeft[0].getType());
					g2 = (Graphics2D)iSAUp[0].getGraphics();
					g2.setColor(new Color(0.0f, 1.0f, 0.0f));
					g2.fillRect(0, 0, 32, 32);
					simpleAttackAnim.put(Heading.Up, new Animation(iSAUp, 1));
					
					BufferedImage[] iSADown = new BufferedImage[1];
					iSADown[0] = new BufferedImage(32, 32, iLeft[0].getType());
					g2 = (Graphics2D)iSADown[0].getGraphics();
					g2.setColor(new Color(1.0f, 0.0f, 1.0f));
					g2.fillRect(0, 0, 32, 32);
					simpleAttackAnim.put(Heading.Down, new Animation(iSADown, 1));
					HashMap<Integer, Rectangle> atRects = new HashMap<Integer, Rectangle>();
					atRects.put(0, new Rectangle(-16, 0, 16, 32));
					int frameDur = 44;
					simpleAttack = new Attack(15, simpleAttackAnim, atRects, frameDur, this);
					
					curAnim = idleAnim.get(heading);
				} catch (IOException e) {
					System.err.print("Couldn't load Players texture!");
					System.exit(1);
				}
	}
	
	/**
	 * Sets the animation for a given object state. 
	 * @param state
	 */
	public void setAnimation(DynamicObjectState state) {
		if(state == DynamicObjectState.Idle)
			setIdleAnim();
		if(state == DynamicObjectState.Walking)
			curAnim = walkAnim.get(heading);
	}
	
	@Override
	public void terminate() {
		if(!isInNetwork) {
			System.out.println("Player is dead!");
			state = DynamicObjectState.Terminated;
			game.playerDead();
		} else {
			game.playerDeadInNetwork(actorID);
		}
	}
	
	/**
	 * Moves the player in the given direction. Can only move in one direction at a time.
	 * @param direction Direction to move. 
	 */
	private void move(Heading direction) {
		if(state == DynamicObjectState.Terminated)
			return;
		if(state == DynamicObjectState.Walking)
		{
			if(heading == direction)
				return;
		}
		
		heading = direction;
		curAnim = walkAnim.get(heading);
		state = DynamicObjectState.Walking;
		
		switch(heading) {
		case Left:
			velocity.x = -1;
			velocity.y = 0;
			break;
		case Right:
			velocity.x = +1;
			velocity.y = 0;
			break;
		case Up:
			velocity.x = 0;
			velocity.y = -1;
			break;
		case Down:
			velocity.x = 0;
			velocity.y = +1;
			break;
		}
	}
	
	/**
	 * The player will perform a simple MeleeAttack.
	 */
	public void simpleAttack() {
		if(state == DynamicObjectState.Attacking)
			return;
		
		state = DynamicObjectState.Attacking;
		
		if(!isInNetwork) {
			curAnim = simpleAttack.getAnimation(heading);
			manager.registerAttack(simpleAttack);
			simpleAttack.activate();
		} else {
			NetworkManager.sendAttackMessage(this);
		}
	}
	
	/**
	 * The player received an attackMessage in a network game. This method sets the AttackingState
	 * and initializes the attack. 
	 */
	public void receivedAttackMsg() {
		state = DynamicObjectState.Attacking;
		curAnim = simpleAttack.getAnimation(heading);
		manager.registerAttack(simpleAttack);
		simpleAttack.activate();
	}
	
	/**
	 * Tries to cast the spell with the given index. Checks cooldown and manacosts. 
	 * @param index
	 */
	public void spellAttack(int index) {
		if(state == DynamicObjectState.Attacking)
			return;
		
		curAnim = idleAnim.get(heading);
		Vector2 spVelo = new Vector2();
		Vector2 startPos = new Vector2();
		if(heading == Heading.Right) {
			spVelo.x = spSpeed;
			startPos = new Vector2(collisionRect.x + collisionRect.width, collisionRect.y);
		}
		else if(heading == Heading.Left) {
			spVelo.x = -spSpeed;
			startPos = new Vector2(collisionRect.x - 32, collisionRect.y);
		}
		else if(heading == Heading.Up) {
			spVelo.y = -spSpeed;
			startPos = new Vector2(collisionRect.x, collisionRect.y - 32);
		} else {
			spVelo.y = spSpeed;
			startPos = new Vector2(collisionRect.x, collisionRect.y + (float)collisionRect.getHeight());
		}

		Spell curSpell = spellManager.getSpell(index);
		if(!curSpell.isOnCooldown() && mana.getCurrentMana() >= curSpell.getManaCost()) {
			mana.reduceMana(curSpell.getManaCost());
			manager.spawnSpell(curSpell.getProjectile(startPos, heading));
			spellManager.activate(index);
			if(isInNetwork && controllerActive) {
				NetworkManager.sendSpellMessage(this, index);
			}
		}
	}
	
	/**
	 * Stops the current movement and sets IdleState.
	 */
	private void stopMovement() {
		this.velocity.x = 0;
		this.velocity.y = 0;
		this.curAnim = idleAnim.get(heading);
		state = DynamicObjectState.Idle;
	}
	
	/**
	 * Handles the keyboard input. 
	 * @param elapsed time elapsed since the last frame
	 */
	private void handleControllerInput(float elapsed) {
		if(controllerActive) {
			if(controller.justPressed(KeyEvent.VK_A)) {
				simpleAttack();
			}
			if(state == DynamicObjectState.Attacking) {
				curAnim.update(elapsed);
				return;
			}
			else if(controller.justPressed(KeyEvent.VK_S))
				spellAttack(0);
			else if(controller.justPressed(KeyEvent.VK_D))
				spellAttack(1);
			else if(controller.justPressed(KeyEvent.VK_F))
				spellAttack(2);
			else if(controller.justPressed(KeyEvent.VK_5))
				spellManager.activateShield(ElementType.Fire);
			else if(controller.justPressed(KeyEvent.VK_6))
				spellManager.activateShield(ElementType.Water);
			else if(controller.justPressed(KeyEvent.VK_7))
				spellManager.activateShield(ElementType.Earth);
			else if(controller.isDownPressed())
				move(Heading.Down);
			else if(controller.isUpPressed())
				move(Heading.Up);
			else if(controller.isLeftPressed())
				move(Heading.Left);
			else if(controller.isRightPressed())
				move(Heading.Right);
			else if(controller.justReleased(KeyEvent.VK_LEFT) || 
					controller.justReleased(KeyEvent.VK_RIGHT) || 
					controller.justReleased(KeyEvent.VK_UP) || 
					controller.justReleased(KeyEvent.VK_DOWN))
				stopMovement();
			else if(controller.justPressed(KeyEvent.VK_ENTER))
				collision.checkOnKeyTriggers(this);
		}
	}
	
	/**
	 * Returns the statsMenu. 
	 * @return
	 */
	public StatsMenu getStatsMenu() {
		return statsMenu;
	}

	@Override
	public void update(float elapsed) {
		if(!isInNetwork && health.isDead()) {
			terminate();
			return;
		}
		
		regenerate();
		spellManager.update(elapsed);

		ElementalShield curShield = spellManager.getCurrentShield();
		if(curShield != null && curShield.getElementType() == ElementType.Fire)
			manager.handleAreaOfEffectSpell(this, curShield.getDamage(), ElementType.Fire, curShield.getAOErect());
		
		if(state == DynamicObjectState.Attacking) {
			curAnim.update(elapsed);
			return;
		}
		
		if(state == DynamicObjectState.Hit) {
			curAnim.update(elapsed);
			curHitDuration -= 1;
			if(curHitDuration <= 0) {
				setState(DynamicObjectState.Idle);
				curHitDuration = maxHitDuration;
				health.setInvul(false);
			}
			
			return;
		}
		
		if(controllerActive)
			handleControllerInput(elapsed);
		
		//DEBUG PURPOSE
		if(controllerActive) {
			if(controller.isPressed(KeyEvent.VK_SHIFT))
				supressEnemyCollision = true;
			else
				supressEnemyCollision = false;
		}
		//-------------
		
		if(velocity.x > 0.01f || velocity.x < -0.01f || velocity.y > 0.01f || velocity.y < -0.01f) {
			collisionRect.x += velocity.x;
			collisionRect.y += velocity.y;
			
			boolean collidingStatic = collision.isCollidingStatic(this);
			boolean collidingDynamic = false;
			if(!supressEnemyCollision)
				collidingDynamic = collision.isCollidingDynamic(this);
			
			if(collidingStatic || collidingDynamic) {
				collisionRect.x -= velocity.x;
				collisionRect.y -= velocity.y;
				stopMovement();
			} else {
				collision.checkTriggers(this);
				screenPosition.x += velocity.x;
				screenPosition.y += velocity.y;
				curAnim.update(elapsed);
			}
		}
		
		if(isInNetwork && controllerActive) {
			sendPositionMessage();
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void draw(Graphics2D graphics) {
		ElementalShield cs = spellManager.getCurrentShield();
		if(cs != null) {
			Color[] c = new Color[3];
			c[0] = new Color(0, 0, 0, 0);
			c[1] = new Color(0, 0, 0, 0);
			ElementType type = cs.getElementType();
			
			switch(type) {
			case Earth:
				c[2] = new Color(0.45f, 0.4f, 0.15f, 0.85f);
				break;
			case Fire:
				c[2] = new Color(1.0f, 0.0f, 0.0f, 0.7f);
				break;
			case Water:
				c[2] = new Color(0.3f, 0.3f, 0.8f, 0.7f);
				break;
			}
			
			float[] dist = { 0.0f, 0.25f, 1f };
			RadialGradientPaint rgp = new RadialGradientPaint(new Point2D.Float((int)screenPosition.x + 16, (int)screenPosition.y + 16), 20, dist, c);
			graphics.setPaint(rgp);
			graphics.fillOval((int)screenPosition.x - 4, (int)screenPosition.y - 4, 40, 40);
		}
		
		graphics.drawImage(getTexture(), (int)screenPosition.x, (int)screenPosition.y, null);
		if(controlled) {
			spellManager.draw(graphics);
			statusbar.draw(graphics);
		}
		
		if(isInNetwork && controllerActive) {
			if(controller.isPressed(KeyEvent.VK_T))
				DeathMatchStatistics.getInstance().draw(graphics);
		}
	}

	@Override
	protected BufferedImage getTexture() {
		return curAnim.getTexture();
	}
	
	/**
	 * Sets the controllerActive-flag.
	 * @param b
	 */
	public void setControllerActive(boolean b) {
		controllerActive = b;
	}
	
	/**
	 * Sends a PositionMessage in a networkgame if needed. 
	 */
	private void sendPositionMessage() {
		if(posState.equals(this))
			return;
		
		posState.update(this);
		NetworkManager.sendPositionMessage(this);
	}
	
	/**
	 * The PositionState is used in networkgames to check if a position update should be sent to the server. 
	 * @author Felix
	 *
	 */
	class PositionState {
		public int veloX;
		public int veloY;
		
		public PositionState(Player p) {
			veloX = (int)p.getVelocity().x;
			veloY = (int)p.getVelocity().y;
		}
		
		public void update(Player p) {
			veloX = (int)p.getVelocity().x;
			veloY = (int)p.getVelocity().y;
		}
		
		public boolean equals(Player p) {
			return 
				veloX == (int)p.getVelocity().x &&
				veloY == (int)p.getVelocity().y;
		}
	}
}
