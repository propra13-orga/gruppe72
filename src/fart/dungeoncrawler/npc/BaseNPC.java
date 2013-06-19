package fart.dungeoncrawler.npc;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Utils.Vector2;
import fart.dungeoncrawler.DynamicObjectManager;
import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.Health;
import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.enums.Heading;
import fart.dungeoncrawler.npc.states.EnemyStateMachine;

public class BaseNPC extends GameObject implements IUpdateable {
	protected Rectangle collisionRect;
	protected DynamicObjectState curState;
	private Vector2 velocity;
	protected EnemyStateMachine machine;
	protected Health health;
	protected Heading heading;
	protected DynamicObjectManager manager;
	protected NPCDescription desc;
	
	public BaseNPC(NPCDescription desc, DynamicObjectManager manager) {
		this.screenPosition = desc.getPosition();
		this.collisionRect = desc.getColRect();
		this.curState = DynamicObjectState.Idle;
		this.manager = manager;
		this.desc = desc;
		
		health = new Health(100);
		
		velocity = new Vector2();
	}
	
	public void setMachine(EnemyStateMachine machine) {
		this.machine = machine;
	}
	
	public Vector2 getPosition() {
		return screenPosition;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public Heading getHeading() {
		return heading;
	}
	
	public NPCDescription getDescription() {
		return desc;
	}
	
	public DynamicObjectManager getManager() {
		return manager;
	}
	
	public void setVelocity(Vector2 velocity) {
		if(velocity.equals(this.velocity))
			return;
		
		this.velocity = velocity;
		//setHeading();
	}
	

	public void addVelocity(Vector2 velocity) {
		this.velocity.x += velocity.x;
		this.velocity.y += velocity.y;
	}
	
	public DynamicObjectState getState() {
		return curState;
	}
	
	public void setState(DynamicObjectState state) {
		curState = state;
	}
	
	public void activateState(DynamicObjectState state) {
		machine.setState(state);
	}
	
	public Health getHealth() {
		return health;
	}

	@Override
	public Rectangle getCollisionRect() {
		return collisionRect;
	}
	
	@Override
	public void terminate() {
		velocity = new Vector2();
		curState = DynamicObjectState.Terminated;
		machine.setState(DynamicObjectState.Terminated);
	}

	@Override
	protected BufferedImage getTexture() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void update(float elapsed) {
		screenPosition = screenPosition.add(velocity);
	}

	public void setPosition(Vector2 position) {
		this.screenPosition = position;
		collisionRect.x = (int)screenPosition.x;
		collisionRect.y = (int)screenPosition.y;
	}

	public void setHeading(Heading heading) {
		this.heading = heading;
	}
}
