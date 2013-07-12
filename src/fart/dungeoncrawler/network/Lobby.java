package fart.dungeoncrawler.network;

import java.awt.Graphics2D;
import java.util.ArrayList;

import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.gamestates.GameStateInLobby;
import fart.dungeoncrawler.network.messages.lobby.LobbyClientReadyMessage;

public class Lobby implements IDrawable {
	public static final String LOBBY_SAYS = "** LOBBY ** - ";
	
	private Client self;
	private ClientInfo selfInfo;
	
	private ArrayList<ClientInfo> clients;
	
	public Lobby() {
		clients = new ArrayList<ClientInfo>();
	}
	
	public Client getSelfClient() {
		return self;
	}
	
	public void setSelfClient(Client c) { 
		self = c; 
		selfInfo = new ClientInfo(c);
	}
	
	public void addOther(ClientInfo client) {
		clients.add(client);
	}
	
	public int getNumPlayers() {
		return clients.size();
	}
	
	public void changeReady() {
		self.changeReady();
		selfInfo.ready = self.isReady();
		
		LobbyClientReadyMessage lcr = new LobbyClientReadyMessage(self.getID(), self.isReady());
		self.sendMessage(lcr);
	}
	
	public void setClientReady(byte clientID, boolean ready) {
		for(int i = 0; i < clients.size(); i++) {
			if(clients.get(i).ID == clientID) {
				clients.get(i).ready = ready;
			}
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(GameStateInLobby.fontColor);
		graphics.setFont(GameStateInLobby.font);
		
		for(int i = 0; i < clients.size(); i++) {
			graphics.drawString(clients.get(i).name, 32, 32 + i * 16);
			
			String ready = "";
			if(clients.get(i).ready)
				ready = "ready";
			else
				ready = "not ready";
			
			graphics.drawString(ready, 32 * 6, 32 + i * 16);
		}
	}
}
