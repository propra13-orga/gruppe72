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

public class NewPlayer extends Actor implements IUpdateable {
	private HashMap<Heading, Animation> walkAnim;
	private HashMap<Heading, Animation> idleAnim;
	private HashMap<Heading, Animation> simpleAttackAnim;
	private Attack simpleAttack;
	private Spell simpleSpell;
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
	private BufferedImage spTex;
	private float spSpeed = 4.0f;
	//private BufferedImage efTex;
	private boolean controllerActive;
	private PositionState posState;
	
	/**
	 * Represents the player.
	 * 
	 * @param Position Startposition in tiles
	 * @param game Current instance of the game
	 */
	public NewPlayer(Game game, ActorDescription desc, Vector2 position, boolean controllerActive) {
		super(game, desc, position);
		
		this.state = DynamicObjectState.Idle;
		this.velocity = new Vector2(0, 0);
		this.game = game;
		statusbar = new StatusBar(this);
		controller = game.getController();
		inventory.setGold(100);
		
		//TEST
		//level.addExperince(level.getExperienceForLevelUp() - 10);
		spellManager = new SpellManager(this);
		spellManager.addShields();

		initAnimations();
		
		//DEBUG
		buildSpell();
		skillTree = new SkillTree(this, controller, collision);
		this.controllerActive = controllerActive;
		isInNetwork = game.isInNetwork();
		posState = new PositionState(this);
	}
	
	public NewPlayer(Game game, ActorDescription desc, Vector2 position, boolean controllerActive, int actID) {
		super(game, desc, position);
		
		this.actorID = actID;
		
		this.state = DynamicObjectState.Idle;
		this.velocity = new Vector2(0, 0);
		this.game = game;
		statusbar = new StatusBar(this);
		controller = game.getController();
		inventory.setGold(100);
		
		//TEST
		//level.addExperince(level.getExperienceForLevelUp() - 10);
		spellManager = new SpellManager(this);
		spellManager.addShields();

		initAnimations();
		
		//DEBUG
		buildSpell();
		skillTree = new SkillTree(this, controller, collision);
		this.controllerActive = controllerActive;
		isInNetwork = game.isInNetwork();
		posState = new PositionState(this);
	}
	
	public void setInNetwork(boolean isInNetwork) {
		this.isInNetwork = isInNetwork;
	}
	
	@Override
	public void levelUp() {
		stats.addStamina(2);
		stats.addAgility(1);
		stats.addStrength(1);
		stats.addWill(1);
		
		health.addMaxHealth(2 * Stats.HEALTH_PER_STAM);
		mana.addMaxMana(1 * Stats.MANA_PER_WILL);
		health.fillHealth();
		mana.fillMana();
		
		spellManager.addShields();
	}
	
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
	
	public void setIdleAnim() {
		curAnim = idleAnim.get(heading);
	}

	private void buildSpell() {
		spTex = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)spTex.getGraphics();
		g2d.setColor(new Color(0, 0, 0, 0));
		g2d.fillRect(0, 0, 32, 32);
		g2d.setColor(Color.cyan);
		g2d.fillOval(8, 8, 16, 16);
		
		simpleSpell = new SpellFireBold(null, new SpellProjectile(this, spTex, collision));
		spellManager.addSpell(simpleSpell);
		
		BufferedImage spTex2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		g2d = (Graphics2D)spTex2.getGraphics();
		g2d.setColor(new Color(0, 0, 0, 0));
		g2d.fillRect(0, 0, 32, 32);
		g2d.setColor(Color.orange);
		g2d.fillOval(8, 8, 16, 16);
		
		Spell simpleSpell2 = new SpellWaterBold(null, new SpellProjectile(this, spTex2, collision));
		spellManager.addSpell(simpleSpell2);
	}
	
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
			/*if(controllerActive)
				NetworkManager.sendPositionMessage(this);*/
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
		
		/*if(isInNetwork && controllerActive) {
			sendPositionMessage();
		}*/
	}
	
	/**
	 * The player will perform a simple MeleeAttack. Mostly debugpurpose. 
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
	
	public void receivedAttackMsg() {
		state = DynamicObjectState.Attacking;
		curAnim = simpleAttack.getAnimation(heading);
		manager.registerAttack(simpleAttack);
		simpleAttack.activate();
	}
	
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
			startPos = new Vector2(collisionRect.x - spTex.getWidth(), collisionRect.y);
		}
		else if(heading == Heading.Up) {
			spVelo.y = -spSpeed;
			startPos = new Vector2(collisionRect.x, collisionRect.y - spTex.getHeight());
		} else {
			spVelo.y = spSpeed;
			startPos = new Vector2(collisionRect.x, collisionRect.y + (float)collisionRect.getHeight());
		}

		Spell curSpell = spellManager.getSpell(index);
		if(!curSpell.isOnCooldown() && mana.getCurrentMana() >= curSpell.getManaCost()) {
			mana.reduceMana(curSpell.getManaCost());
			manager.spawnSpell(this, curSpell.getProjectile(startPos, heading), curSpell);
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
		
		/*if(isInNetwork && controllerActive)
			sendPositionMessage();*/
	}
	
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
		if(controllerActive) {
			statusbar.draw(graphics);
			spellManager.draw(graphics);
		}
		
		if(controller.isPressed(KeyEvent.VK_P))
			skillTree.draw(graphics);
		if(isInNetwork && controllerActive) {
			if(controller.isPressed(KeyEvent.VK_T))
				DeathMatchStatistics.getInstance().draw(graphics);
		}
	}

	@Override
	protected BufferedImage getTexture() {
		return curAnim.getTexture();
	}
	
	private void sendPositionMessage() {
		if(posState.equals(this))
			return;
		
		posState.update(this);
		NetworkManager.sendPositionMessage(this);
	}
	
	class PositionState {
		//public int posX;
		//public int posY;
		public int veloX;
		public int veloY;
		
		public PositionState(NewPlayer p) {
			//posX = (int)p.screenPosition.x;
			//posY = (int)p.screenPosition.y;
			veloX = (int)p.getVelocity().x;
			veloY = (int)p.getVelocity().y;
		}
		
		public void update(NewPlayer p) {
			//posX = (int)p.screenPosition.x;
			//posY = (int)p.screenPosition.y;
			veloX = (int)p.getVelocity().x;
			veloY = (int)p.getVelocity().y;
		}
		
		public boolean equals(NewPlayer p) {
			return 
				//posX == (int)p.screenPosition.x &&
				//posY == (int)p.screenPosition.y &&
				veloX == (int)p.getVelocity().x &&
				veloY == (int)p.getVelocity().y;
		}
	}
}
