package fart.dungeoncrawler.network.messages;

import java.io.Serializable;

/**
 * This message is sent to the client as a response to a JoinClientMessage. It contains the clientID
 * and a flag if the client was accepted.
 * @author Felix
 *
 */
public class JoinServerMessage implements Serializable {
	private static final long serialVersionUID = -1202435566303490949L;
	public static final byte ACCEPTED = 0x1;
	public static final byte DENIED = 0x0;
	
	public byte ID;
	public byte accepted;
	
	public JoinServerMessage() { }
	
	/**
	 * Creates a message containing the valid ID and an ACCEPTED-flag.
	 * @param id
	 */
	public JoinServerMessage(byte id) {
		ID = id;
		accepted = ACCEPTED;
	}
	
	/**
	 * Creates a message containing an invalid ID and an DENIED-flag. 
	 * @return
	 */
	public static JoinServerMessage getDeniedMessage() {
		JoinServerMessage result = new JoinServerMessage();
		result.ID = -1;
		result.accepted = DENIED;
		
		return result;
	}
}
