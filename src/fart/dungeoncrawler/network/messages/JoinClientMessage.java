package fart.dungeoncrawler.network.messages;

import java.io.Serializable;
import java.util.Date;

public class JoinClientMessage implements Serializable {
	private static final long serialVersionUID = 8035644791705453461L;
	public Date stamp;
	public String name;
	
	public JoinClientMessage(String name) {
		stamp = new Date();
		this.name = name;
	}
}
