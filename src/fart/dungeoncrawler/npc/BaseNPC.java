package fart.dungeoncrawler.npc;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import MathUtils.Vector2;
import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.Health;
import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.npc.states.EnemyStateMachine;
import fart.dungeoncrawler.npc.states.NPCState;

public class BaseNPC extends GameObject implements IUpdateable {
	protected Rectangle collisionRect;
	protected DynamicObjectState curState;
	protected Vector2 velocity;
	protected EnemyStateMachine machine;
	protected Health health;
	
	public BaseNPC(NPCDescription desc) {
		this.screenPosition = desc.getPosition();
		this.collisionRect = desc.getColRect();
		this.curState = DynamicObjectState.Idle;
		
		health = new Health(100);
		
		velocity = new Vector2();
	}
	
	public void setMachine(EnemyStateMachine machine) {
		this.machine = machine;
	}
	
	public Vector2 getPosition() {
		return screenPosition;
	}
	
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
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
}
