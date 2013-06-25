package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import Utils.Vector2;
import fart.dungeoncrawler.*;
import fart.dungeoncrawler.enums.*;
import fart.dungeoncrawler.npc.states.EnemyStateMachine;

public class BaseNPC extends Actor implements IUpdateable {
	protected EnemyStateMachine machine;
	protected NPCDescription npcDesc;
	protected NPCType type;
	protected BufferedImage texture;
	
	public BaseNPC(Game game, Vector2 position, NPCDescription npcDesc) {
		super(game, (ActorDescription)npcDesc, position);
		this.state = DynamicObjectState.Idle;
		this.npcDesc = npcDesc;
		velocity = new Vector2();
	}
	
	public BaseNPC(Game game, CheckPointInfo info) {
		super(game, info.getActDesc(), info.getPosition());
		npcDesc = info.getNpcDesc();
		state = info.getState();
		velocity = Vector2.Zero;
		health = info.getHealth();
		mana = info.getMana();
		heading = info.getHeading();
		stats = info.getStats();
	}
	
	public void activateState(DynamicObjectState state) {
		machine.setState(state);
	}
	
	public void setMachine(EnemyStateMachine machine) {
		this.machine = machine;
		machine.setState(state);
	}
	
	public void setVelocity(Vector2 velocity) {
		if(velocity.equals(this.velocity))
			return;
		
		this.velocity = velocity;
	}
	

	public void addVelocity(Vector2 velocity) {
		this.velocity.x += velocity.x;
		this.velocity.y += velocity.y;
	}
	
	@Override
	public void setState(DynamicObjectState state) {
		if(machine != null)
			machine.setState(state);
	}
	
	@Override
	public void terminate() {
		velocity = new Vector2();
		state = DynamicObjectState.Terminated;
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

	public NPCDescription getNPCDescription() {
		return npcDesc;
	}
}
