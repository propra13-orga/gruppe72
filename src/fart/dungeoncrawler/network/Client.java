package fart.dungeoncrawler.network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.enums.GameState;
import fart.dungeoncrawler.network.messages.BaseMessage;
import fart.dungeoncrawler.network.messages.JoinClientMessage;
import fart.dungeoncrawler.network.messages.JoinServerMessage;
import fart.dungeoncrawler.network.messages.game.*;
import fart.dungeoncrawler.network.messages.lobby.*;

public class Client extends Thread{
	private byte ID;
	public byte getID() { return ID; }
	
	private String name;
	public String getClientname() { return name; }
	
	private Socket server;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ArrayList<ClientInfo> allClients = new ArrayList<ClientInfo>();
	private Lobby lobby;
	private boolean accepted = false;
	private boolean ready;
	private Game game;
	
	public Client(Lobby lobby, Game game, String ip, String name) {
		this.lobby = lobby;
		ready = false;
		this.game = game;
		
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
	
	public ArrayList<ClientInfo> getAllClients() {
		return allClients;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void changeReady() {
		ready = !ready;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public void sendMessage(LobbyMessage msg) {
		try {
			output.writeObject(msg);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(BaseMessage msg) {
		try {
			output.writeObject(msg);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(GameMessage msg) {
		try {
			output.writeObject(msg);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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

	public ObjectOutputStream getOutputStream() {
		return output;
	}

	public ObjectInputStream getInputStream() {
		return input;
	}
}
