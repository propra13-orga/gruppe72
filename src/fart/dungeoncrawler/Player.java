package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Utils.Vector2;

import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.enums.Heading;

public class Player extends GameObject implements IUpdateable {
	private Vector2 tilePosition;
	
	private HashMap<Heading, Animation> walkAnim;
	private HashMap<Heading, Animation> idleAnim;
	
	private Heading heading;
	private DynamicObjectState state;
	private Animation curAnim;
	
	private Vector2 velocity;
	private Rectangle collisionRect;
	
	private Collision colDetector;
	private Controller controller;
	private Game game;
	
	private boolean supressEnemyCollision = false;
	private Health health;
	public StatusBar statusbar;
	
	public Player(Vector2 tilePosition, Collision colDetector, Controller controller, Game game) {
		super();
		
		this.colDetector = colDetector;
		this.tilePosition = tilePosition;
		this.screenPosition = new Vector2(tilePosition.x * Tilemap.TILE_SIZE, tilePosition.y * Tilemap.TILE_SIZE);
		this.collisionRect = new Rectangle((int)screenPosition.x, (int)screenPosition.y, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
		this.heading = Heading.Down;
		this.state = DynamicObjectState.Idle;
		this.velocity = new Vector2(0, 0);
		this.controller = controller;
		this.game = game;
		health = new Health(100, 10);
		statusbar = new StatusBar(this);

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
			
			curAnim = idleAnim.get(heading);
		} catch (IOException e) {
			System.err.print("Couldn't load Players texture!");
			System.exit(1);
		}
		
		//DEBUG
		System.out.println("Hold SHIFT to avoid player collision.");
		//
	}
	
	public Health getHealth() {
		return health;
	}

	@Override
	protected BufferedImage getTexture() {
		return curAnim.getTexture();
	}
	
	@Override
	public Rectangle getCollisionRect() {
		return collisionRect;
	}
	
	@Override
	public void terminate() {
		System.out.println("Player ist dead!");
		state = DynamicObjectState.Terminated;
		game.playerDead();
	}
	
	public Vector2 getTilePosition() {
		return tilePosition;
	}
	
	public Vector2 getScreenPosition() {
		return screenPosition;
	}
	
	public void setTilePosition(Vector2 position) {
		tilePosition = position;
		screenPosition = new Vector2(position.x * Tilemap.TILE_SIZE, position.y * Tilemap.TILE_SIZE);
		collisionRect.x = (int)screenPosition.x;
		collisionRect.y = (int)screenPosition.y;
	}
	
	public void setScreenPosition(Vector2 position) {
		screenPosition = position;
		tilePosition = new Vector2(position.x / Tilemap.TILE_SIZE, position.y / Tilemap.TILE_SIZE);
		collisionRect.x = (int)screenPosition.x;
		collisionRect.y = (int)screenPosition.y;
	}
	
	//Players can only move in one direction at a time
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
		
		if(controller.isDownPressed())
			move(Heading.Down);
		else if(controller.isUpPressed())
			move(Heading.Up);
		else if(controller.isLeftPressed())
			move(Heading.Left);
		else if(controller.isRightPressed())
			move(Heading.Right);
		else
			stopMovement();
		
		//DEBUG PURPOSE
		if(controller.isPressed(KeyEvent.VK_SHIFT))
			supressEnemyCollision = true;
		else
			supressEnemyCollision = false;
		//-------------
		
		collisionRect.x += velocity.x;
		collisionRect.y += velocity.y;
		
		boolean collidingStatic = colDetector.isCollidingStatic(this);
		boolean collidingDynamic = false;
		if(!supressEnemyCollision)
			collidingDynamic = colDetector.isCollidingDynamic(this);
		
		if(collidingStatic || collidingDynamic) {
			collisionRect.x -= velocity.x;
			collisionRect.y -= velocity.y;
			stopMovement();
		} else {
			colDetector.checkTriggers(this);
			screenPosition.x += velocity.x;
			screenPosition.y += velocity.y;
			curAnim.update(elapsed);
		}
		
		if(!supressEnemyCollision) {
			
		}
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		graphics.drawImage(getTexture(), (int)screenPosition.x, (int)screenPosition.y, null);
		statusbar.draw(graphics);
	}
}
