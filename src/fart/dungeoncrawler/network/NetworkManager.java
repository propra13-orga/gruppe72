package fart.dungeoncrawler.network;

import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.enums.*;
import fart.dungeoncrawler.network.messages.game.*;

/**
 * Clients use this manager to handle all incoming game-messages and send
 * game-messages to the server. Singleton-class. 
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
	
	/**
	 * Creates and initializes a new instance of the NetworkManager. 
	 * @param client
	 * @param dManager
	 */
	public static void createInstance(Client client, DynamicObjectManager dManager) {
		instance = new NetworkManager(client, dManager);
	}
	
	/**
	 * Returns the (singleton-)instance.
	 * @return instance
	 */
	public static NetworkManager getInstance() {
		return instance;
	}
	
	/**
	 * Creates and sends a GamePositionMessage to the server. 
	 * @param actor
	 */
	public static void sendPositionMessage(Actor actor) {
		GamePositionMessage msg = new GamePositionMessage(actor);
		sendMessage(msg);
	}
	
	/**
	 * Creates and sends a GameSpellMessage to the server. As soon as a player casts a spell
	 * the server is notified.
	 * @param player
	 * @param spellIndex
	 */
	public static void sendSpellMessage(Player player, int spellIndex) {
		GameSpellMessage msg = new GameSpellMessage(player, spellIndex);
		sendMessage(msg);
	}
	
	/**
	 * Creates and sends a GameAttackMessage to the server. As soon as a player attacks the
	 * server is notified
	 * @param player
	 */
	public static void sendAttackMessage(Player player) {
		sendMessage(new GameAttackMessage(player));
	}
	
	/**
	 * Creates and sends a GameStatsUpdateMessage to the server after the stats of the player
	 * have changed.
	 * @param player
	 */
	public static void sendStatsMessage(Player player) {
		sendMessage(new GameStatsUpdateMessage(player));
	}
	
	/**
	 * Creates and sends a message indicating that a player has activated a shield. 
	 * @param p
	 */
	public static void sendShieldMessage(Player p) {
		sendMessage(new GameShieldMessage(p));
	}
	
	/**
	 * Sends a GameMessage to the server. Private because it is only used inside this class. To send a
	 * GameMessage use a specific function like sendPositionMessage()
	 * @param msg the message to send
	 */
	private static void sendMessage(GameMessage msg) {
		instance.client.sendMessage(msg);
	}
	
	/**
	 * All incoming GameMessages are passed here to get processed. 
	 * @param bm game-message to process
	 */
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
	
	/**
	 * Handles a StatsUpdate
	 * @param msg
	 */
	private void handleStatsUpdate(GameStatsUpdateMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.setStats(msg.newStats);
	}

	/**
	 * Handles an AttackMessage
	 * @param msg
	 */
	private void handleAttackMessage(GameAttackMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.receivedAttackMsg();
	}

	/**
	 * Handles a PositionUpdate
	 * @param msg
	 */
	private void handleGamePositionMessage(GamePositionMessage msg) {
		Actor a = dManager.getActorByID(msg.ID);
		a.setScreenPosition(msg.position);
		a.setVelocity(msg.velocity);
		a.setState(DynamicObjectState.values()[msg.state]);
		if(a instanceof Player) {
			((Player)a).setAnimation(a.getState());
		}
	}
	
	/**
	 * Handles a SpellMessage
	 * @param msg
	 */
	private void handleSpellMessage(GameSpellMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.spellAttack(msg.spellIndex);
	}
	
	/**
	 * Handles a HitMessage. 
	 * @param msg
	 */
	private void handleHitMessage(GameHitMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.getHealth().setHealth(msg.health);
		
		if(msg.isSpell)
			dManager.removeProjectileInNetwork(a);
	}
	
	/**
	 * Handles a PlayerKilledMessage. 
	 * @param msg
	 */
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
	
	/**
	 * Handles a ShieldMessage and activates the players shield. 
	 * @param msg
	 */
	private void handleShieldMessage(GameShieldMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.getSpellManager().activateShield(ElementType.values()[msg.shieldID]);
	}
}
