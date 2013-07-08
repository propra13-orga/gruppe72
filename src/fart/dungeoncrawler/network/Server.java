package fart.dungeoncrawler.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import fart.dungeoncrawler.network.messages.BaseMessage;
import fart.dungeoncrawler.network.messages.game.GameMessage;
import fart.dungeoncrawler.network.messages.lobby.*;

public class Server {
	public static final int PORT = 3333;
	public static final int MAX_PLAYERS = 4;
	
	private static Server instance;
	private static boolean isOnline = false;
	
	private ServerSocket socket;
	private ArrayList<ServerClient> clients;
	private ConnectionAccepter accepter;
	
	private boolean isInLobby;
	private boolean isInGame;
	private ServerGameLogic gameLogic;
	
	private Server() {
		clients = new ArrayList<ServerClient>();
		isOnline = true;
		
		try {
			socket = new ServerSocket(PORT);
			isInLobby = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(socket != null) {
			accepter = new ConnectionAccepter(this);
			accepter.start();
		}
	}
	
	public static boolean isOnline() {
		return isOnline;
	}
	
	public static void createInstance() {
		if(instance == null)
			instance = new Server();
	}
	
	public static Server getInstance() {
		if(instance == null)
			createInstance();
		
		return instance;
	}
	
	public boolean isInLobby() {
		return isInLobby;
	}
	
	public boolean isInGame() {
		return isInGame;
	}
	
	public ServerSocket getSocket() {
		return socket;
	}
	
	public void addClient(ServerClient client) {
		clients.add(client);
		
		//Send a list of all clients to the new client
		ArrayList<ClientInfo> infos = new ArrayList<ClientInfo>();
		for(byte i = 0; i < clients.size(); i++)
			infos.add(new ClientInfo(clients.get(i)));
		
		try {
			client.getOutput().writeObject(new LobbyClientListMessage(infos));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Check if all slots are filled. If so, broadcast a message. 
		if(clients.size() == MAX_PLAYERS) {
			LobbyMessage msg = new LobbyTextMessage("All slots are now filled.");
			broadcastMessage(msg);
		}
	}
	
	/**
	 * Checks if the name is valid. A name is valid, if it is minimum 3 characters and does not already exist.
	 * @param name
	 * @return
	 */
	public boolean nameValid(String name) {
		if(name.length() < 2) 
			return false;
		
		for(ServerClient c : clients) 
			if(c.getClientname().equals(name)) 
				return false;
		
		return true;
	}
	
	public void processMessage(LobbyMessage bm) {
		//CLIENT CHANGED HIS READY-STATE - INFORM ALL CLIENTS
		if(bm.messageType == LobbyMessage.LOBBY_CLIENT_READY) {
			LobbyClientReadyMessage msg = (LobbyClientReadyMessage)bm;
			for(ServerClient c : clients) {
				if(msg.clientID == c.getID()) {
					c.setReady(msg.isReady);
					break;
				}
			}
			
			broadcastMessage(msg);
		}
		//HOST SENDS A REQUEST TO START THE GAME - CHECK IF ALL CLIENTS ARE READY
		else if(bm.messageType == LobbyMessage.LOBBY_START_GAME_REQUEST) {
			boolean possible = true;
			for(ServerClient c : clients) {
				if(!c.isReady()) {
					possible = false;
					break;
				}
			}
			
			if(possible) {
				LobbyStartGameMessage msg = new LobbyStartGameMessage();
				broadcastMessage(msg);
				startNewGame();
			}
		}
	}
	
	public void processMessage(GameMessage msg) {
		gameLogic.processGameMessage(msg);
	}
	
	private void startNewGame() {
		ServerClient[] c = new ServerClient[clients.size()];
		clients.toArray(c);
		gameLogic = new ServerGameLogic(this, c);
		gameLogic.startNewGame();
		isInLobby = false;
		isInGame = true;
		
		gameLogic.start();
	}
	
	public void broadcastMessage(BaseMessage message) {
		for(ServerClient c : clients) {
			try {
				c.getOutput().writeObject(message);
				c.getOutput().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void broadcastMessage(LobbyMessage message) {
		for(ServerClient c : clients) {
			try {
				c.getOutput().writeObject(message);
				c.getOutput().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void broadcastMessage(GameMessage message) {
		for(ServerClient c : clients) {
			try {
				c.getOutput().writeObject((GameMessage)message);
				c.getOutput().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This class listens for all incoming connections. It runs until MAX_PLAYERS connected
	 * or the game is started. 
	 * @author Felix
	 *
	 */
	class ConnectionAccepter extends Thread {
		private Server server;
		private ServerSocket socket;
		private boolean keepAccepting;
		private int numConnections;
		
		public ConnectionAccepter(Server server) {
			this.server = server;
			keepAccepting = true;
			numConnections = 0;
			
			socket = server.getSocket();
			if(socket == null) {
				System.out.println("ServerSocket is null. Terminating...");
				System.exit(2);
			}
		}
		
		public void connectionLost() {
			numConnections -= 1;
		}
		
		public void run() {
			System.out.println("[Waiting for incoming connections...]");
			while(keepAccepting) {
				try {
					Socket client = socket.accept();
					client.setTcpNoDelay(true);
					if(numConnections < MAX_PLAYERS) {
						ServerClient c = new ServerClient(server, client);
						if(c.isAccepted()) {
							System.out.println("[Added new client to the list.]");
							c.start();
							//c.run();
							
							LobbyJoinedMessage ljm = new LobbyJoinedMessage(new ClientInfo(c));
							broadcastMessage(ljm);
							//broadcastJoined(c);
							server.addClient(c);
						} else {
							c = null;
							client.close();
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
