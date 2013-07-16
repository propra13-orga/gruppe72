package fart.dungeoncrawler.network.messages;

import java.io.Serializable;

/**
 * This Message is sent from the client to the server immediately after establishing a connection.
 * It only contains the name of the player. The server checks, is the name is valid and sends back
 * a JoinServerMessage.
 * @author Felix
 *
 */
public class JoinClientMessage implements Serializable {
	private static final long serialVersionUID = 8035644791705453461L;
	public String name;
	
	public JoinClientMessage(String name) {
		this.name = name;
	}
}
