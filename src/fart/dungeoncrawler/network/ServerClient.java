package fart.dungeoncrawler.network;

import java.io.*;
import java.net.Socket;

import fart.dungeoncrawler.network.messages.*;
import fart.dungeoncrawler.network.messages.game.GameMessage;
import fart.dungeoncrawler.network.messages.lobby.LobbyMessage;

public class ServerClient extends Thread {
	private static byte IDCounter = 0;
	
	private Server server;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private byte ID;
	private String name;
	private boolean accepted;
	private boolean ready;
	
	public ServerClient(Server server, Socket socket) {
		this.server = server;
		ready = false;
		
		try {
			System.out.println("[Trying to get IO-streams...]");
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("[IOStreams running.]");
			
			ID = IDCounter;
			
			JoinClientMessage msg = (JoinClientMessage)input.readObject();
			if(server.nameValid(msg.name)) {
				JoinServerMessage outMsg = new JoinServerMessage(IDCounter);
				name = msg.name;
				IDCounter += 1;
				accepted = true;
				
				output.writeObject(outMsg);
				output.flush();
			} else {
				JoinServerMessage outMsg = JoinServerMessage.getDeniedMessage();
				accepted = false;
				
				output.writeObject(outMsg);
				output.flush();
				output.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean isAccepted() {
		return accepted;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public byte getID() {
		return ID;
	}
	
	public String getClientname() {
		return name;
	}
	
	public ObjectOutputStream getOutput() {
		return output;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Object bm = input.readObject();
				if(bm instanceof LobbyMessage) {
					LobbyMessage msg = (LobbyMessage)bm;
					server.processMessage(msg);
				} else if(bm instanceof GameMessage) {
					GameMessage msg = (GameMessage)bm;
					server.processMessage(msg);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
