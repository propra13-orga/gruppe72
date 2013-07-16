package fart.dungeoncrawler.network;

import fart.dungeoncrawler.*;
import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.enums.*;
import fart.dungeoncrawler.network.messages.game.*;

/**
 * The ServerGameLogic is created on the serverside when starting the game. It handles incoming game-
 * messages and runs all the logic. 
 * @author Svenja
 *
 */
public class ServerGameLogic extends Thread {
	private Game game;
	private Server server;
	private ServerClient[] clients;
	private DynamicObjectManager dManager;
	
	public ServerGameLogic(Server server, ServerClient[] clients) {
		this.server = server;
		this.clients = clients;
		
		System.out.println("Created ServerGameLogic");
	}
	
	/**
	 * Initializes and starts a new game.
	 */
	public void startNewGame() {
		System.out.println("ServerGameLogic started");
		
		game = new Game((byte)-1, clients.length, true);
		game.setInNetwork(true);
		game.createPlayers((byte) -1, clients.length);
		dManager = game.getDynamicManager();
		game.setGameState(GameState.InGame);
		game.startGame("res/maps/DM1.xml");
		
		game.startGameLoop();
	}
	
	/**
	 * All incoming gameMessages are sent here. After figuring out the type of the message a specific
	 * method is called to handle the message. If needed the message is than broadcastet to the other
	 * players
	 * @param bm
	 */
	public void processGameMessage(GameMessage bm) {
		if(bm.type == GameMessage.GAME_POSITION_MESSAGE) {
			GamePositionMessage msg = (GamePositionMessage)bm;
			handlePositionUpdate(msg);
		} else if(bm.type == GameMessage.GAME_SPELL_MESSAGE) {
			GameSpellMessage msg = (GameSpellMessage)bm;
			handleSpell(msg);
		} else if(bm.type == GameMessage.GAME_ATTACK_MESSAGE) {
			GameAttackMessage msg = (GameAttackMessage)bm;
			handleAttack(msg);
		} else if(bm.type == GameMessage.GAME_STATS_UPDATE) {
			GameStatsUpdateMessage msg = (GameStatsUpdateMessage)bm;
			handleStatsUpdate(msg);
		} else if(bm.type == GameMessage.GAME_SHIELD_MESSAGE) {
			GameShieldMessage msg = (GameShieldMessage)bm;
			handleShield(msg);
		}
	}
	
	/**
	 * Handles a GamePositionMessage.
	 * @param msg
	 */
	private void handlePositionUpdate(GamePositionMessage msg) {
		Actor a = dManager.getActorByID(msg.ID);
		a.setScreenPosition(msg.position);
		a.setVelocity(msg.velocity);
		a.setState(DynamicObjectState.values()[msg.state]);
		
		server.broadcastMessage(msg);
	}
	
	/**
	 * Handles a SpellMessage.
	 * @param msg
	 */
	private void handleSpell(GameSpellMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.spellAttack(msg.spellIndex);
		
		server.broadcastMessage(msg);
	}
	
	/**
	 * Handles an AttackMessage. 
	 * @param msg
	 */
	private void handleAttack(GameAttackMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.simpleAttack();
		
		server.broadcastMessage(msg);
	}
	
	/**
	 * Handles a StatsUpdateMessage.
	 * @param msg
	 */
	private void handleStatsUpdate(GameStatsUpdateMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.setStats(msg.newStats);
		
		server.broadcastMessage(msg);
	}
	
	/**
	 * Handles a ShieldMessage.
	 * @param msg
	 */
	private void handleShield(GameShieldMessage msg) {
		Player a = (Player)dManager.getActorByID(msg.ID);
		a.getSpellManager().activateShield(ElementType.values()[msg.shieldID]);
		
		server.broadcastMessage(msg);
	}
}
