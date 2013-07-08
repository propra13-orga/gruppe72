package fart.dungeoncrawler.network;

import fart.dungeoncrawler.*;
import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.enums.GameState;
import fart.dungeoncrawler.network.messages.game.*;

public class ServerGameLogic extends Thread {
	private Game game;
	private Server server;
	private ServerClient[] clients;
	//private MapLoader loader;
	//private Tilemap map;
	private DynamicObjectManager dManager;
	//private StaticObjectManager sManager;
	//private Collision collision;
	
	//JFrame f;
	
	public ServerGameLogic(Server server, ServerClient[] clients) {
		this.server = server;
		this.clients = clients;
		
		System.out.println("Created ServerGameLogic");
	}
	
	public void startNewGame() {
		System.out.println("ServerGameLogic started");
		
		game = new Game((byte)-1, clients.length, true);
		game.setInNetwork(true);
		game.createPlayers((byte) -1, clients.length);
		dManager = game.getDynamicManager();
		//sManager = game.getStaticManager();
		//collision = game.getCollision();
		//map = game.getMap();
		game.setGameState(GameState.InGame);
		game.startGame(true, "res/maps/DM1.xml");
		
		game.startGameLoop();
	}
	
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
		}
	}
	
	private void handlePositionUpdate(GamePositionMessage msg) {
		Actor a = dManager.getActorByID(msg.ID);
		a.setScreenPosition(msg.position);
		a.setVelocity(msg.velocity);
		a.setState(DynamicObjectState.values()[msg.state]);
		
		System.out.println("**Server: [" + msg.ID + "] moved to (" + msg.position.x + "/" + msg.position.y + ").");
		
		server.broadcastMessage(msg);
	}
	
	private void handleSpell(GameSpellMessage msg) {
		NewPlayer a = (NewPlayer)dManager.getActorByID(msg.ID);
		a.spellAttack(msg.spellIndex);
		
		server.broadcastMessage(msg);
	}
	
	private void handleAttack(GameAttackMessage msg) {
		NewPlayer a = (NewPlayer)dManager.getActorByID(msg.ID);
		a.simpleAttack();
		
		server.broadcastMessage(msg);
	}
}
