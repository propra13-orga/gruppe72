package fart.dungeoncrawler.network.messages;

import java.io.Serializable;

public class BaseMessage implements Serializable {
	private static final long serialVersionUID = -7655127459346493981L;
	//private Date stamp;
	public byte ID;
	public int messageType;
	
	public BaseMessage(byte id, int type) {
		ID = id;
		messageType = type;
	}
}
