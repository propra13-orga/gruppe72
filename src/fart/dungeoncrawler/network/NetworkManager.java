package fart.dungeoncrawler.network;

import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.enums.*;
import fart.dungeoncrawler.network.messages.game.*;

/**
 * A Singleton-class
 * @author Felix
 *
 */
public class NetworkManager {
	private static NetworkManager instance;
	
	private Client client;
	private DynamicObjectManager dManager;
	
	private NetworkManager(Client client, DynamicObjectManager dManager) {
		this.dManager = dManager;
		this.client = client;
	}
	
	public static void createInstance(Client client, DynamicObjectManager dManager) {
		instance = new NetworkManager(client, dManager);
	}
	
	public static NetworkManager getInstance() {
		return instance;
	}
	
	public static void sendPositionMessage(Actor a) {
		GamePositionMessage msg = new GamePositionMessage(a);
		sendMessage(msg);
	}
	
	public static void sendSpellMessage(NewPlayer p, int spellIndex) {
		GameSpellMessage msg = new GameSpellMessage(p, spellIndex);
		sendMessage(msg);
	}
	
	public static void sendAttackMessage(NewPlayer a) {
		sendMessage(new GameAttackMessage(a));
	}
	
	private static void sendMessage(GameMessage msg) {
		instance.client.sendMessage(msg);
	}
	
	public void processMessage(GameMessage bm) {
		if(bm.type == GameMessage.GAME_POSITION_MESSAGE)
			handleGamePositionMessage((GamePositionMessage)bm);
		else if(bm.type == GameMessage.GAME_SPELL_MESSAGE)
			handleSpellMessage((GameSpellMessage)bm);
		else if(bm.type == GameMessage.GAME_ATTACK_MESSAGE)
			handleAttackMessage((GameAttackMessage)bm);
		else if(bm.type == GameMessage.GAME_HIT_MESSAGE)
			handleSpellHitMessage((GameHitMessage)bm);
	}
	
	private void handleAttackMessage(GameAttackMessage msg) {
		NewPlayer a = (NewPlayer)dManager.getActorByID(msg.ID);
		a.simpleAttack();
	}

	private void handleGamePositionMessage(GamePositionMessage msg) {
		Actor a = dManager.getActorByID(msg.ID);
		a.setScreenPosition(msg.position);
		a.setVelocity(msg.velocity);
		a.setState(DynamicObjectState.values()[msg.state]);
		if(a instanceof NewPlayer) {
			((NewPlayer)a).setAnimation(a.getState());
		}
	}
	
	private void handleSpellMessage(GameSpellMessage msg) {
		NewPlayer a = (NewPlayer)dManager.getActorByID(msg.ID);
		a.spellAttack(msg.spellIndex);
	}
	
	private void handleSpellHitMessage(GameHitMessage msg) {
		NewPlayer a = (NewPlayer)dManager.getActorByID(msg.ID);
		a.getHealth().reduceHealth(msg.damage);
		
		if(msg.isSpell)
			dManager.removeProjectileInNetwork(a);
	}
}
