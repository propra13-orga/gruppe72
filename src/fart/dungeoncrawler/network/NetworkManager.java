package fart.dungeoncrawler.network;

import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.enums.*;
import fart.dungeoncrawler.network.messages.game.*;

/**
 * A Singleton-class. Clients use this manager to handle all incoming messages and send
 * messages to the server. 
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

		DeathMatchStatistics.createInstance(client.getAllClients());
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
	
	public static void sendSpellMessage(Player p, int spellIndex) {
		GameSpellMessage msg = new GameSpellMessage(p, spellIndex);
		sendMessage(msg);
	}
	
	public static void sendAttackMessage(Player a) {
		sendMessage(new GameAttackMessage(a));
	}
	
	public static void sendStatsMessage(Player a) {
		sendMessage(new GameStatsUpdateMessage(a));
	}
	
	public static void sendShieldMessage(Player p) {
		sendMessage(new GameShieldMessage(p));
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
			handleHitMessage((GameHitMessage)bm);
		else if(bm.type == GameMessage.GAME_KILLED_MESSAGE)
			handlePlayerKilledMessage((GamePlayerKilledMessage)bm);
		else if(bm.type == GameMessage.GAME_STATS_UPDATE)
			handleStatsUpdate((GameStatsUpdateMessage)bm);
		else if(bm.type == GameMessage.GAME_SHIELD_MESSAGE)
			handleShieldMessage((GameShieldMessage)bm);
	}
	
	private void handleStatsUpdate(GameStatsUpdateMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.setStats(msg.newStats);
	}

	private void handleAttackMessage(GameAttackMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.receivedAttackMsg();
	}

	private void handleGamePositionMessage(GamePositionMessage msg) {
		Actor a = dManager.getActorByID(msg.ID);
		a.setScreenPosition(msg.position);
		a.setVelocity(msg.velocity);
		a.setState(DynamicObjectState.values()[msg.state]);
		if(a instanceof Player) {
			((Player)a).setAnimation(a.getState());
		}
	}
	
	private void handleSpellMessage(GameSpellMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.spellAttack(msg.spellIndex);
	}
	
	private void handleHitMessage(GameHitMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.getHealth().setHealth(msg.health);
		
		if(msg.isSpell)
			dManager.removeProjectileInNetwork(a);
	}
	
	private void handlePlayerKilledMessage(GamePlayerKilledMessage msg) {
		//Terminate the killed player
		Player a = (Player)dManager.getActorByID(msg.deadID);
		a.terminate();
		DeathMatchStatistics.getInstance().killed(msg.ID, msg.deadID);
		
		//Let the killer gain experience
		Player b = (Player)dManager.getActorByID(msg.ID);
		int exp = Level.getMobExperienceForLevel(a.getLevel().getLevel()) * 2;
		b.getLevel().addExperince(exp);
	}
	
	private void handleShieldMessage(GameShieldMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.getSpellManager().activateShield(ElementType.values()[msg.shieldID]);
	}


}
