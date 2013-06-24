package fart.dungeoncrawler.actor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Utils.Vector2;

import fart.dungeoncrawler.*;
import fart.dungeoncrawler.enums.*;

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
	
	//DEBUG
	private int maxHitDuration = 40;
	private int curHitDuration;
	private BufferedImage spTex;
	private float spSpeed = 4.0f;
	
	/**
	 * Represents the player.
	 * 
	 * @param Position Startposition in tiles
	 * @param game Current instance of the game
	 */
	public NewPlayer(Game game, ActorDescription desc, Vector2 position) {
		super(game, desc, position);
		
		this.state = DynamicObjectState.Idle;
		this.velocity = new Vector2(0, 0);
		this.game = game;
		statusbar = new StatusBar(this);
		controller = game.getController();
		inventory.setGold(100);

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
			atRects.put(0, new Rectangle(-16, -16, 16, 16));
			int frameDur = 30;
			simpleAttack = new Attack(15, simpleAttackAnim, atRects, frameDur, this);
			
			curAnim = idleAnim.get(heading);
		} catch (IOException e) {
			System.err.print("Couldn't load Players texture!");
			System.exit(1);
		}
		
		//DEBUG
		System.out.println("Hold SHIFT to avoid player collision.");
		buildSpell();
		//
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

	private void buildSpell() {
		spTex = new BufferedImage(32, 32, ColorSpace.TYPE_RGB);
		Graphics2D g2d = (Graphics2D)spTex.getGraphics();
		g2d.setColor(new Color(0.6f, 0.1f, 0.8f));
		g2d.fillOval(8, 8, 16, 16);
		
		simpleSpell = new Spell(new SpellProjectile(this, spTex, 15, collision), 15, 15, 120, 2.5f);
	}
	
	@Override
	public void terminate() {
		System.out.println("Player is dead!");
		state = DynamicObjectState.Terminated;
		game.playerDead();
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
	 * The player will perform a simple MeleeAttack. Mostly debugpurpose. 
	 */
	private void simpleAttack() {
		if(state == DynamicObjectState.Attacking)
			return;
		
		state = DynamicObjectState.Attacking;
		curAnim = simpleAttack.getAnimation(heading);
	}
	
	private void spellAttack() {
		if(state == DynamicObjectState.Attacking)
			return;
		
		//state = DynamicObjectState.Attacking;
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
		//manager.spawnSpell(this, new SpellProjectile(this, spTex, spVelo, spDmg, startPos, collision));
		if(!simpleSpell.isOnCooldown() && mana.getCurrentMana() >= simpleSpell.getManaCost()) {
			mana.reduceMana(simpleSpell.getManaCost());
			simpleSpell.activate();
			manager.spawnSpell(this, simpleSpell.getProjectile(startPos, heading), simpleSpell);
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

	@Override
	public void update(float elapsed) {
		if(state == DynamicObjectState.Terminated) {
			game.startGame(true);
			return;
		}
		
		if(state == DynamicObjectState.Attacking) {
			if(simpleAttack.Update()) {
				state = DynamicObjectState.Idle;
				curAnim = idleAnim.get(heading);
				return;
			}
			curAnim.update(elapsed);
			//manager.handleAttack(simpleAttack, ID);
			manager.handleAttack(this, simpleAttack);
			
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
		
		if(controller.justPressed(KeyEvent.VK_A))
			simpleAttack();
		else if(controller.justPressed(KeyEvent.VK_S))
			spellAttack();
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
		
		//DEBUG PURPOSE
		if(controller.isPressed(KeyEvent.VK_SHIFT))
			supressEnemyCollision = true;
		else
			supressEnemyCollision = false;
		//-------------
		
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
		
		if(state == DynamicObjectState.Attacking) {
			if(simpleAttack.Update()) {
				state = DynamicObjectState.Idle;
				return;
			}
			curAnim.update(elapsed);
		}
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		graphics.drawImage(getTexture(), (int)screenPosition.x, (int)screenPosition.y, null);
		statusbar.draw(graphics);
	}

	@Override
	protected BufferedImage getTexture() {
		return curAnim.getTexture();
	}
}
