package fart.dungeoncrawler.network.messages;

import java.io.Serializable;

public class JoinServerMessage implements Serializable {
	private static final long serialVersionUID = -1202435566303490949L;
	public static final byte ACCEPTED = 0x1;
	public static final byte DENIED = 0x0;
	
	public byte ID;
	public byte accepted;
	
	public JoinServerMessage() { }
	
	public JoinServerMessage(byte id) {
		ID = id;
		accepted = ACCEPTED;
	}
	
	public static JoinServerMessage getDeniedMessage() {
		JoinServerMessage result = new JoinServerMessage();
		result.ID = -1;
		result.accepted = DENIED;
		
		return result;
	}
}
