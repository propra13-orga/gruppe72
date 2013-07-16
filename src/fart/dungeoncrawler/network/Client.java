package fart.dungeoncrawler.network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.enums.GameState;
import fart.dungeoncrawler.network.messages.JoinClientMessage;
import fart.dungeoncrawler.network.messages.JoinServerMessage;
import fart.dungeoncrawler.network.messages.game.*;
import fart.dungeoncrawler.network.messages.lobby.*;

/**
 * The Client-class is used in networkgames to connect to the server. It is responsible for
 * establishing the connection and sending and receiving data to/from the server. Receiving
 * data runs in its own Thread. 
 * @author Felix
 *
 */
public class Client extends Thread {
	private byte ID;
	/**
	 * Gets the client ID. 
	 * @return unique ID
	 */
	public byte getID() { return ID; }
	
	private String name;
	/**
	 * Gets the client name entered before connecting to the server.
	 * @return client name
	 */
	public String getClientname() { return name; }
	
	private Socket server;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ArrayList<ClientInfo> allClients = new ArrayList<ClientInfo>();
	private Lobby lobby;
	private boolean accepted = false;
	private boolean ready;
	private Game game;
	
	/**
	 * 
	 * @param lobby Lobby to join before the game starts
	 * @param game An instance of the game
	 * @param ip Server-IP
	 * @param name name of the client
	 */
	public Client(Lobby lobby, Game game, String ip, String name) {
		this.lobby = lobby;
		ready = false;
		this.game = game;
		this.name = name;
		
		try {
			InetAddress adr = InetAddress.getByName(ip);
			
			server = new Socket(adr, Server.PORT);
			server.setTcpNoDelay(true);
			
			output = new ObjectOutputStream(server.getOutputStream());
			input = new ObjectInputStream(server.getInputStream());
			System.out.println("**Streams running...");
			
			//Send the first join-message
			JoinClientMessage msg = new JoinClientMessage(name);
			output.writeObject(msg);
			output.flush();
			
			//wait for server to reply
			JoinServerMessage res = (JoinServerMessage)input.readObject();
			if(res.accepted == JoinServerMessage.ACCEPTED) {
				ID = res.ID;
				System.out.println("Connection established. ID is " + ID);
				accepted = true;
			}
			else {
				System.out.println("Could not establish connection.");
				server.close();
				accepted = false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param name client name
	 * @param lobby The lobby to join before the game is started
	 * @param game An instance of the game
	 */
	public Client(String name, Lobby lobby, Game game) {
		this.name = name;
		this.lobby = lobby;
		this.game = game;
		ready = false;
		
		try {
			InetAddress adr = InetAddress.getLocalHost();
			
			server = new Socket(adr, Server.PORT);
			server.setTcpNoDelay(true);
			
			output = new ObjectOutputStream(server.getOutputStream());
			input = new ObjectInputStream(server.getInputStream());
			
			//Send the first join-message
			JoinClientMessage msg = new JoinClientMessage(name);
			output.writeObject(msg);
			output.flush();
			
			//wait for server to reply
			JoinServerMessage res = (JoinServerMessage)input.readObject();
			if(res.accepted == JoinServerMessage.ACCEPTED) {
				ID = res.ID;
				System.out.println("[Connection established. ID is " + ID + "]");
				accepted = true;
			}
			else {
				System.out.println("Could not establish connection.");
				server.close();
				accepted = false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a list of all clients currently connected to the server
	 * @return list of all clients
	 */
	public ArrayList<ClientInfo> getAllClients() {
		return allClients;
	}
	
	/**
	 * Gets the "ready-state". Game can only be started if all players are ready.
	 * @return ready
	 */
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * Flips the "ready-state"
	 */
	public void changeReady() {
		ready = !ready;
	}
	
	/**
	 * Sets the "ready-state".
	 * @param ready new state
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	/**
	 * Sends a lobby-message to the server. 
	 * @param msg message to send
	 */
	public void sendMessage(LobbyMessage msg) {
		try {
			output.writeObject(msg);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a game-message to the server.
	 * @param msg message to send
	 */
	public void sendMessage(GameMessage msg) {
		try {
			output.writeObject(msg);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Receives all incoming messages in a loop. 
	 */
	public void run() {
		while(accepted) {
			try {
				Object o = input.readObject();
				if(o instanceof LobbyMessage) 
					processMessage((LobbyMessage)o);
				else if(o instanceof GameMessage) {
					NetworkManager.getInstance().processMessage((GameMessage)o);
				}
				
			} catch (ClassNotFoundException e) {
				break;
			} catch (IOException e) {
				break;
			}
		}
	}
	
	/**
	 * Received lobby messages are handled in this function. 
	 * @param bm lobby-message
	 */
	private void processMessage(LobbyMessage bm) {
		int type = bm.messageType;
		
		//SOMEONE JOINED OR LEFT THE LOBBY
		if(type == LobbyMessage.LOBBY_JOINED_MESSAGE) {
			LobbyJoinedMessage msg = (LobbyJoinedMessage)bm;
			lobby.addOther(msg.client);
			allClients.add(msg.client);
			System.out.println(Lobby.LOBBY_SAYS + msg.client.name + " joined the lobby (@ID " + msg.client.ID + ")");
		} 
		//LOBBY SENDS A MESSAGE
		else if(type == LobbyMessage.LOBBY_TEXT_MESSAGE) {
			LobbyTextMessage msg = (LobbyTextMessage)bm;
			System.out.println(Lobby.LOBBY_SAYS + msg.text);
		} 
		//LOBBY CHATMESSAGE
		else if(type == LobbyMessage.LOBBY_CHAT_MESSAGE) {
			LobbyChatMessage msg = (LobbyChatMessage)bm;
			lobby.MessageReceived(msg);
			System.out.println();
		}
		//LOBBY SENDS LIST OF ALL CLIENTS
		else if(type == LobbyMessage.LOBBY_CLIENT_LIST) {
			LobbyClientListMessage clMsg = (LobbyClientListMessage)bm;
			for(int i = 0; i < clMsg.clients.size(); i++) {
				lobby.addOther(clMsg.clients.get(i));
				allClients.add(clMsg.clients.get(i));
			}
		}
		//SOMEONE CHANGED READY-STATE
		else if(type == LobbyMessage.LOBBY_CLIENT_READY) {
			LobbyClientReadyMessage msg = (LobbyClientReadyMessage)bm;
			lobby.setClientReady(msg.clientID, msg.isReady);
		}
		//GAME IS STARTED
		else if(type == LobbyMessage.LOBBY_START_GAME_MESSAGE) {
			System.out.println(Lobby.LOBBY_SAYS + "The game is started...");

			game.initGame();
			game.setInNetwork(true);
			game.createPlayers((byte)ID, allClients.size());
			game.startGame("res/maps/DM1.xml");
			
			NetworkManager.createInstance(this, game.getDynamicManager());
			
			game.setGameState(GameState.InGame);
		}
	}
}
