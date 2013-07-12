package fart.dungeoncrawler.network;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import fart.dungeoncrawler.Controller;
import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.gamestates.GameStateInLobby;
import fart.dungeoncrawler.network.messages.lobby.LobbyChatMessage;
import fart.dungeoncrawler.network.messages.lobby.LobbyClientReadyMessage;
import fart.dungeoncrawler.network.messages.lobby.LobbyStartGameRequest;

public class Lobby implements IDrawable {
	public static final String LOBBY_SAYS = "** LOBBY ** - ";
	
	private Client self;
	private ClientInfo selfInfo;
	private Controller controller;
	private boolean isServer;
	private boolean isWriting;
	private StringBuilder text;
	
	private ArrayList<ClientInfo> clients;
	
	public Lobby(Controller controller, boolean isServer) {
		clients = new ArrayList<ClientInfo>();
		this.controller = controller;
		this.isServer = isServer;
		
		text = new StringBuilder();
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
	public void update() {
		if(!isWriting) {
			if(controller.justPressed(KeyEvent.VK_R)) {
				changeReady();
			} else if (controller.justPressed(KeyEvent.VK_T)) {
				isWriting = true;
				text = new StringBuilder();
			}
			
			if(isServer) {
				if(controller.justPressed(KeyEvent.VK_S)) {
					getSelfClient().sendMessage(new LobbyStartGameRequest());
				}
			}
		} else {
			for(int i = KeyEvent.VK_A; i<KeyEvent.VK_Z; i++)
				if(controller.justPressed(i)) {
					text.append((char)i);
					System.out.println(text);
				}
			for(int i = KeyEvent.VK_0; i<KeyEvent.VK_9; i++)
				if(controller.justPressed(i)) {
					text.append((char)i);
					System.out.println(text);
				}
			
			if(controller.justPressed(KeyEvent.VK_SPACE)) //leerzeile
				  text.append(" ");
			
			if(controller.justPressed(KeyEvent.VK_END)) //punkt
				  text.append(".");
			
			if(controller.justPressed(KeyEvent.VK_UNDERSCORE)) //unterstrich
				  text.append("_");
			
			//TODO: Zahlen, Sonderzeichen,
			if(controller.justPressed(KeyEvent.VK_ENTER))
				ChatMessage();
			
			if (controller.justPressed(KeyEvent.VK_BACK_SPACE) && text.length() > 0)
				text.deleteCharAt(text.length() - 1);
		}
	}
	
	private void ChatMessage() {
		isWriting = false;
		StringBuilder message = new StringBuilder();
		message.append(selfInfo.name + ": " + text.toString());
		LobbyChatMessage p = new LobbyChatMessage(message.toString());
		self.sendMessage(p);
	}
	
	public void MessageReceived(LobbyChatMessage message) {
		System.out.println(message.text);
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
		
		//TODO: Nachricht welche angezeigt wird
		if(isWriting) {
			graphics.drawString(text.toString(), 32, 32  * 14);
		}
	}
}
