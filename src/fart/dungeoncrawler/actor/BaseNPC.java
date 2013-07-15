package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import Utils.Vector2;
import fart.dungeoncrawler.*;
import fart.dungeoncrawler.actor.states.EnemyStateMachine;
import fart.dungeoncrawler.enums.*;

/**
 * The abstract base class for all NPCs in the game.
 * @author Erhan/Felix
 *
 */
public abstract class BaseNPC extends Actor implements IUpdateable {
	protected EnemyStateMachine machine;
	protected NPCDescription npcDesc;
	protected NPCType type;
	protected BufferedImage texture;
	
	/**
	 * Creates a BaseNPC from the NPCDescription. 
	 * @param game instance of the game running
	 * @param position position in screenspace
	 * @param npcDesc NPCDescription
	 */
	public BaseNPC(Game game, Vector2 position, NPCDescription npcDesc) {
		super(game, (ActorDescription)npcDesc, position);
		this.state = DynamicObjectState.Idle;
		this.npcDesc = npcDesc;
		velocity = new Vector2();
	}
	
	/**
	 * Creates a BaseNPC from a CheckPointInfo.
	 * @param game instance of the game running
	 * @param info CheckPointInfo
	 */
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
	
	/**
	 * Activates a state in the StateMachine.
	 * @param state
	 */
	public void activateState(DynamicObjectState state) {
		machine.setState(state);
	}
	
	/**
	 * Sets the StateMachine
	 * @param machine
	 */
	public void setMachine(EnemyStateMachine machine) {
		this.machine = machine;
		machine.setState(state);
	}
	
	/**
	 * Sets the current velocity.
	 */
	public void setVelocity(Vector2 velocity) {
		if(velocity.equals(this.velocity))
			return;
		
		this.velocity = velocity;
	}
	
	//@Override
	/*public void setState(DynamicObjectState state) {
		if(machine != null)
			machine.setState(state);
	}*/
	
	@Override
	public void terminate() {
		velocity = new Vector2();
		state = DynamicObjectState.Terminated;
		if(machine != null)
			machine.setState(DynamicObjectState.Terminated);
		manager.removeObject(this);
	}

	@Override
	public void update(float elapsed) {
		screenPosition = screenPosition.add(velocity);
	}

	/**
	 * Returns the NPCDescription
	 * @return
	 */
	public NPCDescription getNPCDescription() {
		return npcDesc;
	}
}
