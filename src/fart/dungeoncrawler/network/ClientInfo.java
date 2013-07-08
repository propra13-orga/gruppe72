package fart.dungeoncrawler.network;

import java.io.Serializable;

public class ClientInfo implements Serializable {
	private static final long serialVersionUID = -1394412798338935906L;
	
	public String name;
	public byte ID;
	public boolean ready;
	
	public ClientInfo(ServerClient client)  {
		name = client.getClientname();
		ID = client.getID();
		ready = client.isReady();
	}
	
	public ClientInfo(Client client)  {
		name = client.getClientname();
		ID = client.getID();
		ready = client.isReady();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof ClientInfo) {
			ClientInfo c = (ClientInfo)o;
			if(c.ID == this.ID)
				return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return ID + 7 * 17;
	}
}
